package example.jni.com.coffeeseller.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialLsit;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.listeners.IMachineCheck;
import example.jni.com.coffeeseller.model.listeners.OnMachineCheckCallBackListener;
import example.jni.com.coffeeseller.utils.Waiter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MachineCheck implements IMachineCheck {
    private OnMachineCheckCallBackListener mOnMachineCheckCallBackListener;
    private HomeActivity mContext;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String TAG = "MachineCheck";

    public MachineCheck(HomeActivity context) {
        MachineInitState.init();
        this.mContext = context;

    }

    @Override
    public void MachineCheck(OnMachineCheckCallBackListener onMachineCheckCallBackListener) {
        this.mOnMachineCheckCallBackListener = onMachineCheckCallBackListener;
        Thread mcheckThread = new Thread(new checkRunnnable());
        mcheckThread.start();

    }

    private void getFormula() {

        if (MachineConfig.getMachineCode() == null || MachineConfig.getMachineCode().isEmpty()) {
            mOnMachineCheckCallBackListener.MaterialGroupGetFailed("机器号未填写,配方获取失败");
        } else {
            Map<String, Object> body = new HashMap<>();
            body.put("machineCode", MachineConfig.getMachineCode());
            Log.e(TAG, "get formula machineCode is " + MachineConfig.getMachineCode());
            try {
                MaterialSql content = new MaterialSql(mContext);

                List<bunkerData> list = new ArrayList<>();
                if (MachineConfig.getHostUrl().isEmpty()) {

                } else {

                }
                String response = OkHttpUtil.post(Constance.FORMULA_GET, JsonUtil.mapToJson(body));
                Log.e(TAG, "machineCode is " + MachineConfig.getMachineCode() + "server is " + Constance.FORMULA_GET);
                JSONObject object = new JSONObject(response);
                if (object.getString("err").equals("")) {

                    JSONObject d = object.getJSONObject("d");

                    Log.e(TAG, d.toString());
                    JSONArray array = d.getJSONArray("list2");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject bunkerdata = (JSONObject) array.opt(i);
                        bunkerData data = new bunkerData();//其情况它
                        data.setContainerID(bunkerdata.getString("containerID"));
                        data.setBunkerID(bunkerdata.getString("bunkerID"));
                        data.setBunkerType(bunkerdata.getString("bunkerType"));
                        data.setMaterialDropSpeed(bunkerdata.getString("output"));
                        data.setMaterialID(bunkerdata.getString("materialID"));
                        data.setMaterialName(bunkerdata.getString("materialName"));
                        data.setMaterialUnit(bunkerdata.getString("materialunit"));
                        data.setMaterialStock(bunkerdata.getString("materialStock"));
                        data.setMaterialType(bunkerdata.getString("materialType"));
                        data.setLastLoadingTime(bunkerdata.getString("lastLoadingTime"));
                        list.add(data);


                    }
                    if (isCanUse(list)) {
                        if (content.getAllbunkersIDs().size() == 0) {//本地数据库为空的时候 ,需以服务端的数据进行绑定数据库
                            for (int i = 0; i < list.size(); i++) {
                                Log.e(TAG, "开始执行创建数据库");
                                if (list.get(i).getContainerID().equals("3")) {
                                    Log.e(TAG, "糖的默认落料量为:" + list.get(i).getMaterialDropSpeed());
                                }
                                boolean b = content.insertContact(list.get(i).getBunkerID(), list.get(i).getBunkerType(), list.get(i).getMaterialID(), list.get(i).getMaterialType(), list.get(i).getMaterialName()
                                        , list.get(i).getMaterialUnit(), list.get(i).getMaterialStock(), list.get(i).getMaterialDropSpeed(), list.get(i).getContainerID(), list.get(i).getLastLoadingTime());
                                if (b) {
                                } else {
                                    Log.e(TAG, "创建数据库插入时失败");
                                }
                            }


                        } else if (array.length() != content.getAllcontainerID().size()) {
                            //本地数据库与服务端返回的料仓长度不一致时,需更新数据库
                            for (int i = 0; i < list.size(); i++) {
                                boolean isupdated = false;
                                for (String ContainerID : content.getAllcontainerID()) {
                                    if (ContainerID.equals(list.get(i).getContainerID())) {  //假如数据库里面已经存在了这个料仓 则不做任何操作
                                        isupdated = true;
                                        break;
                                    }
                                }

                                if (!isupdated) {//假如数据库里面不存在了这个料仓 则插入到数据库中
                                    Log.e(TAG, "开始执行更新数据库");
                                    boolean b = content.insertContact(list.get(i).getBunkerID(), list.get(i).getBunkerType(), list.get(i).getMaterialID(), list.get(i).getMaterialType(), list.get(i).getMaterialName()
                                            , list.get(i).getMaterialUnit(), list.get(i).getMaterialStock(), list.get(i).getMaterialDropSpeed(), list.get(i).getContainerID(), list.get(i).getLastLoadingTime());
                                    if (b) {
                                    } else {
                                        Log.e(TAG, "更新数据库插入时失败");
                                    }
                                }
                            }

                        } else {

                        }
                        SingleMaterialLsit.getInstance(mContext).setCoffeeArray(d.getJSONArray("list"));
                        mOnMachineCheckCallBackListener.MaterialGroupGetSuccess();
                        MachineInitState.GET_FORMULA = MachineInitState.NORMAL;
                        Log.e(TAG, "不对数据库做任何操作");
                    } else { //服务器返回的料仓不为空
                        mOnMachineCheckCallBackListener.MaterialGroupGetFailed("机器未创建料仓,禁止使用");
                    }


                } else {
                    mOnMachineCheckCallBackListener.MaterialGroupGetFailed(object.getString("err"));
                }
            } catch (IOException e) {
                mOnMachineCheckCallBackListener.MaterialGroupGetFailed("网络错误,配方获取失败" + e.getMessage());
            } catch (JSONException e) {
                mOnMachineCheckCallBackListener.MaterialGroupGetFailed(e.getMessage());
            }
        }


    }

    private boolean isCanUse(List<bunkerData> list) {
        if (list.size() == 0) {
            return false;
        }
        return true;
    }

    private void subMQTT() {
        Runnable mRun = new Runnable() {

            @Override
            public void run() {

                if (MachineConfig.getTcpIP().isEmpty()) {
                    mOnMachineCheckCallBackListener.MQTTSubcribeFailed();
                    if (MachineInitState.CHECK_MACHINECODE == MachineInitState.NORMAL && MachineInitState.CHECK_OPENMAINCTRL == MachineInitState.NORMAL && MachineInitState.SUB_MQTT_STATE == MachineInitState.NORMAL && MachineInitState.GET_FORMULA == MachineInitState.NORMAL) {
                        mOnMachineCheckCallBackListener.MachineCheckEnd(true);
                    } else {
                        mOnMachineCheckCallBackListener.MachineCheckEnd(false);
                    }
                } else {
                    Intent intent = new Intent(mContext, TaskService.class);
                    mContext.startService(intent);
                    TaskService.getInstance().start(mOnMachineCheckCallBackListener);
                }
            }
        };
        handler.post(mRun);

    }

    public void checkMainCtrl() {
        CoffMsger mCoffmsger = CoffMsger.getInstance();

        if (mCoffmsger.init()) {
            //   Result result = mCoffmsger.Debug(DebugAction.RESET, 0, 0);//复位机器
            Waiter.doWait(700);
            mCoffmsger.startCheckState();
            MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.NORMAL;
            mOnMachineCheckCallBackListener.OpenMainCrilSuccess();
        /*    if (result.getCode() == Result.SUCCESS) {
                synchronized (mCoffmsger) {
                    mCoffmsger.startCheckState();
                }
                MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.NORMAL;
                mOnMachineCheckCallBackListener.OpenMainCrilSuccess();
            } else {
                mOnMachineCheckCallBackListener.OpenMainCrilFailed("主控板检测出错,错误:" + result.getErrDes());
            }*/


        } else {
            mOnMachineCheckCallBackListener.OpenMainCrilFailed("串口打开失败,请检测串口输入是否正确");
        }

    }

    public void checkMachineCode() {
        Map<String, Object> postBody = new HashMap<>();
        if (SharedPreferencesManager.getInstance(mContext).getMachineCode().isEmpty()) {

            mOnMachineCheckCallBackListener.MachineCodeCheckFailed("机器号未填写,鉴权失败");

        } else {
            postBody.put("machineCode", SharedPreferencesManager.getInstance(mContext).getMachineCode());
            postBody.put("loginPassword", SharedPreferencesManager.getInstance(mContext).getLoginPassword());
            Log.e(TAG, "machineCode is " + SharedPreferencesManager.getInstance(mContext).getMachineCode() + "password is " + SharedPreferencesManager.getInstance(mContext).getLoginPassword());
            try {
                String response = OkHttpUtil.post(Constance.MachineAuthentication_URL, JsonUtil.mapToJson(postBody));
                JSONObject object = new JSONObject(response);

                Log.d(TAG, " oject is " + object.toString());
                if (object.getString("err").equals("")) {

                    Log.d(TAG, "machineCofig is " + object.getJSONObject("d"));
                    SharedPreferencesManager.getInstance(mContext).setTopicIP(object.getJSONObject("d").getString("tcpIP"));
                    MachineConfig.setHostUrl(object.getJSONObject("d").getString("uRL"));
                    MachineConfig.setMachineCode(object.getJSONObject("d").getString("machineCode"));
                    SharedPreferencesManager.getInstance(mContext).setLoginPassword(object.getJSONObject("d").getString("loginPassword"));
                    MachineConfig.setTcpIP(object.getJSONObject("d").getString("tcpIP"));
                    MachineConfig.setTopic(object.getJSONObject("d").getString("topic"));
                    MachineInitState.CHECK_MACHINECODE = MachineInitState.NORMAL;
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

    private class checkRunnnable implements Runnable {

        @Override
        public void run() {
            checkMachineCode();
            Waiter.doWait(2000);
            checkMainCtrl();
            Waiter.doWait(2000);
            if (MachineConfig.getHostUrl().isEmpty()) {
                mOnMachineCheckCallBackListener.MaterialGroupGetFailed("机器鉴权没通过");
            } else {
                getFormula();
            }

            Waiter.doWait(2000);
         /*   if (MachineInitState.CHECK_OPENMAINCTRL == MachineInitState.NORMAL && MachineInitState.GET_FORMULA == MachineInitState.NORMAL) {
                mOnMachineCheckCallBackListener.MachineCheckEnd(true);
            } else {
                mOnMachineCheckCallBackListener.MachineCheckEnd(false);
            }*/
             subMQTT();
            Waiter.doWait(2000);

        }
    }


}
