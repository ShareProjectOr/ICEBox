package com.example.shareiceboxms.models.beans.trade;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WH on 2017/12/9.
 */

public class ItemTradeTongji {

 /*
 * {
   	userID:001(主键), //Number 用户ID
  	loginAccount:'', //String 账户登录名
  	userType:0, //Number 用户角色
    role:'1-2-3',//String 用户权限，详情见用户权限状态代码
  	d:{
      totalMoney:5241.0,//Number 交易总额
      unreceiveMoney:400.00,//Number 未收到的交易总额
      refundMoney:100.00,//Number  交易退款金额
      receiveMoney:1100.00,//Number 已收到的交易总额金额
      totalCustomer:1212,//Number 客户数量
      tradeMoney:[656,4545,12,78],//Array 交易金额
      customerNum:[45,84,876],//Array 独立客户数
  	},
  	c：200, //Number 请求状态码
  	sessionID:'hua78ad78a7aasa809hn' ,//String (session ID)
  	err:'', //String 错误提示，正常返回的错误提示必须为空
}
 * */

    public String totalMoney;
    public String unreceiveMoney;
    public String refundMoney;
    public String receiveMoney;
    public int totalCustomer;


    public static ItemTradeTongji bindTradeTongji(JSONObject item) throws JSONException {
        ItemTradeTongji itemTradeTongji = new ItemTradeTongji();
        try {
            itemTradeTongji.totalMoney = (TextUtils.equals(item.getString("totalMoney"), "null") ? "0" : item.getString("totalMoney"));
            itemTradeTongji.unreceiveMoney = (TextUtils.equals(item.getString("unreceiveMoney"), "null") ? "0" : item.getString("unreceiveMoney"));
            itemTradeTongji.receiveMoney = (TextUtils.equals(item.getString("receiveMoney"), "null") ? "0" : item.getString("receiveMoney"));
            itemTradeTongji.refundMoney = (TextUtils.equals(item.getString("refundMoney"), "null") ? "0" : item.getString("refundMoney"));
            itemTradeTongji.totalCustomer = (TextUtils.equals(item.getString("totalCustomer"), "null") ? 0 : item.getInt("totalCustomer"));
        } catch (JSONException e) {
            Log.e("ItemTradeTongji", e.toString());
        }
        return itemTradeTongji;
    }
}
