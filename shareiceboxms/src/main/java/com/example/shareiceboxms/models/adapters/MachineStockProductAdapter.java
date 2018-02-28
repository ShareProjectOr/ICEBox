package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.product.ItemProduct;
import com.example.shareiceboxms.models.beans.product.ItemStockProduct;
import com.example.shareiceboxms.models.helpers.SecondToDate;

import java.text.ParseException;
import java.util.List;

/**
 * Created by WH on 2017/12/12.
 */

public class MachineStockProductAdapter extends BaseAdapter {
    private Context context;
    private ItemStockProduct itemStockProduct;

    public MachineStockProductAdapter(Context context, ItemStockProduct itemStockProduct) {
        this.context = context;
        this.itemStockProduct = itemStockProduct;
    }

    @Override
    public int getCount() {
        return itemStockProduct.goodsProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return itemStockProduct == null ? null : itemStockProduct.goodsProducts.get(position);
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
        if (position >= itemStockProduct.goodsProducts.size()) {
            return null;
        }
        //添加值
        viewHolder.productName.setText(itemStockProduct.goodsProducts.get(position).goodsName);
        viewHolder.productPrice.setText(itemStockProduct.goodsProducts.get(position).price + "");
        if (TextUtils.equals(itemStockProduct.goodsProducts.get(position).activityPrice, "null")) {
            itemStockProduct.goodsProducts.get(position).activityPrice = "无";
        }
        viewHolder.productSpecPrice.setText(itemStockProduct.goodsProducts.get(position).activityPrice + "");
        if (itemStockProduct.goodsProducts.get(position).residueStorageTime != null) {
            try {
                String time = SecondToDate.getTimeLimitString(String.valueOf(itemStockProduct.goodsProducts.get(position).residueStorageTime));
                viewHolder.timeLimit.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    class ViewHolder {
        private TextView productName, productPrice, productSpecPrice, timeLimit;
    }
}
