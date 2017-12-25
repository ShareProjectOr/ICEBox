package com.example.shareiceboxms.models.beans;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/25.
 */

public class ItemDetailsUpLoad {
    public String goodsName;
    public String price;

    public void bindData(JSONObject object) throws JSONException {
        goodsName = object.getString("goodsName");
        price = object.getString("price");
    }
}
