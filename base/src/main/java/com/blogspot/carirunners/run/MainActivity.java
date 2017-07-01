package com.blogspot.carirunners.run;

import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.google.android.instantapps.InstantApps;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner,
        HasSupportFragmentInjector {
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    NavigationController navigationController;

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            navigationController.navigateToPage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (InstantApps.isInstantApp(this)) {
            getMenuInflater().inflate(R.menu.install_menu, menu);
            Drawable drawable = menu.findItem(R.id.action_install).getIcon();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.icons));
            menu.findItem(R.id.action_install).setIcon(drawable);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_install) {
            try {
                InstantApps.showInstallPrompt(this, 0, "instantapp");
            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
