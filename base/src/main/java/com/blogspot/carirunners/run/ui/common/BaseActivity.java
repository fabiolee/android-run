package com.blogspot.carirunners.run.ui.common;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.carirunners.run.BuildConfig;
import com.blogspot.carirunners.run.R;
import com.google.android.instantapps.InstantApps;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * @author fabiolee
 */
public class BaseActivity extends DaggerAppCompatActivity implements LifecycleRegistryOwner {
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
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
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" +
                        BuildConfig.APPLICATION_ID);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
