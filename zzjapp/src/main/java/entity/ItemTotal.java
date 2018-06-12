package entity;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by WH on 2017/9/21.
 */

public class ItemTotal {
    //支付宝统计项
    private String alipay; //交易金额
    private int alipayFailCup;//失败杯数
    private BigDecimal alipayIncomeTotal;//实收金额
    private BigDecimal alipayRefoundTotal;//退款金额
    private int alipaySuccessCup;//成功杯数

    //现金统计项
    private String cash;
    private int cashFailCup;
    private BigDecimal cashIncomeTotal;
    private BigDecimal cashRefoundTotal;
    private int cashSuccessCup;

    //微信统计项目
    private String weChat;
    private int weChatSuccessCup;
    private int wechatFailCup;
    private BigDecimal wechatIncomeTotal;
    private BigDecimal wechatRefoundTotal;

    //总计统计项
    private int cupNum;  //总杯数
    private String payTime;//时间区间
    private int failCup; //失败杯数
    private int failTotal; //失败交易次数
    private BigDecimal incomeTotal; //实收总计金额
    private int orangeNum; //累积耗橙子
    private BigDecimal refoundTotal; //累积退款金额
    //refundMoney:"-7.92"
    private int successCup;  //成功杯数
    private int successTotal; //成功交易次数
    //time:""
    private int total;//总交易次数
    private String tradeMoney; //总交易金额

