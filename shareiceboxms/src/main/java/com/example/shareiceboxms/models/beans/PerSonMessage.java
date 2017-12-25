package com.example.shareiceboxms.models.beans;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/12/18.
 */

public class PerSonMessage {
    private static JSONObject userObject;
    public static int userId = 7;
    public static String name;
    public static String email;
    public static String tel;
    public static int userType;
    public static int disable;
    public static String loginAccount;
    public static String loginPassword;
    public static String role="2";
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

    public static void bindMessage(String userJson) {
        try {
            userObject = new JSONObject(userJson);
            userId = userObject.getInt("userID");
            name = userObject.getJSONObject("d").getString("name");
            email = userObject.getJSONObject("d").getString("email");
            tel = userObject.getJSONObject("d").getString("tel");
            userType = userObject.getJSONObject("d").getInt("userType");
            disable = userObject.getJSONObject("d").getInt("disable");
            loginAccount = userObject.getString("loginAccount");
            role = userObject.getString("role");
            address = userObject.getJSONObject("d").getString("address");
            idCard = userObject.getJSONObject("d").getString("idCard");
            lastLoginTime = userObject.getJSONObject("d").getString("lastLoginTime");
            loginIP = userObject.getJSONObject("d").getString("loginIP");
            JSONObject company = userObject.getJSONObject("d").getJSONObject("company");
            if (company.has("settleWay")) {
                settleWay = company.getInt("settleWay");
            }
            companyName = company.getString("companyName");
            companyCreditCode = company.getString("companyCreditCode");
            if (company.has("settlementProportion")) {
                settlementProportion = company.getInt("settlementProportion");
            }
            if (company.has("minBalance")) {
                minBalance = company.getString("minBalance");

            }
            if (company.has("bankAccount")) {
                bankAccount = company.getString("bankAccount");
            }
        } catch (JSONException e) {
            Log.e("PerSonMessage", e.toString());
        }
    }


}
