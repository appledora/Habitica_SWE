package com.tgc.appledora.habitica;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.maseno.franklinesable.lifereminder.R;

public class preference_fragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String KEY_PICK_NOTIFICATION_SOUND = "notification_reminder_complete_ringtone";
    private static final String KEY_SNOOZE = "snooze_frequency";
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        setUpSnoozePref();
        setUpRingtonePreference();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setUpSnoozePref();
        setUpRingtonePreference();
    }

    public void setUpRingtonePreference() {
        Preference ringtonePref = findPreference(KEY_PICK_NOTIFICATION_SOUND);
        String ringValue = sharedPreferences.getString(KEY_PICK_NOTIFICATION_SOUND, "");

        if (TextUtils.isEmpty(ringValue)) {
            // Empty values correspond to 'silent' (no ringtone).
            ringtonePref.setSummary("Silent");

        } else {
            Ringtone ringtone = RingtoneManager.getRingtone(
                    ringtonePref.getContext(), Uri.parse(ringValue));

            if (ringtone == null) {
                // Clear the summary if there was a lookup error.
                ringtonePref.setSummary(null);
            } else {
                // Set the summary to reflect the new ringtone display
                // name.
                String name = ringtone.getTitle(ringtonePref.getContext());
                ringtonePref.setSummary(name);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }


    public void setUpSnoozePref() {

        Preference snoozePref = findPreference(KEY_SNOOZE);
        String snoozeValue = sharedPreferences.getString(KEY_SNOOZE, "5");

        ListPreference snoozePreference_List = (ListPreference) snoozePref;
        int index = snoozePreference_List.findIndexOfValue(snoozeValue);
        snoozePref.setSummary(
                index >= 0
                        ? snoozePreference_List.getEntries()[index]
                        : null);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {

        return false;
    }


}