    public static ItemTotal getItemTotal(JSONObject itemJson) {
        ItemTotal itemTotal = new ItemTotal();
        try {
            itemTotal.setAlipay(itemJson.getString("alipay"));
            itemTotal.setAlipayFailCup(itemTotal.string2Int(itemJson.getString("alipayFailCup")));
            itemTotal.setAlipayIncomeTotal(itemTotal.string2BigDecimal(itemJson.getString("alipayIncomeTotal")));
            itemTotal.setAlipayRefoundTotal(itemTotal.string2BigDecimal(itemJson.getString("alipayRefundTotal")));
            itemTotal.setAlipaySuccessCup(itemTotal.string2Int(itemJson.getString("alipaySuccessCup")));

            itemTotal.setCash(itemJson.getString("cash"));
            itemTotal.setCashFailCup(itemTotal.string2Int(itemJson.getString("cashFailCup")));
            itemTotal.setCashIncomeTotal(itemTotal.string2BigDecimal(itemJson.getString("cashIncomeTotal")));
            itemTotal.setCashRefoundTotal(itemTotal.string2BigDecimal(itemJson.getString("cashRefundTotal")));
            itemTotal.setCashSuccessCup(itemTotal.string2Int(itemJson.getString("cashSuccessCup")));

            itemTotal.setWeChat(itemJson.getString("weChat"));
            itemTotal.setWechatFailCup(itemTotal.string2Int(itemJson.getString("wechatFailCup")));
            itemTotal.setWechatIncomeTotal(itemTotal.string2BigDecimal(itemJson.getString("wechatIncomeTotal")));
            itemTotal.setWechatRefoundTotal(itemTotal.string2BigDecimal(itemJson.getString("wechatRefundTotal")));
            itemTotal.setWeChatSuccessCup(itemTotal.string2Int(itemJson.getString("weChatSuccessCup")));

            itemTotal.setCupNum(itemTotal.string2Int(itemJson.getString("cupNum")));
            itemTotal.setPayTime(itemJson.getString("payTime"));
            itemTotal.setFailCup(itemTotal.string2Int(itemJson.getString("failCup")));
            itemTotal.setFailTotal(itemTotal.string2Int(itemJson.getString("failTotal")));
            itemTotal.setIncomeTotal(itemTotal.string2BigDecimal(itemJson.getString("incomeTotal")));
            itemTotal.setOrangeNum(itemTotal.string2Int(itemJson.getString("orangeNum")));
            itemTotal.setRefoundTotal(itemTotal.string2BigDecimal(itemJson.getString("refundTotal")));
            itemTotal.setSuccessCup(itemTotal.string2Int(itemJson.getString("successCup")));
            itemTotal.setSuccessTotal(itemTotal.string2Int(itemJson.getString("successTotal")));
            itemTotal.setTotal(itemTotal.string2Int(itemJson.getString("total")));
            itemTotal.setTradeMoney(itemJson.getString("tradeMoney"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return itemTotal;
    }

    public int string2Int(String value) {
        return Integer.parseInt(TextUtils.isEmpty(value) ? "0" : value);
    }

    public BigDecimal string2BigDecimal(String value) {
        BigDecimal b = new BigDecimal(TextUtils.isEmpty(value) ? "0" : value);
        BigDecimal f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP);
        return f1.abs();
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public int getAlipayFailCup() {
        return alipayFailCup;
    }

    public void setAlipayFailCup(int alipayFailCup) {
        this.alipayFailCup = alipayFailCup;
    }

    public BigDecimal getAlipayIncomeTotal() {
        return alipayIncomeTotal;
    }

    public void setAlipayIncomeTotal(BigDecimal alipayIncomeTotal) {
        this.alipayIncomeTotal = alipayIncomeTotal;
    }

    public BigDecimal getAlipayRefoundTotal() {
        return alipayRefoundTotal;
    }

    public void setAlipayRefoundTotal(BigDecimal alipayRefoundTotal) {
        this.alipayRefoundTotal = alipayRefoundTotal;
    }

    public int getAlipaySuccessCup() {
        return alipaySuccessCup;
    }

    public void setAlipaySuccessCup(int alipaySuccessCup) {
        this.alipaySuccessCup = alipaySuccessCup;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public int getCashFailCup() {
        return cashFailCup;
    }

    public void setCashFailCup(int cashFailCup) {
        this.cashFailCup = cashFailCup;
    }

    public BigDecimal getCashIncomeTotal() {
        return cashIncomeTotal;
    }

    public void setCashIncomeTotal(BigDecimal cashIncomeTotal) {
        this.cashIncomeTotal = cashIncomeTotal;
    }

    public BigDecimal getCashRefoundTotal() {
        return cashRefoundTotal;
    }

    public void setCashRefoundTotal(BigDecimal cashRefoundTotal) {
        this.cashRefoundTotal = cashRefoundTotal;
    }

    public int getCashSuccessCup() {
        return cashSuccessCup;
    }

    public void setCashSuccessCup(int cashSuccessCup) {
        this.cashSuccessCup = cashSuccessCup;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public int getWeChatSuccessCup() {
        return weChatSuccessCup;
    }

    public void setWeChatSuccessCup(int weChatSuccessCup) {
        this.weChatSuccessCup = weChatSuccessCup;
    }

    public int getWechatFailCup() {
        return wechatFailCup;
    }

    public void setWechatFailCup(int wechatFailCup) {
        this.wechatFailCup = wechatFailCup;
    }

    public BigDecimal getWechatIncomeTotal() {
        return wechatIncomeTotal;
    }

    public void setWechatIncomeTotal(BigDecimal wechatIncomeTotal) {
        this.wechatIncomeTotal = wechatIncomeTotal;
    }

    public BigDecimal getWechatRefoundTotal() {
        return wechatRefoundTotal;
    }

    public void setWechatRefoundTotal(BigDecimal wechatRefoundTotal) {
        this.wechatRefoundTotal = wechatRefoundTotal;
    }

    public int getCupNum() {
        return cupNum;
    }

    public void setCupNum(int cupNum) {
        this.cupNum = cupNum;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getFailCup() {
        return failCup;
    }

    public void setFailCup(int failCup) {
        this.failCup = failCup;
    }

    public int getFailTotal() {
        return failTotal;
    }

    public void setFailTotal(int failTotal) {
        this.failTotal = failTotal;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public int getOrangeNum() {
        return orangeNum;
    }

    public void setOrangeNum(int orangeNum) {
        this.orangeNum = orangeNum;
    }

    public BigDecimal getRefoundTotal() {
        return refoundTotal;
    }

    public void setRefoundTotal(BigDecimal refoundTotal) {
        this.refoundTotal = refoundTotal;
    }

    public int getSuccessCup() {
        return successCup;
    }

    public void setSuccessCup(int successCup) {
        this.successCup = successCup;
    }

    public int getSuccessTotal() {
        return successTotal;
    }

    public void setSuccessTotal(int successTotal) {
        this.successTotal = successTotal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }
}
