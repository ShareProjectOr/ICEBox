package example.jni.com.coffeeseller.communicate;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.MachineState;
import cof.ac.inter.Result;
import cof.ac.inter.StateEnum;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.MachineConfig.MachineInitState;
import example.jni.com.coffeeseller.MachineConfig.QRMsger;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.databases.DealOrderInfoManager;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.listener.MessageReceviedListener;
import example.jni.com.coffeeseller.model.listeners.MsgTransListener;
import example.jni.com.coffeeseller.model.listeners.OnMachineCheckCallBackListener;
import example.jni.com.coffeeseller.parse.PayResult;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.SecondToDate;
import example.jni.com.coffeeseller.utils.Waiter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;


public class TaskService extends Service implements MqttCallback {
    public static final String HOST = "tcp://" + "196.168.4.192" + ":61613";//tcp://127.0.0.1:616
    private static MqttClient client;
    private MqttConnectOptions options;
    private String userName = "admin";
    private String passWord = "password";
    private OnMachineCheckCallBackListener mOnMachineCheckCallBackListener;
    private boolean isSubSuccess = true;
    static String TAG = "TaskService";
    static Handler mHandler;
    Timer mTimer = null;
    private
    TimerTask mTimerTask = null;
    public static final long RUN_PERIOD = 60000;

    //VersionManager versionManger;

    static TaskService mInstance;
    BroadcastReceiver mReceiver = null;
    private String ServiceTopic;   //服务端TOPIC

    //SettingDataManager settingDataManager;


