package example.jni.com.coffeeseller.MachineConfig;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MachineInitState {
    public static final int NORMAL = 1;
    public static final int UNNORMAL = -1;

    public static int CHECK_MACHINECODE = -1;
    public static int CHECK_OPENMAINCTRL = -1;
    public static int SUB_MQTT_STATE = -1;
    public static int GET_FORMULA = -1;

    public static void init() {

        CHECK_MACHINECODE = -1;
        CHECK_OPENMAINCTRL = -1;
        SUB_MQTT_STATE = -1;
        GET_FORMULA = -1;
    }
}
