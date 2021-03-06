/*
 * <Count streak by daily working on a task>
 *  Copyright (C) <2019>  <Sanjeev Yadav>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tgc.appledora.habitica.streakCounter.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.streakCounter.db.entity.TaskEntity;
import com.tgc.appledora.habitica.streakCounter.ui.detail_task.TaskDetailActivity;
import com.tgc.appledora.habitica.streakCounter.ui.detail_task.TaskDetailFragment;

import java.util.Calendar;

public final class NotificationUtils {

    private NotificationUtils() {
    }

    public static void scheduleAlarmToTriggerNotification(Context context, TaskEntity taskEntity) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) return;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(taskEntity.getTime());

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, createPendingIntentForAlarm(context, taskEntity));
    }

    public static void cancelAlarmToTriggerNotification(Context context, TaskEntity taskEntity) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(createPendingIntentForAlarm(context, taskEntity));
        }
    }


    private static PendingIntent createPendingIntentForAlarm(Context context, TaskEntity taskEntity) {
        Intent intent = new Intent(context.getApplicationContext(), NotificationPublisher.class);
        intent.putExtra(NotificationPublisher.NOTIFICATION_ID, taskEntity.getId());
        intent.putExtra(NotificationPublisher.NOTIFICATION, generateNotification(context, taskEntity));
        return PendingIntent.getBroadcast(
                context.getApplicationContext(), taskEntity.getId(), intent, 0);
    }

    /*
     * Generates a notification to be shown
     */
    private static Notification generateNotification(Context context, TaskEntity taskEntity) {

        String notificationChannelId = NotificationUtils.createNotificationChannel(context);


        // 3. Set up main Intent for notification.
        Intent detailActivity = new Intent(context, TaskDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TaskDetailFragment.ARG_TASK_ID, taskEntity.getId());
        detailActivity.putExtras(bundle);
        detailActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Construct the PendingIntent for your Notification
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // This uses android:parentActivityName and
        // android.support.PARENT_ACTIVITY meta-data by default
        stackBuilder.addNextIntentWithParentStack(detailActivity);

        PendingIntent pendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(
                context.getApplicationContext(),
                notificationChannelId);

        //GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        return notificationCompatBuilder
                .setAutoCancel(true)
                // BIG_TEXT_STYLE sets title and content for API 16 (4.1 and after).
                //.setStyle(bigTextStyle)
                // Title for API <16 (4.0 and below) devices.
                .setContentTitle(context.getString(R.string.notification_title))
                // Content for API <24 (7.0 and below) devices.
                .setContentText(taskEntity.getTitle())
                .setSmallIcon(R.drawable.ic_flame)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.ic_flame))
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                // Set primary color (important for Wear 2.0 Notifications).
                .setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary))

                // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
                // devices and all Wear devices. If you have more than one notification and
                // you prefer a different summary notification, set a group key and create a
                // summary notification via
                // .setGroupSummary(true)
                // .setGroup(GROUP_KEY_YOUR_NAME_HERE)

                .setCategory(Notification.CATEGORY_REMINDER)

                // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
                // 'importance' which is set in the NotificationChannel. The integers representing
                // 'priority' are different from 'importance', so make sure you don't mix them.
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
                // visibility is set in the NotificationChannel.
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)

                // Adds additional actions specified above.
                //.addAction(snoozeAction)
                //.addAction(dismissAction)
                .build();
    }

    private static String createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = context.getString(R.string.channel_name);

            // The user-visible name of the channel.
            CharSequence channelName = context.getString(R.string.channel_name);
            // The user-visible description of the channel.
            String channelDescription = context.getString(R.string.channel_description);
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            boolean channelEnableVibrate = false;
            int channelLockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }
}
