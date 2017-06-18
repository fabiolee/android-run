package com.blogspot.carirunners.run.di;

import com.blogspot.carirunners.run.ui.page.PageFragment;
import com.blogspot.carirunners.run.ui.repo.RepoFragment;
import com.blogspot.carirunners.run.ui.search.SearchFragment;
import com.blogspot.carirunners.run.ui.user.UserFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract PageFragment contributePageFragment();

    @ContributesAndroidInjector
    abstract RepoFragment contributeRepoFragment();

    @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}
