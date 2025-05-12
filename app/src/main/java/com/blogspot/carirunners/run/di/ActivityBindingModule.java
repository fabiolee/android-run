package com.blogspot.carirunners.run.di;

import com.blogspot.carirunners.run.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity mainActivity();
}
