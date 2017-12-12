package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.Constants;

import java.util.List;

/**
 * Created by WH on 2017/12/11.
 */

public class TradeRecordListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_ITEM = 0;
    private static final int VIEW_LOADING = 1;
    Context context;
    List<ItemTradeRecord> itemTradeRecords;

    public TradeRecordListAdapter(Context context, List<ItemTradeRecord> itemTradeRecords) {
        this.context = context;
        this.itemTradeRecords = itemTradeRecords;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_ITEM) {
            viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_record_list_item, null));
        } else if (viewType == VIEW_LOADING) {
            viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.loading_more, null));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

        } else if (holder instanceof LoadingHolder) {
            if (holder != null) {
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemTradeRecords.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemTradeRecords.get(position) != null ? VIEW_ITEM : VIEW_LOADING;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView totalRecordTime, totalMoneyNum, payState, machineNameAddr, tradeNo;
        public ImageView payIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            totalRecordTime = (TextView) itemView.findViewById(R.id.totalRecordTime);
            totalMoneyNum = (TextView) itemView.findViewById(R.id.totalMoneyNum);
            payIcon = (ImageView) itemView.findViewById(R.id.payIcon);
            payState = (TextView) itemView.findViewById(R.id.payState);
            machineNameAddr = (TextView) itemView.findViewById(R.id.machineNameAddr);
            tradeNo = (TextView) itemView.findViewById(R.id.tradeNo);
        }
    }

    class LoadingHolder extends RecyclerView.ViewHolder {
        public TextView loading;

        public LoadingHolder(View itemView) {
            super(itemView);
            loading = (TextView) itemView.findViewById(R.id.loading);
        }
    }
}
