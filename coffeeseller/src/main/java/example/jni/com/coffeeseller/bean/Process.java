package example.jni.com.coffeeseller.bean;

import cof.ac.inter.WaterType;

/**
 * Created by Administrator on 2018/4/16.
 */

public class Process {
    int container_id; //操作仓ID
    int water_interval; //出水间隔
    int water_capacity; //出水量
    int material_time; // 落料时间
    int rotate_speed; //旋转速度
    int stir_speed;  //搅拌速度
    WaterType water_type;// 水温

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
}
