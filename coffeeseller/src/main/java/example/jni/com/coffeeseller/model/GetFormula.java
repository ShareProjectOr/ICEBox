package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.jni.com.coffeeseller.MachineConfig.MachineInitState;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.bean.bunkerData;
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialLsit;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;

/**
 * Created by WH on 2018/5/4.
 */

public class GetFormula {
    private static String TAG = "GetFormula";

    public Boolean getFormula(Context context) {

        Map<String, Object> body = new HashMap<>();
        body.put("machineCode", MachineConfig.getMachineCode());
        Log.e(TAG, "get formula machineCode is " + MachineConfig.getMachineCode());
        try {
            List<bunkerData> list = new ArrayList<>();
            String response = OkHttpUtil.post(Constance.FORMULA_GET, JsonUtil.mapToJson(body));
            Log.e(TAG, "machineCode is " + MachineConfig.getMachineCode() + "server is " + Constance.FORMULA_GET);
            JSONObject object = new JSONObject(response);
            if (object.getString("err").equals("")) {

                JSONObject d = object.getJSONObject("d");

                Log.e(TAG, d.toString());

                Log.e(TAG, "list =" + d.getJSONArray("list").toString());
                SingleMaterialLsit.getInstance(context).setCoffeeArray(d.getJSONArray("list"));
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}