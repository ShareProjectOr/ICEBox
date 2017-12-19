package com.example.shareiceboxms.models.beans;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/18.
 */

public class ItemPerson {
    /*    "agent":{"disable":0,"name":"测试代理商","tel":"1211231"
                ,"userType":0,"userID":0,"email":"1234@163.com"}
          "manager":{"disable":0,"name":"测试","tel":"1231231321","userType":0
                ,"userID":0,"email":"123@163.com"}
    */
    public int disable;
    public String name;
    public String tel;
    public int userType;
    public int userID;
    public String email;

    public static ItemPerson bindPerson(JSONObject response) throws JSONException {
        ItemPerson itemPerson = new ItemPerson();
        try {
//            JSONObject jsonObject = new JSONObject(response);
            itemPerson.disable = response.getInt("disable");
            itemPerson.name = response.getString("name");
            itemPerson.tel = response.getString("tel");
            itemPerson.userType = response.getInt("userType");
            itemPerson.userID = response.getInt("userID");
            itemPerson.email = response.getString("email");
        } catch (JSONException e) {
            Log.e("ItemMachine", e.toString());
        }
        return itemPerson;
    }
}
