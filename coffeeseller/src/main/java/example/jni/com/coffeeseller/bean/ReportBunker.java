package example.jni.com.coffeeseller.bean;

/**
 * Created by WH on 2018/4/26.
 */

public class ReportBunker {
    private int bunkerID;
    private String materialStock;
    private String unit;

    public int getBunkerID() {
        return bunkerID;
    }

    public void setBunkerID(int bunkerID) {
        this.bunkerID = bunkerID;
    }

    public String getMaterialStock() {
        return materialStock;
    }

    public void setMaterialStock(String materialStock) {
        this.materialStock = materialStock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
