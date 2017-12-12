package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/12.
 */

public class ProductDetailAdapter extends BaseAdapter {
    private Context context;
    private List<ItemProduct> itemProductList;

    public ProductDetailAdapter(Context context) {
        this.context = context;
        itemProductList = new ArrayList<>();
    }

    @Override
    public int getCount() {
//        return itemProductList == null ? 0 : itemProductList.size();
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return itemProductList == null ? null : itemProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.trade_record_detail_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.productPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public void setItemProductList(List<ItemProduct> itemProductList) {
        this.itemProductList = itemProductList;
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        public CheckBox checkbox;
        public TextView productName;
        public TextView productPrice;
    }
}
