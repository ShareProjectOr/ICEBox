package com.example.shareiceboxms.models.beans;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ItemException {
    public int exceptionID;
    public int exceptionLevel;
    public String exceptionCode;
    public String happenTime;
    public String dealTime;
    public int dealMethod;
    public String remark;
    public int machineID;
    public String machineName;
    public String machineCode;
    public String machineAddress;

    public void bindData(JSONObject json) {
        try {
            exceptionID = json.getInt("exceptionID");
            exceptionLevel = json.getInt("exceptionLevel");
            exceptionCode = json.getString("exceptionCode");
            happenTime = json.getString("happenTime");
            dealTime = json.getString("dealTime");
            dealMethod = json.getInt("dealMethod");
            remark = json.getString("remark");
            machineID = json.getJSONObject("machine").getInt("machineID");
            machineName = json.getJSONObject("machine").getString("machineName");
            machineCode = json.getJSONObject("machine").getString("machineCode");
            machineAddress = json.getJSONObject("machine").getString("machineAddress");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
