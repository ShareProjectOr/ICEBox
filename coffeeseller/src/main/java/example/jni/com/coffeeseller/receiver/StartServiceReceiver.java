package example.jni.com.coffeeseller.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import example.jni.com.coffeeseller.bean.MachineConfig;

/**
 * Created by Administrator on 2018/4/26.
 */

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        isNetworkAvailable(context, intent);//当网络变化时 会收到消息

    }

    private void isWifiOrMobile(Intent intent) {
        // 监听网络连接，总网络判断，即包括wifi和移动网络的监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            //连上的网络类型判断：wifi还是移动网络
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                MachineConfig.setNetworkType(2);
                Log.d("netstatus", "总网络 连接的是wifi网络");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                MachineConfig.setNetworkType(1);
                Log.d("netstatus", "总网络 连接的是移动网络");
            }
            //具体连接状态判断
            //  checkNetworkStatus(networkInfo);

        }
    }

    private boolean isNetworkAvailable(Context context, Intent intent) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    isWifiOrMobile(intent);
                    return true;
                }
            }
        }
        MachineConfig.setNetworkType(0);
        return false;
    }


}
