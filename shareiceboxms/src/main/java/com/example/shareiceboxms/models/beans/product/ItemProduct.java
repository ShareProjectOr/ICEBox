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
 */

public class ItemProduct {
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
    public Integer residueStorageTime;
    /*
    * 保存机器实例，可以获得agent,manager,company
    * */
    public ItemMachine machine;
/*  售出商品
    goodsID:78791212,//Number RFID的ID
    categoryID:787,//Number 商品所属品类的ID
    goodsName:'农夫山泉',//String 商品名称
    pirse:1.99，//Number 商品单价
    activityPrice:12.65,//Number 活动价格
    soldPrise:0.99,//Number 交易单价
    RFID:'67tguas8y87as',//String RFID

    inputTime:'2017-11-21 12:23:49',//String 商品录入时间
    bindingTime:'2017-11-21 12:23:49',//String 商品绑定时间
    exhibitTime:'2017-11-21 12:23:49',//String 商品上货时间
    soldTime:'2017-11-21 12:23:49',//String 商品售出时间（即数据库内订单生成时间）
    residueStorageTime:445455,//String  商品剩余存储时间,以S为单位

    isRefund:0,//商品是否退款,详情见商品退款状态码

    trade:{交易记录精简版},//交易ID
    agent:{用户精简版},//代理商
    company:{公司},//公司
    machine:{机器精简版},//机器*/

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
