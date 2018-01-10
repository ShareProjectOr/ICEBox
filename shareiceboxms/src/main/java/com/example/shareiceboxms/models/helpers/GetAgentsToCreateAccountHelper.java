package com.example.shareiceboxms.models.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.shareiceboxms.models.beans.ItemPerson;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2018/1/5.
 */

public class GetAgentsToCreateAccountHelper {
    private static String TAG = "GetAgentsToCreateAccountHelper";
    private static GetAgentsToCreateAccountHelper instance;
    private List<ItemPerson> agents = new ArrayList<>();
    private HomeActivity activity;
    private GetAgentsLisenner getAgentsLisenner;

    public static GetAgentsToCreateAccountHelper getInstance() {
        if (instance == null) {
            instance = new GetAgentsToCreateAccountHelper();
        }
        return instance;
    }

    public void setGetAgentsLisenner(GetAgentsLisenner getAgentsLisenner) {
        this.getAgentsLisenner = getAgentsLisenner;
    }

    public void setContext(HomeActivity activity) {
        this.activity = activity;
    }

    public Map<String, Object> getParams(String agentId, String createTime) {
        Map<String, Object> params = RequestParamsContants.getInstance().getAgentsParams();
        return params;
    }

    public void getDatas() {
        GetAgentsTask task = new GetAgentsTask(RequestParamsContants.getInstance().getAgentsParams(), activity);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class GetAgentsTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private Map<String, Object> params;
        private Dialog dialog;


        public GetAgentsTask(Map<String, Object> params, Context context) {
            this.params = params;
            dialog = MyDialog.loadDialog(context);
        }

        @Override
        protected void onPreExecute() {
            if (dialog != null && !dialog.isShowing())
                dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e(TAG, " request url: " + HttpRequstUrl.USER_LIST);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.USER_LIST, JsonUtil.mapToJson(this.params));
                Log.e(TAG, "response:" + response);
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                agents = ItemPerson.getPersonList(JsonDataParse.getInstance().getArrayList(response));
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
                getAgentsLisenner.getAgents(agents);
            } else {

            }
        }
    }

    public interface GetAgentsLisenner {
        void getAgents(List<ItemPerson> agents);
    }
}
