package example.jni.com.coffeeseller.MachineConfig;

import java.util.ArrayList;
import java.util.List;

import cof.ac.inter.ContainerConfig;

public class DealRecorder {

    int Rqcup = 1;
    String RqTempFormat;
    String order;
    String price;
    String tasteRadio;//口味：多糖-50
    String payTime;
    int formulaID;//Number 配方ID

    String bunkers;

    //消费者id
    String customerId;
    boolean payed = false;
    boolean makeSuccess = false;
    boolean isReportSuccess = false;
    String reportMsg;

    int uploadCount=0;

    List<ContainerConfig> containerConfigs;

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

    public List<ContainerConfig> getContainerConfigs() {
        if (containerConfigs == null) {
            containerConfigs = new ArrayList<>();
        }
        return containerConfigs;
    }

    public void setContainerConfigs(List<ContainerConfig> containerConfigs) {
        this.containerConfigs = containerConfigs;
    }

    public boolean isReportSuccess() {
        return isReportSuccess;
    }

    public void setReportSuccess(boolean reportSuccess) {
        isReportSuccess = reportSuccess;
    }

    public String getReportMsg() {
        return reportMsg;
    }

    public void setReportMsg(String reportMsg) {
        this.reportMsg = reportMsg;
    }

    public String getBunkers() {
        return bunkers;
    }

    public void setBunkers(String bunkers) {
        this.bunkers = bunkers;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public boolean isVlide() {
        if ("null".equals(getOrder())) {
            return false;
        }
        if ("null".equals(getPrice())) {
            return false;
        }


        return true;
    }

    public void clear() {

        Rqcup = 1;
        price = null;
        RqTempFormat = null;
        order = null;
        payed = false;
        makeSuccess = false;

    }

}
