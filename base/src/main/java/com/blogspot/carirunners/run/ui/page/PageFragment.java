package com.blogspot.carirunners.run.ui.page;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.api.BloggerService;
import com.blogspot.carirunners.run.binding.FragmentDataBindingComponent;
import com.blogspot.carirunners.run.databinding.PageFragmentBinding;
import com.blogspot.carirunners.run.di.Injectable;
import com.blogspot.carirunners.run.ui.common.BaseChildFragment;
import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.blogspot.carirunners.run.ui.common.PageItemAdapter;
import com.blogspot.carirunners.run.util.AutoClearedValue;
import com.blogspot.carirunners.run.vo.Favorite;
import com.blogspot.carirunners.run.vo.PageItem;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * The UI Controller for displaying a Blogger Page's information.
 */
public class PageFragment extends BaseChildFragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private PageViewModel viewModel;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<PageFragmentBinding> binding;
    AutoClearedValue<PageItemAdapter> adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PageViewModel.class);
        viewModel.setId(BloggerService.PAGE_ID);

        PageItemAdapter adapter = new PageItemAdapter(dataBindingComponent,
                new PageItemAdapter.Callback() {
                    @Override
                    public void onClick(PageItem pageItem) {
                        parentFragment.navigateToPost(null, pageItem.title, pageItem.urlPath,
                                pageItem.favorite);
                    }

                    @Override
                    public void onClickFavorite(PageItem pageItem) {
                        viewModel.toggleFavorite(pageItem.favorite,
                                new Favorite(null, pageItem.title, pageItem.urlPath));
                    }
                });
        this.adapter = new AutoClearedValue<>(this, adapter);
        binding.get().pageItemList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.get().pageItemList.setAdapter(adapter);

        initView(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PageFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.page_fragment, container, false);
        dataBinding.setRetryCallback(() -> viewModel.retry());

        Toolbar toolbar = dataBinding.toolbar;
        activity.setSupportActionBar(toolbar);

        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    private void initView(PageViewModel viewModel) {
        viewModel.getPageItemList().observe(this, resource -> {
            binding.get().setPageResource(resource);
            binding.get().executePendingBindings();
            if (resource == null || resource.data == null) {
                adapter.get().replace(null);
            } else {
                adapter.get().replace(resource.data);
            }
        });
    }
}
