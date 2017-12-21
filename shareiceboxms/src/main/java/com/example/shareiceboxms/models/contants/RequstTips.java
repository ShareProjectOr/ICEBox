package com.example.shareiceboxms.models.contants;

/**
 * Created by Administrator on 2017/12/18.
 */

public class RequstTips {
    public static String ServerException_Tip_500 = "服务器出现错误（500）";
    public static String Request_ERROR = "请求错误(400)";
    public static String Server_ERROR_404 = "网络不给力(404)";//服务器找不到给定的资源,文件不存在
    public static String Server_ERROR_501 = "未执行,服务器不支持请求的工具(501)";
    public static String Server_ERROR_502 = "错误网关,服务器接收到来自上游服务器的无效响应(502)";
    public static String Server_ERROR_503 = "无法获得服务,由于临时过载或维护，服务器无法处理请求(503)";
    public static String JSONException_Tip = "Json解析错误";
    public static String NETWORK_TIMEOUT = "网络连接超时";

    public static String getErrorMsg(String errorMsg) {
        String errMsg = "";
        if (!errMsg.contains("Unexpected code :")) {
            return NETWORK_TIMEOUT;
        }
        int code = Integer.valueOf(errorMsg.replace("Unexpected code :", "").trim());
        switch (code) {
            case 500:
                errMsg = ServerException_Tip_500;
                break;
            case 404:
                errMsg = Server_ERROR_404;
                break;
            case 400:
                errMsg = Request_ERROR;
                break;
            case 501:
                errMsg = Server_ERROR_501;
                break;
            case 502:
                errMsg = Server_ERROR_502;
                break;
            case 503:
                errMsg = Server_ERROR_503;
                break;

        }
        return errMsg;
    }

}
