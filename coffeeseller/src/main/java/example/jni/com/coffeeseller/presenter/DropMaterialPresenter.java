package example.jni.com.coffeeseller.presenter;

import android.content.Context;

import example.jni.com.coffeeseller.model.TestDropMaterial;
import example.jni.com.coffeeseller.model.listeners.ITestDropMaterial;
import example.jni.com.coffeeseller.model.listeners.TestDropMaterialCallBackListener;
import example.jni.com.coffeeseller.views.viewinterface.IDebugDropMaterialView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class DropMaterialPresenter {
    private ITestDropMaterial mITestDropMaterial;
    private IDebugDropMaterialView iDebugDropMaterialView;

    public DropMaterialPresenter(IDebugDropMaterialView materialView) {
        iDebugDropMaterialView = materialView;
        mITestDropMaterial = new TestDropMaterial();
    }

    public void startDrop(Context context, final int ContainerID) {
        mITestDropMaterial.StartDrop(context, ContainerID, new TestDropMaterialCallBackListener() {
            @Override
            public void TestSuccess() {
                iDebugDropMaterialView.ShowDropResult("落料成功", String.valueOf(ContainerID));
                //       iDebugDropMaterialView.ShowEditDialog(ContainerID + "");
            }

            @Override
            public void TestFailed(int ResultCode) {
                iDebugDropMaterialView.ShowDropResult("落料失败了啊老大  ResultCode is " + ResultCode + "快查查是什么原因!!!", "");
            }

            @Override
            public void TestEnd() {
                iDebugDropMaterialView.updateUi();
            }
        });
    }
}
