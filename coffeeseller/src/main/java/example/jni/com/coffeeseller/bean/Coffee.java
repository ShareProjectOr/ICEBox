package example.jni.com.coffeeseller.bean;

import java.util.List;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.utils.CoffeeFomatInterface;

/**
 * Created by WH on 2018/3/20.
 */

public class Coffee {

    public String price = "0.01";
    public String name;
    public boolean isOver = false;
    public String cacheUrl;
    public int formulaID;
    public List<ContainerConfig> processList;
    public List<Step> stepList;

    public int getFormulaID() {
        return formulaID;
    }

    public void setFormulaID(int formulaID) {
        this.formulaID = formulaID;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
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


    public List<ContainerConfig> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ContainerConfig> processList) {
        this.processList = processList;
    }
}
