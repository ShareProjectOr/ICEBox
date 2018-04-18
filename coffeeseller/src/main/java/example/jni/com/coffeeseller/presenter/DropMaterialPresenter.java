package example.jni.com.coffeeseller.presenter;

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

    public void startDrop() {
        mITestDropMaterial.StartDrop(iDebugDropMaterialView.getContainerID(), iDebugDropMaterialView.getTime(), new TestDropMaterialCallBackListener() {
            @Override
            public void TestSuccess() {
                iDebugDropMaterialView.ShowDropResult("落料成功");
            }

            @Override
            public void TestFailed(int ResultCode) {
                iDebugDropMaterialView.ShowDropResult("落料失败了啊老大  ResultCode is " + ResultCode + "快查查是什么原因!!!");
            }

            @Override
            public void TestEnd() {
                iDebugDropMaterialView.setButtonSate(true);
            }
        });
    }
}
