package com.example.shareiceboxms.models.contants;

import com.example.shareiceboxms.R;

/**
 * Created by WH on 2017/11/28.
 */

public interface Constants {
    String[] TabTitles = {"交易", "机器", "异常", "商品"};
    int[] TabIcons = {R.drawable.selector_trade, R.drawable.selector_machine,
            R.drawable.selector_exception, R.drawable.selector_product};
    String[] TradeTabTitles = {"交易统计", "交易记录", "服务结算费"};
    /*
    * 交易统计
    * */
    String[] TradeTotalTitles = {"订单总金额", "冲抵后实际结算金额", "支付宝消费", "微信消费"};
    String[] TradeTotalTitlesItem = {"已扣款", "代扣款", "已支付", "待支付"};
    String[] EXCEPTION_LV_TITLE = {"全部", "一般严重", "特别严重"};
    int[] TradeRecordPayICON = {R.mipmap.icon_person, R.mipmap.icon_person, R.mipmap.icon_person};
    String[] TradeRecordDetailTitle = {"售出商品(0)", "已退款商品(0)"};
}
