package com.blogspot.carirunners.run.di;

import android.support.v7.app.AppCompatActivity;

import com.blogspot.carirunners.run.MainActivity;
import com.blogspot.carirunners.run.ui.favorite.FavoriteFragment;
import com.blogspot.carirunners.run.ui.page.PageFragment;
import com.blogspot.carirunners.run.ui.post.PostFragment;
import com.blogspot.carirunners.run.ui.search.SearchFragment;
import com.blogspot.carirunners.run.ui.user.UserFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract FavoriteFragment favoriteFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PageFragment pageFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PostFragment postFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract UserFragment userFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchFragment searchFragment();

    @ActivityScoped
    @Binds
    abstract AppCompatActivity activity(MainActivity activity);
}
