package com.example.shareiceboxms.models.contants;

/**
 * Created by Administrator on 2017/12/18.
 */

public interface HttpRequstUrl {
    String HOST_URL = "http://192.168.4.197:8082/";
    //  String HOST_URL = "http://202.98.157.25/fridgesManage_server/";
    String LOGIN_URL = HOST_URL + "base/login";

    /*
    * 机器模块
    * */
    String MACHINE_LIST_URL = "http://192.168.4.224:8082/" + "machine/search";
    String MACHINE_DETAIL_URL = "http://192.168.4.224:8082/" + "machine/get";
    String MACHINE_StockGoods_URL = "http://192.168.4.224:8082/" + "machine/stockGoods";
    String MACHINE_Restart_URL = "http://192.168.4.224:8082/" + "machine/restart";
    String MACHINE_Shutdown_URL = "http://192.168.4.224:8082/" + "machine/powerOff";
    String MACHINE_LightControl_URL = "http://192.168.4.224:8082/" + "machine/toggleLock";
    String MACHINE_Check_URL = "http://192.168.4.224:8082/" + "machine/check";//盘点


    String EDIT_PRICE_UERL = "http://192.168.4.211:8082/category/edit";
    String REPAIR_PASSWORD_URL = HOST_URL + "user/editPassword";
    String PRODUCT_TYPE_LIST_URL = "http://192.168.4.211:8082/category/search";
    String PRODUCT_TYPE_DETAIL_URL = "http://192.168.4.211:8082/category/get";
}
