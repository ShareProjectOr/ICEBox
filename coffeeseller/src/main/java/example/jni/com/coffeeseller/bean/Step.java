package example.jni.com.coffeeseller.bean;

import java.util.List;

import cof.ac.inter.ContainerConfig;

/**
 * Created by Administrator on 2018/4/24.
 */

public class Step {
    ContainerConfig containerConfig;
    List<Taste> Tastes;
    Material material;
    int amount;
    int materialTime;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    public void setContainerConfig(ContainerConfig containerConfig) {
        this.containerConfig = containerConfig;
        //setMaterialTime(containerConfig.getMaterial_time());
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

    public int getMaterialTime() {
        return materialTime;
    }

    public void setMaterialTime(int materialTime) {

        this.materialTime = materialTime;
    }
}
