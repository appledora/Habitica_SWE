package com.tgc.appledora.habitica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long rowId = intent.getExtras().getLong(app_database.tb_Struct.KEY_ROW_ID);
        MyWakefulReceiver.acquireStaticLock(context);
        Intent serviceIntent = new Intent(context, reminder_Service.class);
        serviceIntent.putExtra(app_database.tb_Struct.KEY_ROW_ID, rowId);
        context.startService(serviceIntent);
    }
}
