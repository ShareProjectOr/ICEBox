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


}
