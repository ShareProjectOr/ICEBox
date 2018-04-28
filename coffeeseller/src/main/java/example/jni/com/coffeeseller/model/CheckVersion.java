package example.jni.com.coffeeseller.model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.listeners.ICheckVersion;
import example.jni.com.coffeeseller.model.listeners.OnCheckVersionCallBackListener;

/**
 * Created by Administrator on 2018/4/27.
 */

public class CheckVersion implements ICheckVersion {
    private String TAG = "CheckVersion";

    @Override
    public void CheckVersion(final String locationVersion, final OnCheckVersionCallBackListener onCheckVersionCallBackListener) {
        new AsyncTask<Void, Boolean, Boolean>() {
            String result;

            @Override
            protected Boolean doInBackground(Void... params) {
                Map<String, Object> post = new HashMap<>();
                post.put("version", locationVersion);
                post.put("machineCode", MachineConfig.getMachineCode());
                try {
                    String response = OkHttpUtil.post(Constance.CHECK_VERSION_URL, JsonUtil.mapToJson(post));
                    JSONObject object = new JSONObject(response);
                    if (object.getString("err").equals("")) {
                        String netVersion = object.getJSONObject("d").getString("version");
                        if (!locationVersion.equals(netVersion)) { //版本不一致,更新
                            Log.e(TAG, "begin load file");

                        }

                    }
                } catch (IOException e) {
                    result = e.getMessage();
                } catch (JSONException e) {
                    result = e.getMessage();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {

                } else {
                    onCheckVersionCallBackListener.checkFailed(result);
                }
            }
        }.execute();
    }
}
