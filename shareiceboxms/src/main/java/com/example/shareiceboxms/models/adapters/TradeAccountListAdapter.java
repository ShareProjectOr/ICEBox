package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.trade.ItemTradeAccount;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountDetailFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountFragment;

import java.text.ParseException;
import java.util.List;

/**
 * Created by WH on 2017/12/11.
 */

public class TradeAccountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ITEM = 0;
    private static final int VIEW_LOADING = 1;
    Context context;
    TradeAccountFragment tradeAccountFragment;
    List<ItemTradeAccount> itemTradeAccounts;

    public TradeAccountListAdapter(Context context, List<ItemTradeAccount> itemTradeAccounts, TradeAccountFragment tradeAccountFragment) {
        this.context = context;
        this.tradeAccountFragment = tradeAccountFragment;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tradeAccountFragment != null) {
                        FragmentFactory.getInstance().getSavedBundle().putSerializable("ItemTradeAccount", itemTradeAccounts.get(position));
                        tradeAccountFragment.addFrameFragment(new TradeAccountDetailFragment());
                    }
                }
            });
//            totalAccountTime, accountMoney, accountState, timePeriod;
            ItemTradeAccount itemTradeAccount = itemTradeAccounts.get(position);
            if (itemTradeAccount == null) return;
            switch (itemTradeAccount.divideState) {
                case 0://待审核状态时显示工单创建时间
                    ((ViewHolder) holder).totalAccountTime.setText(itemTradeAccount.createTime);
//                        if (itemTradeAccount.createTime != null && !TextUtils.equals(itemTradeAccount.createTime, ""))
//                            ((ViewHolder) holder).timePeriod.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.createTime)));
//                            ((ViewHolder) holder).totalAccountTime.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.createTime)));

                    break;
                case 1://待确认-审核时间
                    ((ViewHolder) holder).totalAccountTime.setText(itemTradeAccount.checkTime);
                    ((ViewHolder) holder).timePeriod.setText(itemTradeAccount.createTime + "至" + itemTradeAccount.checkTime);
//                        if (itemTradeAccount.checkTime != null && !TextUtils.equals(itemTradeAccount.checkTime, ""))
//                            ((ViewHolder) holder).totalAccountTime.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.checkTime)));
//                        ((ViewHolder) holder).timePeriod.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.createTime))
//                                +"至"+SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.checkTime)));
                    break;
                case 2://待转账-确认时间
                    ((ViewHolder) holder).totalAccountTime.setText(itemTradeAccount.configTime);
                    ((ViewHolder) holder).timePeriod.setText(itemTradeAccount.createTime + "至" + itemTradeAccount.configTime);
//                        if (itemTradeAccount.configTime != null && !TextUtils.equals(itemTradeAccount.configTime, ""))
//                            ((ViewHolder) holder).totalAccountTime.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.configTime)));
//                        ((ViewHolder) holder).timePeriod.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.createTime))
//                                + "至" + SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.configTime)));
                    break;
                case 3://待复审-转账确认时间
                    ((ViewHolder) holder).totalAccountTime.setText(itemTradeAccount.configTransferTime);
                    ((ViewHolder) holder).timePeriod.setText(itemTradeAccount.createTime + "至" + itemTradeAccount.configTransferTime);
                       /* if (itemTradeAccount.configTransferTime != null && !TextUtils.equals(itemTradeAccount.configTransferTime, ""))
                            ((ViewHolder) holder).totalAccountTime.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.configTransferTime)));
                        ((ViewHolder) holder).timePeriod.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.createTime))
                                + "至" + SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.configTransferTime)));*/
                    break;
                case 4://复审完成-复审完成时间
                    ((ViewHolder) holder).totalAccountTime.setText(itemTradeAccount.recheckTime);
                    ((ViewHolder) holder).timePeriod.setText(itemTradeAccount.createTime + "至" + itemTradeAccount.recheckTime);
                       /* if (itemTradeAccount.recheckTime != null && !TextUtils.equals(itemTradeAccount.recheckTime, ""))
                            ((ViewHolder) holder).totalAccountTime.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.recheckTime)));
                        ((ViewHolder) holder).timePeriod.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.createTime))
                                + "至" + SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.recheckTime)));*/
                    break;
                case 5://背撤销-撤销时间
                    ((ViewHolder) holder).totalAccountTime.setText(itemTradeAccount.cancelTime);
                    ((ViewHolder) holder).timePeriod.setText(itemTradeAccount.createTime + "至" + itemTradeAccount.cancelTime);

                       /* if (itemTradeAccount.cancelTime != null && !TextUtils.equals(itemTradeAccount.cancelTime, ""))
                            ((ViewHolder) holder).totalAccountTime.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.cancelTime)));
                        ((ViewHolder) holder).timePeriod.setText(SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.createTime))
                                + "至" + SecondToDate.getStrOfDate(SecondToDate.getDateOfString(itemTradeAccount.cancelTime)));*/
                    break;
            }
            ((ViewHolder) holder).accountMoney.setText(itemTradeAccount.divideMoney);
            ((ViewHolder) holder).accountState.setText(Constants.TradeAccountStateTitle[itemTradeAccount.divideState + 1]);
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
