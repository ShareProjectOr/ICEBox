package example.jni.com.coffeeseller.bean;

import java.util.List;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.ContainerType;
import cof.ac.inter.WaterType;

/**
 * Created by Administrator on 2018/4/24.
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
    List<Taste> Tastes;
    Material material;

    public List<Taste> getTastes() {
        return Tastes;
    }

    public void setTastes(List<Taste> tastes) {
        Tastes = tastes;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

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


}
