package com.example.shareiceboxms.models.beans.trade;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WH on 2017/12/9.
 */

public class ItemTradeTotal {
    public String totalMoney;
    public String unreceiveMoney;
    //    public float receiveMoney;
    public String receiveMoney;
    public String serviceCharge;
    public String refundMoney;
    public String realRefundMoney;
    public String refundServiceCharge;
    public String alipayPoundage;
    public String wechatPoundage;
    public String incomeMoney;
    public String settledMoney;
    public String unsettleMoney;
    public String offsetedMoney;
    public String unoffsetMoney;
    public String paidMoney;
    public String unpayMoney;
    public String serviceMoney;

    //    totalMoney:1500.00,//Number 交易总金额
//    unreceiveMoney:400.00,//Number 未收到的交易总额
//    receiveMoney:1100.00,//Number 已收到的交易总额金额
//    receiveMoney:1100.00,//Number 实收金额
//    serviceCharge:21,//Number 手续费
//    refundMoney:100.00,//Number  交易退款金额
//    realRefundMoney:45415.2,//Number 实退金额
//    refundServiceCharge:0.03,//Number 退回手续费
//    alipayPoundage:6.00,//Number 支付宝手续费
//    wechatPoundage:0.00,//Number 微信手续费
//    incomeMoney:994.00,//Number 收入金额
//    settledMoney:300.00,//Number 已结算金额
//    unsettleMoney:534.96,//Number 待结算金额
//    offsetedMoney:10.00,//Number 已冲抵金额
//    unoffsetMoney:29.76,//Number 带冲抵金额
//    paidMoney:290.00,//Number 已支付金额
//    unpayMoney:505.20,//Number 待支付金额
//    serviceMoney:198.8,//Number 服务费金额
    public static ItemTradeTotal bindTradeTotal(JSONObject item) throws JSONException {
        ItemTradeTotal itemTradeTotal = new ItemTradeTotal();
        try {
            itemTradeTotal.totalMoney = item.getString("unreceiveMoney");
            itemTradeTotal.unreceiveMoney = item.getString("unreceiveMoney");
            itemTradeTotal.receiveMoney = item.getString("receiveMoney");
            itemTradeTotal.receiveMoney = item.getString("receiveMoney");
            itemTradeTotal.serviceCharge = item.getString("serviceCharge");
            itemTradeTotal.refundMoney = item.getString("refundMoney");
            itemTradeTotal.realRefundMoney = item.getString("realRefundMoney");
            itemTradeTotal.refundServiceCharge = item.getString("refundServiceCharge");
            itemTradeTotal.alipayPoundage = item.getString("alipayPoundage");
            itemTradeTotal.wechatPoundage = item.getString("wechatPoundage");
            itemTradeTotal.incomeMoney = item.getString("incomeMoney");
            itemTradeTotal.settledMoney = item.getString("settledMoney");
            itemTradeTotal.unsettleMoney = item.getString("unsettleMoney");
            itemTradeTotal.offsetedMoney = item.getString("offsetedMoney");
            itemTradeTotal.unoffsetMoney = item.getString("unoffsetMoney");
            itemTradeTotal.paidMoney = item.getString("paidMoney");
            itemTradeTotal.unpayMoney = item.getString("unpayMoney");
            itemTradeTotal.serviceMoney = item.getString("serviceMoney");
        } catch (JSONException e) {
            Log.e("ItemTradeTotal", e.toString());
        }
        return itemTradeTotal;
    }
}
