package com.example.alarmclock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmBroadcast extends BroadcastReceiver {
    @SuppressLint("ShowToast")
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("startAlarm".equals(intent.getAction())) {
            //Toast.makeText(context, "Alarm Clock", Toast.LENGTH_LONG).show();

            Intent bell_intent = new Intent(context, BellRingsActivity.class);
            bell_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            bell_intent.putExtra("time", new SimpleDateFormat("HH:mm", Locale.CHINA).format(calendar.getTime()));
            context.startActivity(bell_intent);

        }
    }
}
