package com.tgc.appledora.habitica;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by Frankline Sable on 7/22/2016. Set alarm
 */
public class reminder_Manager {

    private Context context;

    public reminder_Manager(Context context) {
        this.context = context;
    }

    public void setReminderAlarm(Long taskId, Calendar mCalendar, boolean valid) {

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Intent intent = new Intent(context, AlarmReceiver.class);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(app_database.tb_Struct.KEY_ROW_ID, (long) taskId);
        int alarmID = Integer.parseInt(String.valueOf(taskId));

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (valid) {

            if (sharedPref.getBoolean("notification_reminder_complete", true)) {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), alarmIntent);
                enableBootReceiver();
            }
        } else {
            alarmMgr.cancel(alarmIntent);
            alarmIntent.cancel();
        }
    }

    public void enableBootReceiver() {

        ComponentName receiver = new ComponentName(context, boot_Receiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

}
