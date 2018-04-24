package example.jni.com.coffeeseller.bean;

/**
 * Created by Administrator on 2018/4/23.
 */

public class MachineConfig {
    private static String HostUrl="";
    private static String TcpIP="";
    private static String machineCode="";

    public static String getHostUrl() {
        return HostUrl;
    }

    public static void setHostUrl(String hostUrl) {
        HostUrl = hostUrl;
    }

    public static String getTcpIP() {
        return TcpIP;
    }

    public static void setTcpIP(String tcpIP) {
        TcpIP = tcpIP;
    }

    public static String getMachineCode() {
        return machineCode;
    }

    public static void setMachineCode(String machineCode) {
        MachineConfig.machineCode = machineCode;
    }
}
