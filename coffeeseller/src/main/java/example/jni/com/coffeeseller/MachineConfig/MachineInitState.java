package example.jni.com.coffeeseller.MachineConfig;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MachineInitState {
    public static final int NORMAL = 1;
    public static final int UNNORMAL = -1;

    public static int CHECK_MACHINECODE = -1;
    public static int CHECK_OPENMAINCTRL = -1;
    public static int CHECK_NETWORK_STATE = -1;
    public static int CHECK_IC_COMM = -1;

    public static void init() {

        CHECK_MACHINECODE = -1;
        CHECK_OPENMAINCTRL = -1;
        CHECK_NETWORK_STATE = -1;
        CHECK_IC_COMM = -1;
    }
}
