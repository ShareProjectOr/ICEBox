package ViewUtils;

import android.content.Context;

public class UIUtils {  
	  
    /** 
     * 根据手机的分辨率�? dp 的单�? 转成�? px(像素) 
     */  
    public static int dip2px(Context context, int dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp 
     */  
    public static int px2dip(Context context, int pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
   
    }  
  
}  

