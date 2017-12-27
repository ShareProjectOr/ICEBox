package com.example.shareiceboxms.models.beans;

import android.util.Log;

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
                itemTradeRecord.settlementState = item.getInt("settlementState");
                itemTradeRecord.createTime = item.getString("createTime");
                itemTradeRecord.payState = item.getInt("payState");
                itemTradeRecord.realRefundNum = item.getString("realRefundNum");
                itemTradeRecord.tradeMoney = item.getString("tradeMoney");
                itemTradeRecord.receiveMoney = item.getString("receiveMoney");
                itemTradeRecord.serviceCharge = item.getString("serviceCharge");
                itemTradeRecord.chargeProportion = item.getString("chargeProportion");
                itemTradeRecord.refundMoney = item.getString("refundMoney");
                itemTradeRecord.realRefundMoney = item.getString("realRefundMoney");
                itemTradeRecord.refundServiceCharge = item.getString("refundServiceCharge");
                itemTradeRecord.settlementMoney = item.getString("settlementMoney");
                itemTradeRecord.realSettlementMoney = item.getString("realSettlementMoney");
                itemTradeRecord.refundSettlementMoney = item.getString("refundSettlementMoney");
                itemTradeRecord.unlockTime = item.getString("unlockTime");
                itemTradeRecord.openingTime = item.getString("openingTime");
                itemTradeRecord.closingTime = item.getString("closingTime");

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
            itemTradeRecord.settlementState = item.getInt("settlementState");
            itemTradeRecord.createTime = item.getString("createTime");
            itemTradeRecord.payState = item.getInt("payState");
            itemTradeRecord.realRefundNum = item.getString("realRefundNum");
            itemTradeRecord.tradeMoney = item.getString("tradeMoney");
            itemTradeRecord.receiveMoney = item.getString("receiveMoney");
            itemTradeRecord.serviceCharge = item.getString("serviceCharge");
            itemTradeRecord.chargeProportion = item.getString("chargeProportion");
            itemTradeRecord.refundMoney = item.getString("refundMoney");
            itemTradeRecord.realRefundMoney = item.getString("realRefundMoney");
            itemTradeRecord.refundServiceCharge = item.getString("refundServiceCharge");
            itemTradeRecord.settlementMoney = item.getString("settlementMoney");
            itemTradeRecord.realSettlementMoney = item.getString("realSettlementMoney");
            itemTradeRecord.refundSettlementMoney = item.getString("refundSettlementMoney");
            itemTradeRecord.unlockTime = item.getString("unlockTime");
            itemTradeRecord.openingTime = item.getString("openingTime");
            itemTradeRecord.closingTime = item.getString("closingTime");

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
