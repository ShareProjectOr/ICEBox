package example.jni.com.coffeeseller.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalDataManager {

	SharedPreferences sharedPreferences = null;
	Context mContext;
	
	static LocalDataManager mInstance;
	public final static String NO_VALUE="NOVALUE";
	
	public static LocalDataManager getInstance(Context context) {
		
		if(mInstance == null) {
			
			mInstance = new LocalDataManager(context);
		}
		return mInstance;
	}
	
	public static LocalDataManager getInstance() {
		
		return mInstance;
	}
	
	
	public LocalDataManager(Context context) {
		
		mContext = context;
		sharedPreferences = mContext. getSharedPreferences("locdata", Context.MODE_PRIVATE);
	}
	
	public void resetDealCount() {
		
		synchronized(this) {
			Editor editor = sharedPreferences.edit();
			editor.putLong("deal_fail", 0);
			editor.putLong("deal_suc", 0);
			editor.commit();
		}

	}
	public void incDealFailureCount() {
		
		synchronized(this) {
			Editor editor = sharedPreferences.edit();
			long count = getDealFailureCount()+1;
			editor.putLong("deal_fail", count);
			editor.commit();
			incTotalDealFailCount();
		}
	}
	
	public void incDealSuccessCount() {
		
		synchronized(this) {
			Editor editor = sharedPreferences.edit();
			long count = getDealSuccessCount()+1;
			editor.putLong("deal_suc", count);
			editor.commit();
			incTotalDealSucCount();
		}
	}
	
	private void incTotalDealSucCount() {
		
		synchronized(this) {
			Editor editor = sharedPreferences.edit();
			long count = getTotalDealSucCount()+1;
			editor.putLong("total_deal_suc", count);
			editor.commit();
		}
	}
	
	private void incTotalDealFailCount() {
		
		synchronized(this) {
			Editor editor = sharedPreferences.edit();
			long count = getTotalDealFailCount()+1;
			editor.putLong("total_deal_fail", count);
			editor.commit();
		}
	}
	
	
	public long getDealSuccessCount() {
		
	
		long count = sharedPreferences.getLong("deal_suc", 0);
		return count;
		
		
	}
	
	public long getDealFailureCount() {
			
		long count = sharedPreferences.getLong("deal_fail", 0);
		return count;
		
		
	}
	
	public long getTotalDealSucCount() {
			
		long count = sharedPreferences.getLong("total_deal_suc", 0);
		return count;
			
	}
	
	public long getTotalDealFailCount() {
			
		long count = sharedPreferences.getLong("total_deal_fail", 0);
		return count;
		
	}
	
	
	public void writeDownloadAPKVersion(String version) {
		
		synchronized(this) {
			
			Editor editor = sharedPreferences.edit();
			editor.putString("dwloadversion", version);
			editor.commit();
		}
	}
	

	public String getDownloadAPKVersion() {
		
		return sharedPreferences.getString("dwloadversion", NO_VALUE);
				
	}
	
	public void writeDownloadAPKMd5(String apkmd5) {
		
		synchronized(this) {
			
			Editor editor = sharedPreferences.edit();
			editor.putString("apkmd5", apkmd5);
			editor.commit();
		}
	}
	
	public String getDownloadAPKMd5() {
		
		return sharedPreferences.getString("apkmd5", NO_VALUE);
	}
	
	public void setUpgadeMode(boolean isForce) {
		
		synchronized(this) {
			
			Editor editor = sharedPreferences.edit();
			if(isForce) {
				editor.putInt("upgrade_mode", 1);
			} else {
				
				editor.putInt("upgrade_mode", 0);
			}
			editor.commit();
		}
	}
	
	public boolean isForceUpgrade() {
		
		int mode = sharedPreferences.getInt("upgrade_mode", 0);
		if(mode == 1) {
			
			return true;
		}
		return false;
	}
	
	public void setLastCreateLogfileTime(String time){
		
		synchronized(this) {
			
			Editor editor = sharedPreferences.edit();
			editor.putString("lastCreateLogfileTime", time);
			editor.commit();
		}
		
	}
	
	public String getLastCreateLogfileTime(){
		String lastCreateLogfileTime = sharedPreferences.getString("lastCreateLogfileTime",
				(new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())).format(new Date()));
		return lastCreateLogfileTime;
	}
	
	public void resetCupNum() {
				
		setCupNum(70);
		
	}
	
	private void setCupNum(int cupNum) {
		
		synchronized(this) {
			
			Editor editor = sharedPreferences.edit();
			editor.putInt("cup_num", cupNum);
			editor.commit();
		}
	}
	
	public void decCupNum() {
		
		synchronized(this) {
			
			int cupNum = sharedPreferences.getInt("cup_num", 0);
			if(cupNum > 0) {
				
				cupNum--;
				setCupNum(cupNum);
			}
		}
		
	}
	
	/*
	 * 以百分比的形式返回杯子数量
	 */
	public int getCupNum() {
		
		int cupNum = sharedPreferences.getInt("cup_num", 0);
		if(cupNum < 0 ) {
			
			cupNum = 0;
		}
		return cupNum*100/70;
	}

	public void setLastErr(String lastErr) {
		
		if(lastErr != null) {
			
			synchronized(this) {
				
				Editor editor = sharedPreferences.edit();
				editor.putString("lastErr", lastErr);
				editor.commit();
			}
		}
	}
	
	public String getLastErr() {
		
		String lastErr = sharedPreferences.getString("lastErr", "00");
		return lastErr;
	}
	
	public void setLastTipErr(String lastTipErr) {
		
		if(lastTipErr != null) {
			
			synchronized(this) {
				
				Editor editor = sharedPreferences.edit();
				editor.putString("lastTipErr", lastTipErr);
				editor.commit();
			}
		}
	}
	
	
	public String getLastTipErr() {
		
		String lastErr = sharedPreferences.getString("lastErr", "00");
		return lastErr;
	}
	
	
	/**
	 * 更新机器故障时间
	 */
	public void reNewLastErrTime() {
		
		synchronized(this) {
			
			String lastErrTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Editor editor = sharedPreferences.edit();
			editor.putString("lastErrTime", lastErrTime);
			editor.commit();
		}
	}
	
	/**
	 * 获取机器最后一次故障时间
	 * @return
	 */
	public String getLastErrTime() {
	
		String lastErrTime = sharedPreferences.getString("lastErrTime", "");
		return lastErrTime;
	}
	
	public void setTemp(int temp) {
		
		synchronized(this) {
			
			Editor editor = sharedPreferences.edit();		
			editor.putInt("curTemp", temp);			
			editor.commit();
		}
	}
	
	public int getTemp() {
		
		int temp = sharedPreferences.getInt("curTemp", 5);
		return temp;
	}
	
	public void setTempHY(int HY) {
		
		synchronized(this) {
			
			Editor editor = sharedPreferences.edit();		
			editor.putInt("curHY", HY);			
			editor.commit();
		}
		
	}
	
	
	public int getTempHY() {
		
		int temp = sharedPreferences.getInt("curHY", 5);
		return temp;
	}

	
}
