package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.trade.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordsFragment;

import java.util.List;

/**
 * Created by WH on 2017/12/11.
 */

public class TradeRecordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ITEM = 0;
    private static final int VIEW_LOADING = 1;
    Context context;
    List<ItemTradeRecord> itemTradeRecords;
    TradeRecordsFragment tradeRecordsFragment;

    public TradeRecordListAdapter(Context context, List<ItemTradeRecord> itemTradeRecords, TradeRecordsFragment tradeRecordsFragment) {
        this.context = context;
        this.itemTradeRecords = itemTradeRecords;
        this.tradeRecordsFragment = tradeRecordsFragment;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tradeRecordsFragment != null) {
                        FragmentFactory.getInstance().getSavedBundle().putInt("tradeID", itemTradeRecords.get(position).tradeID);
                        tradeRecordsFragment.addFrameFragment();
                    }
                }
            });
            ItemTradeRecord itemTradeRecord = itemTradeRecords.get(position);
            if (itemTradeRecord == null) {
                return;
            }
            ((ViewHolder) holder).totalMoneyNum.setText(itemTradeRecord.tradeMoney);
            ((ViewHolder) holder).totalRecordTime.setText(itemTradeRecord.tradeMoney);
            ((ViewHolder) holder).payState.setText(itemTradeRecord.payState == 0 ? Constants.TradeStateTitle[2] : Constants.TradeStateTitle[1]);
            ((ViewHolder) holder).payState.setTextColor(ContextCompat.getColor(context, Constants.TreadIsPayCOLOR[itemTradeRecord.payState]));
            if (itemTradeRecord.machine != null)
                ((ViewHolder) holder).machineNameAddr.setText(itemTradeRecord.machine.machineName + "(" + itemTradeRecord.machine.machineAddress + ")");
            ((ViewHolder) holder).tradeNo.setText(itemTradeRecord.tradeCode);
            ((ViewHolder) holder).jiesuanState.setText(Constants.TradeJieSuanStateTitle[itemTradeRecord.settleWay]);
            ((ViewHolder) holder).jiesuanState.setText(Constants.TradeJieSuanStateTitle[itemTradeRecord.settleWay]);
//            ((ViewHolder) holder).jiesuanWay.setText(itemTradeRecord.tradeMoney);
            ((ViewHolder) holder).payIcon.setImageDrawable(ContextCompat.getDrawable(context, Constants.TreadIsPayICON[itemTradeRecord.payState]));


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
        public TextView totalRecordTime, totalMoneyNum, payState, machineNameAddr, tradeNo, jiesuanState, jiesuanWay;
        public ImageView payIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            totalRecordTime = (TextView) itemView.findViewById(R.id.totalRecordTime);
            totalMoneyNum = (TextView) itemView.findViewById(R.id.totalMoneyNum);
            payIcon = (ImageView) itemView.findViewById(R.id.payIcon);
            payState = (TextView) itemView.findViewById(R.id.payState);
            machineNameAddr = (TextView) itemView.findViewById(R.id.machineNameAddr);
            tradeNo = (TextView) itemView.findViewById(R.id.tradeNo);
            jiesuanState = (TextView) itemView.findViewById(R.id.jiesuanState);
//            jiesuanWay = (TextView) itemView.findViewById(R.id.jiesuanWay);
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
