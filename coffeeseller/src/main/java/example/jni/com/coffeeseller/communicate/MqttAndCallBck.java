package example.jni.com.coffeeseller.communicate;

import android.os.Handler;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.model.listeners.OnMachineCheckCallBackListener;
import example.jni.com.coffeeseller.utils.SecondToDate;

/**
 * Created by Administrator on 2018/5/8.
 */

public class MqttAndCallBck implements MqttCallback {
    public static MqttAndCallBck mInstance;
    private static MqttClient client;
    private MqttConnectOptions options;
    private String userName = "admin";
    private String passWord = "password";
    private OnMachineCheckCallBackListener mOnMachineCheckCallBackListener;
    private boolean isSubSuccess = true;
    static String TAG = "TaskService";
    static Handler mHandler;

    public MqttAndCallBck() {
    }

    public static synchronized MqttAndCallBck getInstance() {
        if (mInstance == null) {
            mInstance = new MqttAndCallBck();
        }
        return mInstance;
    }

    public void startMqtt() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存

                    client = new MqttClient(MachineConfig.getTcpIP(), MachineConfig.getMachineCode(), new MemoryPersistence());
                    // MQTT的连接设置
                    options = new MqttConnectOptions();
                    // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
                    options.setCleanSession(true);
                    // 设置连接的用户名
                    options.setUserName(userName);
                    // 设置连接的密码
                    options.setPassword(passWord.toCharArray());
                    // 设置超时时间 单位为秒
                    options.setConnectionTimeout(10);
                    // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
                    options.setKeepAliveInterval(20);
                    //   options.setWill();
                    // 设置回调
                    client.setCallback(new TaskService());
                    Log.e(TAG, "topic is " + MachineConfig.getTcpIP());
                    MqttTopic topic = client.getTopic(MachineConfig.getTcpIP());
                    // setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
                    Map<String, Object> bytejson = new HashMap<>();
                    bytejson.put("msgId", UUID.randomUUID().toString());
                    bytejson.put("msgType", "willMsgType");
                    bytejson.put("machineCode", MachineConfig.getMachineCode());
                    bytejson.put("sendTime", SecondToDate.getDateToString(System.currentTimeMillis()));
                    options.setWill(topic, JsonUtil.mapToJson(bytejson).getBytes(), 2, true);
                    client.connect(options);
                    // 订阅消息
                    int[] Qos = {1, 1};
                    if (MachineConfig.getTopic().equals("") || MachineConfig.getTopic().equals("null")) {
                        mOnMachineCheckCallBackListener.MQTTSubcribeFailed();
                        return;
                    }
                    String[] topic1 = {MachineConfig.getTopic() + "#", "client/coffee/sc/#"};
                    Log.e(TAG, topic1[0]);
                    client.subscribe(topic1, Qos);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
