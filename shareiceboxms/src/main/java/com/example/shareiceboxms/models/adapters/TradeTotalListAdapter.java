package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemTradeTotal;
import com.example.shareiceboxms.models.contants.Constants;

import static com.example.shareiceboxms.models.contants.Constants.TradeTotalTitles;
import static com.example.shareiceboxms.models.contants.Constants.TradeTotalTitlesItem;

/**
 * Created by WH on 2017/12/9.
 */

public class TradeTotalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ItemTradeTotal itemTradeTotal;

    public TradeTotalListAdapter(Context context, ItemTradeTotal itemTradeTotal) {
        this.context = context;
        this.itemTradeTotal = itemTradeTotal;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trade_total_list_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.totalMoneyTitle.setText(TradeTotalTitles[position]);
        if (position == 0) {
            viewHolder.hasPayTitle.setText(TradeTotalTitlesItem[0]);
            viewHolder.notPayTitle.setText(TradeTotalTitlesItem[1]);
        } else {
            viewHolder.hasPayTitle.setText(TradeTotalTitlesItem[2]);
            viewHolder.notPayTitle.setText(TradeTotalTitlesItem[3]);
        }
//        viewHolder.totalMoneyNum.setText(itemTradeTotal);
//        viewHolder.hasPayNum.setText(itemTradeTotal);
//        viewHolder.notPayNum.setText(itemTradeTotal);
    }

    @Override
    public int getItemCount() {
        return TradeTotalTitles.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView totalMoneyTitle, totalMoneyNum, hasPayTitle, hasPayNum, notPayTitle, notPayNum;

        public ViewHolder(View itemView) {
            super(itemView);
            totalMoneyTitle = (TextView) itemView.findViewById(R.id.totalMoneyTitle);
            totalMoneyNum = (TextView) itemView.findViewById(R.id.totalMoneyNum);
            hasPayTitle = (TextView) itemView.findViewById(R.id.hasPayTitle);
            hasPayNum = (TextView) itemView.findViewById(R.id.hasPayNum);
            notPayTitle = (TextView) itemView.findViewById(R.id.notPayTitle);
            notPayNum = (TextView) itemView.findViewById(R.id.notPayNum);
        }
    }
}
