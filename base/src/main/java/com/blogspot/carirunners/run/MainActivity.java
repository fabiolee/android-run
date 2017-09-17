package com.blogspot.carirunners.run;

import com.blogspot.carirunners.run.ui.common.BaseActivity;
import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.blogspot.carirunners.run.ui.post.PostActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {
    public static final String KEY_URL = "url";

    @Inject
    NavigationController navigationController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        navigationController.navigateToPage();
        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent(intent);
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
