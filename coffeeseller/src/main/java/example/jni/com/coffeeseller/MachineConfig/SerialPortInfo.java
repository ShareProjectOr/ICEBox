package example.jni.com.coffeeseller.MachineConfig;


/**
 * 榨汁机串口配置信息类，默认情况下返回一体机的配置信息
 *
 * @author LHT
 */
public class SerialPortInfo {


    //默认为迈德，用于兼容老机型
    private static String machineCtrlType = "UltraOcta A83";


    /**
     * 4信工控机
     */
    public static final String SX = "SABRESD-MX6DQ";

    /**
     * <BR>获取榨汁机控制机的名称<BR/>
     * <BR>默认情况下位迈德型号的控制机(UltraOcta A83),其余的控制机名称如下：<BR/>
     * <BR>&nbsp;&nbsp;&nbsp;1.NBox2（致宝）<BR/>
     * <BR>&nbsp;&nbsp;&nbsp;2.UniWin M188（北高智）<BR/>
     * <BR>&nbsp;&nbsp;&nbsp;3.INBOX310(映翰通)<BR/>
     * <BR>&nbsp;&nbsp;&nbsp;4.EMB-7501(华北工控)<BR/>
     * <BR>&nbsp;&nbsp;&nbsp;5.SABRESD-MX6DQ(4信)<BR/>
     */


    /**
     * 获取主板控制通信串口路径
     *
     * @return
     */
    public static String getMainCtrlPortName() {

        if (machineCtrlType.equals(SX)) {

            return "/dev/ttyO3";
        }
        return "/dev/ttysWK1";
    }

    /**
     * 获取纸币器控制通信串口路径
     *
     * @return
     */
    public static String getNoteCtrlPortName() {

        if (machineCtrlType.equals(SX)) {

            return "/dev/ttyO4";
        }
        return "/dev/ttysWK0";
    }

    /**
     * 获取IC存储通信串口路径
     *
     * @return
     */
    public static String getIcCtrlPortName() {

        if (machineCtrlType.equals(SX)) {
            return "/dev/ttyO2";
        }
        return "/dev/ttyS4";
    }

    static {

        machineCtrlType = android.os.Build.MODEL;
    }

}
