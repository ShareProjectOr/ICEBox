package example.jni.com.coffeeseller.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import example.jni.com.coffeeseller.bean.MachineConfig;

/**
 * Created by Administrator on 2018/6/5.
 */

public class NetWorkCheckUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    getNetWorkStatus(context);
                    return true;
                }
            }
        }
        MachineConfig.setNetworkType(0);
        return false;
    }

    private static void getNetWorkStatus(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {
                MachineConfig.setNetworkType(2);
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                MachineConfig.setNetworkType(1);
            }
        }

    }
}
