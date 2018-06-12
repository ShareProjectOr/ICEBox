package contentprovider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class UserMessage {
    private static String managerId;
    private static String managerNum;
    private static String managerPass;
    private static String managerType;
    private static String agentID;
    private static String agentName;
    private static String activatedType;
    private static String managerName;
    private static String managerTelephone;
    private static String managerEmail;
    private static String managerAddress;
    private static String managerCard;
    private static String managerCompany;
    private static String managerBankAccount;
    private static String companyAddress;
    private static String companyNum;
    private static int userlisttotal;
    private static String divideProportion;
    private static String tsy = "";

    public static int getUserlisttotal() {
        return userlisttotal;
    }

    public static void setUserlisttotal(int userlisttotal) {
        UserMessage.userlisttotal = userlisttotal;
    }


    public static String getTsy() {
        return tsy;
    }

    public static void setTsy(String tsy) {
        UserMessage.tsy = tsy;
    }

    public static String getManagerId() {
        return managerId;
    }

    public static void setManagerId(String managerId) {
        UserMessage.managerId = managerId;
    }

    public static String getManagerNum() {
        return managerNum;
    }

    public static void setManagerNum(String managerNum) {
        UserMessage.managerNum = managerNum;
    }

    public static String getManagerPass() {
        return managerPass;
    }

    public static void setManagerPass(String managerPass) {
        UserMessage.managerPass = managerPass;
    }

    public static String getManagerType() {
        return managerType;
    }

    public static void setManagerType(String managerType) {
        UserMessage.managerType = managerType;
    }

    public static String getAgentID() {
        return agentID;
    }

    public static void setAgentID(String agentID) {
        UserMessage.agentID = agentID;
    }

    public static String getActivatedType() {
        return activatedType;
    }

    public static void setActivatedType(String activatedType) {
        UserMessage.activatedType = activatedType;
    }

    public static String getManagerName() {
        return managerName;
    }

    public static void setManagerName(String managerName) {
        UserMessage.managerName = managerName;
    }

    public static String getManagerTelephone() {
        return managerTelephone;
    }

    public static void setManagerTelephone(String managerTelephone) {
        UserMessage.managerTelephone = managerTelephone;
    }

    public static String getManagerEmail() {
        return managerEmail;
    }

    public static void setManagerEmail(String managerEmail) {
        UserMessage.managerEmail = managerEmail;
    }

    public static String getManagerAddress() {
        return managerAddress;
    }

    public static void setManagerAddress(String managerAddress) {
        UserMessage.managerAddress = managerAddress;
    }

    public static String getManagerCard() {
        return managerCard;
    }

    public static void setManagerCard(String managerCard) {
        UserMessage.managerCard = managerCard;
    }

    public static String getManagerCompany() {
        return managerCompany;
    }

    public static void setManagerCompany(String managerCompany) {
        UserMessage.managerCompany = managerCompany;
    }

    public static String getManagerBankAccount() {
        return managerBankAccount;
    }

    public static void setManagerBankAccount(String managerBankAccount) {
        UserMessage.managerBankAccount = managerBankAccount;
    }

    public static String getCompanyAddress() {
        return companyAddress;
    }

    public static void setCompanyAddress(String companyAddress) {
        UserMessage.companyAddress = companyAddress;
    }

    public static String getCompanyNum() {
        return companyNum;
    }

    public static void setCompanyNum(String companyNum) {
        UserMessage.companyNum = companyNum;
    }

    public static String getAgentName() {
        return agentName;
    }

    public static void setAgentName(String agentName) {
        UserMessage.agentName = agentName;
    }

    public static String getDivideProportion() {
        return divideProportion;
    }

    public static void setDivideProportion(String divideProportion) {
        UserMessage.divideProportion = divideProportion;
    }

    public static void setUserMessage(JSONObject object) {//Map<String, String> userMessage
        try {
            UserMessage.managerId = object.getString("managerID");
            UserMessage.managerNum = object.getString("managerNum");
            UserMessage.managerType = object.getString("managerType");
            UserMessage.activatedType = object.getString("activatedType");
            UserMessage.managerName = object.getString("managerName");
            UserMessage.managerTelephone = object.getString("managerTelephone");
            UserMessage.managerEmail = object.getString("managerEmail");
            UserMessage.managerAddress = object.getString("managerAddress");
            UserMessage.managerCard = object.getString("managerCard");
            UserMessage.managerCompany = object.getString("managerCompany");
            UserMessage.agentID = object.getString("agentID");
            UserMessage.agentName = object.getString("agentName");
            UserMessage.managerBankAccount = object.getString("managerBankAccount");
            UserMessage.companyAddress = object.getString("companyAddress");
            UserMessage.companyNum = object.getString("companyNum");
            UserMessage.divideProportion = object.getString("divideProportion");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
