package com.example.shareiceboxms.models.contants;

import com.example.shareiceboxms.models.beans.PerSonMessage;
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

    /*
* 机器温度请求参数
* */
    public Map<String, Object> getMachineTempParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        params.put("targetTemperature", 0);
        params.put("deviationTemperature", 0);
        return params;
    }
    public Map<String, Object> getExceptionListParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("n", 5);
        params.put("p", 1);
        params.put("appUserID", PerSonMessage.userId);
        params.put("isDeal", 0);
        params.put("happenTime", null);
        return params;
    }

    public Map<String, Object> getUploadListParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("n", 5);
        params.put("p", 1);
        params.put("appUserID", PerSonMessage.userId);
        params.put("operationTime", null);
        //   params.put("userID", PerSonMessage.userId);
        return params;
    }

    public Map<String, Object> getUploadDetailsParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("recordID", FragmentFactory.getInstance().getSavedBundle().getString("recordID"));
        params.put("appUserID", PerSonMessage.userId);
        return params;
    }

    /*
* 交易统计-财务明细 请求参数
* */
    public Map<String, Object> getTradeTotalParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", 1);
        params.put("appUserID", 1);
        params.put("checkCode", 1);
        params.put("companyID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        params.put("searchTime", "");
        return params;
    }

    /*
    * 时间参数
    * */
    public String[] getTimeSelectorParams() {
        String[] time = new String[]{"", ""};
        return time;
    }

    public Object[] getSelectTime(String[] selectTime) {
        if (selectTime == null)
            return null;

        Object[] searchTime = new Object[2];
        searchTime[0] = "between";
        searchTime[1] = selectTime;
        return searchTime;


    }

}
