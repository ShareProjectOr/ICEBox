package com.example.shareiceboxms.models.beans;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/25.
 */

public class ItemDetailsUpLoad {
    public String goodsName;
    public String price;

    public String soldPrice;
    public void bindData(JSONObject object) throws JSONException {
        Log.d("object", object.toString());
        goodsName = object.getString("goodsName");
        if (object.has("price")){
            price = object.getString("price");
        }
        if (object.has("soldPrice")){
            price = object.getString("soldPrice");
        }
    }
}
