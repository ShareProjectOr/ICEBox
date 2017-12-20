package com.example.shareiceboxms.models.helpers;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SecondToDate {
    public static String[] formatLongToTimeStr(Long seconds) {
        String[] time = new String[4];
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second;
        second = seconds.intValue();
        if (second > 60) {
            minute = second / 60;         //取整
            second = second % 60;         //取余
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        if (hour > 24) {
            day = hour / 24;
            hour = hour % 24;
        }
        time[0] = String.valueOf(day);
        time[1] = String.valueOf(hour);
        time[2] = String.valueOf(minute);
        time[3] = String.valueOf(second);
        return time;

    }
}
