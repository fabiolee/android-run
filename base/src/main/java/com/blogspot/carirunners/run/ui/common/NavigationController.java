package com.blogspot.carirunners.run.ui.common;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.ui.favorite.FavoriteFragment;
import com.blogspot.carirunners.run.ui.page.PageFragment;
import com.blogspot.carirunners.run.ui.post.PostFragment;
import com.blogspot.carirunners.run.ui.search.SearchFragment;
import com.blogspot.carirunners.run.ui.settings.SettingsFragment;
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

    public void navigateToFavorite() {
        String tag = "FavoriteFragment";
        FavoriteFragment fragment = (FavoriteFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new FavoriteFragment();
        }
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
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

    public void navigateToPost(String id, String title, String path, boolean favorite) {
        String tag = "PostFragment";
        PostFragment fragment = (PostFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = PostFragment.newInstance(id, title, path, favorite);
        }
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void navigateToSettings() {
        String tag = "SettingsFragment";
        SettingsFragment fragment = (SettingsFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new SettingsFragment();
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
