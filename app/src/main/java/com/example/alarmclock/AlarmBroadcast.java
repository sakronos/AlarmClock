package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Alarm Clock", Toast.LENGTH_LONG).show();
        Log.d("Alarm","Received");
        Intent bell_intent = new Intent(context, BellRingsActivity.class);
        bell_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        bell_intent.putExtra("time", new SimpleDateFormat("HH:mm", Locale.CHINA).format(calendar.getTime()));
        context.startActivity(bell_intent);


    }
}
