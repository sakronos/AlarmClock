package com.example.alarmclock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.weigan.loopview.LoopView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.core.content.ContextCompat.getSystemService;

public class FirstFragment extends Fragment {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TextView showTime;
    private Calendar calendar;
    private WheelView selectHour;
    private WheelView selectMinute;

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

        showTime=view.findViewById(R.id.textView);
        selectHour = view.findViewById(R.id.options1);
        selectMinute = view.findViewById(R.id.options2);


        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        selectHour.setCyclic(true);
        selectHour.setCurrentItem(calendar.getTime().getHours());
        final List<Integer> mHours = new ArrayList<>();
        for (int i=0; i<24; i++){
            mHours.add(i);
        }
        selectHour.setAdapter(new ArrayWheelAdapter(mHours));
        selectHour.setOnItemSelectedListener(new com.contrarywind.listener.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(int index) {
                updateTimeOnScroll();
                //showTime.setText(selectHour.getCurrentItem()+":"+selectMinute.getCurrentItem());
                showTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(calendar.getTime()));
            }
        });


        selectMinute.setCyclic(true);
        selectMinute.setCurrentItem(calendar.getTime().getMinutes());
        final List<Integer> mMinutes = new ArrayList<>();
        for (int i=0; i<60; i++){
            mMinutes.add(i);
        }
        selectMinute.setAdapter(new ArrayWheelAdapter(mMinutes));
        selectMinute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(int index) {
                updateTimeOnScroll();
                showTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(calendar.getTime()));
            }
        });

        final Button set_AlarmClock = view.findViewById(R.id.btn_alarm);
        set_AlarmClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm(calendar);
            }
        });

        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(),AlarmBroadcast.class);
        intent.setAction("startAlarm");
        pendingIntent = PendingIntent.getBroadcast(getContext(),110,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        //alarmManager.setTimeZone("Asia/Shanghai");
    }

    private void updateTimeOnScroll(){
        calendar.set(Calendar.HOUR_OF_DAY,selectHour.getCurrentItem());
        calendar.set(Calendar.MINUTE,selectMinute.getCurrentItem());
        calendar.set(Calendar.SECOND,0);
    }



    // 设置闹钟
    private void setAlarm(Calendar calendar) {
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 5), pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
        showTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(calendar.getTime()));
        Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();
    }
}
