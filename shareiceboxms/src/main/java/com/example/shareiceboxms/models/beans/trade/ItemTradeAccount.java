package com.example.shareiceboxms.models.beans.trade;

import android.util.Log;

import com.example.shareiceboxms.models.beans.ItemCompany;
import com.example.shareiceboxms.models.beans.ItemMachine;
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
    public String divideMoney = "0";
    public String offsetMoney = "0";
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
                if (item.has("settlementID") && !item.get("settlementID").equals(null))
                    itemTradeAccount.divideID = item.getInt("settlementID");

                if (item.has("createTime") && !item.get("createTime").equals(null))
                    itemTradeAccount.createTime = item.getString("createTime");

                if (item.has("checkTime") && !item.get("checkTime").equals(null))
                    itemTradeAccount.checkTime = item.getString("checkTime");

                if (item.has("configTime") && !item.get("configTime").equals(null))
                    itemTradeAccount.configTime = item.getString("configTime");

                if (item.has("configTransferTime") && !item.get("configTransferTime").equals(null))
                    itemTradeAccount.configTransferTime = item.getString("configTransferTime");

                if (item.has("recheckTime") && !item.get("recheckTime").equals(null))
                    itemTradeAccount.recheckTime = item.getString("recheckTime");

                if (item.has("cancelTime") && !item.get("cancelTime").equals(null))
                    itemTradeAccount.cancelTime = item.getString("cancelTime");

                if (item.has("startTime") && !item.get("startTime").equals(null))
                    itemTradeAccount.startTime = item.getString("startTime");

                if (item.has("endTime") && !item.get("endTime").equals(null))
                    itemTradeAccount.endTime = item.getString("endTime");

                if (item.has("settlementState") && !item.get("settlementState").equals(null))
                    itemTradeAccount.divideState = item.getInt("settlementState");

                if (item.has("divideNum") && !item.get("divideNum").equals(null))
                    itemTradeAccount.divideNum = item.getInt("divideNum");

                if (item.has("settlementMoney") && !item.get("settlementMoney").equals(null))
                    itemTradeAccount.divideMoney = item.getString("settlementMoney");

                if (item.has("offsetMoney") && !item.get("offsetMoney").equals(null))
                    itemTradeAccount.offsetMoney = item.getString("offsetMoney");

                if (item.has("actualPayment") && !item.get("actualPayment").equals(null))
                    itemTradeAccount.actualPayment = item.getString("actualPayment");

                if (item.has("createType") && !item.get("createType").equals(null))
                    itemTradeAccount.divideType = item.getInt("createType");

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

    public static ItemTradeAccount bindTradeAccount(JSONObject item) throws JSONException {
        ItemTradeAccount itemTradeAccount = new ItemTradeAccount();
        try {

            if (item.has("divideID") && !item.get("divideID").equals(null))
                itemTradeAccount.divideID = item.getInt("divideID");

            if (item.has("createTime") && !item.get("createTime").equals(null))
                itemTradeAccount.createTime = item.getString("createTime");

            if (item.has("checkTime") && !item.get("checkTime").equals(null))
                itemTradeAccount.checkTime = item.getString("checkTime");

            if (item.has("configTime") && !item.get("configTime").equals(null))
                itemTradeAccount.configTime = item.getString("configTime");

            if (item.has("configTransferTime") && !item.get("configTransferTime").equals(null))
                itemTradeAccount.configTransferTime = item.getString("configTransferTime");

            if (item.has("recheckTime") && !item.get("recheckTime").equals(null))
                itemTradeAccount.recheckTime = item.getString("recheckTime");

            if (item.has("cancelTime") && !item.get("cancelTime").equals(null))
                itemTradeAccount.cancelTime = item.getString("cancelTime");

            if (item.has("startTime") && !item.get("startTime").equals(null))
                itemTradeAccount.startTime = item.getString("startTime");

            if (item.has("endTime") && !item.get("endTime").equals(null))
                itemTradeAccount.endTime = item.getString("endTime");

            if (item.has("divideState") && !item.get("divideState").equals(null))
                itemTradeAccount.divideState = item.getInt("divideState");

            if (item.has("divideNum") && !item.get("divideNum").equals(null))
                itemTradeAccount.divideNum = item.getInt("divideNum");

            if (item.has("divideMoney") && !item.get("divideMoney").equals(null))
                itemTradeAccount.divideMoney = item.getString("divideMoney");

            if (item.has("offsetMoney") && !item.get("offsetMoney").equals(null))
                itemTradeAccount.offsetMoney = item.getString("offsetMoney");

            if (item.has("actualPayment") && !item.get("actualPayment").equals(null))
                itemTradeAccount.actualPayment = item.getString("actualPayment");

            if (item.has("actualPayment") && !item.get("actualPayment").equals(null))
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
        } catch (JSONException e) {
            Log.e("ItemTradeAccounts", e.toString());
        }
        return itemTradeAccount;
    }
}
