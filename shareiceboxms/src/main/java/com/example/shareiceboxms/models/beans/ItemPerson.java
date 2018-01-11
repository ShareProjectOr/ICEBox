package com.example.shareiceboxms.models.beans;

import android.util.Log;

import com.example.shareiceboxms.models.contants.JsonDataParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/18.
 */

public class ItemPerson implements Serializable {
    /*    userID:788,//Number 用户编号（ID） 主键
        name:'刘江东', //String 用户ID
        email:'jiangdong.liu@chanaghong.com',//String 邮箱
        tel:'18084889887',//String 电话号码
        userType:0, //Number 用户角色，详情见用户角色状态代码
        disable:0,//Number 是否被禁用，详情见用户禁用状态代码
        loginAccount:'liujiangdong' ,//String 账户登录名
        role:'1-2-3',//String 用户权限，详情见用户权限状态代码
        address:'441502|四川省-绵阳市-涪城区|富乐路口凯德广场二期五楼',//String 用户所在 省-市-县-详细地址
        idCard:'441502199011248975',//String 身份证
        lastLoginTime:'2017-10-14 12:30:16',//String 用户最后登录时间
        loginIP:'192.168.1.1',//String 用户最后登录IP
        company:{公司},
        agent:{用户精简版},//机器管理员对象*/
    public int disable;
    public String name;
    public String tel;
    public int userType;
    public int userID;
    public String email;
    public String loginAccount;
    public String role;
    public String address;
    public String lastLoginTime;
    public String loginIP;
    public String idCard;
    public ItemPerson agent;
    public ItemCompany company;


    /*
    * 用户完整版
    * */
    public static ItemPerson bindPersonFull(JSONObject response) throws JSONException {
        ItemPerson itemPerson = new ItemPerson();
        try {
            if (response.has("disable") && !response.get("disable").equals(null)) {
                itemPerson.disable = response.getInt("disable");
            }

            if (response.has("name") && !response.get("name").equals(null)) {
                itemPerson.name = response.getString("name");
            }
            if (response.has("tel") && !response.get("tel").equals(null)) {
                itemPerson.tel = response.getString("tel");
            }
            if (response.has("userType") && !response.get("userType").equals(null)) {
                itemPerson.userType = response.getInt("userType");
            }
            if (response.has("email") && !response.get("email").equals(null)) {
                itemPerson.email = response.getString("email");
            }
            if (response.has("loginAccount") && !response.get("loginAccount").equals(null)) {
                itemPerson.loginAccount = response.getString("loginAccount");
            }

            if (response.has("role") && !response.get("role").equals(null)) {
                itemPerson.role = response.getString("role");
            }

            if (response.has("address") && !response.get("address").equals(null)) {
                itemPerson.address = response.getString("address");
            }
            if (response.has("lastLoginTime") && !response.get("lastLoginTime").equals(null)) {
                itemPerson.lastLoginTime = response.getString("lastLoginTime");
            }
            if (response.has("loginIP") && !response.get("loginIP").equals(null)) {
                itemPerson.loginIP = response.getString("loginIP");
            }

            if (response.has("idCard") && !response.get("idCard").equals(null)) {
                itemPerson.idCard = response.getString("idCard");
            }
            if (response.has("company") && !response.get("company").equals(null)) {
                itemPerson.company = ItemCompany.bindCompanyFull(response.getJSONObject("company"));
            }
            if (response.has("agent") && !response.get("agent").equals(null)) {
                itemPerson.agent = ItemPerson.bindPerson(response.getJSONObject("agent"));
            }

            if (response.has("userID") && !response.get("userID").equals(null)) {
                itemPerson.userID = response.getInt("userID");
            }


        } catch (JSONException e) {
            Log.e("ItemPerson", e.toString());
        }
        return itemPerson;
    }
    /*
    * 精简版
    * */

    public static ItemPerson bindPerson(JSONObject response) throws JSONException {
        ItemPerson itemPerson = new ItemPerson();
        try {
            if (response.has("disable") && !response.get("disable").equals(null)) {
                itemPerson.disable = response.getInt("disable");
            }

            if (response.has("name") && !response.get("name").equals(null)) {
                itemPerson.name = response.getString("name");
            }
            if (response.has("tel") && !response.get("tel").equals(null)) {
                itemPerson.tel = response.getString("tel");
            }
            if (response.has("userType") && !response.get("userType").equals(null)) {
                itemPerson.userType = response.getInt("userType");
            }
            if (response.has("email") && !response.get("email").equals(null)) {
                itemPerson.email = response.getString("email");
            }
            if (response.has("userID") && !response.get("userID").equals(null)) {
                itemPerson.userID = response.getInt("userID");
            }


        } catch (JSONException e) {
            Log.e("ItemPerson", e.toString());
        }
        return itemPerson;
    }

    public static List<ItemPerson> getPersonList(JSONArray list) throws JSONException {
        List<ItemPerson> persons = new ArrayList<>();
        for (int i = 0; i < list.length(); i++) {
            JSONObject item = (JSONObject) list.get(i);
            ItemPerson itemPerson = ItemPerson.bindPersonFull(item);
            persons.add(itemPerson);
        }
        return persons;
    }
}
