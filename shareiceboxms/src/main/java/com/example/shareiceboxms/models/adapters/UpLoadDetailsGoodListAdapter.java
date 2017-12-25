package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemDetailsUpLoad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class UpLoadDetailsGoodListAdapter extends BaseAdapter {
    private Context mContext;
    private static final int TYPE_HEAD_ITEM = 0;
    private static final int TYPE_BODY_ITEM = 1;
    public boolean isHeadDataUpdate = false;
    private JSONObject mJson;
    private List<ItemDetailsUpLoad> upLoads = new ArrayList<>();
    private List<ItemDetailsUpLoad> downLoads = new ArrayList<>();
    private List<ItemDetailsUpLoad> dataSet = new ArrayList<>();
    private int oprationType = 0;//默认上货

    public UpLoadDetailsGoodListAdapter(Context mContext) {
        this.mContext = mContext;
    }


    private int getItemType(int position) {
        return position == 0 ? TYPE_HEAD_ITEM : TYPE_BODY_ITEM;

    }

    public void changeOprationType() {
        if (oprationType == 0) {
            oprationType = 1;
        } else {
            oprationType = 0;
        }
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    public void setJsonData(JSONObject json) throws JSONException {
        if (upLoads.size() != 0) {
            upLoads.clear();
        }
        if (downLoads.size() != 0) {
            downLoads.clear();
        }
        if (dataSet.size() != 0) {
            dataSet.clear();
        }
        mJson = json;

        JSONArray uploadsArray = json.getJSONArray("exhibitList");
        JSONArray downArray = json.getJSONArray("offShelfList");
        for (int i = 0; i < uploadsArray.length(); i++) {
            JSONObject uploadjson = (JSONObject) uploadsArray.opt(i);
            ItemDetailsUpLoad itemDetailsUpLoad = new ItemDetailsUpLoad();
            itemDetailsUpLoad.bindData(uploadjson);
            upLoads.add(itemDetailsUpLoad);
        }
        for (int i = 0; i < downArray.length(); i++) {
            JSONObject uploadjson = (JSONObject) downArray.opt(i);
            ItemDetailsUpLoad itemDetailsDown = new ItemDetailsUpLoad();
            itemDetailsDown.bindData(uploadjson);
            downLoads.add(itemDetailsDown);
        }


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
                switch (oprationType) {
                    case 0:
                        dataSet = upLoads;
                        break;
                    default:
                        dataSet = downLoads;
                        break;
                }
                holder.goodtypePrice.setText("￥" + (dataSet.get(position)).price);
                holder.goodtypePrice.setText((dataSet.get(position)).goodsName);
                break;
        }


        return convertView;
    }

    class ViewHolder {
        TextView goodtypeName, goodtypePrice, operation;
    }
}
