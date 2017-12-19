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
    public String RFID;
    public String inputTime;
    public String bindingTime;
    public String exhibitTime;
    public Integer residueStorageTime;

    //    private UserCutModel agent;
//    private CompanyCutModel company;
//    private MachineCutModel machine;
    public static List<ItemProduct> bindProductList(JSONArray list) throws JSONException {
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
                itemProduct.RFID = item.getString("RFID");
                itemProduct.inputTime = item.getString("inputTime");
                itemProduct.bindingTime = item.getString("bindingTime");
                itemProduct.exhibitTime = item.getString("exhibitTime");
                itemProduct.residueStorageTime = item.getInt("residueStorageTime");
                itemProducts.add(itemProduct);
            }
        } catch (JSONException e) {
            Log.e("ItemMachine", e.toString());
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
