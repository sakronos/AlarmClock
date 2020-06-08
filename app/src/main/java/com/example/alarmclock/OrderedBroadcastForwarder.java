package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OrderedBroadcastForwarder extends BroadcastReceiver {

    public static final String ACTION_NAME = "action";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent forwardIntent = new Intent(context,AlarmBroadcast.class);
        //forwardIntent.putExtras(intent);
        //forwardIntent.removeExtra(ACTION_NAME);
        forwardIntent.setAction("com.example.alarmbell.startAlarm");
        context.sendOrderedBroadcast(forwardIntent, null);
    }
}
