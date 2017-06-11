package com.blogspot.carirunners.run;

import android.app.Application;

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 * <p>
 * See {@link com.blogspot.carirunners.run.util.GithubTestRunner}.
 */
public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
