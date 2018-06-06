package com.example.shareiceboxms.models.contants;

/**
 * Created by Administrator on 2017/12/18.
 */

public interface HttpRequstUrl {

    boolean isTest = false;//当前使用ip是否为测试版

    /*
    *  外网服务器地址
     */
    String IP_RUN = "202.98.157.25";
/*    String IP_RUN = "119.23.71.185";
   String HOST_URL_RUN = "http://server.aichance.com.cn/fridgesManage_server/";//外网服务器  */

   String HOST_URL_RUN = "http://202.98.157.25/fridgesManage_server/";//外网服务器0
   // String HOST_URL_RUN = "http://119.23.71.185/refrigerator/";//阿里云
    /*
    * 测试服务器地址
     */
    //  String IP_TEST = "10.21.161.45";
    String IP_TEST = "192.168.4.107";
    String HOST_URL_TEST = "http://" + IP_TEST + ":8082/";

    /*
    *使用中的ip
    * */
    String IP = (!isTest ? IP_RUN : IP_TEST);
    String HOST_URL = (!isTest ? HOST_URL_RUN : HOST_URL_TEST);


    String GET_APP_VERSION_URL = HOST_URL + "/upload/GetAppURL";
    String LOGIN_URL = HOST_URL + "base/login";
    String OUT_LOGIN_URL = HOST_URL + "base/outLogin";

    /*
    * 获取所有用户
    * */
    String USER_LIST = HOST_URL + "user/search";
    String USER_DETAIL_INFO = HOST_URL + "user/get";
    /*
    * 机器模块
    * */
    String MACHINE_LIST_URL = HOST_URL + "machine/search";
    String MACHINE_DETAIL_URL = HOST_URL + "machine/get";
    String MACHINE_StockGoods_URL = HOST_URL + "machine/stockGoods";
    String MACHINE_Restart_URL = HOST_URL + "machine/restart";
    String MACHINE_RESET_URL = HOST_URL + "machine/reset";//复位
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
    String TRADE_TONGJI_URL = HOST_URL + "trade/getLine";
    String TRADE_RECORDS_URL = HOST_URL + "trade/search";
    String TRADE_RECOR_DETAIL_URL = HOST_URL + "trade/get";
    String TRADE_RECOR_DETAIL_PRODUCT_URL = HOST_URL + "goods/search";
    String TRADE_ACCOUONT_CHONGDI_RECORD_URL = HOST_URL + "settle/refundLog";
    String TRADE_CREATE_JIESUAN_URL = HOST_URL + "settle/buildSettle";
    String TRADE_JIESUAN_LIST_URL = HOST_URL + "settle/search";
    //批量退款
    String TRADE_REFUND_URL = HOST_URL + "trade/refund";
}
