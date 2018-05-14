package example.jni.com.coffeeseller.utils;

import android.content.Context;

/**
 * Created by WH on 2018/5/11.
 */

public class DensityUtil {

    /**
     *  
     *  根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     *  
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     *  
     *  根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     *  
     */
    public static int px2dp(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue / scale + 0.5f);
    }
}
