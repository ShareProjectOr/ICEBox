package com.example.shareiceboxms.models.contants;

/**
 * Created by Administrator on 2017/12/18.
 */

public interface HttpRequstUrl {
   // String HOST_URL = "http://192.168.4.197:8082/";
   String HOST_URL = "http://202.98.157.25/fridgesManage_server/";
    String LOGIN_URL = HOST_URL + "base/login";
    String TEST_UERL = "http://192.168.4.211:8081/category/addLable";
    String REPAIR_PASSWORD_URL = HOST_URL + "user/editPassword";
    String PRODUCT_TYPE_LIST_URL = HOST_URL + "category/search";
}
