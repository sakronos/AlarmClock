package com.example.alarmclock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.weigan.loopview.LoopView;

import org.litepal.LitePal;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.core.content.ContextCompat.getSystemService;

public class FirstFragment extends Fragment {

    public AlarmManager alarmManager;

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    private PendingIntent pendingIntent;
    private TextView showTime;
    private Calendar calendar;
    private WheelView selectHour;
    private WheelView selectMinute;
    SQLiteDatabase db;
    private String channelID = "1";
    private  String channelName = "AlarmBlock";


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        showTime = view.findViewById(R.id.textView);
        selectHour = view.findViewById(R.id.options1);
        selectMinute = view.findViewById(R.id.options2);


        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        selectHour.setCyclic(true);
        selectHour.setCurrentItem(calendar.getTime().getHours());
        final List<Integer> mHours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            mHours.add(i);
        }
        selectHour.setAdapter(new ArrayWheelAdapter(mHours));
        selectHour.setOnItemSelectedListener(new com.contrarywind.listener.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(int index) {
                updateTimeOnScroll();
                //showTime.setText(selectHour.getCurrentItem()+":"+selectMinute.getCurrentItem());
                //showTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(calendar.getTime()));
            }
        });


        selectMinute.setCyclic(true);
        selectMinute.setCurrentItem(calendar.getTime().getMinutes());
        final List<Integer> mMinutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            mMinutes.add(i);
        }
        selectMinute.setAdapter(new ArrayWheelAdapter(mMinutes));
        selectMinute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(int index) {
                updateTimeOnScroll();
                //showTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(calendar.getTime()));
            }
        });

        db = LitePal.getDatabase();
        final List<AlarmBellTime> alarmBellTimeList = LitePal.findAll(AlarmBellTime.class);
        if (!alarmBellTimeList.isEmpty()) {
            showTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(alarmBellTimeList.get(0).getAlarm_time()));
            calendar.set(Calendar.HOUR_OF_DAY, alarmBellTimeList.get(0).getAlarm_time().getHours());
            calendar.set(Calendar.MINUTE, alarmBellTimeList.get(0).getAlarm_time().getMinutes());
            calendar.set(Calendar.SECOND, 0);
            //alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            setAlarm(calendar);
        }

        //设置闹钟
        final Button set_AlarmClock = view.findViewById(R.id.btn_alarm);
        set_AlarmClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                setAlarm(calendar);
                LitePal.deleteAll(AlarmBellTime.class, "name like ?", "name");
                AlarmBellTime alarmBellTime = new AlarmBellTime();
                alarmBellTime.setAlarm_time(calendar.getTime());
                alarmBellTime.setName("name");
                alarmBellTime.setSkip(false);
                alarmBellTime.save();
                //List<AlarmBellTime> alarmBellTimeList = LitePal.findAll(AlarmBellTime.class);

                Snackbar.make(v, new SimpleDateFormat("闹钟设置为" + "HH:mm", Locale.CHINA).format(calendar.getTime()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });


        Intent intent = new Intent(getContext(), OrderedBroadcastForwarder.class);
        //intent.putExtra(OrderedBroadcastForwarder.ACTION_NAME, "com.example.alarmbell.startAlarm");
        pendingIntent = PendingIntent.getBroadcast(getContext(), 110, intent, PendingIntent.FLAG_CANCEL_CURRENT);

//        Intent intent = new Intent(getContext(), AlarmBroadcast.class);
//        intent.setAction("startAlarm");
//        pendingIntent = PendingIntent.getBroadcast(getContext(), 110, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        Button cancel_AlarmClock = view.findViewById(R.id.btn_alarm_stop);
        cancel_AlarmClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                    alarmManager = null;
                }
                LitePal.deleteAll(AlarmBellTime.class, "name like ?", "name");
                showTime.setText("");
            }
        });

        Button skip_Alarm = view.findViewById(R.id.stop_alarm_once);
        skip_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmManager != null){
                    //alarmManager.cancel(pendingIntent);
                }
            }
        });
    }

    private void updateTimeOnScroll() {
        calendar.set(Calendar.HOUR_OF_DAY, selectHour.getCurrentItem());
        calendar.set(Calendar.MINUTE, selectMinute.getCurrentItem());
        calendar.set(Calendar.SECOND, 0);
    }


    //设置闹钟
    private void setAlarm(Calendar calendar) {
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 5), pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);
        showTime.setText(new SimpleDateFormat("HH:mm", Locale.CHINA).format(calendar.getTime()));
        //Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();

        Intent blockIntent = new Intent(getContext(),ToBlockReceiver.class);
        blockIntent.setAction("Block");
        //blockIntent.putExtra("alarmIntent",pendingIntent);
        PendingIntent blockPendingIntent = PendingIntent.getBroadcast(getContext(),110,blockIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new NotificationCompat.Builder(getContext(),channelID)
                .setContentTitle("闹钟")
                .setContentText(new SimpleDateFormat("闹钟设置为" + "HH:mm", Locale.CHINA).format(calendar.getTime()))
                .setWhen(System.currentTimeMillis())            //此处待修改为闹钟响起前1H
                .setSmallIcon(R.drawable.ic_launcher_background)
                .addAction(R.drawable.ic_launcher_foreground,"Cancel once",blockPendingIntent)
                .build();
        notificationManager.notify(1,notification);
    }
}
