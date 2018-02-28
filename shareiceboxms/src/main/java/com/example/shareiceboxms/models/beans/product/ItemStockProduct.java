package com.example.shareiceboxms.models.beans.product;

import android.util.Log;

import com.example.shareiceboxms.models.beans.ItemCompany;
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

    /*
    *
    {
        "activityPrice":null, "machine":{
        "machineAddress":"510703|四川-绵阳-涪城|长虹商贸中心"
                , "machineCode":"20180201100000001", "machineID":15, "machineName":"商贸中心一号机"
    },"price":8.500000, "num":3, "goods"
    "company":{"companyID":44,"companyAddress":"510703|四川-绵阳-涪城|","companyName":"谢刚","companyCreditCode":"45454"},"goodsName":"有友泡椒凤爪","categoryID":40
    * */
    private boolean isChecked = false;
    public String activityPrice;
    public String price;
    public Integer num;
    public Integer categoryID;
    public String goodsName;
    public ItemMachine machine;
    public ItemCompany company;
    public List<GoodsProduct> goodsProducts = new ArrayList<>();


    public static List<ItemStockProduct> bindProductList(JSONArray list, ItemMachine itemMachine) throws JSONException {
        if (list == null) return null;
        List<ItemStockProduct> itemProducts = new ArrayList<>();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = (JSONObject) list.get(i);
                ItemStockProduct itemProduct = new ItemStockProduct();
                itemProduct.categoryID = item.getInt("categoryID");
                itemProduct.num = item.getInt("num");
                itemProduct.goodsName = item.getString("goodsName");
                itemProduct.price = item.getString("price");
                itemProduct.activityPrice = item.getString("activityPrice");
                if (item.has("goods") && !item.get("goods").equals(null)) {
                    itemProduct.goodsProducts = bindGoodsList(item.getJSONArray("goods"));
                }
                if (itemMachine != null) {
                    itemProduct.machine = itemMachine;
                } else {
                    if (item.has("machine") && !item.get("machine").equals(null)) {
                        itemProduct.machine = ItemMachine.bindMachineNotFull(item.getJSONObject("machine"));
                    }
                }
                if (item.has("company") && !item.get("company").equals(null)) {
                    itemProduct.company = ItemCompany.bindCompany(item.getJSONObject("company"));
                }
                itemProducts.add(itemProduct);
            }
        } catch (JSONException e) {
            Log.e("ItemStockProduct", e.toString());
        }
        return itemProducts;
    }

    public static List<GoodsProduct> bindGoodsList(JSONArray list) throws
            JSONException {
        if (list == null) return null;
        List<GoodsProduct> itemProducts = new ArrayList<>();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = (JSONObject) list.get(i);
                GoodsProduct itemProduct = new GoodsProduct();
                itemProduct.goodsID = item.getInt("goodsID");
                itemProduct.categoryID = item.getInt("categoryID");
                itemProduct.goodsName = item.getString("goodsName");
                itemProduct.price = item.getString("price");
                itemProduct.activityPrice = item.getString("activityPrice");
                itemProduct.rfid = item.getString("rfid");
                itemProduct.state = item.getInt("state");
                itemProduct.inputTime = item.getString("inputTime");
                itemProduct.bindingTime = item.getString("bindingTime");
                itemProduct.exhibitTime = item.getString("exhibitTime");
                itemProduct.residueStorageTime = item.getInt("residueStorageTime");
                if (item.has("machine") && !item.get("machine").equals(null)) {
                    itemProduct.machine = ItemMachine.bindMachineNotFull(item.getJSONObject("machine"));
                }
                if (item.has("company") && !item.get("company").equals(null)) {
                    itemProduct.company = ItemCompany.bindCompany(item.getJSONObject("company"));
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

   public static class GoodsProduct {
        /*

    {
        "goodsID":1708, "inputTime":"2018-02-10 13:26:48.0", "exhibitTime":"2018-02-10 13:38:12.0",
            "residueStorageTime":-1469494, "bindingTime":"2018-02-10 13:26:48.0", "activityPrice":
        null,
                "machine":{
        "machineAddress":"510703|四川-绵阳-涪城|长虹商贸中心", "machineCode":"20180201100000001",
                "machineID":15, "machineName":"商贸中心一号机"
    },"price":8.500000, "company":{
        "companyID":44, "companyAddress":"510703|四川-绵阳-涪城|",
                "companyName":"谢刚", "companyCreditCode":"45454"
    },"rfid":"e20171027200000000001371", "state":3, "goodsName":"有友泡椒凤爪", "categoryID":40
    }*/

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
        public Integer state;
        /*
        * 保存机器实例，可以获得agent,manager,company
        * */
        public ItemMachine machine;
        public ItemCompany company;
    }
}
