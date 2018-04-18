package com.blogspot.carirunners.run.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.blogspot.carirunners.run.BuildConfig;
import com.blogspot.carirunners.run.api.BloggerService;
import com.blogspot.carirunners.run.api.GithubService;
import com.blogspot.carirunners.run.db.AppDatabase;
import com.blogspot.carirunners.run.db.FavoriteDao;
import com.blogspot.carirunners.run.db.PageDao;
import com.blogspot.carirunners.run.db.PostDao;
import com.blogspot.carirunners.run.db.RepoDao;
import com.blogspot.carirunners.run.db.UserDao;
import com.blogspot.carirunners.run.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton
    @Provides
    BloggerService provideBloggerService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        return new Retrofit.Builder()
                .baseUrl(String.format("https://www.googleapis.com/blogger/v3/blogs/%s/",
                        BloggerService.BLOG_ID))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(BloggerService.class);
    }

    @Singleton
    @Provides
    GithubService provideGithubService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(GithubService.class);
    }

    @Singleton
    @Provides
    AppDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class, "app.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    FavoriteDao provideFavoriteDao(AppDatabase db) {
        return db.favoriteDao();
    }

    @Singleton
    @Provides
    PageDao providePageDao(AppDatabase db) {
        return db.pageDao();
    }

    @Singleton
    @Provides
    PostDao providePostDao(AppDatabase db) {
        return db.postDao();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(AppDatabase db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    RepoDao provideRepoDao(AppDatabase db) {
        return db.repoDao();
    }
}
