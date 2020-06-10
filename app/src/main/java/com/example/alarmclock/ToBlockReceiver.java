package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePal;

import java.util.List;

public class ToBlockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SQLiteDatabase db = LitePal.getDatabase();
        boolean skip = false;
        final List<AlarmBellTime> alarmBellTimeList = LitePal.findAll(AlarmBellTime.class);
        if (!alarmBellTimeList.isEmpty()) {

            AlarmBellTime alarmBellTime = LitePal.find(AlarmBellTime.class,1);
            alarmBellTime.setSkip(true);
            alarmBellTime.save();
        }
    }
}
