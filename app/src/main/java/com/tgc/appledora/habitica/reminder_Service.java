package com.tgc.appledora.habitica;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.maseno.franklinesable.lifereminder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Frankline Sable on 7/22/2016. Receive alarm
 */
public class reminder_Service extends MyWakefulReceiver {

    private static final String ACTION_CANCEL = "com.maseno.franklinesable.lifereminder.action.CANCEL";
    private static final String ACTION_SNOOZE = "com.maseno.franklinesable.lifereminder.action.UPDATE";
    private static final String EXTRA_CANCEL = "com.maseno.franklinesable.lifereminder.extra.CANCEL";
    private static final String EXTRA_SNOOZE = "com.maseno.franklinesable.lifereminder.extra.SNOOZE";
    private static final String EXTRA_ID = "com.maseno.franklinesable.lifereminder.extra.ID";
    private static final String EXTRA_INTERVAL = "com.maseno.franklinesable.lifereminder.INTERVAL";
    private int requestCode;
    private long rowId;
    private boolean error_Ocurred = true;

    public static final String TAG = "com.maseno.franklinesable.lifereminder";
    private int intervalEnabled = 0, repeatMode;

    public reminder_Service() {
        super("reminder_Service");
    }

    private String note_title, note_body;
    private Calendar mCalendar = Calendar.getInstance();
    app_database dbHandler;

