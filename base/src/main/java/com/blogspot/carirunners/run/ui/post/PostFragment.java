package com.blogspot.carirunners.run.ui.post;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.binding.FragmentDataBindingComponent;
import com.blogspot.carirunners.run.databinding.PostFragmentBinding;
import com.blogspot.carirunners.run.di.Injectable;
import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.blogspot.carirunners.run.util.AutoClearedValue;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.PostContent;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * The UI Controller for displaying a Blogger Post's information.
 */
public class PostFragment extends Fragment implements LifecycleRegistryOwner, Injectable {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private FirebaseAnalytics analytics;
    private PostViewModel viewModel;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<PostFragmentBinding> binding;

    public static PostFragment newInstance(String id, String path) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(PostActivity.ID, id);
        args.putString(PostActivity.PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PostViewModel.class);
        Bundle args = getArguments();
        String id = null;
        String path = null;
        if (args != null) {
            id = args.getString(PostActivity.ID);
            path = args.getString(PostActivity.PATH);
        }
        viewModel.setId(id, path);
        viewModel.getPost().observe(this, resource -> {
            Post post;
            PostContent postContent;
            if (resource == null || resource.data == null) {
                post = null;
                postContent = null;
            } else {
                post = resource.data;
                Document document = Jsoup.parseBodyFragment(post.content);
                Elements firstElement = document.select("div");
                Elements imgElement = firstElement.select("img");
                String logoUrl = imgElement.attr("src");
                firstElement.remove();
                String htmlContent = document.body().html();
                postContent = new PostContent(logoUrl, htmlContent);

                String eventName = FirebaseAnalytics.Event.VIEW_ITEM;
                Bundle bundle = new Bundle();
                if (post.labels != null) {
                    List<String> northernList = Arrays.asList(getResources()
                            .getStringArray(R.array.northern));
                    List<String> eastCoastList = Arrays.asList(getResources()
                            .getStringArray(R.array.eastcoast));
                    List<String> centralList = Arrays.asList(getResources()
                            .getStringArray(R.array.central));
                    List<String> southernList = Arrays.asList(getResources()
                            .getStringArray(R.array.southern));
                    List<String> eastMalaysiaList = Arrays.asList(getResources()
                            .getStringArray(R.array.eastmalaysia));
                    for (String label : post.labels) {
                        if (northernList.contains(label)) {
                            eventName += "_northern";
                            break;
                        } else if (eastCoastList.contains(label)) {
                            eventName += "_eastcoast";
                            break;
                        } else if (centralList.contains(label)) {
                            eventName += "_central";
                            break;
                        } else if (southernList.contains(label)) {
                            eventName += "_southern";
                            break;
                        } else if (eastMalaysiaList.contains(label)) {
                            eventName += "_eastmalaysia";
                            break;
                        }
                    }
                    bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY,
                            String.valueOf(post.labels));
                    int count = 0;
                    for (String label : post.labels) {
                        bundle.putString("item_label_" + ++count, label);
                    }
                }
                analytics.logEvent(eventName, bundle);
            }
            binding.get().setPost(post);
            binding.get().setPostContent(postContent);
            binding.get().setPostResource(resource);
            binding.get().executePendingBindings();
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PostFragmentBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.post_fragment,
                container, false, dataBindingComponent);
        dataBinding.setRetryCallback(() -> viewModel.retry());
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }
}
