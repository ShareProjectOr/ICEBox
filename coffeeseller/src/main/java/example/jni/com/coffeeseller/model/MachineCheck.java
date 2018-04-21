package example.jni.com.coffeeseller.model;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.DebugAction;
import cof.ac.inter.MachineState;
import cof.ac.inter.Result;
import example.jni.com.coffeeseller.MachineConfig.MachineInitState;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
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

    class checkRunnnable implements Runnable {

        @Override
        public void run() {


            if (mCoffmsger.init()) {
                mOnMachineCheckCallBackListener.OpenMainCrilSuccess();
                mCoffmsger.startCheckState();
                MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.NORMAL;
                // Result result = mCoffmsger.Debug(DebugAction.RESET, 0, 0);//复位机器

               /* if (result.getCode() == Result.SUCCESS) {
                    mOnMachineCheckCallBackListener.OpenMainCrilSuccess();
                    mCoffmsger.startCheckState();
                    MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.NORMAL;
                } else {
                    mOnMachineCheckCallBackListener.OpenMainCrilFailed("主控板检测出错,错误:" + result.getErrDes());
                }*/


            } else {
                mOnMachineCheckCallBackListener.OpenMainCrilFailed("串口打开失败,请检测串口输入是否正确");
                MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.UNNORMAL;
            }

            Map<String, Object> postBody = new HashMap<>();
            postBody.put("machineCode", SharedPreferencesManager.getInstance(mContext).getMachineCode());
            postBody.put("loginPassword",SharedPreferencesManager.getInstance(mContext).getLoginPassword());
            try {
                OkHttpUtil.post("", JsonUtil.mapToJson(postBody));
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (MachineInitState.CHECK_OPENMAINCTRL == MachineInitState.NORMAL) {
                mOnMachineCheckCallBackListener.MachineCheckEnd(true);

            } else {
                mOnMachineCheckCallBackListener.MachineCheckEnd(false);
            }
        }
    }
}
