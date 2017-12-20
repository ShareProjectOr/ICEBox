package com.example.shareiceboxms.models.beans;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/9.
 */

public class ItemProduct {
    private boolean isChecked = false;
    public Integer goodsID;
    public Integer categoryID;
    public String goodsName;
    public float price;
    public float activityPrice;
    public String rFID;
    public String inputTime;
    public String bindingTime;
    public String exhibitTime;
    public Integer residueStorageTime;
    /*
    * 保存机器实例，可以获得agent,manager,company
    * */
    public ItemMachine machine;

/*   {

        "goodsID": 1,
        "RFID": "263132546",
        "inputTime": "2017-12-18 09:39:28.0",
        "exhibitTime": "2017-12-15 09:39:40.0",
        "residueStorageTime": 52443,
        "bindingTime": "2017-12-13 09:39:36.0",
        "activityPrice": 21.1,
        "machine": {
          "machineAddress": "441502",
          "machineCode": "121231",
          "machineID": 1,
          "machineName": "测"
        },
        "price": 23.5,
        "company": {
          "companyID": 1,
          "companyAddress": "四川省绵阳市",
          "companyName": "测试公司"
        },
        "goodsName": "统一",
        "categoryID": 13
      },*/


    public static List<ItemProduct> bindProductList(JSONArray list, ItemMachine itemMachine) throws JSONException {
        List<ItemProduct> itemProducts = new ArrayList<>();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = (JSONObject) list.get(i);
                ItemProduct itemProduct = new ItemProduct();
                itemProduct.goodsID = item.getInt("goodsID");
                itemProduct.categoryID = item.getInt("categoryID");
                itemProduct.goodsName = item.getString("goodsName");
                itemProduct.price = (float) item.getDouble("price");
                itemProduct.activityPrice = (float) item.getDouble("activityPrice");
                itemProduct.rFID = item.getString("rFID");
                itemProduct.inputTime = item.getString("inputTime");
                itemProduct.bindingTime = item.getString("bindingTime");
                itemProduct.exhibitTime = item.getString("exhibitTime");
                itemProduct.residueStorageTime = item.getInt("residueStorageTime");
                if (itemMachine != null) {
                    itemProduct.machine = itemMachine;
                }
                itemProducts.add(itemProduct);
            }
        } catch (JSONException e) {
            Log.e("ItemProduct", e.toString());
        }
        return itemProducts;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
