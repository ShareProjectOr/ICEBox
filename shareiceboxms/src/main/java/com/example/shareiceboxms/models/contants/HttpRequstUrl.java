package com.example.shareiceboxms.models.contants;

/**
 * Created by Administrator on 2017/12/18.
 */

public interface HttpRequstUrl {
    String HOST_URL = "http://192.168.4.197:8080/";
    String LOGIN_URL = HOST_URL + "base/login";
    String MACHINE_LIST_URL = "http://192.168.4.15:8082/" + "machine/search";
    String MACHINE_DETAIL_URL = "http://192.168.4.15:8082/" + "machine/get";
}
