package example.jni.com.coffeeseller.bean;

/**
 * Created by Administrator on 2018/4/24.
 */

public class Material {
    public int materialID;//Number 原料ID
    public String name;//String 原料名称
    public String unit;//String 原料计量单位
    public int output;//Number 原料出料量
    public int type;//Number 0：主料；1：辅料

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
