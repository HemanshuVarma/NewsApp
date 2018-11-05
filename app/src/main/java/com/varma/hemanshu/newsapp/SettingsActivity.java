package com.varma.hemanshu.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference queryPref = findPreference(getString(R.string.settings_query_key));
            bindPreferenceSummaryToString(queryPref);
            Preference pageFeedSize = findPreference(getString(R.string.settings_page_feed_key));
            bindPreferenceSummaryToString(pageFeedSize);
        }

        private void bindPreferenceSummaryToString(Preference pref) {
            pref.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pref.getContext());
            String pageFeedValue = sharedPreferences.getString(pref.getKey(), "");
            onPreferenceChange(pref, pageFeedValue);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String value = newValue.toString();
            preference.setSummary(value);
            return true;
        }
    }
}
