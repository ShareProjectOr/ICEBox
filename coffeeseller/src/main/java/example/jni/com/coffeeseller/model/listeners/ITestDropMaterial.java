package example.jni.com.coffeeseller.model.listeners;

import android.content.Context;

import cof.ac.inter.ContainerType;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface ITestDropMaterial {
    void StartDrop(Context context, int ContainerID, TestDropMaterialCallBackListener testDropMaterialCallBackListener);
}
