package com.blogspot.carirunners.run.ui.common;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
            MenuItem installMenu = menu.findItem(R.id.action_install);
            int iconsColor = ContextCompat.getColor(this, R.color.icons);
            setMenuItemTint(installMenu, iconsColor);
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

    public void setMenuItemTint(MenuItem item, int color) {
        setMenuItemTitleTint(item, color);
        setMenuItemIconTint(item, color);
    }

    public void setMenuItemIconTint(MenuItem item, int color) {
        Drawable drawable = item.getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        item.setIcon(drawable);
    }

    public void setMenuItemIconTint(MenuItem item, Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        item.setIcon(drawable);
    }

    public void setMenuItemTitleTint(MenuItem item, int color) {
        SpannableString title = new SpannableString(item.getTitle());
        title.setSpan(new ForegroundColorSpan(color), 0, title.length(), 0);
        item.setTitle(title);
    }
}
