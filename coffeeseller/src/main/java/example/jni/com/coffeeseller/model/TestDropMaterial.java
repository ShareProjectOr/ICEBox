package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.ContainerType;
import cof.ac.inter.DebugAction;
import cof.ac.inter.Result;
import example.jni.com.coffeeseller.model.listeners.ITestDropMaterial;
import example.jni.com.coffeeseller.model.listeners.TestDropMaterialCallBackListener;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TestDropMaterial implements ITestDropMaterial {

    @Override
    public void StartDrop(Context mContext, final int ContainerID, final TestDropMaterialCallBackListener testDropMaterialCallBackListener) {
        new AlertDialog.Builder(mContext).setTitle(null).setMessage("确定开始落料测试吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                dialog.dismiss();
                Waiter.doWait(40);
                testDropMaterialCallBackListener.TestEnd();
            }
        }).setNegativeButton("点错了", null).create().show();

    }
}
