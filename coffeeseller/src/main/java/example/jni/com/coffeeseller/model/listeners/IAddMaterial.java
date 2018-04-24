package example.jni.com.coffeeseller.model.listeners;

import android.content.Context;

import example.jni.com.coffeeseller.contentprovider.MaterialSql;

/**
 * Created by Administrator on 2018/4/24.
 */

public interface IAddMaterial {
    void addMaterial(Context context, String bunkersID, MaterialSql sql, OnAddMaterialCallBackListener onAddMaterialCallBackListener);
}
