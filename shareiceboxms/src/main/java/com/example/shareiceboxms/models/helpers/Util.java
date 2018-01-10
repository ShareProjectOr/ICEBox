package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.shareiceboxms.views.activities.BaseActivity;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * @author yangyu
 *         ��������������������
 */
public class Util {
    /**
     * 获取手机屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * �获取手机屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * �õ��豸���ܶ�
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * ���ܶ�ת��Ϊ����
     */
    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    /*
    * 判断软键盘是否显示
    * */
    public static boolean isSoftShowing(BaseActivity activity) {
        //获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        Rect rect = new Rect();
        //获取View可见区域的bottom
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        Log.d("--getSoftButtonsBarHeight--", "" + getSoftButtonsBarHeight(activity));
        Log.d("--isSoftShowing--", "" + (screenHeight - rect.bottom - getSoftButtonsBarHeight(activity)));
        return screenHeight - rect.bottom - getSoftButtonsBarHeight(activity) != 0;
    }

    /*
    * 底部虚拟按键栏的高度 
    * */
    private static int getSoftButtonsBarHeight(BaseActivity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }

    }
}
