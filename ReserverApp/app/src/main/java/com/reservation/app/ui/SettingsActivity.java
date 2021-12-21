package com.reservation.app.ui;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;



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
            loadLocale();
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            ListPreference listPreference = findPreference("select_language");
            assert listPreference != null;
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String s = listPreference.getValue();
                    if(s.equals("English")){
                        setLocale("en");
                        requireActivity().recreate();
                        /*PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(),1000,requireActivity().getIntent(),PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager alarmManager = (AlarmManager)requireActivity().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC,System.currentTimeMillis() + 1000,pendingIntent);
                        System.exit(0);*/


                    }
                    else if(s.equals("বাংলা")){
                        setLocale("bn");
                        requireActivity().recreate();
                        /*PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(),1000,requireActivity().getIntent(),PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager alarmManager = (AlarmManager)requireActivity().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC,System.currentTimeMillis() + 1000,pendingIntent);
                        System.exit(0);*/


                    }

                    return true;
                }
            });


        }



        private void setLocale(String s) {
            Locale locale = new Locale(s);
            Locale.setDefault(locale);
            Configuration config =  new Configuration();
            config.locale = locale;
            requireActivity().getBaseContext().getResources().updateConfiguration(config,requireActivity().getBaseContext().getResources().getDisplayMetrics());
            SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Settings",MODE_PRIVATE).edit();
            editor.putString("App_Lang",s);
            editor.apply();
        }

        public void loadLocale(){
            SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String language = prefs.getString("App_Lang","");
            setLocale(language);
        }

    }

    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration config =  new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
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