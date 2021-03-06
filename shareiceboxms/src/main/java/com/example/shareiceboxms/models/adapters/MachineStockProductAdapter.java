package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/12.
 */

public class MachineStockProductAdapter extends BaseAdapter {
    private Context context;
    private List<ItemProduct> itemProducts;

    public MachineStockProductAdapter(Context context, List<ItemProduct> itemProducts) {
        this.context = context;
        this.itemProducts = itemProducts;
    }

    @Override
    public int getCount() {
        return itemProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return itemProducts == null ? null : itemProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.machine_detail_prod_list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.productPrice);
            viewHolder.productSpecPrice = (TextView) convertView.findViewById(R.id.productSpecPrice);
            viewHolder.timeLimit = (TextView) convertView.findViewById(R.id.timeLimit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //添加值
        return convertView;
    }

    class ViewHolder {
        private TextView productName, productPrice, productSpecPrice, timeLimit;
    }
}
