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
    public static String role = "2";
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

            if (userObject.has("agent") && !userObject.get("agent").equals(null)) {
                agent = ItemPerson.bindPerson(userObject.getJSONObject("d").getJSONObject("agent"));
            }
            if (userObject.has("company") && !userObject.get("company").equals(null)) {
                company = ItemCompany.bindCompany(userObject.getJSONObject("d").getJSONObject("company"));
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


}
