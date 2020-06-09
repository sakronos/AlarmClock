package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class OrderedBroadcastForwarder extends BroadcastReceiver {

    private BroadcastReceiver block;
    private BroadcastReceiver alarm;
    //public static final String ACTION_NAME = "action";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent forwardIntent = new Intent();
        forwardIntent.setAction("startAlarm");
        forwardIntent.putExtras(intent);
        forwardIntent.setComponent(new ComponentName("com.example.alarmclock","com.example.alarmclock.AlarmBroadcast"));


        //Intent forwardIntent = new Intent(context,AlarmBroadcast.class);


        //forwardIntent.putExtras(intent);
        //forwardIntent.removeExtra(ACTION_NAME);
        //forwardIntent.setAction("com.example.alarmbell.startAlarm");
        context.sendOrderedBroadcast(forwardIntent, null);
        Log.d("Forwarder", "Ordered");
    }
}
