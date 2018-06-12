package otherutis;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WH on 2017/8/7.
 */

public class TimeUtil {
    /*
    * 将字符串转换为指定格式的日期
    * */
    public static Date str2Date(String format, String dateStr) {//"yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateStr);
            return date;
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    /*
    * 比较两个日期的大小
    * */
    public static int compareTime(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return 1;
        }
        if (date1.getTime() == date2.getTime()) {
            return 0;
        }
        if (date1.getTime() < date2.getTime()) {
            return -1;
        }
        return 0;
    }
}
