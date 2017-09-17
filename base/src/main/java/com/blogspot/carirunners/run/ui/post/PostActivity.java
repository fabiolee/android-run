package com.blogspot.carirunners.run.ui.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.ui.common.BaseActivity;
import com.blogspot.carirunners.run.ui.common.NavigationController;

import javax.inject.Inject;

/**
 * @author fabiolee
 */
public class PostActivity extends BaseActivity {
    public static final String ID = "id";
    public static final String PATH = "path";

    @Inject
    NavigationController navigationController;

    public static Intent getStartIntent(Context context, String id, String path) {
        return new Intent(context, PostActivity.class)
                .putExtra(ID, id)
                .putExtra(PATH, path);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String id = intent.getStringExtra(ID);
        String path = intent.getStringExtra(PATH);
        navigationController.navigateToPost(id, path);
    }
}
