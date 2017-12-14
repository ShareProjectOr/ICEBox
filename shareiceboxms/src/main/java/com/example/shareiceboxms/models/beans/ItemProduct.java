package com.example.shareiceboxms.models.beans;

/**
 * Created by WH on 2017/12/9.
 */

public class ItemProduct {
    private boolean isChecked = false;
    private float price = 2.0f;


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
