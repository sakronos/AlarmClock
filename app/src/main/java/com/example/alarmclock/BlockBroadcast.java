package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BlockBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Block","Received");
        //AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SQLiteDatabase db = LitePal.getDatabase();
        boolean skip = false;
        final List<AlarmBellTime> alarmBellTimeList = LitePal.findAll(AlarmBellTime.class);
        if (!alarmBellTimeList.isEmpty()) {
            skip = alarmBellTimeList.get(0).isSkip();
            if (skip){
                abortBroadcast();
            }
            AlarmBellTime alarmBellTime = LitePal.find(AlarmBellTime.class,1);
            alarmBellTime.setSkip(false);
            alarmBellTime.save();
        }




    }
}
