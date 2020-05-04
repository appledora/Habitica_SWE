package com.tgc.appledora.habitica.moodChart.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.maseno.franklinesable.lifereminder.R;


public class Preferences {
    // Return mood color or return the default color
    public static Integer getMoodColor(Context context, String key, int defaultColor) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultColor);
    }

    // Set mood color
    public static void setMoodColor(Context context, String key, int color) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, color);
        editor.apply();
    }

    // Return mood label or return the default label
    public static String getMoodLabel(Context context, String key, String defaultLabel) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultLabel);
    }

    // Set mood label
    public static void setMoodLabel(Context context, String key, String label) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, label);
        editor.apply();
    }

    // Return selected year or current year
    public static Integer getSelectedYear(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(context.getString(R.string.pref_selected_year_key), SpecialUtils.getCurrentYear());
    }

    // Set year
    public static void setSelectedYear(Context context, int year) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.pref_selected_year_key), year);
        editor.apply();
    }

    // Return true if the year was initialized or false otherwise
    public static boolean checkSelectedYearInitializationState(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(context.getString(R.string.pref_year_initialized_key), false);
    }

    // Set true if selected year was initialized
    public static void setSelectedYearInitializationState(Context context, boolean initialized) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(context.getString(R.string.pref_year_initialized_key), initialized);
        editor.apply();
    }
}
