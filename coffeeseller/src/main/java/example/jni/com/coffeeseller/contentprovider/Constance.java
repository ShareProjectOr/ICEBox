package example.jni.com.coffeeseller.contentprovider;

import example.jni.com.coffeeseller.bean.MachineConfig;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface Constance {

    //    String SERVER = MachineConfig.getHostUrl();192.168.4.192
    String SERVER = "http://119.23.71.185:8083";

    String GET_QR = SERVER + "/trade/getQrCode";

    String CHECK_PAY = SERVER + "/trade/payResult";

    String TRADE_UPLOAD = SERVER + "/trade/upload";

    String TRADE_CLOSE = SERVER + "/trade/close";

    String MachineAuthentication_URL = SERVER + "/machine/activate";
    String COMMIT_MATERIAL_URL = "http://192.168.4.152:8083" + "/material/save";
    String PASSWORD_CHANGE = SERVER + "/machine/updatePassword";
    String CHECK_VERSION_URL = "http://192.168.4.152:8083" + "/app/versioncheck";
    String FORMULA_GET = SERVER + "/formula/getFormula";
    String MATERIAL_LIST_GET_URL = SERVER + "/material/get";
    // String[] bunkersName = {"咖啡豆仓", "热水仓", "料仓1", "料仓2", "料仓3", "料仓4", "料仓5", "料仓6"};
    int[] bunkersID = {0xAA, 0x00, 1, 2, 3, 4, 5, 6, 7, 8};
    String[] bunkersName = {"咖啡豆仓", "热水仓", "料仓1", "料仓2", "料仓3", "料仓4", "料仓5", "料仓6-预留位", "料仓7-预留", "料仓8-预留"};
    //   String Host_URL =
    // String MachineAuthentication_URL =   ;
}
