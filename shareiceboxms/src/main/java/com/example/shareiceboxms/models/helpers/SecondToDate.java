package com.example.shareiceboxms.models.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    /*
       *今天日期
       **/
    public static String[] getDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return new String[]{formatter.format(date) + " 00:00", formatter.format(date) + " 23:59"};
    }

    /*
    *本周开始日期
    **/
    public static String getWeek() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int date = ca.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + date;
    }

    /*
    *本月开始日期
    * */
    public static String getTimeOfMonthStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int date = ca.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + date;
    }

    /*
    * 本年的开始日期
    * */
    public static String getTimeOfYearStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_YEAR, 1);
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int date = ca.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + date;
    }
}
