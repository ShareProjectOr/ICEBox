package com.example.shareiceboxms.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.http.mqtt.GetService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Administrator on 2017/12/12.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, MqttCallback {
    private static BaseActivity mInstance;
    private static Handler handler;

    @Override
    public void onClick(View v) {

    }

    public static BaseActivity getInstance() {

        if (mInstance == null) {

            mInstance = new BaseActivity();
        }
        return mInstance;
    }

    public BaseActivity() {
    }

    public void jumpActivity(Class<?> activitycalss, Bundle intentData) {
        Log.e("HomeActivity", "扫码");
        Intent intent = new Intent();
        if (activitycalss != null) {
            intent.setClass(getApplication(), activitycalss);
            if (intentData != null) {
                intent.putExtra("intentdata", intentData);
            }
            startActivity(intent);
        }

    }

    @Override
    public void connectionLost(Throwable throwable) {

        // 连接丢失后，一般在这里面进行重连

    }

    public static final String BROADCAST_ACTION = "topic";

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
      /*  Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("topic", topic);
        sendBroadcast(intent);*/

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
    }
}
