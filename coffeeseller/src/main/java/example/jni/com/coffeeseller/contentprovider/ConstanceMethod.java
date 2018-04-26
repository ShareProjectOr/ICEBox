package example.jni.com.coffeeseller.contentprovider;

import java.util.HashMap;
import java.util.Map;

import example.jni.com.coffeeseller.bean.MachineConfig;

/**
 * Created by WH on 2018/4/25.
 */

public class ConstanceMethod {
    public static Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("machineCode", MachineConfig.getMachineCode());
        return params;
    }
}
