package example.jni.com.coffeeseller.contentprovider;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SharedPreferencesManager {
    private static SharedPreferencesManager mInstance;
    private SharedPreferences sharedPreferences;
    Context mContext;

    public static synchronized SharedPreferencesManager getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesManager(mContext);
        }
        return mInstance;
    }

    public SharedPreferencesManager(Context mContext) {
        this.mContext = mContext;
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public void setTopicIP(String topicIP) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("topicIP", topicIP);
        editor.commit();
    }


    public String getTopicIP() {
        return sharedPreferences.getString("topicIP", "");
    }

    public void setLoginAccount(String adminAcount) {


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("adminAcount", adminAcount);
        editor.commit();
    }

    public String getLoginAccount() {

        String adminAcount = sharedPreferences.getString("adminAcount", "chjsyjs");
        return adminAcount;

    }

    public void setMachineCode(String machineCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("machineCode", machineCode);
        editor.commit();
    }

    public String getMachineCode() {
        return sharedPreferences.getString("machineCode", "4545");
    }

    public void setLoginPassword(String adminPassword) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("adminPassword", adminPassword);
        editor.commit();
    }

    public String getLoginPassword() {

        String password = sharedPreferences.getString("adminPassword", "chjsyjs-jj");
        return password;
    }

    public void setLastAppUpdateTime(String date) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LastAppUpdateTime", date);
        editor.apply();
    }
 public String getLastAppUpdateTime(){
     return   sharedPreferences.getString("LastAppUpdateTime", "1.0.0");
 }
    public void setCupNum(int CupNum) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CupNum", CupNum);
        editor.apply();
    }

    public int getCupNum() {
        return sharedPreferences.getInt("CupNum", 0);
    }

    public void setWaterCount(String WaterCount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("WaterCount", WaterCount);
        editor.apply();
    }

    public void setAddBeanTime(String addBeanTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("addBeanTime", addBeanTime);
        editor.apply();
    }

    public String getAddBeanTime() {
        return sharedPreferences.getString("addBeanTime", "");
    }

    public void setAddCupTime(String addCupTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("addCupTime", addCupTime);
        editor.apply();
    }

    public String getAddCupTime() {
        return sharedPreferences.getString("addCupTime", "");
    }


    public void setAddWaterTime(String addWaterTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("addWaterTime", addWaterTime);
        editor.apply();
    }

    public String getAddWaterTime() {
        return sharedPreferences.getString("addWaterTime", "");
    }

    public String getWaterCount() {
        return sharedPreferences.getString("WaterCount", "");
    }

    public void setCoffeeBeanAcount(String CoffeeBeanAcount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CoffeeBeanAcount", CoffeeBeanAcount);
        editor.apply();
    }

    public String getCoffeeBeanAcount() {
        return sharedPreferences.getString("CoffeeBeanAcount", "");
    }

    public void SetCoffeeList(String coffeeListArray) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("coffeeListArray", coffeeListArray);
        editor.apply();
    }

    public String getCoffeeListArray() {
        return sharedPreferences.getString("coffeeListArray", "");
    }
}
