package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemDetailsUpLoad;
import com.example.shareiceboxms.models.helpers.SecondToDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class UpLoadDetailsGoodListAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private static final int TYPE_HEAD_ITEM = 0;
    private static final int TYPE_BODY_ITEM = 1;
    private static final int SPECIAL_ITEM_COUNT = 1;
    public boolean isHeadDataUpdate = false;
    private JSONObject mJson;
    private List<ItemDetailsUpLoad> upLoads = new ArrayList<>();
    private List<ItemDetailsUpLoad> downLoads = new ArrayList<>();
    private List<ItemDetailsUpLoad> dataSet = new ArrayList<>();
    private int oprationType = 0;//默认上货
    private int[] mTextViews = {R.id.spendtime, R.id.recordCode, R.id.machineName, R.id.machienCode, R.id.machineAddr};

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
        switch (oprationType) {
            case 0:
                return upLoads.size() + SPECIAL_ITEM_COUNT;
            case 1:
                return downLoads.size() + SPECIAL_ITEM_COUNT;
        }
        return upLoads.size() + SPECIAL_ITEM_COUNT;
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
                    TextView operationType = (TextView) convertView.findViewById(R.id.operationType);
                    operationType.setOnClickListener(this);
                }
                if (mJson != null) {
                    String timeString = "获取失败";
                    try {
                        Log.d("-----------------", mJson.getString("openingTime"));
                        if (mJson.getString("openingTime").equals("null") || mJson.getString("closingTime").equals("null")) {
                            timeString = "数据异常,获取失败...";
                        } else {
                            long oPentime = SecondToDate.dateToStamp(mJson.getString("openingTime"));
                            long closeTime = SecondToDate.dateToStamp(mJson.getString("closingTime"));
                            long Sendtime = closeTime - oPentime;

                            String[] mOperationTime = SecondToDate.formatLongToTimeStr(Sendtime / 1000);
                            if (mOperationTime[1].equals("0")) {
                                timeString = mOperationTime[2] + "分" + mOperationTime[3] + "秒";
                            } else if (mOperationTime[0].equals("0")) {
                                timeString = mOperationTime[1] + "时" + mOperationTime[2] + "分" + mOperationTime[3] + "秒";
                            } else {
                                timeString = mOperationTime[0] + "天" + mOperationTime[1] + "时" + mOperationTime[2] + "分" + mOperationTime[3] + "秒";
                            }
                        }


                        String msg[] = {"操作耗时:" + timeString, "记录编号:" + mJson.getInt("recordID"),
                                "机器名称:" + mJson.getJSONObject("machine").getString("machineName"), "机器编号:" + mJson.getJSONObject("machine").getString("machineCode"),
                                "安装地址:" + mJson.getJSONObject("machine").getString("machineAddress")};
                        for (int i = 0; i < mTextViews.length; i++) {
                            TextView ts = (TextView) convertView.findViewById(mTextViews[i]);
                            ts.setText(msg[i]);
                        }


                    } catch (ParseException | JSONException e) {
                        Toast.makeText(mContext, "解析错误" + e, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case TYPE_BODY_ITEM:
                position -= SPECIAL_ITEM_COUNT;
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
                        holder.goodtypePrice.setText("￥" + (upLoads.get(position)).price);
                        holder.goodtypePrice.setText((upLoads.get(position)).goodsName);
                        holder.operation.setText("上货");
                        break;
                    default:
                        holder.goodtypePrice.setText("￥" + (downLoads.get(position)).price);
                        holder.goodtypePrice.setText((downLoads.get(position)).goodsName);
                        holder.operation.setText("下货");
                        break;
                }

                break;
        }


        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.operationType:
                changeOprationType();
                this.notifyDataSetChanged();
                break;
        }
    }

    class ViewHolder {
        TextView goodtypeName, goodtypePrice, operation;
    }
}
