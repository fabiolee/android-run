package com.blogspot.carirunners.run.ui.common;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.ui.page.PageFragment;
import com.blogspot.carirunners.run.ui.post.PostFragment;
import com.blogspot.carirunners.run.ui.search.SearchFragment;
import com.blogspot.carirunners.run.ui.user.UserFragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

/**
 * A utility class that handles navigation in {@link AppCompatActivity}.
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(AppCompatActivity activity) {
        this.containerId = R.id.container;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToPage() {
        String tag = "PageFragment";
        PageFragment fragment = (PageFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new PageFragment();
        }
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void navigateToPost(String id, String path) {
        String tag = "PostFragment";
        PostFragment fragment = (PostFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = PostFragment.newInstance(id, path);
        }
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void navigateToUser(String login) {
        String tag = "user" + "/" + login;
        UserFragment userFragment = UserFragment.create(login);
        fragmentManager.beginTransaction()
                .replace(containerId, userFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
