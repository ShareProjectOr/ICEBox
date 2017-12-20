package com.example.shareiceboxms.models.beans;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/19.
 */

public class ItemProductType {
    public int categoryID;  //商品编号
    public String categoryName;
    public String categoryPrice;//品类价格
    public String activityPrice;//活动价格
    public String soldOutPrice;//总销售额
    public int soldOutNum;//已售
    public int salingNum;//售卖中
    public int noExhibitNum;//未上货数量
    public int storageTimeLimit;//存储时间，以S为单位
    public int breakNum;//报损数量

    public void bindData(JSONObject object) {
        try {

            categoryID = object.getInt("categoryID");
            categoryName = object.getString("categoryName");
            categoryPrice = object.getString("categoryPrice");
            activityPrice = object.getString("activityPrice");
            soldOutPrice = object.getString("soldOutPrice");
            soldOutNum = object.getInt("soldOutNum");
            salingNum = object.getInt("salingNum");
            noExhibitNum = object.getInt("noExhibitNum");
            storageTimeLimit = object.getInt("storageTimeLimit");
            breakNum = object.getInt("breakNum");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
