package com.blogspot.carirunners.run.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.blogspot.carirunners.run.ui.favorite.FavoriteViewModel;
import com.blogspot.carirunners.run.ui.page.PageViewModel;
import com.blogspot.carirunners.run.ui.post.PostViewModel;
import com.blogspot.carirunners.run.ui.search.SearchViewModel;
import com.blogspot.carirunners.run.ui.user.UserViewModel;
import com.blogspot.carirunners.run.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel.class)
    abstract ViewModel bindFavoriteViewModel(FavoriteViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PageViewModel.class)
    abstract ViewModel bindPageViewModel(PageViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel.class)
    abstract ViewModel bindPostViewModel(PostViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
