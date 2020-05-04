package com.tgc.appledora.habitica;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
public class reminderUpdate_Service extends IntentService {

    private static final String ACTION_CANCEL = "com.maseno.franklinesable.lifereminder.action.CANCEL";
    private static final String ACTION_SNOOZE = "com.maseno.franklinesable.lifereminder.action.UPDATE";
    private static final String EXTRA_CANCEL = "com.maseno.franklinesable.lifereminder.extra.CANCEL";
    private static final String EXTRA_SNOOZE = "com.maseno.franklinesable.lifereminder.extra.SNOOZE";
    private static final String EXTRA_ID = "com.maseno.franklinesable.lifereminder.extra.ID";
    private static final String EXTRA_INTERVAL = "com.maseno.franklinesable.lifereminder.INTERVAL";
    public static final String DATE_TIME_FORMAT = "MM/dd/yy HH:mm:ss";

    public reminderUpdate_Service() {
        super("reminderUpdate_Service");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CANCEL.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CANCEL);
                handleActionCancel(param1);
            } else if (ACTION_SNOOZE.equals(action)) {
                final String requestCode = intent.getStringExtra(EXTRA_SNOOZE);
                final long rowId = intent.getLongExtra(EXTRA_ID, 0);
                final int interval = intent.getIntExtra(EXTRA_INTERVAL, 5);
                handleActionSnooze(requestCode, rowId, interval);
            }
        }
    }

    private void handleActionCancel(String requestCode) {

        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(reminder_Service.TAG, Integer.parseInt(requestCode));
    }


    private void handleActionSnooze(String requestCode, long rowId, int interval) {

        app_database dbHandler = new app_database(this);
        dbHandler.open();
        Calendar mCalendar = Calendar.getInstance();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        int currentMinute = mCalendar.get(Calendar.MINUTE);
        int currentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int setMinute = currentMinute + interval;

        if (setMinute > 60) {

            setMinute = setMinute - 60;
            currentHour += 1;
            mCalendar.set(Calendar.HOUR_OF_DAY, currentHour);
            mCalendar.set(Calendar.MINUTE, setMinute);
        } else {
            mCalendar.set(Calendar.MINUTE, setMinute);
        }

        String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());
        dbHandler.updateTimeOnlyDb(rowId, reminderDateTime);

        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(reminder_Service.TAG, Integer.parseInt(requestCode));
        new reminder_Manager(getApplicationContext()).setReminderAlarm(rowId, mCalendar, true);
    }
}
