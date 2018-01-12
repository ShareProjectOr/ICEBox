package com.example.shareiceboxms.models.contants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by WH on 2017/7/20.
 */

public class ConstanceMethod {
    //跳转功能
    public static void startIntent(Activity activity, Class myClass, Object flags) {
        Intent intent = new Intent();
        if (flags != null) {
            intent.setFlags((int) flags);
        }
        intent.setClass(activity, myClass);
        activity.startActivity(intent);
    }

    public static void startIntent(Activity activity, Class myClass, Bundle bundle, Object flags) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (flags != null) {
            intent.setFlags((int) flags);
        }
        intent.setClass(activity, myClass);
        activity.startActivity(intent);
    }

    //清空栈
    public static void clearActivityFromStack(Activity activity, Class myClass) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(activity, myClass);
        activity.startActivity(intent);
    }

    //修改是否登陆
    public static void isFirstLogin(Context context, boolean isLogined) {
        SharedPreferences preferences = getSharedPreferences(context, "ShowWelcome");//context.getSharedPreferences("ShowWelcome", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("isFirst", isLogined);
        edit.apply();
    }

    //保存推送时间戳
    public static void addHasDealMsg(Context context, Long msgTag) {
        SharedPreferences preferences = getSharedPreferences(context, "Msg");//context.getSharedPreferences("ShowWelcome", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong("msgTag", msgTag);
        edit.apply();
    }

    public static SharedPreferences getSharedPreferences(Context context, String name) {
        SharedPreferences mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return mPreferences;
    }

    //取得当前时间的前后N天的日期
    public static String getOtherDate(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, n);
//        return calendar.getTime();
        return calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DAY_OF_MONTH);
    }

    //取得当前时间
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DAY_OF_MONTH);
    }
}
