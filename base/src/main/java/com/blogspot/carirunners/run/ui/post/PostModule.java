package com.blogspot.carirunners.run.ui.post;

import android.support.v7.app.AppCompatActivity;

import com.blogspot.carirunners.run.di.ActivityScoped;
import com.blogspot.carirunners.run.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author fabiolee
 */
@Module
public abstract class PostModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract PostFragment postFragment();

    @ActivityScoped
    @Binds
    abstract AppCompatActivity activity(PostActivity activity);
}
