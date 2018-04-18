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
    public void StartDrop(int dropBunker, int time, final TestDropMaterialCallBackListener testDropMaterialCallBackListener) {
        CoffMsger coffMsger = CoffMsger.getInstance();
        Result result = coffMsger.Debug(DebugAction.OUT_INGREDIENT, dropBunker, 5);
        if (result.getCode() == Result.SUCCESS) {
            testDropMaterialCallBackListener.TestSuccess();
            testDropMaterialCallBackListener.TestEnd();
        } else {
            testDropMaterialCallBackListener.TestFailed(result.getCode());
            testDropMaterialCallBackListener.TestEnd();
        }
    }
}
