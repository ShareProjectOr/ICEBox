package example.jni.com.coffeeseller.utils;

import android.util.Log;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MyLog {
    private static String mTAG = "CHJUICE";

    public static void d(String TAG,String msg) {

        if(TAG != null && msg != null) {

            Log.d(mTAG, TAG+"--"+msg);
            //LogFileOperator.getInstance().writeLogToFile(TAG, msg);
        }
    }

    public static void W(String TAG,String msg) {

        Log.d(mTAG, TAG+"--"+msg);
        LogFileOperator mLogFileOperator = LogFileOperator.getInstance();
        if(mLogFileOperator != null) {

            mLogFileOperator.writeLogToFile(TAG+"---"+msg);
        }
    }
}
