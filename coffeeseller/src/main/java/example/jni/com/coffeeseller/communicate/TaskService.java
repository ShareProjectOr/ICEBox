package example.jni.com.coffeeseller.communicate;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
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
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.model.listeners.OnMachineCheckCallBackListener;
import example.jni.com.coffeeseller.utils.MyLog;


public class TaskService extends Service implements MqttCallback {
    public static final String HOST = "tcp://" + "" + ":61616";//tcp://127.0.0.1:61613
    private static MqttClient client;
    private MqttConnectOptions options;
    private String userName = "admin";
    private String passWord = "password";
    private OnMachineCheckCallBackListener mOnMachineCheckCallBackListener;
    private boolean isSubSuccess = true;
    static String TAG = "TaskService";
    static Handler mHandler;
    Timer mTimer = null;
    TimerTask mTimerTask = null;
    public static final long RUN_PERIOD = 5000;

    //VersionManager versionManger;

    static TaskService mInstance;
    BroadcastReceiver mReceiver = null;

    //SettingDataManager settingDataManager;


    public TaskService() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj == null) {
                    isSubSuccess = false;

                } else {

                }
            }
        };

    }

    private void checkSubSuccess() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 3000) {
            if (!isSubSuccess) {
                mOnMachineCheckCallBackListener.MQTTSubcribeFailed();
                return;
            }
        }
        mOnMachineCheckCallBackListener.MQTTSubcribeSuccess();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static synchronized TaskService getInstance() {

        return mInstance;
    }

    public void start(OnMachineCheckCallBackListener mOnMachineCheckCallBackListener) {
        if (mOnMachineCheckCallBackListener != null) {
            this.mOnMachineCheckCallBackListener = mOnMachineCheckCallBackListener;
        }
        Log.d("连接中", ".......");
        init();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mInstance = this;
        //settingDataManager = SettingDataManager.getSettingDataManager(mInstance);

        checkNetState();
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
            client = new MqttClient(HOST, String.valueOf(MachineConfig.getMachineCode()), new MemoryPersistence());
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
            // 设置回调
            client.setCallback(new TaskService());
            MqttTopic topic = client.getTopic("000");
            // setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            options.setWill(topic, "close".getBytes(), 2, true);
            client.connect(options);
            // 订阅消息
            int[] Qos = {2};
            String[] topic1 = {"T-M-" + MachineConfig.getMachineCode()};
            client.subscribe(topic1, Qos);
            checkSubSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    /**
     * 判断是否需要升级提示
     *
     * @param showType
     * @param msg
     */
    public void showDialog(final int showType, final String msg) {
    /*
        if(!APPUpdateDialog.isShow()) {
			
			Runnable mRun = new Runnable() {
				
				@Override
				public void run() {
					APPUpdateDialog dialog = new APPUpdateDialog(mInstance, showType, msg);
					if(!APPUpdateDialog.isShow()) {
						dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
						dialog.showDialog();
					}
				}
			};
			mHandler.post(mRun);
		}*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MyLog.d(TAG, "TaskService has been started");
        if (!hasTimerTask()) {

            createTimerTask();
        }
        //manageVersion();
        return super.onStartCommand(intent, flags, startId);

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

                        //sendLocDealMsg();
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

		/*SendMsg msg = new  SendMsg();
        BasicInfoRecorder mRecorder = BasicInfoRecorder.getInstance();
		msg.setKey(mRecorder.getKey());
		msg.setMachineCode(mRecorder.getMachineid());
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		String sendTime = dateformat.format(new Date());
		msg.setSendTime(sendTime);
		msg.setMsgId(System.currentTimeMillis()+"");
		msg.setMsgTypeCode(MsgTypeCode.State_Type);
		msg.setMsgCode(MsgFCodeType.State_Code);
		SStateBean stateBean = new SStateBean();
		stateBean.setStateMachineCode(mRecorder.getMachineid());
		stateBean.setStateCupNum(LocalDataManager.getInstance(this).getCupNum());
	//	MachineState state = CPUMsger.getCPUMsger().getMachineState();
		CPUMsger cpuMsger = CPUMsger.getCPUMsger();
		MachineState state = cpuMsger.getMachineState();

		if (state != null && cpuMsger.isCurStateValid()) {
			stateBean.setStateExceptionType(state.getStateByte());
			stateBean.setStateOrangeNum(state.getOrangeNum());
			stateBean.setStateCapNum(state.getCoverNum());
			stateBean.setStateMachineType(state.getErrCode());
			MyLog.d(TAG, "error of machineState is " + state.getErrCode());
			stateBean.setStateDriver(state.getMVersion() + "." + state.getLVersion());
		} else {

			MyLog.W(TAG, "MachineState==" + state);
			MyLog.W(TAG, "cpuMsger.isCurStateValid()==" + cpuMsger.isCurStateValid());
			stateBean.setStateExceptionType("1000");
			stateBean.setStateOrangeNum(0);
			stateBean.setStateCupNum(0);
			stateBean.setStateCapNum(0);
			stateBean.setStateMachineType("1000");
			stateBean.setStateDriver("0.0");

		}
*/
    /*	if(state != null) {

			stateBean.setStateExceptionType(state.getStateByte());
			stateBean.setStateOrangeNum(state.getOrangeNum());			
			stateBean.setStateCapNum(state.getCoverNum());
			stateBean.setStateMachineType(state.getErrCode());
			MyLog.d(TAG, "error of machineState is "+state.getErrCode());
			stateBean.setStateDriver(state.getMVersion()+"."+state.getLVersion());
		}else {
			
			stateBean.setStateExceptionType("1000");
			stateBean.setStateOrangeNum(0);
			stateBean.setStateCupNum(0);
			stateBean.setStateCapNum(0);
			stateBean.setStateMachineType("1000");
			stateBean.setStateDriver("0.0");
		}*/
    /*	stateBean.setStateMediaVersion("0.0");
        CurVersionProvider versionProvider = new CurVersionProvider(this);
		String versionName = versionProvider.getVersionName();
		stateBean.setStateClientVersion(versionName);
		msg.setState(stateBean);
		DeliverMsger.getInstance().sendMessage(msg, DeliverMsger.secQuick);*/
    }

    /**
     * 发送本地交易数据至服务器
     */
    private void sendLocDealMsg() {


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

    private void checkNetState() {

        if (mReceiver == null) {

            mReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    // TODO Auto-generated method stub

				/*	if(NetChecker.getInstance(TaskService.this).isConnected()) {

						MyLog.W(TAG, "connected to network");
					}else {
						
						MyLog.W(TAG, "network disconnected!!");
					}*/

                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mReceiver, filter);
        }
    }

}
