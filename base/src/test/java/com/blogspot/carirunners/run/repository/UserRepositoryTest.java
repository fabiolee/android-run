package com.blogspot.carirunners.run.repository;

import com.blogspot.carirunners.run.api.ApiResponse;
import com.blogspot.carirunners.run.api.GithubService;
import com.blogspot.carirunners.run.db.UserDao;
import com.blogspot.carirunners.run.util.ApiUtil;
import com.blogspot.carirunners.run.util.InstantAppExecutors;
import com.blogspot.carirunners.run.util.TestUtil;
import com.blogspot.carirunners.run.vo.Resource;
import com.blogspot.carirunners.run.vo.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserRepositoryTest {
    private UserDao userDao;
    private GithubService githubService;
    private UserRepository repo;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        userDao = mock(UserDao.class);
        githubService = mock(GithubService.class);
        repo = new UserRepository(new InstantAppExecutors(), userDao, githubService);
    }

    @Test
    public void loadUser() {
        repo.loadUser("abc");
        verify(userDao).findByLogin("abc");
    }

    @Test
    public void goToNetwork() {
        MutableLiveData<User> dbData = new MutableLiveData<>();
        when(userDao.findByLogin("foo")).thenReturn(dbData);
        User user = TestUtil.createUser("foo");
        LiveData<ApiResponse<User>> call = ApiUtil.successCall(user);
        when(githubService.getUser("foo")).thenReturn(call);
        Observer<Resource<User>> observer = mock(Observer.class);

        repo.loadUser("foo").observeForever(observer);
        verify(githubService, never()).getUser("foo");
        MutableLiveData<User> updatedDbData = new MutableLiveData<>();
        when(userDao.findByLogin("foo")).thenReturn(updatedDbData);
        dbData.setValue(null);
        verify(githubService).getUser("foo");
    }

    @Test
    public void dontGoToNetwork() {
        MutableLiveData<User> dbData = new MutableLiveData<>();
        User user = TestUtil.createUser("foo");
        dbData.setValue(user);
        when(userDao.findByLogin("foo")).thenReturn(dbData);
        Observer<Resource<User>> observer = mock(Observer.class);
        repo.loadUser("foo").observeForever(observer);
        verify(githubService, never()).getUser("foo");
        verify(observer).onChanged(Resource.success(user));
    }
}