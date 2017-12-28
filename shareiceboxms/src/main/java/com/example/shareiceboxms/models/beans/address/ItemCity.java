package com.example.shareiceboxms.models.beans.address;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WH on 2017/7/22.
 */
//市
public class ItemCity extends ItemBase implements Serializable {
    //    public String name;
//    private String id;//区号
//    private String parentId;//父类区号
    public String cityCode;//电话码
    public String zipCode;//邮编
    public List<ItemArea> areas;//区


    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public List<ItemArea> getAreas() {
        return areas;
    }

    public void setAreas(List<ItemArea> areas) {
        this.areas = areas;
    }
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(String parentId) {
//        this.parentId = parentId;
//    }

//    public void getParent(ItemCity city) {
//
//    }
}
