package example.jni.com.coffeeseller.bean;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MachineState {
    String msgId;
    String machineCode;
    String pageType;
    String msgType;
    String sendTime ;
    int networkType = 1;//int 0-掉线 1-移动蜂窝 2-wifi
    int cupHouseState = 0;// int  杯仓状态 1-有杯   0-无杯
    int cupHolderState = 1;//int  杯架状态 1-有杯(未取杯)  0-无杯
    int boilerTemperature;// int  锅炉温度,单位℃
    int boilerPressure ; //int 锅炉压力，单位pa
    int doorState; // int 大门状态 0-关闭 1-开启
    int cupDoorState; // int 0-关闭 1-开启
    String driverVersion;// string 机器驱动软件版本
    String clientVersion; // string 机器售卖（客户端）软件版本
    String mediaVersion;// string 机器媒体软件版本（目前没有）
    String errCode; //   string 设备错误码 00 代表正常  参照咖啡机WEB和服务器接口异常类型列表

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public int getCupHouseState() {
        return cupHouseState;
    }

    public void setCupHouseState(int cupHouseState) {
        this.cupHouseState = cupHouseState;
    }

    public int getCupHolderState() {
        return cupHolderState;
    }

    public void setCupHolderState(int cupHolderState) {
        this.cupHolderState = cupHolderState;
    }

    public int getBoilerTemperature() {
        return boilerTemperature;
    }

    public void setBoilerTemperature(int boilerTemperature) {
        this.boilerTemperature = boilerTemperature;
    }

    public int getBoilerPressure() {
        return boilerPressure;
    }

    public void setBoilerPressure(int boilerPressure) {
        this.boilerPressure = boilerPressure;
    }

    public int getDoorState() {
        return doorState;
    }

    public void setDoorState(int doorState) {
        this.doorState = doorState;
    }

    public int getCupDoorState() {
        return cupDoorState;
    }

    public void setCupDoorState(int cupDoorState) {
        this.cupDoorState = cupDoorState;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getMediaVersion() {
        return mediaVersion;
    }

    public void setMediaVersion(String mediaVersion) {
        this.mediaVersion = mediaVersion;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
