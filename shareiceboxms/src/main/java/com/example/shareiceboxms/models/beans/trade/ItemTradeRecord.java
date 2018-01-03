package com.example.shareiceboxms.models.beans.trade;

import android.util.Log;

import com.example.shareiceboxms.models.beans.ItemCompany;
import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.ItemPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/9.
 */

public class ItemTradeRecord {
    /*    tradeID:5459882,//Number 交易ID
        tradeCode:'sajjssasa5512'//String 交易单号
        settlementState:0,//Number 交易结算状态，详情见交易结算状态代码
        createTime:'2016-04-03 12:12:58',//String 订单创建时间
        payState:0,//Number 交易订单状态，详情见交易订单状态代码
        realRefundNum:1,//String 退款次数

        tradeMoney:545.2,//Number 交易金额（订单金额）
        receiveMoney:1100.00,//Number 实收金额
        serviceCharge:21,//Number 手续费
        chargeProportion:12,//Number 手续费比例（费率）
        refundMoney:45415.2,//Number 退款总金额
        realRefundMoney:45415.2,//Number 实退金额
        refundServiceCharge:0.03,//Number 退回手续费
        settlementMoney:0.03,//Number 结算金额
        realSettlementMoney:0.03,//Number 实际结算金额
        refundSettlementMoney:0.03,//Number 退回结算金额
        unlockTime:'2016-04-03 12:12:58',//String 交易开锁时间
        openingTime:'2016-04-03 12:12:58',//String 交易开门时间
        closingTime:'2016-04-03 12:12:58',//String 交易关门时间

        machine:
        {
            机器精简版
        },//机器
        agent:
        {
            用户精简版
        },//代理商
        consumer:
        {
            消费者
        },//消费者
        company:
        {
            公司
        },//公司*/

/*    {
        "refundSettlementMoney":null, "realRefundNum":null, "openingTime":null, "payState":
        null, "realSettlementMoney":null, "realRefundMoney":null, "serviceCharge":0.111000,
            "closingTime":null,
            "userCutModel":{
        "disable":0, "name":"代理商", "tel":"18888888888", "userType":2, "userID":29, "email":
        "agent@changhong.com"
    },
        "company":{
        "agentWechat":"444", "bankAccount":"333", "companyID":16, "minBalance":
        4545.000000, "companyAddress":"510703|四川-绵阳-涪城|绵州大道中段长虹工业园"
                , "companyName":"代理商", "agentAlipay":"555", "companyCreditCode":
        "222", "settlementProportion":40, "settleWay":0
    }
        ,"refundServiceCharge":null,
            machineCutModel:{
        "machineAddress":"||", "machineCode":"121231", "machineID":1, "machineName":"测"
    },
        "consumer":{
        "consumerType":0, "unpaidMoney":0.000000, "money":5623.000000, "transactionsNum":
        263, "consumerID":1, "disable":1, "openID":"652322563", "nickname":"程序猿"
    },
        "settleWay":0, "tradeID":1, "receiveMoney":null, "tradeCode":"12312312312", "refundMoney":
        213, "settlementState":null, "tradeMoney":11.10,
            "createTime":"2018-01-03 11:07:43.0", "unlockTime":null, "settlementMoney":
/*   0*//*.333000, "chargeProportion":0.0100, "settlementProportion":0.0300
    }*/

    public int tradeID;
    public String tradeCode;
    public int settlementState;
    public String createTime;
    public int payState;
    public String realRefundNum;
    public String tradeMoney;
    public String receiveMoney;
    public String serviceCharge;
    public String refundMoney;
    public String realRefundMoney;
    public String refundServiceCharge;
    public String settlementMoney;
    public String realSettlementMoney;
    public String chargeProportion;
    public String refundSettlementMoney;
    public String unlockTime;
    public String openingTime;
    public String closingTime;
    public String settlementProportion;
    public String payTime;
    public int settleWay;
    public ItemMachine machine;
    public ItemPerson agent;
    public ItemPerson consumer;
    public ItemCompany company;

