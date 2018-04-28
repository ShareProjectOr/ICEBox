package example.jni.com.coffeeseller.contentprovider;

import example.jni.com.coffeeseller.bean.MachineConfig;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface Constance {

    String SERVER = MachineConfig.getHostUrl();

    String GET_QR = "http://119.23.71.185:8083" + "/trade/getQrCode";

    String CHECK_PAY = "http://119.23.71.185:8083" + "/trade/payResult";

    String TRADE_UPLOAD = "http://119.23.71.185:8083" + "/trade/upload";

    String TRADE_CLOSE = "http://119.23.71.185:8083" + "/trade/close";

    String MachineAuthentication_URL = "http://192.168.4.152:8083" + "/machine/activate";
    String COMMIT_MATERIAL_URL = "http://192.168.4.152:8083" + "/material/save";
    String PASSWORD_CHANGE = "http://192.168.4.152:8083/machine/updatePassword";
    String CHECK_VERSION_URL = "http://192.168.4.152:8083/app/versioncheck";
    String FORMULA_GET = "http://192.168.4.152:8083/formula/getFormula";
    String MATERIAL_LIST_GET_URL = "http://192.168.4.152:8083/materials/get";
    // String[] bunkersName = {"咖啡豆仓", "热水仓", "料仓1", "料仓2", "料仓3", "料仓4", "料仓5", "料仓6"};
    int[] bunkersID = {0xAA, 0x00, 1, 2, 3, 4, 5, 6, 7, 8};
    String[] bunkersName = {"咖啡豆仓", "热水仓", "料仓1", "料仓2", "料仓3", "料仓4", "料仓5", "料仓6-预留位", "料仓7-预留", "料仓8-预留"};
    //   String Host_URL =
    // String MachineAuthentication_URL =   ;
}
