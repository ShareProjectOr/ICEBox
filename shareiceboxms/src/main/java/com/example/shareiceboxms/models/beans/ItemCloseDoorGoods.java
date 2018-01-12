package com.example.shareiceboxms.models.beans;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WH on 2018/1/3.
 */

public class ItemCloseDoorGoods {
    public String goodsName;
    public int upLoadNum;
    public int unLoadNum;

    public void bindData(JSONObject object) throws JSONException {
        goodsName = object.getString("goodsName");
        unLoadNum = object.getInt("unLoadNum");
        upLoadNum = object.getInt("upLoadNum");
    }
}
