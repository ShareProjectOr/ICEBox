package com.example.shareiceboxms.models.contants;

import com.example.shareiceboxms.models.factories.FragmentFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WH on 2017/12/6.
 * 接口参数类
 */

public class RequestParamsContants {
    private static RequestParamsContants instance;

    public static synchronized RequestParamsContants getInstance() {
        if (instance == null) {
            instance = new RequestParamsContants();
        }
        return instance;
    }

    /*
    *登录使用的参数
    * */
    public static String[] LOGIN_PARAMS = {};

    /*
   *机器列表请求参数
   * */
    public Map<String, Object> getMachineListParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("keyword", "");
        params.put("p", 1);
        params.put("n", 4);
        params.put("machineAddress", "");
        params.put("activationState", "");
        params.put("activatedTime", "");
        params.put("faultState", "");
        params.put("agentID", "");
        params.put("managerID", "");
        params.put("checkCode", 1);
        return params;
    }

    /*
    * 机器详情请求参数
    * */
    public Map<String, Object> getMachineDetailParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
    * 机器库存商品请求参数
    * */
    public Map<String, Object> getMachineStockProductParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("checkCode", 1);
        params.put("p", 1);
        params.put("n", 5);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
* 机器灯控制请求参数
* */
    public Map<String, Object> getMachineLightControlParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
* 机器关机请求参数
* */
    public Map<String, Object> getMachineShutdownParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
* 机器重启请求参数
* */
    public Map<String, Object> getMachineRestartParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
* 机器盘点请求参数
* */
    public Map<String, Object> getMachineCheckParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }
}
