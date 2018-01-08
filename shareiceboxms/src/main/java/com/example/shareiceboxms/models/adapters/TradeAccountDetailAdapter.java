package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.trade.ItemChongdiRefundRecord;
import com.example.shareiceboxms.models.beans.trade.ItemTradeRecord;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/12.
 */

public class TradeAccountDetailAdapter extends BaseAdapter {
    private Context context;
    private List<ItemTradeRecord> itemRecordList;
    private List<ItemChongdiRefundRecord> itemChongdiRecordList;
    TradeAccountDetailFragment tradeAccountDetailFragment;
    private boolean isRefundList = false;//是否是应退记录

    public TradeAccountDetailAdapter(Context context, TradeAccountDetailFragment tradeAccountDetailFragment) {
        this.context = context;
        this.tradeAccountDetailFragment = tradeAccountDetailFragment;
        itemRecordList = new ArrayList<>();
        itemChongdiRecordList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return isRefundList ? itemChongdiRecordList.size() : itemRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return isRefundList ? itemChongdiRecordList.get(position) : itemRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.trade_account_detail_list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.itemMoney = (TextView) convertView.findViewById(R.id.itemMoney);
            viewHolder.itemTime = (TextView) convertView.findViewById(R.id.itemTime);
            viewHolder.operator = (Button) convertView.findViewById(R.id.operator);
            viewHolder.operator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tradeAccountDetailFragment != null) {
                        FragmentFactory.getInstance().getSavedBundle().putInt("tradeID", itemRecordList.get(position).tradeID);
                        tradeAccountDetailFragment.addFrameFragment();
                    }
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //添加值
        if (!isRefundList) {
            ItemTradeRecord itemTradeRecord = itemRecordList.get(position);
            if (itemTradeRecord != null) {
                viewHolder.itemMoney.setText(itemTradeRecord.tradeMoney);
                viewHolder.itemTime.setText(itemTradeRecord.closingTime);
            }
        } else {
            ItemChongdiRefundRecord itemChongdiRefundRecord = itemChongdiRecordList.get(position);
            if (itemChongdiRefundRecord != null) {
                viewHolder.itemMoney.setText(itemChongdiRefundRecord.refundMoney);
                viewHolder.itemTime.setText(itemChongdiRefundRecord.refundTime);
            }
        }


        return convertView;
    }

    public void setRefundList(boolean refundList) {
        isRefundList = refundList;
    }

    public void setItemAccountList(List<ItemTradeRecord> itemRecordList) {
        this.itemRecordList = itemRecordList;
        this.notifyDataSetChanged();
    }

    public void setItemChongdiAccountList(List<ItemChongdiRefundRecord> itemRecordList) {
        this.itemChongdiRecordList = itemRecordList;
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        private TextView itemMoney;
        private TextView itemTime;
        private Button operator;
    }
}
