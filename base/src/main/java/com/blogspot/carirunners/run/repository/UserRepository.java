package com.blogspot.carirunners.run.repository;

import com.blogspot.carirunners.run.AppExecutors;
import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.api.ApiResponse;
import com.blogspot.carirunners.run.api.GithubService;
import com.blogspot.carirunners.run.db.UserDao;
import com.blogspot.carirunners.run.vo.Resource;
import com.blogspot.carirunners.run.vo.User;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles User objects.
 */
@Singleton
public class UserRepository {
    private final UserDao userDao;
    private final GithubService githubService;
    private final AppExecutors appExecutors;
    private final String emptyMsg;

    @Inject
    UserRepository(Context context, AppExecutors appExecutors, UserDao userDao,
                   GithubService githubService) {
        this.userDao = userDao;
        this.githubService = githubService;
        this.appExecutors = appExecutors;
        this.emptyMsg = context.getString(R.string.empty_message);
    }

    public LiveData<Resource<User>> loadUser(String login) {
        return new NetworkBoundResource<User,User>(appExecutors, emptyMsg) {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin(login);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return githubService.getUser(login);
            }
        }.asLiveData();
    }
}
