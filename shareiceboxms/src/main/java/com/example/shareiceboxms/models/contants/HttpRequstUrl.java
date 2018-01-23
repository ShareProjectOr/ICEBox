package com.example.shareiceboxms.models.contants;

import android.os.Environment;

/**
 * Created by Administrator on 2017/12/18.
 */

public interface HttpRequstUrl {
    String IP = "10.4.68.82";
    String HOST_URL = "http://202.98.157.25/fridgesManage_server/";//外网服务器
    //    String HOST_URL = "http://" + IP + ":8082/";//李坤明
//    String HOST_URL_1 = "http://192.168.4.158:8082/";//李志灏
//    String HOST_URL_2 = "http://192.168.4.224:8082/";//刘建成
//    String HOST_URL_3 = "http://192.168.1.103:8082/";//刘建成
    //  String HOST_URL = "http://202.98.157.25/fridgesManage_server/";+
    String GET_APP_VERSION_URL = HOST_URL + "/Upload/GetAppURL";
    String LOGIN_URL = HOST_URL + "base/login";

    /*
    * 获取所有用户
    * */
    String USER_LIST = HOST_URL + "user/search";
    /*
    * 机器模块
    * */
    String MACHINE_LIST_URL = HOST_URL + "machine/search";
    String MACHINE_DETAIL_URL = HOST_URL + "machine/get";
    String MACHINE_StockGoods_URL = HOST_URL + "machine/stockGoods";
    String MACHINE_Restart_URL = HOST_URL + "machine/restart";
    String MACHINE_Shutdown_URL = HOST_URL + "machine/powerOff";
    String MACHINE_LightControl_URL = HOST_URL + "machine/toggleLight";
    String MACHINE_Check_URL = HOST_URL + "machine/check";//盘点
    String MACHINE_Temp_URL = HOST_URL + "machine/setTemperature";


    String REPAIR_PASSWORD_URL = HOST_URL + "user/editPassword";
    String PRODUCT_TYPE_LIST_URL = HOST_URL + "category/search";
    String PRODUCT_TYPE_DETAIL_URL = HOST_URL + "category/get";
    String EDIT_PRICE_UERL = HOST_URL + "category/editPrice";
    String EXCEPTION_LIST_URL = HOST_URL + "exception/search";
    String UPLOAD_RECORD_LIST_URL = HOST_URL + "goods/unloadRecord";
    String UPLOAD_RECORD_DETAILS_URL = HOST_URL + "goods/unloadDetail";
    String OPEN_MACHINE_DOOR_URL = HOST_URL + "machine/opendoor";
    /*
    * 交易统计
    * */
    String TRADE_TOTAL_URL = HOST_URL + "trade/financialTotal";
    String TRADE_RECORDS_URL = HOST_URL + "trade/search";
    String TRADE_RECOR_DETAIL_URL = HOST_URL + "trade/get";
    String TRADE_RECOR_DETAIL_PRODUCT_URL = HOST_URL + "goods/search";
    String TRADE_ACCOUONT_CHONGDI_RECORD_URL = HOST_URL + "settle/refundLog";
    String TRADE_CREATE_JIESUAN_URL = HOST_URL + "settle/buildSettle";
    String TRADE_JIESUAN_LIST_URL = HOST_URL + "settle/search";
    //批量退款
    String TRADE_REFUND_URL = HOST_URL + "trade/refund";
}
