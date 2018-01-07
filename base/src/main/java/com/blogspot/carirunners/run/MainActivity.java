package com.blogspot.carirunners.run;

import com.blogspot.carirunners.run.ui.common.BaseActivity;
import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.blogspot.carirunners.run.ui.post.PostActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import javax.inject.Inject;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static final String KEY_URL = "url";

    @Inject
    NavigationController navigationController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation_activity);
        initView();
        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_home) {
            navigationController.navigateToPage();
        } else if (itemId == R.id.action_settings) {
            navigationController.navigateToSettings();
        }
        return true;
    }

    private void initView() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    private void parseIntent(Intent intent) {
        String url = intent.getStringExtra(KEY_URL);
        String urlPath = null;
        if (url != null) {
            urlPath = Uri.parse(url).getPath();
        }
        if (urlPath != null) {
            startActivity(PostActivity.getStartIntent(this, null, urlPath));
        }
    }
}
