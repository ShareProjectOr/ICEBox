package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by WH on 2017/12/20.
 * 关机
 * 重启
 * 灯控制
 * 盘点
 * 修改温度
 */

public class TeleControlHelper {
    private static TeleControlHelper instance;
    private Context context;

    public static synchronized TeleControlHelper getInstance() {
        if (instance == null) {
            instance = new TeleControlHelper();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void getDatas(String url, final Map<String, Object> params) {
        MachineTeleControlTask task = new MachineTeleControlTask(url, params);
        task.execute();
    }

    /**
     * //机器详情远程操作异步任务
     */

    public class MachineTeleControlTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err = "net_work_err";
        private Map<String, Object> params;
        private String url;
        private boolean isAllow = false;


        public MachineTeleControlTask(String url, Map<String, Object> params) {
            this.params = params;
            this.url = url;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(url, JsonUtil.mapToJson(this.params));
                Log.e("response", response.toString());
                if (url.equals(HttpRequstUrl.MACHINE_Temp_URL)) {

                } else {
                    isAllow = JsonDataParse.getInstance().getTeleControlIsArrow(response.toString());
                }
                return true;
            } catch (IOException e) {
                Log.e("erro", e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                if (url.equals(HttpRequstUrl.MACHINE_Temp_URL)) {
                    Toast.makeText(context, "温度信息已保存", Toast.LENGTH_SHORT).show();
                } else {
                    if (isAllow) {
                        Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "操作失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Log.e("request error :", response + "");
            }
        }
    }

}
