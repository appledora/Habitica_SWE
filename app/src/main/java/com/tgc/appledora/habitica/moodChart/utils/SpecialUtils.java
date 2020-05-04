package com.tgc.appledora.habitica.moodChart.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.maseno.franklinesable.lifereminder.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SpecialUtils {
    public static String getMonthInitial(Context context, int month) {
        switch (month) {
            case 1:
                return context.getString(R.string.month_initial_january);
            case 2:
                return context.getString(R.string.month_initial_february);
            case 3:
                return context.getString(R.string.month_initial_march);
            case 4:
                return context.getString(R.string.month_initial_april);
            case 5:
                return context.getString(R.string.month_initial_may);
            case 6:
                return context.getString(R.string.month_initial_june);
            case 7:
                return context.getString(R.string.month_initial_july);
            case 8:
                return context.getString(R.string.month_initial_august);
            case 9:
                return context.getString(R.string.month_initial_september);
            case 10:
                return context.getString(R.string.month_initial_october);
            case 11:
                return context.getString(R.string.month_initial_november);
            default:
                return context.getString(R.string.month_initial_december);
        }
    }

    public static String getMonthName(Context context, int month) {
        switch (month) {
            case 1:
                return context.getString(R.string.january);
            case 2:
                return context.getString(R.string.february);
            case 3:
                return context.getString(R.string.march);
            case 4:
                return context.getString(R.string.april);
            case 5:
                return context.getString(R.string.may);
            case 6:
                return context.getString(R.string.june);
            case 7:
                return context.getString(R.string.july);
            case 8:
                return context.getString(R.string.august);
            case 9:
                return context.getString(R.string.september);
            case 10:
                return context.getString(R.string.october);
            case 11:
                return context.getString(R.string.november);
            default:
                return context.getString(R.string.december);
        }
    }

    public static boolean isLeapYear(int year) {
        // Calculate if it's a leap year
        if (year % 4 == 0 && year % 100 != 0) {
            return true;
        } else if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int getCurrentYear() {
        Date time = new Date(new Date().getTime());
        SimpleDateFormat simpleYearFormat = new SimpleDateFormat("yyyy", Locale.US);
        return Integer.parseInt(simpleYearFormat.format(time));
    }

    public static int getColor(Context context, int moodIndex) {
        switch (moodIndex) {
            case 1:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_1_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_1));
            case 2:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_2_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_2));
            case 3:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_3_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_3));
            case 4:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_4_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_4));
            case 5:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_5_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_5));
            case 6:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_6_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_6));
            case 7:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_7_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_7));
            case 8:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_8_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_8));
            case 9:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_9_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_9));
            case 10:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_10_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_10));
            case 11:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_11_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_11));
            case 12:
                return Preferences.getMoodColor(
                        context,
                        context.getString(R.string.pref_mood_12_color_key),
                        ContextCompat.getColor(context, R.color.mood_color_12));
            default:
                return 0xFFFFFFFF;
        }
    }

    public static int[] getColors(Context context) {
        int[] colors = new int[12];

        colors[0] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_1_color_key),
                ContextCompat.getColor(context, R.color.mood_color_1));
        colors[1] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_2_color_key),
                ContextCompat.getColor(context, R.color.mood_color_2));
        colors[2] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_3_color_key),
                ContextCompat.getColor(context, R.color.mood_color_3));
        colors[3] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_4_color_key),
                ContextCompat.getColor(context, R.color.mood_color_4));
        colors[4] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_5_color_key),
                ContextCompat.getColor(context, R.color.mood_color_5));
        colors[5] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_6_color_key),
                ContextCompat.getColor(context, R.color.mood_color_6));
        colors[6] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_7_color_key),
                ContextCompat.getColor(context, R.color.mood_color_7));
        colors[7] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_8_color_key),
                ContextCompat.getColor(context, R.color.mood_color_8));
        colors[8] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_9_color_key),
                ContextCompat.getColor(context, R.color.mood_color_9));
        colors[9] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_10_color_key),
                ContextCompat.getColor(context, R.color.mood_color_10));
        colors[10] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_11_color_key),
                ContextCompat.getColor(context, R.color.mood_color_11));
        colors[11] = Preferences.getMoodColor(
                context,
                context.getString(R.string.pref_mood_12_color_key),
                ContextCompat.getColor(context, R.color.mood_color_12));

        return colors;
    }

    public static String getMoodLabel(Context context, int moodId) {
        switch (moodId) {
            case 1:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_1_label_key),
                        context.getString(R.string.label_mood_1));
            case 2:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_2_label_key),
                        context.getString(R.string.label_mood_2));
            case 3:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_3_label_key),
                        context.getString(R.string.label_mood_3));
            case 4:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_4_label_key),
                        context.getString(R.string.label_mood_4));
            case 5:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_5_label_key),
                        context.getString(R.string.label_mood_5));
            case 6:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_6_label_key),
                        context.getString(R.string.label_mood_6));
            case 7:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_7_label_key),
                        context.getString(R.string.label_mood_7));
            case 8:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_8_label_key),
                        context.getString(R.string.label_mood_8));
            case 9:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_9_label_key),
                        context.getString(R.string.label_mood_9));
            case 10:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_10_label_key),
                        context.getString(R.string.label_mood_10));
            case 11:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_11_label_key),
                        context.getString(R.string.label_mood_11));
            default:
                return Preferences.getMoodLabel(
                        context,
                        context.getString(R.string.pref_mood_12_label_key),
                        context.getString(R.string.label_mood_12));
        }
    }

    public static String[] getMoodLabels(Context context) {
        String[] labels = new String[12];
        labels[0] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_1_label_key),
                context.getString(R.string.label_mood_1));
        labels[1] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_2_label_key),
                context.getString(R.string.label_mood_2));
        labels[2] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_3_label_key),
                context.getString(R.string.label_mood_3));
        labels[3] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_4_label_key),
                context.getString(R.string.label_mood_4));
        labels[4] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_5_label_key),
                context.getString(R.string.label_mood_5));
        labels[5] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_6_label_key),
                context.getString(R.string.label_mood_6));
        labels[6] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_7_label_key),
                context.getString(R.string.label_mood_7));
        labels[7] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_8_label_key),
                context.getString(R.string.label_mood_8));
        labels[8] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_9_label_key),
                context.getString(R.string.label_mood_9));
        labels[9] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_10_label_key),
                context.getString(R.string.label_mood_10));
        labels[10] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_11_label_key),
                context.getString(R.string.label_mood_11));
        labels[11] = Preferences.getMoodLabel(
                context,
                context.getString(R.string.pref_mood_12_label_key),
                context.getString(R.string.label_mood_12));
        return labels;
    }

    public static String[] getMonthsNames(Context context) {
        String[] months = new String[12];
        months[0] = context.getString(R.string.january);
        months[1] = context.getString(R.string.february);
        months[2] = context.getString(R.string.march);
        months[3] = context.getString(R.string.april);
        months[4] = context.getString(R.string.may);
        months[5] = context.getString(R.string.june);
        months[6] = context.getString(R.string.july);
        months[7] = context.getString(R.string.august);
        months[8] = context.getString(R.string.september);
        months[9] = context.getString(R.string.october);
        months[10] = context.getString(R.string.november);
        months[11] = context.getString(R.string.december);

        return months;
    }

    public static float getScreenDensity(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) windowManager.getDefaultDisplay().getMetrics(displayMetrics);

//        return new float[]{
//                (float) displayMetrics.widthPixels,
//                (float) displayMetrics.heightPixels,
//                displayMetrics.density};
        return displayMetrics.density;
    }

    public static boolean isPortraitMode(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isDarkColor(int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            return Color.luminance(color) < 0.3;
//        } else {
//            return ColorUtils.calculateLuminance(color) < 0.3;
//        }

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                Color.luminance(color) < 0.3 : ColorUtils.calculateLuminance(color) < 0.3;
    }
}
