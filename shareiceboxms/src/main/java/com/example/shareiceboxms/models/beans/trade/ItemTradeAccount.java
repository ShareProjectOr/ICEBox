package com.example.shareiceboxms.models.beans.trade;

import android.util.Log;

import com.example.shareiceboxms.models.beans.ItemCompany;
import com.example.shareiceboxms.models.beans.ItemPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/9.
 * 运营商创建的结算工单
 */

public class ItemTradeAccount implements Serializable {
    /*
    {
        divideID:
        74854485,//Number 工单号(分成ID)
        createTime:'2017-11-21 12:23:49',//String 工单创建时间
        creator:{
        用户精简版
    },//创建人信息
        checkTime:
        '2017-11-21 12:23:49',//String 工单审核时间
        checker:{
        用户精简版
    },//审核人信息
        configTime:
        '2017-11-21 12:23:49',//String 工单确认时间
          configer:{
        用户精简版
    },//确认人
        configTransferTime:
        '2017-11-21 12:23:49',//String 转账确认时间
        configTransfer:{
        用户精简版
    },//确认转账人
        recheckTime:
        '2017-11-21 12:23:49',//String 复审时间
         rechecker:{
        用户精简版
    },//复审人
        cancelTime：'2017-11-21 12:23:49',//String 撤销时间
        canceler:{
        用户精简版
    },//撤销人
        startTime:
        '2017-11-21 12:23:49',//String 工单开始时间
         endTime:'2017-11-21 12:23:49',//String 工单结束时间
          divideState:0,//Number 工单状态,详情见工单状态代码
            divideNum:12,//Number 交易次数
            divideMoney:45.2,//Number 结算金额
            offsetMoney:23.5,//Number 冲抵金额
            actualPayment:25.6,//Number 实际支付金额
            divideType:0,//Number 工单类型，详见工单类型代码

            company:{
        公司
    },//公司
        agent:
        {
            用户精简版
        },//代理商
    }*/

    public int divideID;
    public String createTime;
    public String checkTime;
    public String configTime;
    public String configTransferTime;
    public String recheckTime;
    public String cancelTime;
    public String startTime;
    public String endTime;
    public int divideState;
    public int divideNum;
    public String divideMoney;
    public String offsetMoney;
    public String actualPayment;
    public int divideType;


    public ItemPerson creator;
    public ItemPerson checker;
    public ItemPerson configer;
    public ItemPerson configTransfer;
    public ItemPerson rechecker;
    public ItemPerson canceler;
    public ItemPerson agent;
    public ItemCompany company;

    public static List<ItemTradeAccount> bindTradeAccountsList(JSONArray list) throws JSONException {
        List<ItemTradeAccount> itemTradeAccounts = new ArrayList<>();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = (JSONObject) list.get(i);
                ItemTradeAccount itemTradeAccount = new ItemTradeAccount();
                itemTradeAccount.divideID = item.getInt("divideID");
                itemTradeAccount.createTime = item.getString("createTime");
                itemTradeAccount.checkTime = item.getString("checkTime");
                itemTradeAccount.configTime = item.getString("configTime");
                itemTradeAccount.configTransferTime = item.getString("configTransferTime");
                itemTradeAccount.recheckTime = item.getString("recheckTime");
                itemTradeAccount.cancelTime = item.getString("cancelTime");
                itemTradeAccount.startTime = item.getString("startTime");
                itemTradeAccount.endTime = item.getString("endTime");
                itemTradeAccount.divideState = item.getInt("divideState");
                itemTradeAccount.divideNum = item.getInt("divideNum");
                itemTradeAccount.divideMoney = item.getString("divideMoney");
                itemTradeAccount.offsetMoney = item.getString("offsetMoney");
                itemTradeAccount.actualPayment = item.getString("actualPayment");
                itemTradeAccount.divideType = item.getInt("divideType");

                if (item.has("creator") && !item.get("creator").equals(null))
                    itemTradeAccount.creator = ItemPerson.bindPerson(item.getJSONObject("creator"));

                if (item.has("checker") && !item.get("checker").equals(null))
                    itemTradeAccount.checker = ItemPerson.bindPerson(item.getJSONObject("checker"));

                if (item.has("configer") && !item.get("configer").equals(null))
                    itemTradeAccount.configer = ItemPerson.bindPerson(item.getJSONObject("configer"));

                if (item.has("configTransfer") && !item.get("configTransfer").equals(null))
                    itemTradeAccount.configTransfer = ItemPerson.bindPerson(item.getJSONObject("configTransfer"));

                if (item.has("rechecker") && !item.get("rechecker").equals(null))
                    itemTradeAccount.rechecker = ItemPerson.bindPerson(item.getJSONObject("rechecker"));

                if (item.has("canceler") && !item.get("canceler").equals(null))
                    itemTradeAccount.canceler = ItemPerson.bindPerson(item.getJSONObject("canceler"));

                if (item.has("agent") && !item.get("agent").equals(null))
                    itemTradeAccount.agent = ItemPerson.bindPerson(item.getJSONObject("agent"));

                if (item.has("company") && !item.get("company").equals(null))
                    itemTradeAccount.company = ItemCompany.bindCompanyFull(item.getJSONObject("company"));

                itemTradeAccounts.add(itemTradeAccount);
            }
        } catch (JSONException e) {
            Log.e("ItemTradeAccounts", e.toString());
        }
        return itemTradeAccounts;
    }
}