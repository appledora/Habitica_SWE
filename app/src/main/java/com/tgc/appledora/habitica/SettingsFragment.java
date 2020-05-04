package com.tgc.appledora.habitica;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.maseno.franklinesable.lifereminder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        SettingsFragment fragmentSettings;
        fragmentSettings = (SettingsFragment) fragmentManager.findFragmentByTag("settings_fragment");
        //checking whether the background fragment has been retained, if not create a new one
        if (fragmentSettings == null) {

            fragmentSettings = new SettingsFragment();
            fragmentSettings.setTargetFragment(this, 0);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(fragmentSettings, "settings_fragment");
            fragmentTransaction.commit();
        }
        preferences_values();
    }

    public void preferences_values() {

        final SharedPreferences[] sharedPref = new SharedPreferences[1];

        new Thread(() -> {
            sharedPref[0] = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            new Handler(Looper.getMainLooper()).post(() -> {


                if (sharedPref[0].getBoolean(preference_activity.KEY_FULLSCREEN, false)) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                } else {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                if (sharedPref[0].getBoolean(preference_activity.KEY_SECURE_BAR, false)) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                } else {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }

                if (sharedPref[0].getBoolean(preference_activity.KEY_GUARD_LOCK, false)) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                } else {

                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                }
                if (sharedPref[0].getBoolean(preference_activity.KEY_SCREEN_ON, false)) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                if (sharedPref[0].getBoolean(preference_activity.KEY_ANIM, false)) {

                    switch (sharedPref[0].getString(preference_activity.KEY_ANIM_LIST, "43")) {
                        case "32":
                            getActivity().overridePendingTransition(R.anim.window_anim_fade_in, R.anim.window_anim_fade_in);

                            break;
                        case "43":
                            getActivity().overridePendingTransition(R.anim.window_anim_rotate, R.anim.window_anim_rotate);
                            break;
                        case "69":
                            getActivity().overridePendingTransition(R.anim.window_anim_blank, R.anim.window_anim_blank);
                            break;
                    }
                }
            });
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences_values();
    }
}