package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.listeners.IBindMaterial;
import example.jni.com.coffeeseller.model.listeners.OnBindMaterialCallBackListener;
import example.jni.com.coffeeseller.utils.SecondToDate;
import example.jni.com.coffeeseller.views.viewinterface.IBindMaterialView;

/**
 * Created by Administrator on 2018/4/27.
 */

public class BindMaterial implements IBindMaterial {
    private List<Material> list;
    private List<String> materialNameList;
    private String TAG = "BindMaterial";

    @Override
    public void bindMaterial(final IBindMaterialView iBindMaterialView, final Context context, final TextView textView, final String bunkerID, final String bunkerType, final OnBindMaterialCallBackListener onBindMaterialCallBackListener) {
        iBindMaterialView.ShowLoading();
        list = new ArrayList<>();
        materialNameList = new ArrayList<>();
        if (list.size() != 0) {
            list.clear();
        }
        if (materialNameList.size() != 0) {
            list.clear();
        }
        new AsyncTask<Void, Boolean, Boolean>() {
            String result = "";

            @Override
            protected Boolean doInBackground(Void... params) {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("machineCode", MachineConfig.getMachineCode());
                try {
                    String response = OkHttpUtil.post(Constance.MATERIAL_LIST_GET_URL, JsonUtil.mapToJson(postMap));
                    JSONObject object = new JSONObject(response);
                    Log.e(TAG, "machineCode is " + MachineConfig.getMachineCode() + " bunkerID is " + bunkerID + " bunkerType is  " + bunkerType + "object is " + object.toString());
                    if (object.getString("err").equals("")) {

                        JSONArray array = object.getJSONArray("d");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject materialObject = (JSONObject) array.opt(i);

                            if (!bunkerType.equals("null") && materialObject.getString("materialType").equals(bunkerType)) {
                                Material material = new Material();
                                material.setMaterialID(materialObject.getInt("materialID"));
                                material.setUnit(materialObject.getString("materialunit"));
                                material.setName(materialObject.getString("materialName"));
                                material.setType(Integer.parseInt(materialObject.getString("materialType")));
                                material.setOutput(materialObject.getInt("output"));
                                materialNameList.add(materialObject.getString("materialName"));
                                list.add(material);
                            }


                        }
                        materialNameList.add(0, "解除绑定");
                        return true;
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

                    final ListPopupWindow window = new ListPopupWindow(context);
                    window.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_expandable_list_item_1, materialNameList));
                    window.setAnchorView(textView);
                    window.setWidth(300);
                    // window.setHorizontalOffset(textView.getWidth() / 4);
                    window.setModal(true);
                    window.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MaterialSql sql = new MaterialSql(context);
                            Log.e("choose name", materialNameList.get(position));
                            switch (position) {
                                case 0:
                                    Boolean update = sql.updateContact(bunkerID, "", "null", "null", "null",
                                            "null", "0", "null", "null", "", SecondToDate.getDateToString(System.currentTimeMillis()));
                                    if (update) {
                                        onBindMaterialCallBackListener.BindSuccess(list);
                                    } else {
                                        onBindMaterialCallBackListener.BindFailed("更新本地数据库失败");
                                    }
                                    break;
                                default:
                                    position -= 1;
                                    Boolean update1 = sql.updateContact(bunkerID, "", list.get(position).getMaterialID() + "", list.get(position).getType() + "", materialNameList.get(position + 1),
                                            list.get(position).getUnit(), "0", "null", list.get(position).getOutput() + "", "", SecondToDate.getDateToString(System.currentTimeMillis()));
                                    if (update1) {
                                        onBindMaterialCallBackListener.BindSuccess(list);
                                    } else {
                                        onBindMaterialCallBackListener.BindFailed("更新本地数据库失败");
                                    }
                                    break;

                            }

                            window.dismiss();
                        }
                    });
                    window.show();
                } else {
                    onBindMaterialCallBackListener.BindFailed(result);
                }
                iBindMaterialView.HideLoading();
                ;
            }
        }.execute();


    }
}
