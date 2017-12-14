package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shareiceboxms.R;

/**
 * Created by Administrator on 2017/12/13.
 */

public class UpLoadDetailsGoodListAdapter extends BaseAdapter {
    private Context mContext;

    public UpLoadDetailsGoodListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.upload_details_goodlist_itemlayout, null);
            holder = new ViewHolder();
            holder.goodtypeName = (TextView) convertView.findViewById(R.id.goodtypeName);
            holder.goodtypePrice = (TextView) convertView.findViewById(R.id.goodtypePrice);
            holder.operation = (TextView) convertView.findViewById(R.id.operation);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {
        TextView goodtypeName, goodtypePrice, operation;
    }
}
