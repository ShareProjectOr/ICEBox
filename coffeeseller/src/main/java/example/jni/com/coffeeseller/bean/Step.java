package example.jni.com.coffeeseller.bean;

import java.util.List;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.ContainerType;
import cof.ac.inter.WaterType;

/**
 * Created by Administrator on 2018/4/24.
 */

public class Step {
    ContainerConfig containerConfig;
    ContainerType curContainer;
    WaterType water_type;
    List<Taste> Tastes;
    Material material;

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    public void setContainerConfig(ContainerConfig containerConfig) {
        this.containerConfig = containerConfig;
    }

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

    public WaterType getWater_type() {
        return water_type;
    }

    public void setWater_type(WaterType water_type) {
        this.water_type = water_type;
    }


}
