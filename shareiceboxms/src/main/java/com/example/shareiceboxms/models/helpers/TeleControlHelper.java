package com.example.shareiceboxms.models.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Created by WH on 2017/12/20.
 * 关机
 * 重启
 * 灯控制
 * 盘点？
 */

public class TeleControlHelper {
    private static TeleControlHelper instance;

    public static synchronized TeleControlHelper getInstance() {
        if (instance == null) {
            instance = new TeleControlHelper();
        }
        return instance;
    }

    /**
     * //机器详情远程操作异步任务
     */

    public class MachineTeleControlTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err = "net_work_err";
        private Map<String, Object> params;


        public MachineTeleControlTask(Map<String, Object> params) {
            this.params = params;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.MACHINE_StockGoods_URL, JsonUtil.mapToJson(this.params));
                Log.e("response", response.toString());
                return true;
            } catch (IOException e) {
                Log.e("erro", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {

            } else {
                Log.e("request error :", response + "");
            }
        }


    }
}
