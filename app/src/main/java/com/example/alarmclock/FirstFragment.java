package com.example.alarmclock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.weigan.loopview.LoopView;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

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

        final TextView showTime=view.findViewById(R.id.textView);
        final WheelView selectHour = view.findViewById(R.id.options1);
        final WheelView selectMinute = view.findViewById(R.id.options2);
        selectHour.setCyclic(true);
        final List<Integer> mHours = new ArrayList<>();
        for (int i=0; i<24; i++){
            mHours.add(i);
        }
        selectHour.setAdapter(new ArrayWheelAdapter(mHours));
        selectHour.setOnItemSelectedListener(new com.contrarywind.listener.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                showTime.setText(selectHour.getCurrentItem()+":"+selectMinute.getCurrentItem());
            }
        });


        selectMinute.setCyclic(true);
        final List<Integer> mMinutes = new ArrayList<>();
        for (int i=0; i<60; i++){
            mMinutes.add(i);
        }
        selectMinute.setAdapter(new ArrayWheelAdapter(mMinutes));
        selectMinute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                showTime.setText(selectHour.getCurrentItem()+":"+selectMinute.getCurrentItem());
            }
        });

    }
}
