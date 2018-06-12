package entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by WH on 2017/9/4.
 */

public class ItemMachine extends Item {
    private String agentId;
    private String cfgSellSoftware;
    private String stateAdvertisingScreen;
    private String cfgCardReader;
    private String longLat;
    private String stateMachineType;
    private String productTime;
    private String stateChangeDevice;
    private String machineName;
    private String managerName;
    private String cfgCoinMachine;
    private String stateCardReader;
    private String stateCupNum;
    private String alipayPrice;
    private String weChatPrice;
    private String cashPrice;
    private String stateBtimeOut;
    private String stateBoundar;
    private String stateMisPos;
    private String activatedType;
    private String cfgAdvertisingScreen;
    private String machineCode;
    private String stateExceptionType;
    private String factoryTime;
    private String agentName;
    private String stateCapNum;
    private String managerId;
    private String cfgDocoinMachine;
    private String stateLongLat;
    private String cfgMediaSortware;
    private String machineAddress;
    private String cfgMisPos;
    private String cfgPaperMachine;
    private String activatedTime;
    private String showPrice;
    private String statePaperMachine;
    private String stateOrangeNum;
    private String cfgDriver;


    public static ItemMachine setMachine(JSONObject object) {
        ItemMachine machine = new ItemMachine();
        try {
            machine.setAgentId(object.getString("agentID"));
            machine.setCfgSellSoftware(object.getString("cfgSellSoftware"));
            machine.setStateAdvertisingScreen(object.getString("stateAdvertisingScreen"));
            machine.setCfgCardReader(object.getString("cfgCardReader"));
            machine.setLongLat(object.getString("longLat"));
            machine.setStateMachineType(object.getString("stateMachineType"));
            machine.setProductTime(object.getString("productTime"));
            machine.setStateChangeDevice(object.getString("stateChangeDevice"));
            machine.setManagerName(object.getString("managerName"));
            machine.setMachineName(object.getString("machineName"));
            machine.setCfgCoinMachine(object.getString("cfgCoinMachine"));
            machine.setStateCardReader(object.getString("stateCardReader"));
            machine.setStateCupNum(object.getString("stateCupNum"));
            machine.setAlipayPrice(object.getString("alipayPrice"));
            machine.setWeChatPrice(object.getString("weChatPrice"));
            machine.setCashPrice(object.getString("cashPrice"));
            machine.setStateBtimeOut(object.getString("stateBtimeOut"));
            machine.setStateBoundar(object.getString("stateBoundar"));
            machine.setStateMisPos(object.getString("stateMisPos"));
            machine.setActivatedType(object.getString("activatedType"));
            machine.setCfgAdvertisingScreen(object.getString("cfgAdvertisingScreen"));
            machine.setMachineCode(object.getString("machineCode"));
            machine.setStateExceptionType(object.getString("stateExceptionType"));
            machine.setFactoryTime(object.getString("factoryTime"));
            machine.setAgentName(object.getString("agentName"));
            machine.setStateCapNum(object.getString("stateCapNum"));
            machine.setManagerId(object.getString("managerID"));
            machine.setCfgDocoinMachine(object.getString("cfgDocoinMachine"));
            machine.setStateLongLat(object.getString("stateLongLat"));
            machine.setCfgMediaSortware(object.getString("cfgMediaSoftware"));
            machine.setMachineAddress(object.getString("machineAddress"));
            machine.setCfgMisPos(object.getString("cfgMisPos"));
            machine.setCfgPaperMachine(object.getString("cfgPaperMachine"));
            machine.setActivatedTime(object.getString("activatedTime"));
            machine.setShowPrice(object.getString("showPrice"));
            machine.setStatePaperMachine(object.getString("statePaperMachine"));
            machine.setStateOrangeNum(object.getString("stateOrangeNum"));
            machine.setCfgDriver(object.getString("cfgDriver"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return machine;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCfgSellSoftware() {
        return cfgSellSoftware;
    }

    public void setCfgSellSoftware(String cfgSellSoftware) {
        this.cfgSellSoftware = cfgSellSoftware;
    }

    public String getStateAdvertisingScreen() {
        return stateAdvertisingScreen;
    }

    public void setStateAdvertisingScreen(String stateAdvertisingScreen) {
        this.stateAdvertisingScreen = stateAdvertisingScreen;
    }

    public String getCfgCardReader() {
        return cfgCardReader;
    }

    public void setCfgCardReader(String cfgCardReader) {
        this.cfgCardReader = cfgCardReader;
    }

    public String getLongLat() {
        return longLat;
    }

    public void setLongLat(String longLat) {
        this.longLat = longLat;
    }

    public String getStateMachineType() {
        return stateMachineType;
    }

    public void setStateMachineType(String stateMachineType) {
        this.stateMachineType = stateMachineType;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getStateChangeDevice() {
        return stateChangeDevice;
    }

    public void setStateChangeDevice(String stateChangeDevice) {
        this.stateChangeDevice = stateChangeDevice;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getCfgCoinMachine() {
        return cfgCoinMachine;
    }

    public void setCfgCoinMachine(String cfgCoinMachine) {
        this.cfgCoinMachine = cfgCoinMachine;
    }

    public String getStateCardReader() {
        return stateCardReader;
    }

    public void setStateCardReader(String stateCardReader) {
        this.stateCardReader = stateCardReader;
    }

    public String getStateCupNum() {
        return stateCupNum;
    }

    public void setStateCupNum(String stateCupNum) {
        this.stateCupNum = stateCupNum;
    }

    public String getAlipayPrice() {
        return alipayPrice;
    }

    public void setAlipayPrice(String alipayPrice) {
        this.alipayPrice = alipayPrice;
    }

    public String getWeChatPrice() {
        return weChatPrice;
    }

    public void setWeChatPrice(String weChatPrice) {
        this.weChatPrice = weChatPrice;
    }

    public String getCashPrice() {
        return cashPrice;
    }

    public void setCashPrice(String cashPrice) {
        this.cashPrice = cashPrice;
    }

    public String getStateBtimeOut() {
        return stateBtimeOut;
    }

    public void setStateBtimeOut(String stateBtimeOut) {
        this.stateBtimeOut = stateBtimeOut;
    }

    public String getStateBoundar() {
        return stateBoundar;
    }

    public void setStateBoundar(String stateBoundar) {
        this.stateBoundar = stateBoundar;
    }

    public String getStateMisPos() {
        return stateMisPos;
    }

    public void setStateMisPos(String stateMisPos) {
        this.stateMisPos = stateMisPos;
    }

    public String getActivatedType() {
        return activatedType;
    }

    public void setActivatedType(String activatedType) {
        this.activatedType = activatedType;
    }

    public String getCfgAdvertisingScreen() {
        return cfgAdvertisingScreen;
    }

    public void setCfgAdvertisingScreen(String cfgAdvertisingScreen) {
        this.cfgAdvertisingScreen = cfgAdvertisingScreen;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getStateExceptionType() {
        return stateExceptionType;
    }

    public void setStateExceptionType(String stateExceptionType) {
        this.stateExceptionType = stateExceptionType;
    }

    public String getFactoryTime() {
        return factoryTime;
    }

    public void setFactoryTime(String factoryTime) {
        this.factoryTime = factoryTime;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getStateCapNum() {
        return stateCapNum;
    }

    public void setStateCapNum(String stateCapNum) {
        this.stateCapNum = stateCapNum;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getCfgDocoinMachine() {
        return cfgDocoinMachine;
    }

    public void setCfgDocoinMachine(String cfgDocoinMachine) {
        this.cfgDocoinMachine = cfgDocoinMachine;
    }

    public String getStateLongLat() {
        return stateLongLat;
    }

    public void setStateLongLat(String stateLongLat) {
        this.stateLongLat = stateLongLat;
    }

    public String getCfgMediaSortware() {
        return cfgMediaSortware;
    }

    public void setCfgMediaSortware(String cfgMediaSortware) {
        this.cfgMediaSortware = cfgMediaSortware;
    }

    public String getMachineAddress() {
        return machineAddress;
    }

    public void setMachineAddress(String machineAddress) {
        this.machineAddress = machineAddress;
    }

    public String getCfgMisPos() {
        return cfgMisPos;
    }

    public void setCfgMisPos(String cfgMisPos) {
        this.cfgMisPos = cfgMisPos;
    }

    public String getCfgPaperMachine() {
        return cfgPaperMachine;
    }

    public void setCfgPaperMachine(String cfgPaperMachine) {
        this.cfgPaperMachine = cfgPaperMachine;
    }

    public String getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(String activatedTime) {
        this.activatedTime = activatedTime;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(String showPrice) {
        this.showPrice = showPrice;
    }

    public String getStatePaperMachine() {
        return statePaperMachine;
    }

    public void setStatePaperMachine(String statePaperMachine) {
        this.statePaperMachine = statePaperMachine;
    }

    public String getStateOrangeNum() {
        return stateOrangeNum;
    }

    public void setStateOrangeNum(String stateOrangeNum) {
        this.stateOrangeNum = stateOrangeNum;
    }

    public String getCfgDriver() {
        return cfgDriver;
    }

    public void setCfgDriver(String cfgDriver) {
        this.cfgDriver = cfgDriver;
    }
}
