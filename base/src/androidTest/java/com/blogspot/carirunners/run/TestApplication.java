package com.blogspot.carirunners.run;

import android.support.multidex.MultiDexApplication;

import com.blogspot.carirunners.run.util.AppTestRunner;

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 * <p>
 * See {@link AppTestRunner}.
 */
public class TestApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
