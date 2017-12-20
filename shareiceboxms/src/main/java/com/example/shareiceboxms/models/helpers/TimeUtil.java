package com.example.shareiceboxms.models.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WH on 2017/12/20.
 */

public class TimeUtil {
    /**
     * 秒数转化为日期
     */
    public static String getDateFromSeconds(String seconds) {
        if (seconds == null)
            return " ";
        else {
            Date date = new Date();
            try {
                date.setTime(Long.parseLong(seconds));
            } catch (NumberFormatException nfe) {

            }
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd天hh时mm分ss秒");
            return sdf.format(date);
        }
    }

}
