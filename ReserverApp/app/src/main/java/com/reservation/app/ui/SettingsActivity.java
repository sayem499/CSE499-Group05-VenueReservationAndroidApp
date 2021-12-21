package com.reservation.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.reservation.app.MainActivity;
import com.reservation.app.R;

import java.util.Locale;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.settings_activity);
        toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            ListPreference listPreference = findPreference("select_language");
            assert listPreference != null;
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String s = newValue.toString();
                    if(s.equals("English")){
                        setLocale("en");
                        requireActivity().recreate();

                    }
                    else if(s.equals("বাংলা")){
                        setLocale("bn");
                        requireActivity().recreate();
                    }

                    return true;
                }
            });


        }

        private void setLocale(String s) {
            Locale locale = new Locale(s);
            Locale.setDefault(locale);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration config =  res.getConfiguration();
            config.locale = locale;
            res.updateConfiguration(config,dm);
            SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Settings",MODE_PRIVATE).edit();
            editor.putString("App_Lang",s);
            editor.apply();
        }

    }

    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config =  res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config,dm);
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("App_Lang",s);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("App_Lang","");
        setLocale(language);
    }

}