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
import com.example.shareiceboxms.models.ItemCloseDoorGoods;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.beans.product.ItemSellProduct;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/12/12.
 */

public class UpdateGoodsCompleteAdapter extends BaseAdapter {
    private Context context;
    List<ItemCloseDoorGoods> datas;

    public UpdateGoodsCompleteAdapter(Context context, List<ItemCloseDoorGoods> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.close_door_list_item, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //更新值

        return convertView;
    }

    class ViewHolder {
        public TextView productName;
        public TextView productAddNum;
        public TextView productSubNum;

        public ViewHolder(View itemView) {

            productName = (TextView) itemView.findViewById(R.id.productName);
            productAddNum = (TextView) itemView.findViewById(R.id.productAddNum);
            productSubNum = (TextView) itemView.findViewById(R.id.productSubNum);
        }
    }
}
