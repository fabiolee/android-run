package com.blogspot.carirunners.run.ui.common;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.MainFragment;
import com.blogspot.carirunners.run.ui.search.SearchFragment;
import com.blogspot.carirunners.run.ui.settings.SettingsFragment;
import com.blogspot.carirunners.run.ui.user.UserFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        MainFragment fragment = (MainFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = MainFragment.newInstance(tag);
        }
        changeTab(fragment, tag);
    }

    public MainFragment navigateToPage() {
        String tag = "PageFragment";
        MainFragment fragment = (MainFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = MainFragment.newInstance(tag);
        }
        changeTab(fragment, tag);
        return fragment;
    }

    public void navigateToPost(String id, String title, String path, boolean favorite) {
        MainFragment parentFragment = navigateToPage();
        parentFragment.navigateToPostWhenReady(id, title, path, favorite);
    }

    public void navigateToSettings() {
        String tag = "SettingsFragment";
        SettingsFragment fragment = (SettingsFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new SettingsFragment();
        }
        changeTab(fragment, tag);
    }

    public void navigateToUser(String login) {
        String tag = "user" + "/" + login;
        UserFragment userFragment = UserFragment.create(login);
        fragmentManager.beginTransaction()
                .replace(containerId, userFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void changeTab(Fragment fragment, String tag) {
        if (fragment.isAdded()) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentById(containerId);
        if (currentFragment != null) {
            currentFragment.setMenuVisibility(false);
            currentFragment.setUserVisibleHint(false);
            transaction.detach(currentFragment);
        }

        if (fragment.isDetached()) {
            transaction.attach(fragment);
        } else {
            transaction.add(containerId, fragment, tag);
        }
        fragment.setMenuVisibility(true);
        fragment.setUserVisibleHint(true);

        transaction.commitAllowingStateLoss();
    }
}
