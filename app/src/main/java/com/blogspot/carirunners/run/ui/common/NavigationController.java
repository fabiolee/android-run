package com.blogspot.carirunners.run.ui.common;

import com.blogspot.carirunners.run.MainActivity;
import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.ui.repo.RepoFragment;
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

    public void navigateToRepo(String owner, String name) {
        RepoFragment fragment = RepoFragment.create(owner, name);
        String tag = "repo" + "/" + owner + "/" + name;
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