    public static List<ItemTradeRecord> bindTradeRecordsList(JSONArray list) throws JSONException {
        List<ItemTradeRecord> itemTradeRecords = new ArrayList<>();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = (JSONObject) list.get(i);
                ItemTradeRecord itemTradeRecord = new ItemTradeRecord();
                itemTradeRecord.tradeID = item.getInt("tradeID");
                itemTradeRecord.tradeCode = item.getString("tradeCode");
                if (item.has("settlementState") && !item.get("settlementState").equals(null)) {
                    itemTradeRecord.settlementState = item.getInt("settlementState");
                }
                itemTradeRecord.createTime = item.getString("createTime");
                if (item.has("payState") && !item.get("payState").equals(null)) {
                    itemTradeRecord.payState = item.getInt("payState");
                }
                if (item.has("realRefundNum") && !item.get("realRefundNum").equals(null)) {
                    itemTradeRecord.realRefundNum = item.getString("realRefundNum");
                }
                if (item.has("tradeMoney") && !item.get("tradeMoney").equals(null)) {
                    itemTradeRecord.tradeMoney = item.getString("tradeMoney");
                }
                if (item.has("receiveMoney") && !item.get("receiveMoney").equals(null)) {
                    itemTradeRecord.receiveMoney = item.getString("receiveMoney");
                }
                if (item.has("serviceCharge") && !item.get("serviceCharge").equals(null)) {
                    itemTradeRecord.serviceCharge = item.getString("serviceCharge");
                }
                if (item.has("chargeProportion") && !item.get("chargeProportion").equals(null)) {
                    itemTradeRecord.chargeProportion = item.getString("chargeProportion");
                }
                if (item.has("refundMoney") && !item.get("refundMoney").equals(null)) {
                    itemTradeRecord.refundMoney = item.getString("refundMoney");
                }
                if (item.has("realRefundMoney") && !item.get("realRefundMoney").equals(null)) {
                    itemTradeRecord.realRefundMoney = item.getString("realRefundMoney");
                }
                if (item.has("refundServiceCharge") && !item.get("refundServiceCharge").equals(null)) {
                    itemTradeRecord.refundServiceCharge = item.getString("refundServiceCharge");
                }
                if (item.has("settlementMoney") && !item.get("settlementMoney").equals(null)) {
                    itemTradeRecord.settlementMoney = item.getString("settlementMoney");
                }
                if (item.has("realSettlementMoney") && !item.get("realSettlementMoney").equals(null)) {
                    itemTradeRecord.realSettlementMoney = item.getString("realSettlementMoney");
                }
                if (item.has("refundSettlementMoney") && !item.get("refundSettlementMoney").equals(null)) {
                    itemTradeRecord.refundSettlementMoney = item.getString("refundSettlementMoney");
                }
                if (item.has("unlockTime") && !item.get("unlockTime").equals(null)) {
                    itemTradeRecord.unlockTime = item.getString("unlockTime");
                }
                if (item.has("openingTime") && !item.get("openingTime").equals(null)) {
                    itemTradeRecord.openingTime = item.getString("openingTime");
                }
                if (item.has("closingTime") && !item.get("closingTime").equals(null)) {
                    itemTradeRecord.closingTime = item.getString("closingTime");
                }
                if (item.has("settlementProportion") && !item.get("settlementProportion").equals(null)) {
                    itemTradeRecord.settlementProportion = item.getString("settlementProportion");
                }
                if (item.has("payTime") && !item.get("payTime").equals(null)) {
                    itemTradeRecord.payTime = item.getString("payTime");
                }
                if (item.has("settleWay") && !item.get("settleWay").equals(null)) {
                    itemTradeRecord.settleWay = item.getInt("settleWay");

                }
                if (item.has("machine") && !item.get("machine").equals(null))
                    itemTradeRecord.machine = ItemMachine.bindMachine(item.getJSONObject("machine"));

                if (item.has("consumer") && !item.get("consumer").equals(null))
                    itemTradeRecord.consumer = ItemPerson.bindPerson(item.getJSONObject("consumer"));

                if (item.has("agent") && !item.get("agent").equals(null))
                    itemTradeRecord.agent = ItemPerson.bindPerson(item.getJSONObject("agent"));

                if (item.has("company") && !item.get("company").equals(null))
                    itemTradeRecord.company = ItemCompany.bindCompanyFull(item.getJSONObject("company"));

                itemTradeRecords.add(itemTradeRecord);
            }
        } catch (JSONException e) {
            Log.e("ItemTradeRecord", e.toString());
        }
        return itemTradeRecords;
    }

    public static ItemTradeRecord bindTradeRecord(JSONObject item) throws JSONException {
        ItemTradeRecord itemTradeRecord = new ItemTradeRecord();
        try {
            itemTradeRecord.tradeID = item.getInt("tradeID");
            itemTradeRecord.tradeCode = item.getString("tradeCode");
            if (item.has("settlementState") && !item.get("settlementState").equals(null)) {
                itemTradeRecord.settlementState = item.getInt("settlementState");
            }
            itemTradeRecord.createTime = item.getString("createTime");
            if (item.has("payState") && !item.get("payState").equals(null)) {
                itemTradeRecord.payState = item.getInt("payState");
            }
            if (item.has("realRefundNum") && !item.get("realRefundNum").equals(null)) {
                itemTradeRecord.realRefundNum = item.getString("realRefundNum");
            }
            if (item.has("tradeMoney") && !item.get("tradeMoney").equals(null)) {
                itemTradeRecord.tradeMoney = item.getString("tradeMoney");
            }
            if (item.has("receiveMoney") && !item.get("receiveMoney").equals(null)) {
                itemTradeRecord.receiveMoney = item.getString("receiveMoney");
            }
            if (item.has("serviceCharge") && !item.get("serviceCharge").equals(null)) {
                itemTradeRecord.serviceCharge = item.getString("serviceCharge");
            }
            if (item.has("chargeProportion") && !item.get("chargeProportion").equals(null)) {
                itemTradeRecord.chargeProportion = item.getString("chargeProportion");
            }
            if (item.has("refundMoney") && !item.get("refundMoney").equals(null)) {
                itemTradeRecord.refundMoney = item.getString("refundMoney");
            }
            if (item.has("realRefundMoney") && !item.get("realRefundMoney").equals(null)) {
                itemTradeRecord.realRefundMoney = item.getString("realRefundMoney");
            }
            if (item.has("refundServiceCharge") && !item.get("refundServiceCharge").equals(null)) {
                itemTradeRecord.refundServiceCharge = item.getString("refundServiceCharge");
            }
            if (item.has("settlementMoney") && !item.get("settlementMoney").equals(null)) {
                itemTradeRecord.settlementMoney = item.getString("settlementMoney");
            }
            if (item.has("realSettlementMoney") && !item.get("realSettlementMoney").equals(null)) {
                itemTradeRecord.realSettlementMoney = item.getString("realSettlementMoney");
            }
            if (item.has("refundSettlementMoney") && !item.get("refundSettlementMoney").equals(null)) {
                itemTradeRecord.refundSettlementMoney = item.getString("refundSettlementMoney");
            }
            if (item.has("unlockTime") && !item.get("unlockTime").equals(null)) {
                itemTradeRecord.unlockTime = item.getString("unlockTime");
            }
            if (item.has("openingTime") && !item.get("openingTime").equals(null)) {
                itemTradeRecord.openingTime = item.getString("openingTime");
            }
            if (item.has("closingTime") && !item.get("closingTime").equals(null)) {
                itemTradeRecord.closingTime = item.getString("closingTime");
            }
            if (item.has("settlementProportion") && !item.get("settlementProportion").equals(null)) {
                itemTradeRecord.settlementProportion = item.getString("settlementProportion");
            }
            if (item.has("payTime") && !item.get("payTime").equals(null)) {
                itemTradeRecord.payTime = item.getString("payTime");
            }
            if (item.has("settleWay") && !item.get("settleWay").equals(null)) {
                itemTradeRecord.settleWay = item.getInt("settleWay");

            }
            if (item.has("machine") && !item.get("machine").equals(null))
                itemTradeRecord.machine = ItemMachine.bindMachine(item.getJSONObject("machine"));

            if (item.has("consumer") && !item.get("consumer").equals(null))
                itemTradeRecord.consumer = ItemPerson.bindPerson(item.getJSONObject("consumer"));

            if (item.has("agent") && !item.get("agent").equals(null))
                itemTradeRecord.agent = ItemPerson.bindPerson(item.getJSONObject("agent"));

            if (item.has("company") && !item.get("company").equals(null))
                itemTradeRecord.company = ItemCompany.bindCompanyFull(item.getJSONObject("company"));

        } catch (JSONException e) {
            Log.e("ItemTradeRecord", e.toString());
        }
        return itemTradeRecord;
    }

    /*
    * 交易记录精简版{
 	tradeID:5459882,//Number 交易ID
    tradeCode:'sajjssasa5512'//String 交易单号
    settlementState:0,//Number 交易结算状态，详情见交易结算状态代码
    createTime:'2016-04-03 12:12:58',//String 订单创建时间
    payState:0,//Number 交易订单状态，详情见交易订单状态代码
    realRefundNum:1,//String 退款次数
  	tradeMoney:545.2,//Number 交易金额（订单金额）
}
    *
    * */
    public static ItemTradeRecord bindTradeRecordNotFull(JSONObject item) throws JSONException {
        ItemTradeRecord itemTradeRecord = new ItemTradeRecord();
        try {
            itemTradeRecord.tradeID = item.getInt("tradeID");
            itemTradeRecord.tradeCode = item.getString("tradeCode");
            itemTradeRecord.settlementState = item.getInt("settlementState");
            itemTradeRecord.createTime = item.getString("createTime");
            itemTradeRecord.payState = item.getInt("payState");
            itemTradeRecord.realRefundNum = item.getString("realRefundNum");
            itemTradeRecord.tradeMoney = item.getString("tradeMoney");
        } catch (JSONException e) {
            Log.e("ItemTradeRecord", e.toString());
        }
        return itemTradeRecord;
    }
}
