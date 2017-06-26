package com.blogspot.carirunners.run.ui.post;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.binding.FragmentDataBindingComponent;
import com.blogspot.carirunners.run.databinding.PostFragmentBinding;
import com.blogspot.carirunners.run.di.Injectable;
import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.blogspot.carirunners.run.util.AutoClearedValue;
import com.blogspot.carirunners.run.vo.Post;
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
 * The UI Controller for displaying a Blogger Post's information.
 */
public class PostFragment extends Fragment implements LifecycleRegistryOwner, Injectable {

    private static final String KEY_ID = "id";
    private static final String KEY_PATH = "path";

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private PostViewModel viewModel;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<PostFragmentBinding> binding;
    AutoClearedValue<ContributorAdapter> adapter;

    public static PostFragment newInstance(String id, String path) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        args.putString(KEY_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostViewModel.class);
        Bundle args = getArguments();
        String id = null;
        String path = null;
        if (args != null) {
            id = args.getString(KEY_ID);
            path = args.getString(KEY_PATH);
        }
        viewModel.setId(id, path);
        LiveData<Resource<Post>> post = viewModel.getPost();
        post.observe(this, resource -> {
            binding.get().setPost(resource == null ? null : resource.data);
            binding.get().setPostResource(resource);
            binding.get().executePendingBindings();
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PostFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.post_fragment, container, false);
        dataBinding.setRetryCallback(() -> viewModel.retry());
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }
}
