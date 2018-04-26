package example.jni.com.coffeeseller.parse;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import example.jni.com.coffeeseller.utils.MyLog;

/**
 * Created by WH on 2018/4/25.
 * 解析二维码数据
 */

public class ParseRQMsg {
    public static String TAG = "ParseRQMsg";
    private String qrCode;//string 二维码连接
    private String price;// Dicmal  价格
    private int cupNum; //Number 杯数
    private String timeStamp; // long 时间戳
    private String tradeCode;    // string 交易单号

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCupNum() {
        return cupNum;
    }

    public void setCupNum(int cupNum) {
        this.cupNum = cupNum;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public static ParseRQMsg parseRQMsg(String response) {

        if (TextUtils.isEmpty(response) || new JSONException(response) == null) {
            return null;
        }
        ParseRQMsg parseRQMsg = new ParseRQMsg();
        try {
            JSONObject responsesObject = new JSONObject(response);

            parseRQMsg.setQrCode(responsesObject.getString("qRcode"));
            if (!"null".equals(responsesObject.getString("cupNum"))) {
                parseRQMsg.setCupNum(responsesObject.getInt("cupNum"));
            } else {
                parseRQMsg.setCupNum(0);
            }

            parseRQMsg.setPrice(responsesObject.getString("price"));
            parseRQMsg.setTimeStamp(responsesObject.getString("timeStamp"));
            parseRQMsg.setTradeCode(responsesObject.getString("tradeCode"));

        } catch (JSONException e) {

            MyLog.d(TAG, "json parse has error !");
            e.printStackTrace();
        }
        return parseRQMsg;
    }
}