    @Override
    void doReminderWork(Intent intent) {

        dbHandler = new app_database(this);
        rowId = intent.getExtras().getLong(app_database.tb_Struct.KEY_ROW_ID);
        requestCode = Integer.parseInt(String.valueOf(rowId));

        new Thread(() -> {
            dbHandler.open();
            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(reminder_Service.this);
            final Cursor cursor = dbHandler.readDb(app_database.tb_Struct.KEY_ROW_ID + "=" + rowId, null, null);

            new Handler(getMainLooper()).post(() -> {
                try {
                    note_title = cursor.getString(cursor.getColumnIndex(app_database.tb_Struct.KEY_TITLE));
                    note_body = cursor.getString(cursor.getColumnIndex(app_database.tb_Struct.KEY_BODY));
                    configureRepeatMode(cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_REPEAT)),
                            cursor.getString(cursor.getColumnIndex(app_database.tb_Struct.KEY_SCHEDULED_DATE)));

                    error_Ocurred = false;
                } catch (Exception e) {
                    error_Ocurred = true;
                } finally {
                    if (!error_Ocurred) {

                        final Bitmap picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        final String ticker = getString(R.string.ticker_text);
                        final String title = getResources().getString(R.string.title_and_description, ticker);
                        final String text = note_title + "\n" + note_body;
                        intervalEnabled = Integer.parseInt(sharedPref.getString("snooze_frequency", "5"));

                        Intent resultIntent = new Intent(reminder_Service.this, reminder_view.class);
                        resultIntent.putExtra(app_database.tb_Struct.KEY_ROW_ID, rowId);

                        Intent cancel_Intent = new Intent(reminder_Service.this, reminderUpdate_Service.class);
                        cancel_Intent.setAction(ACTION_CANCEL);
                        cancel_Intent.putExtra(EXTRA_CANCEL, Integer.toString(requestCode));


                        final NotificationCompat.Builder builder = new NotificationCompat.Builder(reminder_Service.this)
                                .setDefaults(Notification.DEFAULT_LIGHTS)
                                .setSmallIcon(R.drawable.ic_reminder_notification)
                                .setContentTitle(note_title + " has completed")
                                .setContentText(note_body)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setLargeIcon(picture)
                                .setTicker(ticker).setSound(Uri.parse(sharedPref.getString("notification_reminder_complete_ringtone", "")))
                                .setNumber(1)
                                .setContentIntent(PendingIntent.getActivity(reminder_Service.this, requestCode,
                                        resultIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT))
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(text)
                                        .setBigContentTitle(title)
                                        .setSummaryText("Click to view reminder"))
                                .addAction(R.drawable.ic_snooze_time, "SNOOZE", snoozeEnabled()
                                )
                                .addAction(R.drawable.ic_dismiss_notification, "DISMISS", PendingIntent.getService(reminder_Service.this, requestCode, cancel_Intent, PendingIntent.FLAG_ONE_SHOT))
                                .setAutoCancel(true);

                        if (sharedPref.getBoolean("notification_reminder_complete_vibrate", true))
                            builder.setDefaults(Notification.DEFAULT_VIBRATE);

                        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.notify(TAG, requestCode, builder.build());
                    }
                }
            });
        }).start();
    }

    public PendingIntent snoozeEnabled() {
        PendingIntent checkSnooze;
        if (intervalEnabled >= 0) {
            Intent snooze_Intent = new Intent(this, reminderUpdate_Service.class);
            snooze_Intent.setAction(ACTION_SNOOZE);
            snooze_Intent.putExtra(EXTRA_SNOOZE, Integer.toString(requestCode));
            snooze_Intent.putExtra(EXTRA_INTERVAL, intervalEnabled);
            snooze_Intent.putExtra(EXTRA_ID, rowId);

            checkSnooze = PendingIntent.getService(this, requestCode, snooze_Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {
            checkSnooze = null;
        }
        return checkSnooze;
    }

    public void configureRepeatMode(int repeatMode, String date) {

        SimpleDateFormat timeFmt = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);
        try {
            Date date2 = timeFmt.parse(date);
            mCalendar.setTime(date2);
        } catch (ParseException e) {/*ignore error silently*/
        }
        int currentDate = mCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = mCalendar.get(Calendar.MONTH);
        int currentYear = mCalendar.get(Calendar.YEAR);
        int daysTotalMonth = mCalendar.getActualMaximum(Calendar.DATE);

        switch (repeatMode) {
            case 0:
                //do nothing
                break;
            case 1:
                int setDate = currentDate + 1;
                if (setDate > daysTotalMonth) {

                    setDate = setDate - daysTotalMonth;
                    currentMonth += 1;
                    mCalendar.set(Calendar.DAY_OF_MONTH, setDate);
                    mCalendar.set(Calendar.MONTH, currentMonth);
                } else {
                    mCalendar.set(Calendar.DAY_OF_MONTH, setDate);

                }
                break;
            case 2:
                int setWeek = currentDate + 7;
                if (setWeek > daysTotalMonth) {

                    setDate = setWeek - daysTotalMonth;
                    currentMonth += 1;
                    mCalendar.set(Calendar.DAY_OF_MONTH, setDate);
                    mCalendar.set(Calendar.MONTH, currentMonth);
                } else {
                    mCalendar.set(Calendar.DAY_OF_MONTH, setWeek);

                }
                break;
            case 3:
                int set2Week = currentDate + 14;
                if (set2Week > daysTotalMonth) {

                    setDate = set2Week - daysTotalMonth;
                    currentMonth += 1;
                    mCalendar.set(Calendar.DAY_OF_MONTH, setDate);
                    mCalendar.set(Calendar.MONTH, currentMonth);
                } else {
                    mCalendar.set(Calendar.DAY_OF_MONTH, set2Week);

                }
                break;
            case 4:
                int newMonth = currentMonth + 1;
                if (newMonth > 12) {
                    newMonth = newMonth - 12;
                }
                mCalendar.set(Calendar.MONTH, newMonth);
                break;
            case 5:
                int setYear = currentYear + 1;
                mCalendar.set(Calendar.YEAR, setYear);
                break;
        }
        if (repeatMode != 0) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);
            final String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());
            dbHandler.updateTimeOnlyDb(rowId, reminderDateTime);
            new reminder_Manager(this).setReminderAlarm(rowId, mCalendar, true);
        }
    }
}
