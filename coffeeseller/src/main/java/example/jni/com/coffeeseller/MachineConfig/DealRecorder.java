package example.jni.com.coffeeseller.MachineConfig;

public class DealRecorder {

    int Rqcup = 1;
    String RqTempFormat;
    String order;
    String price;
    String taste;
    String tasteRadio;
    String payTime;
    int formulaID;//Number 配方ID
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

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
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

    public String getTasteRadio() {
        return tasteRadio;
    }

    public void setTasteRadio(String tasteRadio) {
        this.tasteRadio = tasteRadio;
    }

    public int getFormulaID() {
        return formulaID;
    }

    public void setFormulaID(int formulaID) {
        this.formulaID = formulaID;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public void clear() {

        Rqcup = 1;
        price = null;
        taste = null;
        RqTempFormat = null;
        order = null;
        payed = false;
        makeSuccess = false;

    }

}
