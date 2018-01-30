package com.example.shareiceboxms.models.beans;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.shareiceboxms.models.beans.trade.ItemTradeTotal;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18.
 */

public class PerSonMessage {
    private static JSONObject userObject;
    public static int userId;
    public static String name;
    public static String email;
    public static String tel;
    public static int userType;
    public static int disable;
    public static String loginAccount;
    public static String loginPassword;
    public static String role = "1-2-3";
    public static String address;
    public static String idCard;
    public static String lastLoginTime;
    public static String loginIP;
    public static String companyName;
    public static String companyCreditCode;
    public static Integer settleWay;//sp
    public static Integer settlementProportion;//sp
    public static String minBalance;//sp
    public static String bankAccount;//sp
    public static boolean isexcit = false;
    /*    company:{公司},
        agent:{用户精简版},//机器管理员对象*/
    public static ItemPerson agent;
    public static ItemCompany company;



    public static List<ItemPerson> childPerson = new ArrayList<>();

    public static void bindMessage(String userJson) {
        try {
            userObject = new JSONObject(userJson);
            JSONObject data = userObject.getJSONObject("d");
            userId = userObject.getInt("userID");
            name = data.getString("name");
            email = data.getString("email");
            tel = data.getString("tel");
            userType = data.getInt("userType");
            disable = data.getInt("disable");
            loginAccount = userObject.getString("loginAccount");
            role = userObject.getString("role");
            address = data.getString("address");
            idCard = data.getString("idCard");
            lastLoginTime = data.getString("lastLoginTime");
            loginIP = data.getString("loginIP");

            if (data.has("agent") && !data.get("agent").equals(null)) {
                agent = ItemPerson.bindPerson(data.getJSONObject("agent"));
            }
            if (data.has("company") && !data.get("company").equals(null)) {
                company = ItemCompany.bindCompany(data.getJSONObject("company"));
            }
            /*
            * 下面这些真是多余，但又用到了，不改
            * */
            JSONObject companyObject = userObject.getJSONObject("d").getJSONObject("company");
            if (companyObject.has("settleWay")) {
                settleWay = companyObject.getInt("settleWay");
            }
            companyName = companyObject.getString("companyName");
            companyCreditCode = companyObject.getString("companyCreditCode");
            if (companyObject.has("settlementProportion")) {
                settlementProportion = companyObject.getInt("settlementProportion");
            }
            if (companyObject.has("minBalance")) {
                minBalance = companyObject.getString("minBalance");

            }
            if (companyObject.has("bankAccount")) {
                bankAccount = companyObject.getString("bankAccount");
            }

        } catch (JSONException e) {
            Log.e("PerSonMessage", e.toString());
        }
    }

    //退出登录
    public static class GetOutLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e("退出登录", "request URL: " + HttpRequstUrl.TRADE_TOTAL_URL);
                response = OkHttpUtil.post(HttpRequstUrl.OUT_LOGIN_URL, JsonUtil.mapToJson(RequestParamsContants.getInstance().getOutLogin()));
                Log.e("退出登录", "response" + response.toString());
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                return true;
            } catch (IOException e) {
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                err = RequstTips.JSONException_Tip;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }
    }

}
