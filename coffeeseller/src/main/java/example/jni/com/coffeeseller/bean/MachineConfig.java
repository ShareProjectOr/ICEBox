package example.jni.com.coffeeseller.bean;

import cof.ac.inter.*;

/**
 * Created by Administrator on 2018/4/23.
 */

public class MachineConfig {
    private static String HostUrl = "";
    private static String TcpIP = "";
    private static String machineCode;
    private static int networkType = 1;
    private static String topic;
    private static String errTip = "";
    private static String phone = "";
    private static StateEnum currentState;
    private static boolean hasReserve = true;

    public static String getTopic() {
        return topic;
    }

    public static boolean isHasReserve() {
        return hasReserve;
    }

    public static void setHasReserve(boolean hasReserve) {
        MachineConfig.hasReserve = hasReserve;
    }

    public static StateEnum getCurrentState() {
        CoffMsger msger = CoffMsger.getInstance();
        cof.ac.inter.MachineState state = msger.getLastMachineState();
        if (state != null) {
            currentState = state.getMajorState().getCurStateEnum();
        }

        return currentState;
    }

    public static String getPhone() {
        StringBuilder sb = new StringBuilder();
        if (phone.isEmpty()) {
            return "";
        }
        for (int i = 0; i < phone.length(); i++) {
            if (i != 3 && i != 8 && phone.charAt(i) == ' ') {

                continue;

            } else {
                sb.append(phone.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }

        return phone;
    }

    public static void setPhone(String phone) {
        MachineConfig.phone = phone;
    }

    public static void setTopic(String topic) {
        MachineConfig.topic = topic;
    }

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

    public static String getErrCode() {
        return errTip;
    }

    public static void setErrCode(String errTip) {
        MachineConfig.errTip = errTip;
    }

    public static String getNetWorkTypeString() {
        if (networkType == 1) {
            return "移动网络";
        } else if (networkType == 2) {
            return "wifi";
        } else if (networkType == 0) {
            return "无网络";
        } else {
            return "";
        }

    }

    public static void setNetworkType(int networkType) {
        MachineConfig.networkType = networkType;
    }

    public static void setMachineCode(String machineCode) {
        MachineConfig.machineCode = machineCode;
    }
}
