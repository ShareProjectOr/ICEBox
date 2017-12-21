package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shareiceboxms.R;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/13.
 */

public class UpLoadDetailsGoodListAdapter extends BaseAdapter {
    private Context mContext;
    private static final int TYPE_HEAD_ITEM = 0;
    private static final int TYPE_BODY_ITEM = 1;
    private boolean isHeadDataUpdate = false;
    private JSONObject mJson;

    public UpLoadDetailsGoodListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private int getItemType(int position) {
        return position == 0 ? TYPE_HEAD_ITEM : TYPE_BODY_ITEM;

    }

    @Override
    public int getCount() {
        return 10;
    }

    public void setJsonData(JSONObject json) {

        mJson = json;
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
        int type = getItemType(position);
        ViewHolder holder;
        switch (type) {
            case TYPE_HEAD_ITEM:
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.upload_details_goodlist_itemheadlayout, null);
                    if (mJson != null) {
                        if (!isHeadDataUpdate) {
                            
                        }
                    }
                }
                break;
            case TYPE_BODY_ITEM:
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
                break;
        }


        return convertView;
    }

    class ViewHolder {
        TextView goodtypeName, goodtypePrice, operation;
    }
}
