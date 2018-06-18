package com.blogspot.carirunners.run.ui.settings;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blogspot.carirunners.run.MainActivity;
import com.blogspot.carirunners.run.R;

import timber.log.Timber;

/**
 * @author fabiolee
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String PREF_VERSION = "pref_version";

    private MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activity = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must be MainActivity");
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        initView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
    }

    private void initView() {
        try {
            PackageInfo packageInfo = getContext().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0);
            Preference version = findPreference(PREF_VERSION);
            version.setSummary(getString(R.string.summary_version, packageInfo.versionName,
                    packageInfo.versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }
    }
}
