package example.jni.com.coffeeseller.MachineConfig;

import android.util.Log;

import cof.ac.inter.MachineState;
import example.jni.com.coffeeseller.utils.MyLog;

/**
 * Created by WH on 2018/5/9.
 */

public enum MachineInitMaterial {
    MACHINE_STATE_NULL,
    POT_TEMP_OVER_130,
    POT_PRESSURE_OVER_1500,
    WATER_NOT_ENOUGH,
    BEAN_NOT_ENOUGH,
    WASTE_CONTAINNER_FULL,
    LITTLE_DOOR_OPEN,
    CUP_SHELF_NOT_RIGHT_PLACE,
    HAS_CUP_ON_SHELF
}
