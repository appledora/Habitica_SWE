package com.tgc.appledora.habitica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class boot_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            ////Set the alarm here
        }
    }
}
