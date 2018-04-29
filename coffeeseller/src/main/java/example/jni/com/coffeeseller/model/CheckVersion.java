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
            String downLoadFileUrl;

            @Override
            protected Boolean doInBackground(Void... params) {
                Map<String, Object> post = new HashMap<>();
                post.put("version", locationVersion);
                post.put("machineCode", MachineConfig.getMachineCode());
                Log.e(TAG, " post params is " + post.toString());
                try {
                    String response = OkHttpUtil.post(Constance.CHECK_VERSION_URL, JsonUtil.mapToJson(post));
                    JSONObject object = new JSONObject(response);
                    Log.e(TAG, "result is " + object);
                    if (object.getString("err").equals("")) {
                        String netVersion = object.getJSONObject("d").getString("version");
                        if (!locationVersion.equals(netVersion)) { //版本不一致,更新
                            Log.e(TAG, "begin load file");
                            downLoadFileUrl = object.getJSONObject("d").getString("fileUrl");
                            return true;
                        } else {
                            result = "目前已经是最新的版本了!!!";
                        }

                    } else {
                        result = object.getString("err");
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
                    onCheckVersionCallBackListener.checkSuccess(downLoadFileUrl, true);
                } else {
                    onCheckVersionCallBackListener.checkFailed(result);
                }
            }
        }.execute();
    }
}
