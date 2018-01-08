package com.example.shareiceboxms.models.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.shareiceboxms.models.beans.product.ItemSellProduct;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordDetailFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2018/1/5.
 */

public class RefundMoreHelper {
    private static String TAG = "RefundMoreTask";
    private static RefundMoreHelper instance;
    private HomeActivity activity;
    private TradeRecordDetailFragment tradeRecordDetailFragment;
    private Map<String, Object> params;
    private boolean isRefundSuccessed;//退款操作是否成功

    public static RefundMoreHelper getInstance() {
        if (instance == null) {
            instance = new RefundMoreHelper();
        }
        return instance;
    }

    public void setContext(HomeActivity activity) {
        this.activity = activity;
    }

    public void setTradeRecordDetailFragment(TradeRecordDetailFragment tradeRecordDetailFragment) {
        this.tradeRecordDetailFragment = tradeRecordDetailFragment;
    }

    public void setParams(String RFIDList, String refundReason) {
        params = RequestParamsContants.getInstance().getRefundParams();
        params.put("RFIDList", RFIDList);
        params.put("refundReason", refundReason);
    }

    public void getDatas() {
        RefundMoreTask task = new RefundMoreTask(params, activity);
        task.execute();
    }

    public boolean isRefundSuccessed() {
        return isRefundSuccessed;
    }

    public class RefundMoreTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private boolean isRefundSuccess = false;
        private Map<String, Object> params;
        private Dialog dialog;


        public RefundMoreTask(Map<String, Object> params, Context context) {
            this.params = params;
            dialog = MyDialog.loadDialog(context);
        }

        @Override
        protected void onPreExecute() {
            if (dialog != null && !dialog.isShowing())
                dialog.show();
            isRefundSuccessed = false;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e(TAG, " request url: " + HttpRequstUrl.TRADE_REFUND_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_REFUND_URL, JsonUtil.mapToJson(this.params));
                Log.e(TAG, "response:" + response);
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                isRefundSuccess = JsonDataParse.getInstance().getTeleControlIsArrow(response);
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
                if (isRefundSuccess) {
                    Toast.makeText(activity, "退款成功！", Toast.LENGTH_SHORT).show();
                    isRefundSuccessed = true;
                    if (tradeRecordDetailFragment != null) {
                        tradeRecordDetailFragment.getProductDatas();
                    }
                } else {
                    Toast.makeText(activity, "退款失败！", Toast.LENGTH_SHORT).show();
                    isRefundSuccessed = false;
                }
            } else {
                Toast.makeText(activity, "退款失败！" + err, Toast.LENGTH_SHORT).show();
                isRefundSuccessed = false;
            }
        }
    }
}
