package com.example.shareiceboxms.models.contants;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
    public static String role;
    public static String address;
    public static String idCard;
    public static String lastLoginTime;
    public static String loginIP;
    public static String companyName;
    public static String companyAddress;

    public static void bindMessage(String userJson) {
        try {
            userObject = new JSONObject(userJson);
            userId = userObject.getInt("userID");
            name = userObject.getString("name");
            email = userObject.getString("email");
            tel = userObject.getString("tel");
            userType = userObject.getInt("userType");
            disable = userObject.getInt("disable");
            loginAccount = userObject.getString("loginAccount");
            role = userObject.getString("role");
            address = userObject.getString("address");
            idCard = userObject.getString("idCard");
            lastLoginTime = userObject.getString("lastLoginTime");
            loginIP = userObject.getString("loginIP");
        } catch (JSONException e) {
            Log.e("PerSonMessage", e.toString());
        }
    }


}
