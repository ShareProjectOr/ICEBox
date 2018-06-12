package otherutils;

import android.content.Context;

import com.example.zhazhijiguanlixitong.R;

/**
 * Created by Administrator on 2017/8/4.
 */

public class FormatLongToTimeStr {

    public static String formatLongToTimeStr(Context context,Long l) {
        int hour = 0;
        int minute = 0;
        int second;
        int day = 0;
        second = l.intValue();
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
            hour = hour & 24;
        }
        // return hour + "：" + minute + "：" + second;
        return minute + context.getString(R.string.fen) + second + context.getString(R.string.mia);

    }
}
