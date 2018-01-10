package com.example.shareiceboxms.models.helpers;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SecondToDate {
    public static int TODAY_CODE = 0;
    public static int WEEK_CODE = 1;
    public static int MONTH_CODE = 2;
    public static int YEAR_CODE = 3;
    public static String FORMAT_TYPE = "yyyy-MM-dd HH:mm:ss";

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
    * 获取日期数组参数
    * */
    public static String[] getDateParams(int code) {
        String[] date = getDay();
        switch (code) {
            case 0:
                break;
            case 1:
                date[0] = getWeek();
                break;
            case 2:
                date[0] = getTimeOfMonthStart();
                break;
            case 3:
                date[0] = getTimeOfYearStart();
                break;
        }
        return date;
    }

    public static String getDateUiShow(String[] time) {
        String[] dateShow = new String[2];
        dateShow[0] = time[0].replace(" 00:00:00", "");
        dateShow[1] = time[1].replace(" 23:59:59", "");
        return dateShow[0] + " 至 " + dateShow[1];
    }

    public static String getTimePart() {
        String timePart;
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        if (11 <= hour) {
            if (14 <= hour) {
                timePart = "下午好";
            } else {
                timePart = "中午好";
            }
        } else {
            timePart = "上午好";
        }
        return timePart;
    }

    /*
       *今天日期
       **/
    public static String[] getDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return new String[]{formatter.format(date) + " 00:00:00", formatter.format(date) + " 23:59:59"};
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
        return year + "-" + month + "-" + date + " 00:00:00";
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
        return year + "-" + month + "-" + date + " 00:00:00";
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
        return year + "-" + month + "-" + date + " 00:00:00";
    }

    /*
 * 日期转化为字符串
 * */
    public static String getStrOfDate(Date date) throws ParseException {
        return new SimpleDateFormat(FORMAT_TYPE).format(date);
    }

    /*
    * 字符串转换为日期
    * */
    public static Date getDateOfString(String dateStr) throws ParseException {
        if ("".equals(dateStr) || dateStr == null) {
            return null;
        } else {
            SimpleDateFormat format = new SimpleDateFormat(FORMAT_TYPE);
            Date date = format.parse(dateStr);
            return date;
        }
    }

    /*
    * 将long型转化为date型
    * */
    public static Date getDateOfLong(long dateLong) throws ParseException {
        Date date = new Date(dateLong);
        String dateStr = getStrOfDate(date);
        return getDateOfString(dateStr);
    }

    /*
    * date转化为long型
    * */
    public static long getLongOfDate(Date date) throws ParseException {
        return date.getTime();
    }

    /*
    * 获取两个日期之间差值 long型
    * */
    public static long getSubOfDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return 0;
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long subTime = Math.abs(endTime - startTime);
        return subTime;
    }

    /*  
  * 将时间戳 转化为日期
  */
    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_TYPE);
        return format.format(date);
    }

    /*  
  * 将时间转换为时间戳 
  */
    public static long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_TYPE);
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
    }


    /*
  * 将两string差值直接转换为天时分秒
  * */
    public static String getSubString(String startTime, String endTime) throws ParseException {
        String timeStr = "";
        String[] time = SecondToDate.formatLongToTimeStr(
                SecondToDate.getSubOfDates(SecondToDate.getDateOfString(startTime)
                        , SecondToDate.getDateOfString(endTime)) / 1000);
        if (!time[0].equals("0")) {
            timeStr += time[0] + "天";
        }
        if (!time[1].equals("0")) {
            timeStr += time[1] + "时";
        }
        if (!time[2].equals("0")) {
            timeStr += time[2] + "分";
        }
        if (!time[3].equals("0")) {
            timeStr += time[3] + "秒";
        }
        if (TextUtils.equals(timeStr, "")) {
            return "已过期";
        }
        return timeStr;
    }
}
