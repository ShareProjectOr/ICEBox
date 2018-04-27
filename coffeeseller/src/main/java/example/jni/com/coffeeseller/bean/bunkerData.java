package example.jni.com.coffeeseller.bean;

/**
 * Created by Administrator on 2018/4/23.
 */

public class bunkerData {
    String bunkerID;//料仓编号
    String MaterialID;//原料编号
    String bunkersName;//料仓名字
    String MaterialType;//原料种类
    String MaterialName;//原料名字
    String MaterialUnit;//原料单位
    String MaterialStock;//原料剩余量
    String MaterialDropSpeed;//单位落料量
    String containerID;//料仓实际排列编号
    String lastLoadingTime; //最后补料时间

    public String getLastLoadingTime() {
        return lastLoadingTime;
    }

    public void setLastLoadingTime(String lastLoadingTime) {
        this.lastLoadingTime = lastLoadingTime;
    }

    public String getBunkersName() {
        return bunkersName;
    }

    public void setBunkersName(String bunkersName) {
        this.bunkersName = bunkersName;
    }

    public String getBunkerID() {
        return bunkerID;
    }

    public void setBunkerID(String bunkerID) {
        this.bunkerID = bunkerID;
    }

    public String getMaterialID() {
        return MaterialID;
    }

    public void setMaterialID(String materialID) {
        MaterialID = materialID;
    }

    public String getMaterialType() {
        return MaterialType;
    }

    public void setMaterialType(String materialType) {
        MaterialType = materialType;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getMaterialUnit() {
        return MaterialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        MaterialUnit = materialUnit;
    }

    public String getMaterialStock() {
        return MaterialStock;
    }

    public void setMaterialStock(String materialStock) {
        MaterialStock = materialStock;
    }

    public String getMaterialDropSpeed() {
        return MaterialDropSpeed;
    }

    public void setMaterialDropSpeed(String materialDropSpeed) {
        MaterialDropSpeed = materialDropSpeed;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }
}
