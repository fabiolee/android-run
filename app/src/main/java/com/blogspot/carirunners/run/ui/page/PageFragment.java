package com.blogspot.carirunners.run.ui.page;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.api.BloggerService;
import com.blogspot.carirunners.run.binding.FragmentDataBindingComponent;
import com.blogspot.carirunners.run.databinding.PageFragmentBinding;
import com.blogspot.carirunners.run.di.Injectable;
import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.blogspot.carirunners.run.util.AutoClearedValue;
import com.blogspot.carirunners.run.vo.Page;
import com.blogspot.carirunners.run.vo.Resource;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * The UI Controller for displaying a Blogger Page's information.
 */
public class PageFragment extends Fragment implements LifecycleRegistryOwner, Injectable {
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private PageViewModel viewModel;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<PageFragmentBinding> binding;

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PageViewModel.class);
        viewModel.setId(BloggerService.PAGE_ID);
        LiveData<Resource<Page>> page = viewModel.getPage();
        page.observe(this, resource -> {
            binding.get().setPage(resource == null ? null : resource.data);
            binding.get().setPageResource(resource);
            binding.get().executePendingBindings();
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PageFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.page_fragment, container, false);
        dataBinding.setRetryCallback(() -> viewModel.retry());
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }
}
