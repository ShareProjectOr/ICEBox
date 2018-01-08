package com.example.shareiceboxms.models.beans.trade;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2018/1/8.
 * 冲抵的应退记录
 */

public class ItemChongdiRefundRecord {
    /*refundMoney:52.3,//Number 应退金额
    refundID:5454545,//Number 退款记录(退款ID)
    refundTime:'2016-04-03 12:12:00',//String 退款时间*/
    public String refundMoney;
    public String refundTime;
    public int refundID;

    public static List<ItemChongdiRefundRecord> bindRefundRecordsList(JSONArray list) throws JSONException {
        List<ItemChongdiRefundRecord> itemTradeRecords = new ArrayList<>();
        for (int i = 0; i < list.length(); i++) {
            JSONObject item = (JSONObject) list.get(i);
            ItemChongdiRefundRecord itemTradeRecord = new ItemChongdiRefundRecord();
            itemTradeRecord.refundMoney = item.getString("refundMoney");
            itemTradeRecord.refundTime = item.getString("refundTime");
            itemTradeRecord.refundID = item.getInt("refundID");
        }
        return itemTradeRecords;
    }
}
