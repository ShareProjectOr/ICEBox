package example.jni.com.coffeeseller.parse;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WH on 2018/4/25.
 */

public class PayResult {

    private String msgType;
    private int payResult;
    private String tradeCode;
    private String payTime;
    private String price;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getPayResult() {
        return payResult;
    }

    public void setPayResult(int payResult) {
        this.payResult = payResult;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public static PayResult getPayResult(String response) {
        PayResult payResult = new PayResult();
        try {
            if (TextUtils.isEmpty(response) || new JSONObject(response) == null) {
                return null;
            }

            JSONObject resultObject = new JSONObject(response);
            if (!resultObject.has("msgContent")) {
                return null;
            }
            payResult.setMsgType(resultObject.getString("msgType"));
            JSONObject msgContent = resultObject.getJSONObject("msgContent");
            if (msgContent != null) {
                payResult.setTradeCode(msgContent.getString("tradeCode"));
                payResult.setPrice(msgContent.getString("price"));
                payResult.setPayTime(msgContent.getString("payTime"));
                payResult.setPayResult(msgContent.getInt("payResult"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payResult;
    }


}
