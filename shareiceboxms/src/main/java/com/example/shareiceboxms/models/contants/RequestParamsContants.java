package com.example.shareiceboxms.models.contants;

import android.text.TextUtils;
import android.util.Log;

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
    * 根据用户角色设置参数
    * */
    private void putId(Map<String, Object> params) {
        if (PerSonMessage.userType == Constants.AGENT_MANAGER) {
            params.put("agentID", PerSonMessage.userId);
        } else if (PerSonMessage.userType == Constants.MACHINE_MANAGER) {
            if (PerSonMessage.agent != null) {
                params.put("agentID", PerSonMessage.agent.userID);
            } else {
                params.put("agentID", null);
            }
            params.put("managerID", "");
        } else {
            params.put("agentID", "");
            params.put("managerID", "");
        }
    }

    /*
    * 退出登录
    * */
    public Map<String, Object> getOutLogin() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        return params;
    }

    /*
   *机器列表请求参数
   *  agentID:968, //Number 所属代理商ID
  managerID:004,//Number 机器管理员ID
   * */
    public Map<String, Object> getMachineListParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("keyword", "");
        params.put("p", 1);
        params.put("n", 4);
        putId(params);
        params.put("machineAddress", "");
        params.put("activationState", "");
        params.put("activatedTime", "");
        params.put("faultState", "");
        putId(params);
        params.put("checkCode", 1);
        return params;
    }

    /*
    * 机器详情请求参数
    * */
    public Map<String, Object> getMachineDetailParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", PerSonMessage.userId);
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
    * 机器库存商品请求参数
    * */
    public Map<String, Object> getMachineStockProductParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", PerSonMessage.userId);
        params.put("appUserID", PerSonMessage.userId);
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
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
* 机器关机请求参数
* */
    public Map<String, Object> getMachineShutdownParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", PerSonMessage.userId);
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
* 机器重启请求参数
* */
    public Map<String, Object> getMachineRestartParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
* 机器盘点请求参数
* */
    public Map<String, Object> getMachineCheckParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("machineID", FragmentFactory.getInstance().getSavedBundle().getInt("machineID"));
        return params;
    }

    /*
* 机器温度请求参数
* */
    public Map<String, Object> getMachineTempParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
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
        params.put("recordID", FragmentFactory.getInstance().getSavedBundle().getInt("recordID"));
        params.put("appUserID", PerSonMessage.userId);
        return params;
    }

    /*
* 交易统计-财务明细 请求参数
* */
    public Map<String, Object> getTradeTotalParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
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

    /*
* 交易记录列表参数
* */
    public Map<String, Object> getTradeRecordsParams() {
        Map<String, Object> params = new HashMap<>();

        params.put("appUserID", PerSonMessage.userId);
//        params.put("checkCode", 1);
        params.put("tradeCode", "");
//        params.put("payState", "");
        params.put("createTime", null);
        params.put("settlementID", "");
        params.put("machineID", "");
        params.put("consumerID", "");
        params.put("n", 6);
        params.put("p", 1);
        return params;
    }

    /*
* 交易记录详情请求参数
* */
    public Map<String, Object> getTradeRecoredDetailParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("tradeID", FragmentFactory.getInstance().getSavedBundle().getInt("tradeID", 0));
        return params;
    }

    /*
* 交易记录详情售出商品请求参数,请求所有数据
* */
    public Map<String, Object> getTradeRecoredDetailProductParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("RFID", "");
//        params.put("categoryID", 0);
//        params.put("machineID", 0);
//        params.put("isRefund", 0);//0:未退款
//        params.put("operationTime", "");
        params.put("p", 1);
        params.put("n", 1000);
        params.put("type", "3");//商品状态类型，默认1 ： 0-失效商品表（报损、丢失）；1-有效商品（正在售卖的）；2-未上架商品；3；售出的（售出及退货）
        params.put("tradeID", FragmentFactory.getInstance().getSavedBundle().getInt("tradeID", 0));//（type=3）时才有这个参数
        return params;
    }

    /*
* 服务费结算请求参数，针对运营商    结算工单列表
* {
  agentID:8787878745,//Number 代理商ID
  createTime:[ //Array 时间区间
    'between',
    ['2016-04-03 12:12','2016-04-04 12:12']
  ],
  divideState:0,//Number 工单状态,详情见工单状态代码
  n:12,//Number 每页显示个数
  p:1,//Number 当前是第几页
  sessionID:'hua78ad78a7aasa809hn' ,//String (session ID)
}
* */
    public Map<String, Object> getAccountsParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("p", 1);
        params.put("n", 2);
        if (PerSonMessage.userType == Constants.AGENT_MANAGER) {
            params.put("agentID", PerSonMessage.userId);
        }
        params.put("createTime", null);
        return params;
    }


    /*
    * 创建结算工单
    *   agentID:8787878745,//Number 代理商ID
  createTime:[ //Array 时间区间
    'between',
    ['2016-04-03 12:12','2016-04-04 12:12']
  ],
    * */
    public Map<String, Object> getCreateJieSuanParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
//        params.put("agentID", "");
        params.put("createTime", null);
        return params;
    }

    /*
       * 批量退款
       *   RFIDList:RFID-RFID-RFID,//String 单个或多个RFID组成的字符串
      refundReason:'就是想退款',//String 退款原因
      sessionID:'hua78ad78a7aasa809hn' ,//String (session ID)
       * */
    public Map<String, Object> getRefundParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
        params.put("RFIDList", null);
        params.put("refundReason", null);
        return params;
    }

    /*
    * 获取旗下所有代理商
    * keyword:'张',//String 关键词查询 包括姓名、手机号、公司
    p:1,// Number 当前页码
    n:12,//Number 当前页数数量
    userType:0, //Number 用户角色，详情见用户角色状态代码
    disable:0,//Number 是否被禁用，详情见用户禁用状态代码
    companyID:4545454,//Number 公司ID
    agentID: 0000, //Number 所属代理商ID
  	address:'441502|四川省-绵阳市-涪城区|富乐路口凯德广场二期五楼',//String 用户所在 省-市-县-详细地址
    sessionID:'hua78ad78a7aasa809hn' ,//String (session ID)
    * */
    public Map<String, Object> getAgentsParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("checkCode", 1);
//        params.put("agentID", null);
        params.put("userType", 2);
        params.put("disable", 0);
        params.put("p", 1);
        params.put("n", 1000);
        return params;
    }

    /*
    * 冲抵的应退记录参数
    * */
    public Map<String, Object> getRefundOfChongdiParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("appUserID", PerSonMessage.userId);
        params.put("settlementID", "");
        params.put("p", 1);
        params.put("n", 6);
        params.put("checkCode", 1);
        return params;
    }
}
