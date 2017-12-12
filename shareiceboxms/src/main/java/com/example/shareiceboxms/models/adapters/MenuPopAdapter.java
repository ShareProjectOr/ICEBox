package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shareiceboxms.R;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MenuPopAdapter extends BaseAdapter {
    private String[] mTitleArray;
    private Context mContext;

    public MenuPopAdapter(Context context, String[] strings) {
        mTitleArray = strings;
        mContext = context;
    }

    @Override

    public int getCount() {
        return mTitleArray.length;
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
        viewHolder holder;
        if (convertView == null) {
            holder = new viewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_pop_itemlayout, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        holder.title.setText(mTitleArray[position]);
        return convertView;
    }

    class viewHolder {
        TextView title;
    }
}
