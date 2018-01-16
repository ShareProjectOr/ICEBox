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

import java.util.List;

/**
 * Created by WH on 2017/12/12.
 */

public class MachineStockProductAdapter extends BaseAdapter {
    private Context context;
    private List<ItemStockProduct> itemProducts;

    public MachineStockProductAdapter(Context context, List<ItemStockProduct> itemProducts) {
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
        if (position >= itemProducts.size()) {
            return null;
        }
        //添加值
        viewHolder.productName.setText(itemProducts.get(position).goodsName);
        viewHolder.productPrice.setText(itemProducts.get(position).price + "");
        viewHolder.productSpecPrice.setText(itemProducts.get(position).activityPrice + "");
        if (itemProducts.get(position).residueStorageTime != null) {
            String[] secondToDate = SecondToDate.formatLongToTimeStr(Long.valueOf(itemProducts.get(position).residueStorageTime));
            viewHolder.timeLimit.setText(secondToDate[0] + "天" + secondToDate[1] + "时" + secondToDate[2] + "分" + secondToDate[3] + "秒");
        }
        return convertView;
    }

    class ViewHolder {
        private TextView productName, productPrice, productSpecPrice, timeLimit;
    }
}
