package example.jni.com.coffeeseller.MachineConfig;

public class DealRecorder {

    int Rqcup = 1;
    String RqTempFormat;
    String RqSugerFormat;
    String RqMilkFormat;
    String order;
    String price;
    //消费者id
    String customerId;
    boolean payed = false;
    boolean makeSuccess = false;

    public DealRecorder() {
    }

    public int getRqcup() {
        return Rqcup;
    }

    public void setRqcup(int rqcup) {
        Rqcup = rqcup;
    }

    public String getRqTempFormat() {
        return RqTempFormat;
    }

    public void setRqTempFormat(String rqTempFormat) {
        RqTempFormat = rqTempFormat;
    }

    public String getRqSugerFormat() {
        return RqSugerFormat;
    }

    public void setRqSugerFormat(String rqSugerFormat) {
        RqSugerFormat = rqSugerFormat;
    }

    public String getRqMilkFormat() {
        return RqMilkFormat;
    }

    public void setRqMilkFormat(String rqMilkFormat) {
        RqMilkFormat = rqMilkFormat;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public boolean isMakeSuccess() {
        return makeSuccess;
    }

    public void setMakeSuccess(boolean makeSuccess) {
        this.makeSuccess = makeSuccess;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }



    public void clear() {

        Rqcup = 1;
        price = null;
        RqMilkFormat = null;
        RqSugerFormat = null;
        RqTempFormat = null;
        order = null;
        payed = false;
        makeSuccess = false;

    }

}
