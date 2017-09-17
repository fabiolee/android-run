package com.blogspot.carirunners.run.di;

import com.blogspot.carirunners.run.MainActivity;
import com.blogspot.carirunners.run.ui.post.PostActivity;
import com.blogspot.carirunners.run.ui.post.PostModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PostModule.class)
    abstract PostActivity postActivity();
}
