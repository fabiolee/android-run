package com.blogspot.carirunners.run;

import com.blogspot.carirunners.run.di.AppInjector;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.android.instantapps.InstantApps;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import io.fabric.sdk.android.Fabric;
import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;


public class AppApplication extends MultiDexApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Crashlytics.setBool("InstantApp", InstantApps.isInstantApp(this));
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Timber.plant(new Timber.DebugTree());
        }
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
