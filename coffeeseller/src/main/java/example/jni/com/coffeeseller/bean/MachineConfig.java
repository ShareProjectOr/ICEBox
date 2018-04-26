package example.jni.com.coffeeseller.bean;

/**
 * Created by Administrator on 2018/4/23.
 */

public class MachineConfig {
    private static String HostUrl = "";
    private static String TcpIP = "";
    private static String machineCode = "20180423134200001";
    private static int networkType;

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

    public static int getNetworkType() {
        return networkType;
    }

    public static void setNetworkType(int networkType) {
        MachineConfig.networkType = networkType;
    }

    public static void setMachineCode(String machineCode) {
        MachineConfig.machineCode = machineCode;
    }
}
