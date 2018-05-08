package example.jni.com.coffeeseller.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WH on 2018/5/1.
 */

public class TextUtil {
    //筛选字符串中汉字的个数
    public static int getCount(String text) {
        String regex = "[\\u4e00-\\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static int textSize(String text) {
        int textCount = getCount(text);
        if (textCount <= 3) {
            return 30;
        }
        return 25;
    }

    public static String textPointNum(String text, int pointNum) {
        if (pointNum == 2) {
            return text + ".";
        }
        if (pointNum == 1) {
            return text + "..";
        }
        if (pointNum == 0) {
            return text + "...";
        }
        return text;
    }
}
