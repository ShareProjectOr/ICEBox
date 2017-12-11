package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemTradeTotal;
import com.example.shareiceboxms.models.contants.Constants;

/**
 * Created by WH on 2017/12/9.
 */

public class TradeTotalListAdapter extends BaseAdapter {
    Context context;
    ItemTradeTotal itemTradeTotal;

    public TradeTotalListAdapter(Context context, ItemTradeTotal itemTradeTotal) {
        this.context = context;
        this.itemTradeTotal = itemTradeTotal;
    }

    @Override
    public int getCount() {
        return Constants.TradeTotalTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.trade_total_list_item, null);
            viewHolder.totalMoneyTitle = (TextView) convertView.findViewById(R.id.totalMoneyTitle);
            viewHolder.totalMoneyNum = (TextView) convertView.findViewById(R.id.totalMoneyNum);
            viewHolder.hasPayTitle = (TextView) convertView.findViewById(R.id.hasPayTitle);
            viewHolder.hasPayNum = (TextView) convertView.findViewById(R.id.hasPayNum);
            viewHolder.notPayTitle = (TextView) convertView.findViewById(R.id.notPayTitle);
            viewHolder.notPayNum = (TextView) convertView.findViewById(R.id.notPayNum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return null;
    }

    class ViewHolder {
        TextView totalMoneyTitle, totalMoneyNum, hasPayTitle, hasPayNum, notPayTitle, notPayNum;
    }
}
