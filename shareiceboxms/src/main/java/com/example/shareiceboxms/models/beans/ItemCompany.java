package com.example.shareiceboxms.models.beans;

import android.util.Log;
import android.widget.Toast;

import com.example.shareiceboxms.models.contants.JsonDataParse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WH on 2017/12/18.
 */

public class ItemCompany {
    public String agentWechat;
    public String bankAccount;
    public int companyID;
    public int minBalance;
    public String companyAddress;
    public String companyName;
    public String agentAlipay;
    public String companyCreditCode;
    public String settlementProportion;
    public int settleWay;
    /*  "company":{"agentWechat":"12312312312","bankAccount":"","companyID":1,"minBalance":2000.000000
            ,"companyAddress":"四川省绵阳市","companyName":"测试公司","agentAlipay":"12221323123"
            ,"companyCreditCode":"010101","settlementProportion":1,"settleWay":0}*/

    public static ItemCompany bindCompanyFull(JSONObject response) throws JSONException {
        ItemCompany itemCompany = new ItemCompany();
        try {
//            JSONObject jsonObject = new JSONObject(response);
            itemCompany.companyID = response.getInt("companyID");
            itemCompany.agentWechat = response.getString("agentWechat");
            itemCompany.bankAccount = response.getString("bankAccount");
            if (response.get("minBalance").equals(null)) {
                itemCompany.minBalance = response.getInt("minBalance");
            }
            itemCompany.companyAddress = response.getString("companyAddress");
            itemCompany.companyName = response.getString("companyName");
            itemCompany.agentAlipay = response.getString("agentAlipay");
            itemCompany.companyCreditCode = response.getString("companyCreditCode");
            itemCompany.settlementProportion = response.getString("settlementProportion");
            itemCompany.settleWay = response.getInt("settleWay");
        } catch (JSONException e) {
            Log.e("ItemCompany", e.toString());
        }
        return itemCompany;
    }

    /*
    * 公司精简版
    *   companyID:4545454,//Number 公司ID
  companyName:'长虹技佳精工有限公司',//String 公司名称
  companyAddress:'441502|四川省-绵阳市-涪城区|富乐路口凯德广场二期五楼',//String 机器安装地址省-市-县-详细地址
    * */
    public static ItemCompany bindCompany(JSONObject response) throws JSONException {
        ItemCompany itemCompany = new ItemCompany();
        try {
            itemCompany.companyID = response.getInt("companyID");
            itemCompany.companyAddress = response.getString("companyAddress");
            itemCompany.companyName = response.getString("companyName");
        } catch (JSONException e) {
            Log.e("ItemCompany", e.toString());
        }
        return itemCompany;
    }
}
