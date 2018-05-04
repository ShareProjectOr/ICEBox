package example.jni.com.coffeeseller.model;

import android.os.CountDownTimer;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.ContainerType;
import cof.ac.inter.DebugAction;
import cof.ac.inter.Result;
import example.jni.com.coffeeseller.model.listeners.ITestDropMaterial;
import example.jni.com.coffeeseller.model.listeners.TestDropMaterialCallBackListener;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TestDropMaterial implements ITestDropMaterial {

    @Override
    public void StartDrop(int ContainerID, final TestDropMaterialCallBackListener testDropMaterialCallBackListener) {
        CoffMsger coffMsger = CoffMsger.getInstance();
        if (ContainerID == 0) {
            Result result = coffMsger.Debug(DebugAction.CRUSH_BEAN, 0, 50);
            if (result.getCode() == Result.SUCCESS) {
                testDropMaterialCallBackListener.TestSuccess();
            } else {
                testDropMaterialCallBackListener.TestFailed(result.getCode());
            }
        } else if (ContainerID == 7) {
            Result result = coffMsger.Debug(DebugAction.OUT_HOTWATER, 0, 50);
            if (result.getCode() == Result.SUCCESS) {
                testDropMaterialCallBackListener.TestSuccess();
            } else {
                testDropMaterialCallBackListener.TestFailed(result.getCode());
            }
        } else {
            Result result = coffMsger.Debug(DebugAction.OUT_INGREDIENT, ContainerID, 50);
            if (result.getCode() == Result.SUCCESS) {
                testDropMaterialCallBackListener.TestSuccess();
            } else {
                testDropMaterialCallBackListener.TestFailed(result.getCode());
            }
        }


        testDropMaterialCallBackListener.TestEnd();
    }
}
