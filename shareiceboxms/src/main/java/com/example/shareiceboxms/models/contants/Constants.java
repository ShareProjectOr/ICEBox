package com.example.shareiceboxms.models.contants;

import com.example.shareiceboxms.R;

/**
 * Created by WH on 2017/11/28.
 */

public interface Constants {
    public static final String[] TabTitles = {"交易", "机器", "异常", "商品"};
    public static final int[] TabIcons = {R.drawable.selector_trade, R.drawable.selector_machine,
            R.drawable.selector_exception, R.drawable.selector_product};
    public static final String[] TradeTabTitles = {"交易统计", "交易记录", "服务结算费"};
    /*
    * 交易统计
    * */
    public static final String[] TradeTotalTitles = {"订单总金额", "冲抵后实际结算金额", "支付宝消费", "微信消费"};
    public static final String[] TradeTotalTitlesItem = {"已扣款", "代扣款", "已支付", "待支付"};
}
