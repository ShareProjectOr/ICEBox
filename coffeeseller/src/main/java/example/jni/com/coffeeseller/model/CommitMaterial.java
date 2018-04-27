package example.jni.com.coffeeseller.model;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.jni.com.coffeeseller.bean.CommitMaterialObject;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.listeners.ICommitMaterial;
import example.jni.com.coffeeseller.model.listeners.OnCommitMaterialCallBackListener;

/**
 * Created by Administrator on 2018/4/27.
 */

public class CommitMaterial implements ICommitMaterial {
    @Override
    public void Commit(List<CommitMaterialObject> list, final OnCommitMaterialCallBackListener onCommitMaterialCallBackListener) {
        final Map<String, Object> postBody = new HashMap<>();
        postBody.put("machineCode", MachineConfig.getMachineCode());
        postBody.put("bunkers", list);
        new AsyncTask<Void, Boolean, Boolean>() {
            String response;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(OkHttpUtil.post(Constance.COMMIT_MATERIAL_URL, JsonUtil.mapToJson(postBody)));
                    if (object.getString("err").equals("")) {
                        response = "";
                        return true;
                    } else {
                        response = object.getString("err");
                    }
                } catch (IOException e) {
                    response = e.getMessage();
                } catch (JSONException e) {
                    response = e.getMessage();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    onCommitMaterialCallBackListener.commitSuccess();
                } else {
                    onCommitMaterialCallBackListener.commitFailed(response);
                }
            }
        }.execute();
    }
}
