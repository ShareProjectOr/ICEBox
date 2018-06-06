package com.example.shareiceboxms.models.beans;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/25.
 */

public class ItemUpload {
    public int recordID;
    public String unlockTime;
    public String openingTime = "";
    public String closingTime;
    public int userID;
    public String name;
    public String email;
    public String tel;
    public int userType;
    public int disable;
    public int machineID;
    public String machineName;
    public String machineCode;
    public String machineAddress;
    public String DownLoadNum;
    public String UpLoadNum;

    public void bindData(JSONObject object) throws JSONException {
        Log.d("上下货", object.toString());
        recordID = object.getInt("recordID");
        unlockTime = object.getString("unlockTime");
        if (!"null".equals(object.getString("openingTime")) && !TextUtils.isEmpty(object.getString("openingTime"))) {
            openingTime = object.getString("openingTime");
        }
        closingTime = object.getString("closingTime");
        UpLoadNum = object.getString("exhibitNum");
        DownLoadNum = object.getString("offShelfNum");
        JSONObject user = object.getJSONObject("operator");
        JSONObject machine = object.getJSONObject("machine");
        //    userID = user.getInt("userID");
        name = user.getString("name");
        email = user.getString("email");
        tel = user.getString("tel");
        //  userType = user.getInt("userType");
        disable = user.getInt("disable");
        machineID = machine.getInt("machineID");
        machineName = machine.getString("machineName");
        machineCode = machine.getString("machineCode");
        machineAddress = machine.getString("machineAddress");
    }
}
