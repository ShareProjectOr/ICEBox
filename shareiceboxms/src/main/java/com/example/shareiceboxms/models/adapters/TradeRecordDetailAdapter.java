package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/12.
 */

public class TradeRecordDetailAdapter extends BaseAdapter {
    private Context context;
    private List<ItemProduct> itemProductList;
    TradeRecordDetailFragment tradeRecordDetailFragment;
    boolean isAllChecked = false;
    int totalCheckedCount = 0;
    float totalCheckedMoney = 0;

    public TradeRecordDetailAdapter(Context context, TradeRecordDetailFragment tradeRecordDetailFragment) {
        this.context = context;
        this.tradeRecordDetailFragment = tradeRecordDetailFragment;
        itemProductList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return itemProductList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.trade_record_detail_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.productPrice);
            //如果不是退回商品，就显示checkbox

            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    itemProductList.get(position).setChecked(isChecked);
                    if (isChecked && totalCheckedCount != getCount()) {
                        totalCheckedCount += 1;
                        totalCheckedMoney += 2.00f;
                        if (totalCheckedCount == getCount()) {
                            isAllChecked = true;
                        }
                    }
                    if (!isChecked && totalCheckedCount > 0) {
                        totalCheckedCount--;
                        totalCheckedMoney -= 2.00f;
                        isAllChecked = false;
                    }
                    tradeRecordDetailFragment.changeCheckedCount();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkbox.setChecked(itemProductList.get(position).isChecked());
        return convertView;
    }

    public void setItemProductList(List<ItemProduct> itemProductList) {
        this.itemProductList = itemProductList;
        this.notifyDataSetChanged();
    }

    public void checkAllItem(boolean isChecked) {

        for (int i = 0; i < getCount(); i++) {
            ItemProduct itemProduct = itemProductList.get(i);
            itemProduct.setChecked(isChecked);
        }
        isAllChecked = isChecked;
        notifyDataSetChanged();

    }


    public boolean isAllChecked() {
        return isAllChecked;
    }

    public int getTotalCheckedCount() {
        return totalCheckedCount;
    }

    public float getTotalCheckedMoney() {
        return totalCheckedMoney;
    }

    public void setAllChecked(boolean allChecked) {
        isAllChecked = allChecked;
    }

    public void setTotalCheckedCount(int totalCheckedCount) {
        this.totalCheckedCount = totalCheckedCount;
    }

    public void setTotalCheckedMoney(float totalCheckedMoney) {
        this.totalCheckedMoney = totalCheckedMoney;
    }

    class ViewHolder {
        public CheckBox checkbox;
        public TextView productName;
        public TextView productPrice;
    }
}
