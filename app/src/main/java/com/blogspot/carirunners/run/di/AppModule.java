package com.blogspot.carirunners.run.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.blogspot.carirunners.run.api.GithubService;
import com.blogspot.carirunners.run.db.GithubDb;
import com.blogspot.carirunners.run.db.RepoDao;
import com.blogspot.carirunners.run.db.UserDao;
import com.blogspot.carirunners.run.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton @Provides
    GithubService provideGithubService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(GithubService.class);
    }

    @Singleton @Provides
    GithubDb provideDb(Application app) {
        return Room.databaseBuilder(app, GithubDb.class,"github.db").build();
    }

    @Singleton @Provides
    UserDao provideUserDao(GithubDb db) {
        return db.userDao();
    }

    @Singleton @Provides
    RepoDao provideRepoDao(GithubDb db) {
        return db.repoDao();
    }
}
