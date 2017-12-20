package com.example.shareiceboxms.models.beans;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/13.
 */

public class ItemMachine {
    public double loginitude;
    public int doorState;
    public double latitude;
    public int activationState;
    public String clientVersion;
    public String deviationTemperature;
    public int lockState;
    public String machineName;
    public String productionPlant;
    public int machineID;
    public int powerState;
    public String targetTemperature;
    public String machineTemperature;
    public int sensorState;
    public String machineCode;
    public String factoryTime;
    public String updateTime;
    public int networkState;
    public String machineAddress;
    public int refrigeratorState;
    public String qrCode;//QRCode
    public String breakTime;
    public int lightState;
    public String activatedTime;
    public String driverVersion;
    public String productionTime;
    public int blowerState;
    public int faultState;
    public static int userType;
    public static int userID;
    public static int checkCode;
    public ItemCompany itemCompany;
    public ItemPerson itemManager;
    public ItemPerson itemAgent;

    /*  {"loginAccount":"test","c":200,"role":"","d":{"p":10,"t":1
              ,"list":[{"loginitude":0.0,"agent":{"disable":0,"name":"测试代理商","tel":"1211231"
              ,"userType":0,"userID":0,"email":"1234@163.com"},"doorState":0,"latitude":0.0
              ,"activationState":1,"clientVersion":"","deviationTemperature":"","lockState":0
              ,"machineName":"测","productionPlant":"阿斯dad","machineID":4,"powerState":0,"targetTemperature":""
              ,"company":{"agentWechat":"12312312312","bankAccount":"","companyID":1,"minBalance":2000.000000
              ,"companyAddress":"四川省绵阳市","companyName":"测试公司","agentAlipay":"12221323123"
              ,"companyCreditCode":"010101","settlementProportion":1,"settleWay":0},"machineTemperature":""
              ,"sensorState":1,"machineCode":"121231","manager":{"disable":0,"name":"测试","tel":"1231231321","userType":0
              ,"userID":0,"email":"123@163.com"},"factoryTime":"2017-12-13 11:19:45.0","updateTime":"","networkState":0
              ,"machineAddress":"441502","refrigeratorState":1,"QRCode":"","breakTime":"","lightState":1
              ,"activatedTime":"2017-12-14 13:18:25.0","driverVersion":"","blowerState":1,"faultState":0
              ,"productionTime":"2017-12-13 15:19:41.0"}],"n":1},"err":"","sessionID":""
              ,"userType":3,"userID":1,"checkCode":0}*/
    public static List<ItemMachine> bindMachineList(JSONArray list) throws JSONException {
        List<ItemMachine> itemMachines = new ArrayList<>();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = (JSONObject) list.get(i);
                ItemMachine itemMachine = new ItemMachine();
                itemMachine.loginitude = item.getDouble("loginitude");
                itemMachine.doorState = item.getInt("doorState");
                itemMachine.latitude = item.getDouble("latitude");
                itemMachine.activationState = item.getInt("activationState");
                itemMachine.clientVersion = item.getString("clientVersion");
                itemMachine.activationState = item.getInt("activationState");
                itemMachine.deviationTemperature = item.getString("deviationTemperature");
                itemMachine.lockState = item.getInt("lockState");
                itemMachine.machineName = item.getString("machineName");
                itemMachine.machineID = item.getInt("machineID");
                itemMachine.powerState = item.getInt("powerState");
                itemMachine.targetTemperature = item.getString("targetTemperature");
                itemMachine.machineTemperature = item.getString("machineTemperature");
                itemMachine.sensorState = item.getInt("sensorState");
                itemMachine.machineCode = item.getString("machineCode");
                itemMachine.factoryTime = item.getString("factoryTime");
                itemMachine.updateTime = item.getString("updateTime");
                itemMachine.networkState = item.getInt("networkState");
                itemMachine.machineAddress = item.getString("machineAddress");
                itemMachine.refrigeratorState = item.getInt("refrigeratorState");
                itemMachine.qrCode = item.getString("qrCode");//qRCode
                itemMachine.breakTime = item.getString("breakTime");
                itemMachine.lightState = item.getInt("lightState");
                itemMachine.activatedTime = item.getString("activatedTime");
                itemMachine.driverVersion = item.getString("driverVersion");
                itemMachine.blowerState = item.getInt("blowerState");
                itemMachine.faultState = item.getInt("faultState");
                itemMachine.productionTime = item.getString("productionTime");

                itemMachine.itemManager = ItemPerson.bindPerson(item.getJSONObject("manager"));

                itemMachine.itemAgent = ItemPerson.bindPerson(item.getJSONObject("agent"));

                itemMachine.itemCompany = ItemCompany.bindCompany(item.getJSONObject("company"));

                itemMachines.add(itemMachine);
            }
        } catch (JSONException e) {
            Log.e("ItemMachine", e.toString());
        }
        return itemMachines;
    }

    public static ItemMachine bindMachine(JSONObject item) throws JSONException {
        ItemMachine itemMachine = new ItemMachine();
        try {
            itemMachine.loginitude = item.getDouble("loginitude");
            itemMachine.doorState = item.getInt("doorState");
            itemMachine.latitude = item.getDouble("latitude");
            itemMachine.activationState = item.getInt("activationState");
            itemMachine.clientVersion = item.getString("clientVersion");
            itemMachine.activationState = item.getInt("activationState");
            itemMachine.deviationTemperature = item.getString("deviationTemperature");
            itemMachine.lockState = item.getInt("lockState");
            itemMachine.machineName = item.getString("machineName");
            itemMachine.machineID = item.getInt("machineID");
            itemMachine.powerState = item.getInt("powerState");
            itemMachine.targetTemperature = item.getString("targetTemperature");
            itemMachine.machineTemperature = item.getString("machineTemperature");
            itemMachine.sensorState = item.getInt("sensorState");
            itemMachine.machineCode = item.getString("machineCode");
            itemMachine.factoryTime = item.getString("factoryTime");
            itemMachine.updateTime = item.getString("updateTime");
            itemMachine.networkState = item.getInt("networkState");
            itemMachine.machineAddress = item.getString("machineAddress");
            itemMachine.refrigeratorState = item.getInt("refrigeratorState");
            itemMachine.qrCode = item.getString("qrCode");
            itemMachine.breakTime = item.getString("breakTime");
            itemMachine.lightState = item.getInt("lightState");
            itemMachine.activatedTime = item.getString("activatedTime");
            itemMachine.driverVersion = item.getString("driverVersion");
            itemMachine.blowerState = item.getInt("blowerState");
            itemMachine.faultState = item.getInt("faultState");
            itemMachine.productionTime = item.getString("productionTime");
            itemMachine.itemManager = ItemPerson.bindPerson(item.getJSONObject("manager"));

            itemMachine.itemAgent = ItemPerson.bindPerson(item.getJSONObject("agent"));

            itemMachine.itemCompany = ItemCompany.bindCompany(item.getJSONObject("company"));
        } catch (JSONException e) {
            Log.e("ItemMachine", e.toString());
        }
        return itemMachine;
    }
    /*
    * 绑定库存商品中保存的部分信息，也可以不保存
    * */

}
