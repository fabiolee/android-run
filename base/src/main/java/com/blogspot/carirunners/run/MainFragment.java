package com.blogspot.carirunners.run;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.carirunners.run.ui.common.BaseFragment;
import com.blogspot.carirunners.run.ui.favorite.FavoriteFragment;
import com.blogspot.carirunners.run.ui.page.PageFragment;
import com.blogspot.carirunners.run.ui.post.PostFragment;

/**
 * @author fabiolee
 */
public class MainFragment extends BaseFragment {
    public static final String TYPE = "type";

    private boolean navigateToPostWhenReady;
    private String id;
    private String title;
    private String path;
    private boolean favorite;

    public static MainFragment newInstance(String tag) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            String tag = getArguments().getString(TYPE);
            setFragment(tag);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(() -> {
            if (navigateToPostWhenReady) {
                navigateToPost(id, title, path, favorite);
                navigateToPostWhenReady = false;
                id = null;
                title = null;
                path = null;
                favorite = false;
            }
        });
    }

    public void navigateToPost(String id, String title, String path, boolean favorite) {
        String tag = "PostFragment";
        PostFragment fragment = PostFragment.newInstance(id, title, path, favorite);
        changeScreen(fragment, tag);
    }

    public void navigateToPostWhenReady(String id, String title, String path, boolean favorite) {
        this.navigateToPostWhenReady = true;
        this.id = id;
        this.title = title;
        this.path = path;
        this.favorite = favorite;
    }

    private void changeScreen(Fragment fragment, String tag) {
        // Add a fragment on top of the current screen
        getChildFragmentManager().beginTransaction()
                .replace(R.id.container_child, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void setFragment(String tag) {
        Fragment fragment;
        switch (tag) {
            case "PageFragment":
                fragment = new PageFragment();
                break;
            case "FavoriteFragment":
                fragment = new FavoriteFragment();
                break;
            default:
                throw new IllegalArgumentException("Invalid fragment tag");
        }
        //add child fragment
        getChildFragmentManager().beginTransaction()
                .add(R.id.container_child, fragment, tag)
                .commitAllowingStateLoss();
    }
}
