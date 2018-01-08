package com.example.shareiceboxms.models.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.shareiceboxms.models.adapters.TradeAccountDetailAdapter;
import com.example.shareiceboxms.models.beans.trade.ItemChongdiRefundRecord;
import com.example.shareiceboxms.models.beans.trade.ItemTradeAccount;
import com.example.shareiceboxms.models.beans.trade.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordDetailFragment;
import com.google.gson.internal.LinkedHashTreeMap;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2018/1/5.
 */

public class RefundChongdiHelper {
    private static String TAG = "RefundChongdiHelper";
    private static RefundChongdiHelper instance;
    private HomeActivity activity;
    private List<ItemChongdiRefundRecord> itemRefundProducts;
    private TradeAccountDetailAdapter adapter;
    ItemTradeAccount itemTradeAccount;
    private PageHelper pageHelper;
    public int curPage = 1, requestNum, totalNum = 0, totalPage = 1;

    public static RefundChongdiHelper getInstance() {
        if (instance == null) {
            instance = new RefundChongdiHelper();
        }
        return instance;
    }

    public void setAdapter(TradeAccountDetailAdapter adapter) {
        this.adapter = adapter;
    }

    public void setItemTradeAccount(ItemTradeAccount itemTradeAccount) {
        this.itemTradeAccount = itemTradeAccount;
    }

    public void setItemRefundProducts(List<ItemChongdiRefundRecord> itemRefundProducts) {
        this.itemRefundProducts = itemRefundProducts;
    }

    public void setContext(HomeActivity activity) {
        this.activity = activity;
    }


    public void getDatas(Map<String, Object> params) {
        RefundChongdiTask task = new RefundChongdiTask(params, activity);
        task.execute();
    }

    public class RefundChongdiTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private Map<String, Object> params;
        private Dialog dialog;
        private List<ItemChongdiRefundRecord> itemChongdiRefundRecords;


        public RefundChongdiTask(Map<String, Object> params, Context context) {
            this.params = params;
            dialog = MyDialog.loadDialog(context);
            itemChongdiRefundRecords = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            if (dialog != null && !dialog.isShowing())
                dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e(TAG, " request url: " + HttpRequstUrl.TRADE_ACCOUONT_CHONGDI_RECORD_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_ACCOUONT_CHONGDI_RECORD_URL, JsonUtil.mapToJson(this.params));
                Log.e(TAG, "response:" + response);
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                itemChongdiRefundRecords = ItemChongdiRefundRecord.bindRefundRecordsList(JsonDataParse.getInstance().getArrayList(response));
                totalNum = JsonDataParse.getInstance().getTotalNum();
                curPage = JsonDataParse.getInstance().getCurPage();
                requestNum = JsonDataParse.getInstance().getRequestNum();
                totalPage = JsonDataParse.getInstance().getTotalPage();
                return true;
            } catch (IOException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.JSONException_Tip;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (success) {
                itemRefundProducts.clear();
                itemRefundProducts.addAll(itemChongdiRefundRecords);
                adapter.notifyDataSetChanged();
            } else {

            }
        }
    }
}
