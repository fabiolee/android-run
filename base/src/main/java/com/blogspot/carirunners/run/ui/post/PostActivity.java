package com.blogspot.carirunners.run.ui.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.ui.common.BaseActivity;
import com.blogspot.carirunners.run.ui.common.NavigationController;

import javax.inject.Inject;

/**
 * @author fabiolee
 */
public class PostActivity extends BaseActivity {
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String PATH = "path";
    public static final String FAVORITE = "favorite";

    @Inject
    NavigationController navigationController;

    public static Intent getStartIntent(Context context, String id, String title, String path,
                                        boolean favorite) {
        return new Intent(context, PostActivity.class)
                .putExtra(ID, id)
                .putExtra(TITLE, title)
                .putExtra(PATH, path)
                .putExtra(FAVORITE, favorite);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        Intent intent = getIntent();
        String id = intent.getStringExtra(ID);
        String title = intent.getStringExtra(TITLE);
        String path = intent.getStringExtra(PATH);
        boolean favorite = intent.getBooleanExtra(FAVORITE, false);
        navigationController.navigateToPost(id, title, path, favorite);
    }
}
