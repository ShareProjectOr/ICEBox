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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import example.jni.com.coffeeseller.utils.MyLog;


public class TaskService extends Service {
	
	static String TAG = "TaskService";
	Handler mHandler;
	Timer mTimer = null;
	TimerTask mTimerTask = null;
	public static final long RUN_PERIOD = 5000;
	
	//VersionManager versionManger;
	
	static TaskService mInstance;
	BroadcastReceiver mReceiver = null;
	
	//SettingDataManager settingDataManager;

	
	public TaskService() {
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	public static TaskService getInstance() {
		
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		//settingDataManager = SettingDataManager.getSettingDataManager(mInstance);
		mHandler = new Handler();
		checkNetState();
	}
	
	/**
	 * 判断是否需要升级提示
	 * @param showType
	 * @param msg
	 */
	public void showDialog(final int showType,final String msg) {
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
		if(!hasTimerTask()) {
			
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
		
		if(mTimer == null || mTimerTask == null) {
			
			return false;
		}
		return true;
	}
	
	private void createTimerTask() {
		
		if(mTimer == null) {
			
			mTimer = new Timer(true);
		}
		if(mTimerTask == null) {
			
			mTimerTask = new TimerTask() {
				
				long COUNT = 0;
				@Override
				public void run() {
					MyLog.d(TAG, "timertask is been doing");
					COUNT++;
					//videoScreenCheck();//开关屏检测	
					
				//	LocalDataBaseUtil.deleteOutDateItem();
					sendStateMsg();									
					if(COUNT % 3 == 0) {//本地交易记录上传
						
						//sendLocDealMsg();
					}
					if(COUNT % 120 == 0) {//版本检测
						
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
		
		if(mTimer != null) {
			
			mTimer.cancel();
			mTimer = null;
		}
		if(mTimerTask != null) {
			
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
	
	void turnOffVoice(){
		
		AudioManager   audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int volumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//此时获取到的音量值是0 ；
		MyLog.d(TAG, "volumn--"+volumn);
		if(volumn!=0){

			audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);//设置成静音
		}
	}

	void turnOnVoice(){

		AudioManager   audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int volumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//此时获取到的音量值是0 ；
		MyLog.d(TAG, "volumn--"+volumn);
		if(volumn ==0){

			audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);//设置成静音

		}		
	}
	
	private void checkNetState() {
		
		if(mReceiver == null) {
			
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
