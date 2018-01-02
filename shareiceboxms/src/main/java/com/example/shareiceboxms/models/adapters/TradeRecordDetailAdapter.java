package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.beans.product.ItemProduct;
import com.example.shareiceboxms.models.beans.product.ItemSellProduct;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/12.
 */

public class TradeRecordDetailAdapter extends BaseAdapter {
    private Context context;
    private List<ItemSellProduct> itemProductList;
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ItemSellProduct product = itemProductList.get(position);
        if (product != null) {
            viewHolder.checkbox.setChecked(itemProductList.get(position).isChecked());
            viewHolder.productName.setText(product.goodsName);
            viewHolder.productPrice.setText(product.soldPrise);
            /*
            * 如果是机器管理员，不能操作退款
            * */
            if (PerSonMessage.userType == Constants.MACHINE_MANAGER || PerSonMessage.userType == Constants.AGENT_MANAGER) {
                viewHolder.checkbox.setVisibility(View.GONE);
            } else {
                viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        itemProductList.get(position).setChecked(isChecked);
                        if (isChecked && totalCheckedCount != getCount()) {
                            totalCheckedCount += 1;
                            totalCheckedMoney += Float.parseFloat(product.soldPrise);
                            if (totalCheckedCount == getCount()) {
                                isAllChecked = true;
                            }
                        }
                        if (!isChecked && totalCheckedCount > 0) {
                            totalCheckedCount--;
                            totalCheckedMoney -= Float.parseFloat(product.soldPrise);
                            isAllChecked = false;
                        }
                        tradeRecordDetailFragment.changeCheckedCount();
                    }
                });
            }
        }

        return convertView;
    }

    public void setItemProductList(List<ItemSellProduct> itemProductList) {
        if (itemProductList != null) {
            this.itemProductList.clear();
            this.itemProductList.addAll(itemProductList);
            this.notifyDataSetChanged();
        }
    }


    public List<ItemSellProduct> getItemProductList() {
        return itemProductList;
    }

    public void checkAllItem(boolean isChecked) {

        for (int i = 0; i < getCount(); i++) {
            ItemSellProduct itemProduct = itemProductList.get(i);
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
