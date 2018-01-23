package com.example.shareiceboxms.models.beans.product;

import android.util.Log;

import com.example.shareiceboxms.models.beans.ItemMachine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/9.
 * 库存商品
 */

public class ItemStockProduct {
    private boolean isChecked = false;
    public Integer goodsID;
    public Integer categoryID;
    public String goodsName;
    public String price;
    public String activityPrice;
    public String rfid;
    public String inputTime;
    public String bindingTime;
    public String exhibitTime;
    public Integer residueStorageTime = 0;
    /*
    * 保存机器实例，可以获得agent,manager,company
    * */
    public ItemMachine machine;

    public static List<ItemStockProduct> bindProductList(JSONArray list, ItemMachine itemMachine) throws JSONException {
        List<ItemStockProduct> itemProducts = new ArrayList<>();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = (JSONObject) list.get(i);
                ItemStockProduct itemProduct = new ItemStockProduct();
                itemProduct.goodsID = item.getInt("goodsID");
                itemProduct.categoryID = item.getInt("categoryID");
                itemProduct.goodsName = item.getString("goodsName");
                itemProduct.price = item.getString("price");
                itemProduct.activityPrice = item.getString("activityPrice");
                itemProduct.rfid = item.getString("rfid");
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
            Log.e("ItemStockProduct", e.toString());
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
