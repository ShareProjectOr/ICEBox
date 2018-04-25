package example.jni.com.coffeeseller.contentprovider;

import example.jni.com.coffeeseller.bean.MachineConfig;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface Constance {
    // String[] bunkersName = {"咖啡豆仓", "热水仓", "料仓1", "料仓2", "料仓3", "料仓4", "料仓5", "料仓6"};
    int[] bunkersID = {0xAA, 0x00, 1, 2, 3, 4, 5, 6, 7, 8};
    String[] bunkersName = {"咖啡豆仓", "热水仓", "料仓1", "料仓2", "料仓3", "料仓4", "料仓5", "料仓6-预留位", "料仓7-预留", "料仓8-预留"};
    String Host_URL = "http://" + MachineConfig.getHostUrl();
    String MachineAuthentication_URL = "http://192.168.4.152:8083" + "/machine/activate";
    String FORMULA_GET = "http://192.168.4.152:8083/formula/getFormula";
}
