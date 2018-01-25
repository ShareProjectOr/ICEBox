package com.example.shareiceboxms.models.beans;

import android.util.Log;

import com.example.shareiceboxms.models.beans.trade.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.JsonDataParse;

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
                if (item.has("loginitude") && !item.get("loginitude").equals(null)) {
                    itemMachine.loginitude = item.getDouble("loginitude");
                }
                if (item.has("doorState") && !item.get("doorState").equals(null)) {
                    itemMachine.doorState = item.getInt("doorState");
                }

                if (item.has("latitude") && !item.get("latitude").equals(null)) {
                    itemMachine.latitude = item.getDouble("latitude");
                }
                if (item.has("activationState") && !item.get("activationState").equals(null)) {
                    itemMachine.activationState = item.getInt("activationState");
                }
                if (item.has("clientVersion") && !item.get("clientVersion").equals(null)) {
                    itemMachine.clientVersion = item.getString("clientVersion");
                }
                if (item.has("activationState") && !item.get("activationState").equals(null)) {
                    itemMachine.activationState = item.getInt("activationState");
                }
                if (item.has("deviationTemperature") && !item.get("deviationTemperature").equals(null)) {
                    itemMachine.deviationTemperature = item.getString("deviationTemperature");
                }
                if (item.has("lockState") && !item.get("lockState").equals(null)) {
                    itemMachine.lockState = item.getInt("lockState");
                }
                if (item.has("machineName") && !item.get("machineName").equals(null)) {
                    itemMachine.machineName = item.getString("machineName");
                }

                if (item.has("machineID") && !item.get("machineID").equals(null)) {
                    itemMachine.machineID = item.getInt("machineID");
                }
                if (item.has("powerState") && !item.get("powerState").equals(null)) {
                    itemMachine.powerState = item.getInt("powerState");
                }

                if (item.has("targetTemperature") && !item.get("targetTemperature").equals(null)) {
                    itemMachine.targetTemperature = item.getString("targetTemperature");
                }

                if (item.has("machineTemperature") && !item.get("machineTemperature").equals(null)) {
                    itemMachine.machineTemperature = item.getString("machineTemperature");
                }
                if (item.has("sensorState") && !item.get("sensorState").equals(null)) {
                    itemMachine.sensorState = item.getInt("sensorState");
                }
                if (item.has("machineCode") && !item.get("machineCode").equals(null)) {
                    itemMachine.machineCode = item.getString("machineCode");
                }
                if (item.has("factoryTime") && !item.get("factoryTime").equals(null)) {
                    itemMachine.factoryTime = item.getString("factoryTime");
                }
                if (item.has("updateTime") && !item.get("updateTime").equals(null)) {
                    itemMachine.updateTime = item.getString("updateTime");
                }

                if (item.has("networkState") && !item.get("networkState").equals(null)) {
                    itemMachine.networkState = item.getInt("networkState");
                }

                if (item.has("machineAddress") && !item.get("machineAddress").equals(null)) {
                    itemMachine.machineAddress = item.getString("machineAddress");
                }
                if (item.has("refrigeratorState") && !item.get("refrigeratorState").equals(null)) {
                    itemMachine.refrigeratorState = item.getInt("refrigeratorState");
                }
                if (item.has("qrCode") && !item.get("qrCode").equals(null)) {
                    itemMachine.qrCode = item.getString("qrCode");
                }
                if (item.has("breakTime") && !item.get("breakTime").equals(null)) {
                    itemMachine.breakTime = item.getString("breakTime");
                }
                if (item.has("lightState")) {
                    itemMachine.lightState = item.getInt("lightState");
                }
                if (item.has("activatedTime") && !item.get("activatedTime").equals(null)) {
                    itemMachine.activatedTime = item.getString("activatedTime");
                }
                if (item.has("driverVersion") && !item.get("driverVersion").equals(null)) {
                    itemMachine.driverVersion = item.getString("driverVersion");
                }
                if (item.has("blowerState") && !item.get("blowerState").equals(null)) {
                    itemMachine.blowerState = item.getInt("blowerState");
                }
                if (item.has("faultState") && !item.get("faultState").equals(null)) {
                    itemMachine.faultState = item.getInt("faultState");
                }
                if (item.has("productionTime") && !item.get("productionTime").equals(null)) {
                    itemMachine.productionTime = item.getString("productionTime");
                }

                if (item.has("manager") && !item.get("manager").equals(null)) {
                    itemMachine.itemManager = ItemPerson.bindPerson(item.getJSONObject("manager"));
                }

                if (item.has("agent") && !item.get("agent").equals(null)) {
                    itemMachine.itemAgent = ItemPerson.bindPerson(item.getJSONObject("agent"));
                }
                if (item.has("company") && !item.get("company").equals(null)) {
                    itemMachine.itemCompany = ItemCompany.bindCompanyFull(item.getJSONObject("company"));
                }
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
            if (item.has("loginitude") && !item.get("loginitude").equals(null)) {
                itemMachine.loginitude = item.getDouble("loginitude");
            }
            if (item.has("doorState") && !item.get("doorState").equals(null)) {
                itemMachine.doorState = item.getInt("doorState");
            }

            if (item.has("latitude") && !item.get("latitude").equals(null)) {
                itemMachine.latitude = item.getDouble("latitude");
            }
            if (item.has("activationState") && !item.get("activationState").equals(null)) {
                itemMachine.activationState = item.getInt("activationState");
            }
            if (item.has("clientVersion") && !item.get("clientVersion").equals(null)) {
                itemMachine.clientVersion = item.getString("clientVersion");
            }
            if (item.has("activationState") && !item.get("activationState").equals(null)) {
                itemMachine.activationState = item.getInt("activationState");
            }
            if (item.has("deviationTemperature") && !item.get("deviationTemperature").equals(null)) {
                itemMachine.deviationTemperature = item.getString("deviationTemperature");
            }
            if (item.has("lockState") && !item.get("lockState").equals(null)) {
                itemMachine.lockState = item.getInt("lockState");
            }
            if (item.has("machineName") && !item.get("machineName").equals(null)) {
                itemMachine.machineName = item.getString("machineName");
            }

            if (item.has("machineID") && !item.get("machineID").equals(null)) {
                itemMachine.machineID = item.getInt("machineID");
            }
            if (item.has("powerState") && !item.get("powerState").equals(null)) {
                itemMachine.powerState = item.getInt("powerState");
            }

            if (item.has("targetTemperature") && !item.get("targetTemperature").equals(null)) {
                itemMachine.targetTemperature = item.getString("targetTemperature");
            }

            if (item.has("machineTemperature") && !item.get("machineTemperature").equals(null)) {
                itemMachine.machineTemperature = item.getString("machineTemperature");
            }
            if (item.has("sensorState") && !item.get("sensorState").equals(null)) {
                itemMachine.sensorState = item.getInt("sensorState");
            }
            if (item.has("machineCode") && !item.get("machineCode").equals(null)) {
                itemMachine.machineCode = item.getString("machineCode");
            }
            if (item.has("factoryTime") && !item.get("factoryTime").equals(null)) {
                itemMachine.factoryTime = item.getString("factoryTime");
            }
            if (item.has("updateTime") && !item.get("updateTime").equals(null)) {
                itemMachine.updateTime = item.getString("updateTime");
            }

            if (item.has("networkState") && !item.get("networkState").equals(null)) {
                itemMachine.networkState = item.getInt("networkState");
            }

            if (item.has("machineAddress") && !item.get("machineAddress").equals(null)) {
                itemMachine.machineAddress = item.getString("machineAddress");
            }
            if (item.has("refrigeratorState") && !item.get("refrigeratorState").equals(null)) {
                itemMachine.refrigeratorState = item.getInt("refrigeratorState");
            }
            if (item.has("qrCode") && !item.get("qrCode").equals(null)) {
                itemMachine.qrCode = item.getString("qrCode");
            }
            if (item.has("breakTime") && !item.get("breakTime").equals(null)) {
                itemMachine.breakTime = item.getString("breakTime");
            }
            if (item.has("lightState") && !item.get("lightState").equals(null)) {
                itemMachine.lightState = item.getInt("lightState");
            }
            if (item.has("activatedTime") && !item.get("activatedTime").equals(null)) {
                itemMachine.activatedTime = item.getString("activatedTime");
            }
            if (item.has("driverVersion") && !item.get("driverVersion").equals(null)) {
                itemMachine.driverVersion = item.getString("driverVersion");
            }
            if (item.has("blowerState") && !item.get("blowerState").equals(null)) {
                itemMachine.blowerState = item.getInt("blowerState");
            }
            if (item.has("faultState") && !item.get("faultState").equals(null)) {
                itemMachine.faultState = item.getInt("faultState");
            }
            if (item.has("productionTime") && !item.get("productionTime").equals(null)) {
                itemMachine.productionTime = item.getString("productionTime");
            }
            if (item.has("manager") && !item.get("manager").equals(null)) {
                itemMachine.itemManager = ItemPerson.bindPerson(item.getJSONObject("manager"));
            }

            if (item.has("agent") && !item.get("agent").equals(null)) {
                itemMachine.itemAgent = ItemPerson.bindPerson(item.getJSONObject("agent"));
            }
            if (item.has("company") && !item.get("company").equals(null)) {
                itemMachine.itemCompany = ItemCompany.bindCompanyFull(item.getJSONObject("company"));
            }
        } catch (JSONException e) {
            Log.e("ItemMachine", e.toString());
        }
        return itemMachine;
    }

    /*
    * 机器精简版
    *  	machineID:454545，//Number 机器ID
  	machineName:'展览机一号',//String 机器名称，管理员识别机器名称
  	machineCode:'788ss' ,//String 机器码，机器唯一识别编码
    machineAddress:'441502|四川省-绵阳市-涪城区|富乐路口凯德广场二期五楼',//String 机器安装地址省-市-县-详细地址
    * */

    public static ItemMachine bindMachineNotFull(JSONObject item) throws JSONException {
        ItemMachine itemMachine = new ItemMachine();
        try {

            itemMachine.machineName = item.getString("machineName");
            itemMachine.machineID = item.getInt("machineID");
            itemMachine.machineCode = item.getString("machineCode");
            itemMachine.machineAddress = item.getString("machineAddress");
        } catch (JSONException e) {
            Log.e("ItemMachine", e.toString());
        }
        return itemMachine;
    }

}
