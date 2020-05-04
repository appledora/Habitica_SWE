package com.tgc.appledora.habitica;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by Frankline Sable on 7/22/2016. From Maseno University in Kenya. LifeReminderEpic
 */
public abstract class MyWakefulReceiver extends IntentService {

    abstract void doReminderWork(Intent intent);

    public static final String LOCK_NAME_STATIC = "com.cct.franklinesable.lifereminder.Static";

    private static PowerManager.WakeLock lockStatic = null;

    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }

    synchronized private static PowerManager.WakeLock getLock(Context context) {

        if (lockStatic == null) {
            PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            lockStatic = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return (lockStatic);
    }

    public MyWakefulReceiver(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            doReminderWork(intent);
        } finally {
            getLock(this).release();
        }
    }
}