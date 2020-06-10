package com.example.alarmclock;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;


public class AlarmBellTime extends LitePalSupport {
    @Column(nullable = false)
    private Date alarm_time;
    private String name;
    private boolean skip;

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getName() {
        return name;
    }

    public Date getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(Date alarm_time) {
        this.alarm_time = alarm_time;
    }

    public void setName(String name) {
        this.name = name;
    }


}
