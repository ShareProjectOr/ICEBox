package com.example.shareiceboxms.models.http.mqtt;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by WH on 2017/12/28.
 */

public class MqttService extends IntentService {
    public MqttService() {
        super("MqttService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public MqttService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GetService.getInstance().start();
    }
}
