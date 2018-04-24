package example.jni.com.coffeeseller.bean;

import java.util.List;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.utils.CoffeeFomatInterface;

/**
 * Created by WH on 2018/3/20.
 */

public class Coffee {
    public String netUrl;
    public String price = "0.01";
    public String name;
    public boolean isOver = false;
    public String cacheUrl;
    public int restNum = 10;
    public int surgerFomat;
    public int milkFomat = -1;
    public List<Step> processList;

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public String getCacheUrl() {
        return cacheUrl;
    }

    public void setCacheUrl(String cacheUrl) {
        this.cacheUrl = cacheUrl;
    }

    public int getRestNum() {
        return restNum;
    }

    public void setRestNum(int restNum) {
        this.restNum = restNum;
    }

    public int getSurgerFomat() {
        return surgerFomat;
    }

    public void setSurgerFomat(int surgerFomat) {
        this.surgerFomat = surgerFomat;
    }

    public int getMilkFomat() {
        return milkFomat;
    }

    public void setMilkFomat(int milkFomat) {
        this.milkFomat = milkFomat;
    }

    public List<Step> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Step> processList) {
        this.processList = processList;
    }
}
