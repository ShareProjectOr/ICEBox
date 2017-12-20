package com.example.shareiceboxms.models.contentprovider;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.shareiceboxms.models.beans.ItemProductType;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lyu on 2017/12/19.
 * 品类列表提供类
 */

public class ProductListData {
    private RecyclerView.Adapter mAdapter;
    private Activity mActivty;
    private List<ItemProductType> DataSet = new ArrayList<>();
    private int total = 0;
    private int initAccountPage = 0;

    public ProductListData(RecyclerView.Adapter mAdapter, Activity mActivty) {
        this.mAdapter = mAdapter;
        this.mActivty = mActivty;
    }

    public void getData(final String url, final Map<String, Object> body, final boolean refresh) {
        final Dialog dialog = MyDialog.loadDialog(mActivty);
        dialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            String error;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    JSONObject object = new JSONObject(OkHttpUtil.post(
                            url, JsonUtil.mapToJson(body)));
                    if (refresh) {
                        DataSet.clear();
                    }
                    error = object.getString("err");
                    if (!error.equals("")) {//请求出错
                        return false;
                    }
                    JSONObject d = object.getJSONObject("d");
                    total = d.getInt("t");
                    JSONArray array = d.getJSONArray("list");
                    Log.e("订单列表", d.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject itemobject = (JSONObject) array.opt(i);
                        ItemProductType item = new ItemProductType();
                        item.bindData(itemobject);

                        DataSet.add(item);
                    }
                } catch (IOException e1) {
                    error = "网络错误\n";
                    return false;

                } catch (JSONException e1) {
                    error = RequstTips.JSONException_Tip;
                    Log.e("sss", e1 + "");
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                if (DataSet.size() == 0 && error.equals("")) {
                    //没有数据的情况
                }
                if (!aBoolean) {
                }
                mAdapter.notifyDataSetChanged();

            }
        }.execute();
    }

    public int GetDataSetSize() {
        return DataSet.size();
    }

    public int GetMaxPageAccount() {
        if (total != 0) {

            if (initAccountPage != 0 && GetDataSetSize() != 0) {
                return initAccountPage;
            } else if (GetDataSetSize() != 0) {
                initAccountPage = total % GetDataSetSize() > 0 ? total
                        / GetDataSetSize() + 1 : total / GetDataSetSize();
                return initAccountPage;
            } else {
                return 1;
            }
        } else {
            return 1;
        }

    }

    public ItemProductType getItem(int position) {
        return DataSet.get(position);
    }
}
