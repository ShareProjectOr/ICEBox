package example.jni.com.coffeeseller.bean;

/**
 * Created by WH on 2018/4/24.
 */

public class Milk {
    //id    0：无糖，1：少糖 ，2：正常 ，3：多糖
    private int id;
    private String amount;//落料比例
    private String reMark;//标志

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
    }
}
