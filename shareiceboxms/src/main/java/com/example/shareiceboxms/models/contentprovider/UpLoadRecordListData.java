package com.example.shareiceboxms.models.contentprovider;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.shareiceboxms.models.beans.ItemProductType;
import com.example.shareiceboxms.models.beans.ItemUpload;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
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
 * Created by Administrator on 2017/12/25.
 */

public class UpLoadRecordListData {
    private RecyclerView.Adapter mAdapter;
    private Activity mActivty;
    private List<ItemUpload> DataSet = new ArrayList<>();
    private int total = 0;
    private LoadMoreHelper mloadMoreHelper;
    private int currentPage;
    private int pagerNum = 1;

    public UpLoadRecordListData(RecyclerView.Adapter mAdapter, Activity mActivty) {
        this.mAdapter = mAdapter;
        this.mActivty = mActivty;
    }

    public void setCanLoad(LoadMoreHelper mloadMoreHelper, int currentPage) {
        this.mloadMoreHelper = mloadMoreHelper;
        this.currentPage = currentPage;
    }

    public void getData(final String url, final Map<String, Object> body, final boolean refresh) {
      /*  final Dialog dialog = MyDialog.loadDialog(mActivty);
        dialog.show();*/
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
                    if (!error.equals("") && !error.equals("null")) {//请求出错
                        return false;
                    }
                    //清空上一次的末尾占位符
                    if (GetDataSetSize() != 0 && DataSet.get(GetDataSetSize() - 1) == null) {
                        Log.e("清空末位", "" + (GetDataSetSize() - 1));
                        DataSet.remove(GetDataSetSize() - 1);
                    }
                    JSONObject d = object.getJSONObject("d");
                    total = d.getInt("t");
                    currentPage = d.getInt("p");//请求成功则将页数加1
                    pagerNum = d.getInt("n");
                    JSONArray array = d.getJSONArray("list");
                    Log.e("上下货记录", "list" + d.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject itemobject = (JSONObject) array.opt(i);
                        ItemUpload item = new ItemUpload();
                        item.bindData(itemobject);
                        DataSet.add(item);
                    }
                    if (GetDataSetSize() != 0 && GetDataSetSize() < total) {
                        DataSet.add(null);
                        Log.e("添加占位后长度", GetDataSetSize() + "");
                        mloadMoreHelper.setLoading(false);
                    }

                } catch (IOException e1) {
                    error = RequstTips.getErrorMsg(e1.getMessage());

                    Log.e("response", e1 + "");
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
             //   dialog.dismiss();
                if (!aBoolean) {
                    Toast.makeText(mActivty, error, Toast.LENGTH_LONG).show();
                } else {
                    if (DataSet.size() == 0) {
                        //没有数据的情况
                        Toast.makeText(mActivty, "暂无任何上下货记录信息", Toast.LENGTH_LONG).show();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public int GetDataSetSize() {
        return DataSet.size();
    }

    public int GetMaxPageAccount() {
        return total % pagerNum > 0 ? total / pagerNum + 1 : total / pagerNum;

    }

    public ItemUpload getItem(int position) {
        return DataSet.get(position);
    }
}
