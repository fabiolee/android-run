package com.blogspot.carirunners.run.ui.user;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.binding.FragmentBindingAdapters;
import com.blogspot.carirunners.run.testing.SingleFragmentActivity;
import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.blogspot.carirunners.run.util.RecyclerViewMatcher;
import com.blogspot.carirunners.run.util.TestUtil;
import com.blogspot.carirunners.run.util.ViewModelUtil;
import com.blogspot.carirunners.run.vo.Repo;
import com.blogspot.carirunners.run.vo.Resource;
import com.blogspot.carirunners.run.vo.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserFragmentTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private UserViewModel viewModel;
    private NavigationController navigationController;
    private FragmentBindingAdapters fragmentBindingAdapters;
    private MutableLiveData<Resource<User>> userData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Repo>>> repoListData = new MutableLiveData<>();

    @Before
    public void init() {
        UserFragment fragment = UserFragment.create("foo");
        viewModel = mock(UserViewModel.class);
        when(viewModel.getUser()).thenReturn(userData);
        when(viewModel.getRepositories()).thenReturn(repoListData);
        navigationController = mock(NavigationController.class);
        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);

        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        fragment.navigationController = navigationController;
        fragment.dataBindingComponent = () -> fragmentBindingAdapters;

        activityRule.getActivity().setFragment(fragment);
    }

    @Test
    public void loading() {
        userData.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }

    @Test
    public void error() {
        userData.postValue(Resource.error("wtf", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.error_msg)).check(matches(withText("wtf")));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).perform(click());
        verify(viewModel).retry();
    }

    @Test
    public void loadingWithUser() {
        User user = TestUtil.createUser("foo");
        userData.postValue(Resource.loading(user));
        onView(withId(R.id.name)).check(matches(withText(user.name)));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void loadedUser() {
        User user = TestUtil.createUser("foo");
        userData.postValue(Resource.success(user));
        onView(withId(R.id.name)).check(matches(withText(user.name)));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void loadRepos() {
        List<Repo> repos = setRepos(2);
        for (int pos = 0; pos < repos.size(); pos++) {
            Repo repo = repos.get(pos);
            onView(listMatcher().atPosition(pos)).check(
                    matches(hasDescendant(withText(repo.name))));
            onView(listMatcher().atPosition(pos)).check(
                    matches(hasDescendant(withText(repo.description))));
            onView(listMatcher().atPosition(pos)).check(
                    matches(hasDescendant(withText("" + repo.stars))));
        }
        Repo repo3 = setRepos(3).get(2);
        onView(listMatcher().atPosition(2)).check(
                matches(hasDescendant(withText(repo3.name))));
    }

    @Test
    public void nullUser() {
        userData.postValue(null);
        onView(withId(R.id.name)).check(matches(not(isDisplayed())));
    }

    @Test
    public void nullRepoList() {
        repoListData.postValue(null);
        onView(listMatcher().atPosition(0)).check(doesNotExist());
    }

    @Test
    public void nulledUser() {
        User user = TestUtil.createUser("foo");
        userData.postValue(Resource.success(user));
        onView(withId(R.id.name)).check(matches(withText(user.name)));
        userData.postValue(null);
        onView(withId(R.id.name)).check(matches(not(isDisplayed())));
    }

    @Test
    public void nulledRepoList() {
        setRepos(5);
        onView(listMatcher().atPosition(1)).check(matches(isDisplayed()));
        repoListData.postValue(null);
        onView(listMatcher().atPosition(0)).check(doesNotExist());
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.repo_list);
    }

    private List<Repo> setRepos(int count) {
        List<Repo> repos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repos.add(TestUtil.createRepo("foo", "name " + i, "desc" + i));
        }
        repoListData.postValue(Resource.success(repos));
        return repos;
    }
}