package example.jni.com.coffeeseller.bean;

import java.util.ArrayList;
import java.util.List;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.WaterType;
import example.jni.com.coffeeseller.utils.CoffeeFomatInterface;

/**
 * Created by WH on 2018/3/22.
 * 购买时的Coffee规格
 */
    /*
    * coffeeId:选择的咖啡标识
    * cup:咖啡数量
    *   :加糖规格
    *   ：加奶规格
    *   :加水规格
    *   ...
    * */
public class CoffeeFomat {
    private int cup = 1;
    private List<String> tasteNameRatio;
    private WaterType waterType;
    private List<ContainerConfig> containerConfigs;

    public boolean isMin() {
        return cup == 1;
    }

    public int getCup() {
        return cup;
    }

    public void setCup(int cup) {
        this.cup = cup;
    }

    public WaterType getWaterType() {
        return waterType;
    }

    public void setWaterType(WaterType waterType) {
        this.waterType = waterType;
    }

    public List<ContainerConfig> getContainerConfigs() {
        if (containerConfigs == null) {
            containerConfigs = new ArrayList<>();
        }
        return containerConfigs;
    }

    public void setContainerConfigs(List<ContainerConfig> containerConfigs) {
        this.containerConfigs = containerConfigs;
    }

    public List<String> getTasteNameRatio() {
        if (tasteNameRatio == null) {
            tasteNameRatio = new ArrayList<>();
        }
        return tasteNameRatio;
    }

    public void setTasteNameRatio(List<String> tasteNameRatio) {
        this.tasteNameRatio = tasteNameRatio;
    }
}
