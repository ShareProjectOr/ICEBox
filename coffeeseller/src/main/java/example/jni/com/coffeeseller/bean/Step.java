package example.jni.com.coffeeseller.bean;

import android.text.TextUtils;

import java.util.List;

import cof.ac.inter.ContainerType;
import cof.ac.inter.WaterType;

/**
 * Created by WH on 2018/4/24.
 */

public class Step {

    ContainerType curContainer;
    int container_id;
    int water_interval;
    int water_capacity;
    int material_time;
    int rotate_speed;
    int stir_speed;
    WaterType water_type;

    private List<Milk> milkList = null;
    private List<Suger> sugerList = null;

    public ContainerType getCurContainer() {
        return curContainer;
    }

    public void setCurContainer(ContainerType curContainer) {
        this.curContainer = curContainer;
    }

    public int getContainer_id() {
        return container_id;
    }

    public void setContainer_id(int container_id) {
        this.container_id = container_id;
    }

    public int getWater_interval() {
        return water_interval;
    }

    public void setWater_interval(int water_interval) {
        this.water_interval = water_interval;
    }

    public int getWater_capacity() {
        return water_capacity;
    }

    public void setWater_capacity(int water_capacity) {
        this.water_capacity = water_capacity;
    }

    public int getMaterial_time() {
        return material_time;
    }

    public void setMaterial_time(int material_time) {
        this.material_time = material_time;
    }

    public int getRotate_speed() {
        return rotate_speed;
    }

    public void setRotate_speed(int rotate_speed) {
        this.rotate_speed = rotate_speed;
    }

    public int getStir_speed() {
        return stir_speed;
    }

    public void setStir_speed(int stir_speed) {
        this.stir_speed = stir_speed;
    }

    public WaterType getWater_type() {
        return water_type;
    }

    public void setWater_type(WaterType water_type) {
        this.water_type = water_type;
    }

    public List<Milk> getMilk() {
        return milkList;
    }

    public void setMilk(List<Milk> milk) {
        this.milkList = milk;
    }

    public List<Suger> getSuger() {
        return sugerList;
    }

    public void setSuger(List<Suger> suger) {
        this.sugerList = suger;
    }

    public float getMilkRadio(int milkFormat) {
        float ratio = 1;
        if (milkList != null && milkList.size() > 0) {
            if (milkList.size() > milkFormat)
                for (int i = 0; i < milkList.size(); i++) {
                    Milk milk = milkList.get(i);
                    if (milkFormat == milk.getId()) {
                        if (TextUtils.isEmpty(milk.getAmount())) {
                            return ratio;
                        }
                        float amount = Float.parseFloat(milk.getAmount());
                        ratio = amount / 100;
                    }
                }
        }
        return ratio;
    }

    public float getSugerRadio(int sugerFormat) {
        float ratio = 1;
        if (sugerList != null && sugerList.size() > 0) {
            if (sugerList.size() > sugerFormat)
                for (int i = 0; i < sugerList.size(); i++) {
                    Suger suger = sugerList.get(i);
                    if (sugerFormat == suger.getId()) {
                        if (TextUtils.isEmpty(suger.getAmount())) {
                            return ratio;
                        }
                        float amount = Float.parseFloat(suger.getAmount());
                        ratio = amount / 100;
                    }
                }
        }
        return ratio;
    }
}
