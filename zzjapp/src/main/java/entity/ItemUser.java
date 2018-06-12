package entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WH on 2017/9/4.
 */

public class ItemUser extends Item {
    private String managerId;
    private String managerNum;
    private String managerType;
    private String activatedType;
    private String managerName;
    private String managerTelephone;
    private String managerEmail;
    private String managerCard;
    private String managerAddress;
    private String managerCompany;
    private String agentId;
    private String managerAgent;
    private String agentName;
    private String managerBankAccount;
    private String commpanyAddress;
    private String companyNum;
    private String divideProportion;

    public static ItemUser setUser(JSONObject object) {
        ItemUser user = new ItemUser();
        try {
            user.setManagerId(object.getString("managerID"));
            user.setManagerNum(object.getString("managerNum"));
            user.setManagerType(object.getString("managerType"));
            user.setActivatedType(object.getString("activatedType"));
            user.setManagerName(object.getString("managerName"));
            user.setManagerTelephone(object.getString("managerTelephone"));
            user.setManagerEmail(object.getString("managerEmail"));
            user.setManagerAddress(object.getString("managerAddress"));
            user.setManagerCard(object.getString("managerCard"));
            user.setManagerCompany(object.getString("managerCompany"));
            user.setAgentId(object.getString("agentID"));
            user.setManagerBankAccount(object.getString("managerBankAccount"));
            user.setManagerAgent(object.getString("managerAgent"));
            if (object.has("agentName")) {
                user.setAgentName(object.getString("agentName"));
            }
            if (object.has("commpanyAddress")) {
                user.setCommpanyAddress(object.getString("commpanyAddress"));
            }
            if (object.has("companyNum")) {
                user.setCompanyNum(object.getString("companyNum"));
            }
            if (object.has("divideProportion")){
                user.setDivideProportion(object.getString("divideProportion"));
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return user;
    }

    public String getDivideProportion() {
        return divideProportion;
    }

    public void setDivideProportion(String divideProportion) {
        this.divideProportion = divideProportion;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerNum() {
        return managerNum;
    }

    public void setManagerNum(String managerNum) {
        this.managerNum = managerNum;
    }

    public String getManagerType() {
        return managerType;
    }

    public void setManagerType(String managerType) {
        this.managerType = managerType;
    }

    public String getActivatedType() {
        return activatedType;
    }

    public void setActivatedType(String activatedType) {
        this.activatedType = activatedType;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerTelephone() {
        return managerTelephone;
    }

    public void setManagerTelephone(String managerTelephone) {
        this.managerTelephone = managerTelephone;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerCard() {
        return managerCard;
    }

    public void setManagerCard(String managerCard) {
        this.managerCard = managerCard;
    }

    public String getManagerAddress() {
        return managerAddress;
    }

    public void setManagerAddress(String managerAddress) {
        this.managerAddress = managerAddress;
    }

    public String getManagerCompany() {
        return managerCompany;
    }

    public void setManagerCompany(String managerCompany) {
        this.managerCompany = managerCompany;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getManagerAgent() {
        return managerAgent;
    }

    public void setManagerAgent(String managerAgent) {
        this.managerAgent = managerAgent;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getManagerBankAccount() {
        return managerBankAccount;
    }

    public void setManagerBankAccount(String managerBankAccount) {
        this.managerBankAccount = managerBankAccount;
    }

    public String getCommpanyAddress() {
        return commpanyAddress;
    }

    public void setCommpanyAddress(String commpanyAddress) {
        this.commpanyAddress = commpanyAddress;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }
}
