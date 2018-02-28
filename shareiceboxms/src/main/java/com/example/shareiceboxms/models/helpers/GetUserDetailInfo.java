package com.example.shareiceboxms.models.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.shareiceboxms.models.beans.ItemPerson;
import com.example.shareiceboxms.models.beans.product.ItemStockProduct;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2018/2/28.
 */

public class GetUserDetailInfo {
    public Context context;
    private GetUserDetailInfoCallback callback;

    public GetUserDetailInfo(Context context, GetUserDetailInfoCallback callback) {
        this.context = context;
        this.callback = callback;

    }

    public void getData() {
        new GetUserDetailTask().execute();
    }

    public interface GetUserDetailInfoCallback {
        void getUserDetail(ItemPerson itemPerson);
    }

    public class GetUserDetailTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err = "net_work_err";
        private ItemPerson user;


        public GetUserDetailTask() {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e("request params: ", JsonUtil.mapToJson(RequestParamsContants.getInstance().getUserDetailInfoParams()));
                response = OkHttpUtil.post(HttpRequstUrl.USER_DETAIL_INFO, JsonUtil.mapToJson(RequestParamsContants.getInstance().getUserDetailInfoParams()));
                user = ItemPerson.bindPersonFull(JsonDataParse.getInstance().getSingleObject(response));
                Log.e("response", response);
                return true;
            } catch (IOException e) {
                Log.e("erro", e.toString());
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                Log.e("erro", e.toString());
                err = RequstTips.JSONException_Tip;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {

            } else {
                Log.e("request error :", response + "");
            }
            if (callback != null) {
                callback.getUserDetail(user);
            }
        }
    }
}
