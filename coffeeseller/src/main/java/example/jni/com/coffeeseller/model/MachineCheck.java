package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.DebugAction;
import cof.ac.inter.Result;
import example.jni.com.coffeeseller.MachineConfig.MachineInitState;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.bean.bunkerData;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialLsit;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.listeners.IMachineCheck;
import example.jni.com.coffeeseller.model.listeners.OnMachineCheckCallBackListener;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MachineCheck implements IMachineCheck {
    private OnMachineCheckCallBackListener mOnMachineCheckCallBackListener;
    private CoffMsger mCoffmsger;
    private Context mContext;

    public MachineCheck(Context context) {
        MachineInitState.init();
        this.mContext = context;
        mCoffmsger = CoffMsger.getInstance();
    }

    @Override
    public void MachineCheck(OnMachineCheckCallBackListener onMachineCheckCallBackListener) {
        this.mOnMachineCheckCallBackListener = onMachineCheckCallBackListener;
        Thread mcheckThread = new Thread(new checkRunnnable());
        mcheckThread.start();

    }

    private class checkRunnnable implements Runnable {

        @Override
        public void run() {

            checkMachineCode();
            Waiter.doWait(700);
            checkMainCtrl();
            Waiter.doWait(700);
            subMQTT();
            Waiter.doWait(700);
            getFormula();
            if (MachineInitState.CHECK_OPENMAINCTRL == MachineInitState.NORMAL && MachineInitState.CHECK_MACHINECODE == MachineInitState.NORMAL
                    && MachineInitState.SUB_MQTT_STATE == MachineInitState.NORMAL && MachineInitState.GET_FORMULA == MachineInitState.NORMAL) {
                mOnMachineCheckCallBackListener.MachineCheckEnd(true);
            } else {
                mOnMachineCheckCallBackListener.MachineCheckEnd(false);
            }
        }
    }

    private void getFormula() {

        if (MachineConfig.getMachineCode().isEmpty()) {
            mOnMachineCheckCallBackListener.MaterialGroupGetFailed("机器号未填写,配方获取失败");
        } else {
            Map<String, Object> body = new HashMap<>();
            body.put("machineCode", MachineConfig.getMachineCode());
            try {
                MaterialSql content = new MaterialSql(mContext);

                List<bunkerData> list = new ArrayList<>();
                String response = OkHttpUtil.post("", JsonUtil.mapToJson(body));
                JSONObject object = new JSONObject(response);
                if (object.getString("err").equals("")) {
                    JSONObject d = object.getJSONObject("d");
                    JSONArray array = d.getJSONArray("list2");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject bunkerdata = (JSONObject) array.opt(i);
                        bunkerData data = new bunkerData();
                        data.setBunkerID(bunkerdata.getInt("bunkerID") + "");
                        data.setContainerID(bunkerdata.getInt("containerID") + "");
                        data.setMaterialDropSpeed(bunkerdata.getInt("output") + "");
                        data.setMaterialID(bunkerdata.getInt("materialID") + "");
                        data.setMaterialName(bunkerdata.getString("materialName"));
                        data.setMaterialUnit(bunkerdata.getString("materialunit"));
                        data.setMaterialStock(bunkerdata.getInt("materialStock") + "");
                        data.setMaterialType(bunkerdata.getInt("materialType") + "");
                        data.setLastLoadingTime(bunkerdata.getString("lastLoadingTime"));
                        list.add(i, data);
                    }
                    SingleMaterialLsit.getInstance(mContext).setCoffeeList(d.getJSONArray("list"));
                    if (content.getAllbunkersIDs().size() == 0) {
                        for (int i = 0; i < list.size(); i++) {
                            content.insertContact(list.get(i).getBunkerID(), list.get(i).getMaterialID(), list.get(i).getMaterialType(), list.get(i).getMaterialName()
                                    , list.get(i).getMaterialUnit(), list.get(i).getMaterialStock(), list.get(i).getMaterialDropSpeed(), list.get(i).getContainerID(), list.get(i).getLastLoadingTime());
                        }
                    }

                    mOnMachineCheckCallBackListener.MaterialGroupGetSuccess();
                } else {
                    mOnMachineCheckCallBackListener.MaterialGroupGetFailed(object.getString("err"));
                }
            } catch (IOException e) {
                mOnMachineCheckCallBackListener.MaterialGroupGetFailed("网络错误,配方获取失败");
            } catch (JSONException e) {
                mOnMachineCheckCallBackListener.MaterialGroupGetFailed(e.getMessage());
            }
        }

    }

    private void subMQTT() {
        Intent intent = new Intent(mContext, TaskService.class);
        mContext.startService(intent);
        if (MachineConfig.getTcpIP().isEmpty()) {
            mOnMachineCheckCallBackListener.MQTTSubcribeFailed();
        } else {
            TaskService.getInstance().start(mOnMachineCheckCallBackListener);
        }

    }

    public void checkMainCtrl() {
        if (mCoffmsger.init()) {
            mOnMachineCheckCallBackListener.OpenMainCrilSuccess();
            mCoffmsger.startCheckState();
            Result result = mCoffmsger.Debug(DebugAction.RESET, 0, 0);//复位机器
            Waiter.doWait(700);
            if (result.getCode() == Result.SUCCESS) {
                mOnMachineCheckCallBackListener.OpenMainCrilSuccess();
                mCoffmsger.startCheckState();
                MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.NORMAL;
            } else {
                mOnMachineCheckCallBackListener.OpenMainCrilFailed("主控板检测出错,错误:" + result.getErrDes());
            }


        } else {
            mOnMachineCheckCallBackListener.OpenMainCrilFailed("串口打开失败,请检测串口输入是否正确");
            MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.UNNORMAL;
        }

    }

    public void checkMachineCode() {
        Map<String, Object> postBody = new HashMap<>();
        if (SharedPreferencesManager.getInstance(mContext).getMachineCode().isEmpty()) {

            mOnMachineCheckCallBackListener.MachineCodeCheckFailed("机器号未填写,鉴权失败");

        } else {
            postBody.put("machineCode", SharedPreferencesManager.getInstance(mContext).getMachineCode());
            postBody.put("loginPassword", SharedPreferencesManager.getInstance(mContext).getLoginPassword());
            try {
                String response = OkHttpUtil.post("", JsonUtil.mapToJson(postBody));
                JSONObject object = new JSONObject(response);
                if (object.getString("err").equals("")) {
                    MachineInitState.CHECK_MACHINECODE = MachineInitState.NORMAL;
                    SharedPreferencesManager.getInstance(mContext).setTopicIP(object.getJSONObject("d").getString("topicIP"));
                    MachineConfig.setHostUrl(object.getJSONObject("d").getString("hostUrl"));
                    MachineConfig.setMachineCode(SharedPreferencesManager.getInstance(mContext).getMachineCode());
                    MachineConfig.setTcpIP(object.getJSONObject("d").getString("tcpIP"));
                    mOnMachineCheckCallBackListener.MachineCodeCheckSuccess();
                } else {
                    mOnMachineCheckCallBackListener.MachineCodeCheckFailed(object.getString("err"));
                }
            } catch (IOException e) {
                mOnMachineCheckCallBackListener.MachineCodeCheckFailed(e.getMessage());
            } catch (JSONException e) {
                mOnMachineCheckCallBackListener.MachineCodeCheckFailed(e.getMessage());
            }
        }


    }

/*    public void checkNetWorkState() {
        try {
            String response = OkHttpUtil.getStringFromServer("www.baidu.com");
            if (!response.isEmpty()) {
                mOnMachineCheckCallBackListener.NetWorkState(true);
                MachineInitState.CHECK_NETWORK_STATE = MachineInitState.NORMAL;
            }
        } catch (IOException e) {
            mOnMachineCheckCallBackListener.NetWorkState(false);
            MachineInitState.CHECK_NETWORK_STATE = MachineInitState.UNNORMAL;
        }
    }*/
}
