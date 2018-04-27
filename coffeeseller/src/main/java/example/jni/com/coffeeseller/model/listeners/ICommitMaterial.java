package example.jni.com.coffeeseller.model.listeners;

import java.util.List;

import example.jni.com.coffeeseller.bean.CommitMaterialObject;

/**
 * Created by Administrator on 2018/4/27.
 */

public interface ICommitMaterial {
    void Commit(List<CommitMaterialObject> list, OnCommitMaterialCallBackListener onCommitMaterialCallBackListener);
}
