package httputil;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import contentprovider.UserMessage;
import entity.ItemExceptionData;
import entity.ItemMachine;
import entity.ItemTotal;
import entity.ItemTradeData;
import entity.ItemUser;

/**
 * Created by WH on 2017/7/21.
 */

public class JsonParseUtil {
    public JSONObject parseD(String result, Context context) {
        JSONObject jsonObject = null;
        JSONObject message = null;
        try {
            jsonObject = new JSONObject(result);
            String err = jsonObject.getString("err");
            if (TextUtils.isEmpty(err)) {
                UserMessage.setTsy(jsonObject.getString("tsy"));
                message = jsonObject.getJSONObject("d");
                if (message != null) {
                    Toast.makeText(context, R.string.shujuxiugaichenggong, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    //解析机器数据
    public Map<String, String> addMachineMessageToMap(JSONObject userMessage) throws JSONException {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("agentId", userMessage.getString("agentID"));
        msgMap.put("cfgSellSoftware", userMessage.getString("cfgSellSoftware"));
        msgMap.put("stateAdvertisingScreen", userMessage.getString("stateAdvertisingScreen"));
        msgMap.put("cfgCardReader", userMessage.getString("cfgCardReader"));
        msgMap.put("longLat", userMessage.getString("longLat"));
        msgMap.put("stateMachineType", userMessage.getString("stateMachineType"));
        msgMap.put("productTime", userMessage.getString("productTime"));
        msgMap.put("stateChangeDevice", userMessage.getString("stateChangeDevice"));
        msgMap.put("managerName", userMessage.getString("managerName"));
        msgMap.put("machineName", userMessage.getString("machineName"));
        msgMap.put("cfgCoinMachine", userMessage.getString("cfgCoinMachine"));
        msgMap.put("stateCardReader", userMessage.getString("stateCardReader"));
        msgMap.put("stateCupNum", userMessage.getString("stateCupNum"));

        msgMap.put("alipayPrice", userMessage.getString("alipayPrice"));
        msgMap.put("weChatPrice", userMessage.getString("weChatPrice"));
        msgMap.put("cashPrice", userMessage.getString("cashPrice"));
        msgMap.put("alipayPrice", userMessage.getString("alipayPrice"));

        msgMap.put("stateBtimeOut", userMessage.getString("stateBtimeOut"));
        msgMap.put("stateBoundar", userMessage.getString("stateBoundar"));
        msgMap.put("stateMisPos", userMessage.getString("stateMisPos"));
        msgMap.put("activatedType", userMessage.getString("activatedType"));
        msgMap.put("cfgAdvertisingScreen", userMessage.getString("cfgAdvertisingScreen"));
        msgMap.put("machineCode", userMessage.getString("machineCode"));
        msgMap.put("stateExceptionType", userMessage.getString("stateExceptionType"));
        msgMap.put("factoryTime", userMessage.getString("factoryTime"));
        msgMap.put("agentName", userMessage.getString("agentName"));
        msgMap.put("stateCapNum", userMessage.getString("stateCapNum"));
        msgMap.put("managerId", userMessage.getString("managerID"));
        msgMap.put("cfgDocoinMachine", userMessage.getString("cfgDocoinMachine"));
        msgMap.put("stateLongLat", userMessage.getString("stateLongLat"));
        msgMap.put("cfgMediaSortware", userMessage.getString("cfgMediaSortware"));
        msgMap.put("machineAddress", userMessage.getString("machineAddress"));
        msgMap.put("cfgMisPos", userMessage.getString("cfgMisPos"));
        msgMap.put("cfgPaperMachine", userMessage.getString("cfgPaperMachine"));

        msgMap.put("activatedTime", userMessage.getString("activatedTime"));
        msgMap.put("showPrice", userMessage.getString("showPrice"));

        msgMap.put("statePaperMachine", userMessage.getString("statePaperMachine"));
        msgMap.put("stateOrangeNum", userMessage.getString("stateOrangeNum"));
        msgMap.put("cfgDriver", userMessage.getString("cfgDriver"));
        return msgMap;
    }

    //取得所有用户
    public List<ItemUser> getUser(JSONObject message) {
        if (message == null) {
            return null;
        }
        List<ItemUser> users = new ArrayList<>();
        try {
            JSONArray array = message.getJSONArray("l");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.opt(i);
                users.add(ItemUser.setUser(object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (users.size() == 0) {
            ItemUser user_no = new ItemUser();
            user_no.setManagerName("无");
            user_no.setManagerId("nothing");
            users.add(0, user_no);
        } else {
            ItemUser user_all = new ItemUser();
            user_all.setManagerName("所有");
            user_all.setManagerId("everything");
            users.add(0, user_all);
        }
        return users;
    }

    //取得所有机器
    public List<ItemMachine> getMachine(Context context,JSONObject message) {
        if (message == null) {
            return null;
        }
        List<Map<String, String>> names = new ArrayList<>();
        List<ItemMachine> machines = new ArrayList<>();


        try {
            JSONArray array = message.getJSONArray("l");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Log.d("debug", "machineNAME==" + object.getString("machineCode"));
                machines.add(ItemMachine.setMachine(object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (machines.size() == 0) {
            ItemMachine machine_no = new ItemMachine();
            machine_no.setMachineName(context.getString(R.string.wu));
            machine_no.setMachineCode("nothing");
            machines.add(0, machine_no);
        } else {
            ItemMachine machine_all = new ItemMachine();
            machine_all.setMachineName(context.getString(R.string.suoyou));
            machine_all.setMachineCode("everything");
            machines.add(0, machine_all);
        }
        return machines;
    }

    //取得统计结果(目前未使用)
    public Map<String, String> getTotalResult(JSONObject message, Map<String, String> keys) {
        if (message == null) {
            return null;
        }
        Map<String, String> results = new HashMap<>();
        try {
            results.put("total", message.getString("total"));
            results.put("successTotal", message.getString("successTotal"));
            results.put("failTotal", message.getString("failTotal"));
            if (TextUtils.isEmpty(message.getString("tradeMoney"))) {
                results.put("tradeMoney", "0");
            } else {
                results.put("tradeMoney", message.getString("tradeMoney"));
            }

            if (!TextUtils.isEmpty(message.getString("refundMoney"))) {
                results.put("refundMoney", message.getString("refundMoney"));
            } else {
                results.put("refundMoney", "0");
            }

            if (!TextUtils.isEmpty(message.getString("cupNum"))) {
                results.put("cupNum", message.getString("cupNum"));
            }
            if (!TextUtils.isEmpty(message.getString("orangeNum"))) {
                results.put("orangeNum", message.getString("orangeNum"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    //取得统计结果
    public ItemTotal getTotalResult(JSONObject message) {
        return ItemTotal.getItemTotal(message);
    }

    //取得交易趋势数据
    public List<ItemTradeData> getTotalTrendResult(JSONArray message, Map<String, String> keys) {
        if (message == null) {
            return null;
        }
        Log.d("debug", "key==========" + keys.toString());
        List<ItemTradeData> values = new ArrayList<>();
        try {
            for (int i = 0; i < message.length(); i++) {
                ItemTradeData data = new ItemTradeData();
                JSONObject object = (JSONObject) message.get(i);
                if (object.has("tradeMoney")) {
                    if (TextUtils.isEmpty(object.getString("tradeMoney"))) {
                        data.setTradeMoney("0");
                    } else {
                        data.setTradeMoney(object.getString("tradeMoney"));
                    }
                }
                if (object.has("total")) {
                    if (TextUtils.isEmpty(object.getString("total"))) {
                        data.setTotal("0");
                    } else {
                        data.setTotal(object.getString("total"));
                    }
                }
                if (object.has("alipay")) {
                    if (TextUtils.isEmpty(object.getString("alipay"))) {
                        data.setAlipay("0");
                    } else {
                        data.setAlipay(object.getString("alipay"));
                    }
                }
                if (object.has("successTotal")) {
                    if (TextUtils.isEmpty(object.getString("successTotal"))) {
                        data.setSuccessTotal("0");
                    } else {
                        data.setSuccessTotal(object.getString("successTotal"));
                    }
                }
                if (object.has("failTotal")) {
                    if (TextUtils.isEmpty(object.getString("failTotal"))) {
                        data.setFailTotal("0");
                    } else {
                        data.setFailTotal(object.getString("failTotal"));
                    }
                }
                if (object.has("orangeNum")) {
                    if (TextUtils.isEmpty(object.getString("orangeNum"))) {
                        data.setOrangeNum("0");
                    } else {
                        data.setOrangeNum(object.getString("orangeNum"));
                    }
                }
                if (object.has("weChat")) {
                    if (TextUtils.isEmpty(object.getString("weChat"))) {
                        data.setWeChat("0");
                    } else {
                        data.setWeChat(object.getString("weChat"));
                    }
                }
                if (object.has("time")) {
                    if (TextUtils.isEmpty(object.getString("time"))) {
                        data.setTime("0");
                    } else {
                        data.setTime(object.getString("time"));
                    }
                }
                if (object.has("refundMoney")) {
                    if (TextUtils.isEmpty(object.getString("refundMoney"))) {
                        data.setRefundMoney("0");
                    } else {
                        data.setRefundMoney(object.getString("refundMoney"));
                    }
                }
                if (object.has("cash")) {
                    if (TextUtils.isEmpty(object.getString("cash"))) {
                        data.setCash("0");
                    } else {
                        data.setCash(object.getString("cash"));
                    }
                }
                if (object.has("cupNum")) {
                    if (TextUtils.isEmpty(object.getString("cupNum"))) {
                        data.setCupNum("0");
                    } else {
                        data.setCupNum(object.getString("cupNum"));
                    }
                }
                values.add(i, data);
            }
        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }
        return values;
    }

    //取得故障统计结果
    public Map<String, String> getFaultResult(JSONObject message) {
        if (message == null) {
            return null;
        }
        Map<String, String> results = new HashMap<>();
        try {
            results.put("total", message.getString("total"));
            if (TextUtils.equals(message.getString("spendTime"), "NaN")) {
                results.put("spendTime", "0");
            } else {
                results.put("spendTime", message.getString("spendTime"));
            }
            results.put("dealTotal", message.getString("dealTotal"));
            int nDealTotal = Integer.parseInt(message.getString("total")) - Integer.parseInt(message.getString("dealTotal"));
            if (nDealTotal >= 0) {
                results.put("nDealTotal", String.valueOf(nDealTotal));
            }
            if (TextUtils.isEmpty(message.getString("dealTotal")) || TextUtils.equals(message.getString("dealTotal"), "0")) {
                results.put("avgTime", "0");
            } else {
                float avgTime = Float.parseFloat(message.getString("spendTime")) / Float.parseFloat(message.getString("dealTotal"));
                DecimalFormat format = new DecimalFormat("#.00");
                results.put("avgTime", String.valueOf(format.format(avgTime)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    //取得故障趋势数据
    public List<ItemExceptionData> getFaultTrendResult(JSONArray message) {
        if (message == null) {
            return null;
        }
        List<ItemExceptionData> values = new ArrayList<>();
        try {
            for (int i = 0; i < message.length(); i++) {
                ItemExceptionData data = new ItemExceptionData();
                JSONObject object = (JSONObject) message.get(i);
                if (object.has("total")) {
                    if (TextUtils.isEmpty(object.getString("total"))) {
                        data.setTotal("0");
                    } else {
                        data.setTotal(object.getString("total"));
                    }
                }
                if (object.has("dealTotal")) {
                    if (TextUtils.isEmpty(object.getString("dealTotal"))) {
                        data.setDealTotal("0");
                    } else {
                        data.setTotal(object.getString("dealTotal"));
                    }
                }
                if (object.has("time")) {
                    if (TextUtils.isEmpty(object.getString("time"))) {
                        data.setTime("0");
                    } else {
                        data.setTime(object.getString("time"));
                    }
                }
                int nDealTotal = Integer.parseInt(object.getString("total")) - Integer.parseInt(object.getString("dealTotal"));
                if (nDealTotal >= 0) {
                    data.setnDealTotal(String.valueOf(nDealTotal));
                }
                if (object.has("spendTime")) {
                    if (TextUtils.isEmpty(object.getString("spendTime"))) {
                        data.setSpendTime("0");
                    } else {
                        data.setSpendTime(object.getString("spendTime"));
                    }
                }
                values.add(i, data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return values;
    }
}
