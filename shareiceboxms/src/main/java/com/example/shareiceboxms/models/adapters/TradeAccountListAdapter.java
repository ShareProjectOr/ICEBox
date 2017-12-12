package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemTradeAccount;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;

import java.util.List;

/**
 * Created by WH on 2017/12/11.
 */

public class TradeAccountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ITEM = 0;
    private static final int VIEW_LOADING = 1;
    Context context;
    List<ItemTradeAccount> itemTradeAccounts;

    public TradeAccountListAdapter(Context context, List<ItemTradeAccount> itemTradeAccounts) {
        this.context = context;
        this.itemTradeAccounts = itemTradeAccounts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_ITEM) {
            viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_account_list_item, null));
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
        return itemTradeAccounts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemTradeAccounts.get(position) != null ? VIEW_ITEM : VIEW_LOADING;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView totalAccountTime, accountMoney, accountState, timePeriod;

        public ViewHolder(View itemView) {
            super(itemView);
            totalAccountTime = (TextView) itemView.findViewById(R.id.totalAccountTime);
            accountMoney = (TextView) itemView.findViewById(R.id.accountMoney);
            accountState = (TextView) itemView.findViewById(R.id.accountState);
            timePeriod = (TextView) itemView.findViewById(R.id.timePeriod);
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
