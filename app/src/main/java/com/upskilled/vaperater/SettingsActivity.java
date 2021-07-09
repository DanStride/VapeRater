package com.upskilled.vaperater;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_PREF_LARGE_FONT_SWITCH = "large_font_switch";
    public static final String KEY_PREF_MAX_BRIGHTNESS_SWITCH = "max_brightness_switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        setBrightness();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBrightness();
    }

    private void setBrightness() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_MAX_BRIGHTNESS_SWITCH, false);

        WindowManager.LayoutParams settings = getWindow().getAttributes();

        if (switchPref == true) {
            settings.screenBrightness = 1;
        } else {
            settings.screenBrightness = -1;
        }
        getWindow().setAttributes(settings);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

}