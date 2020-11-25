package com.track.trackandtrigger;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import static com.track.trackandtrigger.SplashScreenActivity.applyTheme;

public class MainSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_preference);
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
        boolean themePref = sharedPref.getBoolean("dark_light", false);
        applyTheme(themePref);
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) pref;
//            switch (listPreference.getValue()) {
//                case "en":
//                    setLocale("en");
//                    break;
//                case "ta":
//                    setLocale("ta");
//                    break;
//                case "mal":
//                    setLocale("ml");
//                    break;
//                default:
//                    Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
//            }
            pref.setSummaryProvider(preference -> {
                switch (listPreference.getValue()) {
                       case "en":
                           return "English";
                       case "ta":
                           return "தமிழ்";
                       case "mal":
                           return "മലയാളം";
                       default:
                           return "";
                   }
            });
        }
    }
//    public void setLocale(String lang) {
//        Locale myLocale = new Locale(lang);
//        Resources resources = getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        Configuration configuration = resources.getConfiguration();
//        configuration.locale = myLocale;
//        resources.updateConfiguration(configuration, dm);
//        requireActivity().recreate();
//    }
}