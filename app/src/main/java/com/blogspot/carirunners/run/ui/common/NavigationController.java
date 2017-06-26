package com.blogspot.carirunners.run.ui.common;

import com.blogspot.carirunners.run.MainActivity;
import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.ui.page.PageFragment;
import com.blogspot.carirunners.run.ui.post.PostFragment;
import com.blogspot.carirunners.run.ui.search.SearchFragment;
import com.blogspot.carirunners.run.ui.user.UserFragment;

import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToPage() {
        String tag = "page";
        PageFragment fragment = new PageFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                //.addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToPost(String id, String path) {
        PostFragment fragment = PostFragment.newInstance(id, path);
        String tag = "post" + "/" + id + "/path/" + path;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
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
