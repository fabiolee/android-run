package com.blogspot.carirunners.run.ui.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.blogspot.carirunners.run.R;

import timber.log.Timber;

/**
 * @author fabiolee
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String PREF_VERSION = "pref_version";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        initView();
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
