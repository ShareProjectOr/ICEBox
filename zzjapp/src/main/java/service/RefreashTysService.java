package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import contentprovider.UserMessage;
import httputil.Constance;
import httputil.HttpRequest;


public class RefreashTysService extends Service {
    private boolean IsStop = false;
    private final IBinder binder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.e("tsy", UserMessage.getTsy());
                while (!IsStop) {
                    SystemClock.sleep(1000 * 9 * 60);
                    Map<String, String> map = new HashMap<>();
                    map.put("tsy", UserMessage.getTsy());
                    try {
                        String response = HttpRequest.postString(Constance.HOST_URL + "/User/Ping", map);
                        Log.e("刷新TSY", response);
                        JSONObject object = new JSONObject(response);
                        String Tsy = object.getString("tsy");
                        UserMessage.setTsy(Tsy);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        IsStop = true;
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        IsStop = true;
        return super.onUnbind(intent);
    }
}
