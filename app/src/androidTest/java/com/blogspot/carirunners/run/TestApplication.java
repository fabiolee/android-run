package com.blogspot.carirunners.run;

import android.app.Application;

import com.blogspot.carirunners.run.util.AppTestRunner;

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 * <p>
 * See {@link AppTestRunner}.
 */
public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
