package com.example.shareiceboxms.models.helpers;

/**
 * Created by Administrator on 2017/12/25.
 */

public class ExceptionTypeUtils {
    public static String getExceptionTypeByCode(String code) {
        String ExceptionType;
        switch (code) {
            case "01":
                ExceptionType = "有异物";
                break;
            case "02":
                ExceptionType = "失效货物被放回";
                break;
            case "03":
                ExceptionType = "售卖超时";
                break;
            case "04":
                ExceptionType = "销售提示";
                break;
            case "11":
                ExceptionType = "锁关门开";
                break;
            case "12":
                ExceptionType = "无法开门";
                break;
            case "13":
                ExceptionType = "盘点失败故障";
                break;
            case "14":
                ExceptionType = "网络故障";
                break;
            case "15":
                ExceptionType = "保质期即将到期提示";
                break;
            default:
                ExceptionType = "未知异常";
                break;

        }
        return ExceptionType;
    }
}
