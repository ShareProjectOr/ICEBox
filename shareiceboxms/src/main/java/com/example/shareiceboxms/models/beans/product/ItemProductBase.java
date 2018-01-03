package com.example.shareiceboxms.models.beans.product;

import android.util.Log;

import com.example.shareiceboxms.models.beans.ItemCompany;
import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.ItemPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/9.
 * 商品基类
 */

public class ItemProductBase {
    private boolean isChecked = false;
    public Integer goodsID;
    public Integer categoryID;
    public String goodsName;
    public float pirse;
    public float activityPrice;
    public String rfid;
    public String inputTime;
    public String bindingTime;
    public String exhibitTime;
    public Integer residueStorageTime;
    public int state;

    public ItemPerson agent;
    public ItemCompany company;
    public ItemMachine machine;

    /*
    * 商品精简版
    * categoryID:6545454,//Number 商品品类编码（ID）
  	categoryName:'鲜奶类',//String 商品名称 联合主键
    categoryPrice:23.56,//Number 品类价格
    activityPrice:12.65,//Number 活动价格
    * */


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