    public TaskService() {
        mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj == null) {
                    isSubSuccess = false;
                    subcribeMqtt();
                } else {

                    JSONObject msgObject = (JSONObject) msg.obj;
                    Log.e(TAG, "收到消息  msg is " + msgObject.toString());
                    try {
                        switch (msgObject.getString("msgType")) {
                            case "payResult":
                                PayResult payResult = PayResult.getPayResult(msg.obj.toString());
                                if (msgTransListener != null) {
                                    msgTransListener.onMsgArrived(payResult);
                                }
                                break;
                            case "updateFormula":
                                if (MachineConfig.getCurrentState() == StateEnum.IDLE) {//空闲状态更新配方
                                    JSONObject d = msgObject.getJSONObject("d");
                                    if (d.getString("updateType").equals("remove")) {
                                        JSONObject formulaObject = d.getJSONObject("formula");

                                    } else if (d.getString("updateType").equals("update")) {

                                    } else if (d.getString("updateType").equals("add")) {

                                    }
                                }
                                messageReceviedListener.getMsgType(msgObject.toString());
                                break;
                            case "machineOrder":
                                messageReceviedListener.getMsgType(msgObject.toString());
                                break;
                            case "relayType":
                                Log.e(TAG, "收到回执 uuid is " + msgObject.getString("msgId"));
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

    }

    private void checkSubSuccess() {
        if (!isConnected()) {
            Log.e(TAG, "获取了 topic 但是订阅失败");
            mOnMachineCheckCallBackListener.MQTTSubcribeFailed();
        } else {
            mOnMachineCheckCallBackListener.MQTTSubcribeSuccess();
            MachineInitState.SUB_MQTT_STATE = MachineInitState.NORMAL;
        }
        if (MachineInitState.CHECK_OPENMAINCTRL == MachineInitState.NORMAL && MachineInitState.CHECK_MACHINECODE == MachineInitState.NORMAL && MachineInitState.SUB_MQTT_STATE == MachineInitState.NORMAL && MachineInitState.GET_FORMULA == MachineInitState.NORMAL) {
            mOnMachineCheckCallBackListener.MachineCheckEnd(true);
        } else {
            mOnMachineCheckCallBackListener.MachineCheckEnd(false);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static synchronized TaskService getInstance() {
        if (mInstance == null) {
            mInstance = new TaskService();
        }
        return mInstance;
    }

    public void start(OnMachineCheckCallBackListener mOnMachineCheckCallBackListener) {
        if (mOnMachineCheckCallBackListener != null) {
            this.mOnMachineCheckCallBackListener = mOnMachineCheckCallBackListener;
        }
        Log.d("连接中", ".......");

        subcribeMqtt();
    }

    private void subcribeMqtt() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mInstance = this;
        //settingDataManager = SettingDataManager.getSettingDataManager(mInstance);
    }

    public void breakClient() {
        Log.d("断开", ".......");
        if (client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isConnected() {
        if (client != null) {
            return client.isConnected();
        } else {
            return false;
        }
    }

    private void init() {
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
        Waiter.doWait(1000);
        checkSubSuccess();
    }

    @Override
    public void connectionLost(Throwable throwable) {
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        JSONObject object = new JSONObject(new String(mqttMessage.getPayload()));
        Message msg = new Message();
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //   MyLog.d(TAG, "TaskService has been started");
        startTimer();
        return super.onStartCommand(intent, flags, startId);

    }

    public void startTimer() {
        if (!hasTimerTask()) {
            if (isConnected()) {
                Log.e(TAG, "订阅成功" + "开始发送消息");
                createTimerTask();
            }

        }
    }

    private MsgTransListener msgTransListener;

    public void SetOnMsgListener(MsgTransListener msgTransListener) {
        this.msgTransListener = msgTransListener;
    }

    private MessageReceviedListener messageReceviedListener;

    public void setOnMessageReceviedListener(MessageReceviedListener listener) {
        messageReceviedListener = listener;
    }

    /**
     * 版本检测
     */
    private void manageVersion() {
        /*
        Runnable mRun = new Runnable() {
			
			@Override
			public void run() {
				if(versionManger == null) {
					
					versionManger = new VersionManager(mInstance);
				}
				versionManger.manageVersionOnline();
			}
		};
		Thread t = new Thread(mRun);
		t.start();*/
    }

    public void stopService() {

        stopSelf();
        destroyTimerTask();
    }

    private boolean hasTimerTask() {

        if (mTimer == null || mTimerTask == null) {

            return false;
        }
        return true;
    }

    private void createTimerTask() {

        if (mTimer == null) {

            mTimer = new Timer(true);
        }
        if (mTimerTask == null) {

            mTimerTask = new TimerTask() {

                long COUNT = 0;

                @Override
                public void run() {
                    MyLog.d(TAG, "timertask is been doing");
                    COUNT++;
                    //videoScreenCheck();//开关屏检测

                    //	LocalDataBaseUtil.deleteOutDateItem();
                    sendStateMsg();
                    if (COUNT % 3 == 0) {//本地交易记录上传

                        sendLocDealMsg();
                    }
                    if (COUNT % 120 == 0) {//版本检测

                        manageVersion();
                    }
                    /*if(JuiceReleaseRecorder.getRecorder().shouldRelease()&& ConfigDataInitManager.getInstance().hasJuiceCollect()) {

						CPUMsger.getCPUMsger().releaseJuice();
						JuiceReleaseRecorder.getRecorder().notifyReleased();
					}*/

                }
            };
            mTimer.schedule(mTimerTask, 0, RUN_PERIOD);
        }

    }

    private void destroyTimerTask() {

        if (mTimer != null) {

            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {

            mTimerTask = null;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        MyLog.d(TAG, "任务摧毁");
        destroyTimerTask();
        mInstance = null;
    }


    private void sendStateMsg() {
        MqttMessage message = new MqttMessage();
        message.setQos(1);
        message.setRetained(false);

        Map<String, Object> msg = new HashMap<>();
        msg.put("msgId", UUID.randomUUID().toString());
        msg.put("machineCode", MachineConfig.getMachineCode());
        msg.put("pageType", FragmentFactory.getInstance().getPageType(FragmentFactory.curPage));
        msg.put("msgType", "runningStateType");
        msg.put("sendTime", SecondToDate.getDateToString(System.currentTimeMillis()));
        Log.e(TAG, SecondToDate.getDateToString(System.currentTimeMillis()));
        msg.put("networkType", MachineConfig.getNetworkType());
        msg.put("cupHouseState", null);
      /*  CoffMsger msger = CoffMsger.getInstance();
        MachineState state = msger.getLastMachineState();
        Result result = state.getResult();*/
/*
        if (result.getCode() == Result.SUCCESS) {
            if (state.hasCupOnShelf()) {
                msg.put("cupHolderState", 1);

            } else {
                msg.put("cupHolderState", 0);
            }
            msg.put("boilerTemperature", (int) state.getPotTemp());
            msg.put("boilerPressure", state.getPotPressure());
            if (state.isFrontDoorOpen()) {
                msg.put("doorState", 1);
            } else {
                msg.put("doorState", 0);
            }
            if (state.isLittleDoorOpen()) {
                msg.put("cupDoorState", 1);
            } else {
                msg.put("cupDoorState", 0);
            }
            msg.put("driverVersion", state.getVersion());
            msg.put("errCode", state.getMajorState().getState_byte() + "");

        } else {*/

        msg.put("cupHolderState", null);


        msg.put("boilerTemperature", null);
        msg.put("boilerPressure", null);

        msg.put("doorState", null);


        msg.put("cupDoorState", null);

        msg.put("driverVersion", "1.0.0");
        msg.put("errCode", 51);
        // }
        msg.put("clientVersion", HomeActivity.getInstance().getVersion());
        msg.put("mediaVersion", "1.0.0");
        message.setPayload(JsonUtil.mapToJson(msg).getBytes());
        MqttTopic topic = client.getTopic("server/coffee/" + MachineConfig.getMachineCode());
        try {
            Log.e(TAG, "start publish message");
            topic.publish(message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送本地交易数据至服务器
     */
    private void sendLocDealMsg() {

        QRMsger qrMsger = new QRMsger();
        List<DealRecorder> dealRecorderList = DealOrderInfoManager.getInstance(mInstance).getLocalTableDatas();
        for (int i = 0; i < dealRecorderList.size(); i++) {
            DealRecorder dealRecorder = dealRecorderList.get(i);
            if (dealRecorder.isReportSuccess()) {
                continue;
            } else {
                qrMsger.reportTradeToServer(dealRecorder, dealRecorder.getBunkers());
                MyLog.d(TAG, "order= " + dealRecorder.getOrder() + ", report to server into taskservice");
            }
        }

    }

    /**
     * 读取广告屏开启、关闭时间
     */
    private void videoScreenCheck() {

	/*	if(settingDataManager.isScreenAuto()){

			String screenStartTime = settingDataManager.getScreenOnTime();
			CPUMsger cpuMsger = CPUMsger.getCPUMsger();

			MyLog.d(TAG, "turn on srceen time is "+screenStartTime);
			if(TimeMatcher.isNowTime(screenStartTime)){
				
				MyLog.d(TAG, "turn on screen and voice");				
				turnOnVoice();
				cpuMsger.turnOnScreen();
				return;
			}
			
			String screenStopTime = settingDataManager.getScreenOffTime();
			MyLog.d(TAG, "turn off srceen time is "+screenStopTime);
			if(TimeMatcher.isNowTime(screenStopTime)){
				
				MyLog.d(TAG, "turn off screen and voice");
				turnOffVoice();
				cpuMsger.turnOffScreen();
				return;
			}
			
		}*/
    }

    void turnOffVoice() {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//此时获取到的音量值是0 ；
        MyLog.d(TAG, "volumn--" + volumn);
        if (volumn != 0) {

            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);//设置成静音
        }
    }

    void turnOnVoice() {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//此时获取到的音量值是0 ；
        MyLog.d(TAG, "volumn--" + volumn);
        if (volumn == 0) {

            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);//设置成静音

        }
    }


}
