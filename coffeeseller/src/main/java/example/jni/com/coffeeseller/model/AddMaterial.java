package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.listeners.IAddMaterial;
import example.jni.com.coffeeseller.model.listeners.OnAddMaterialCallBackListener;

/**
 * Created by Administrator on 2018/4/24.
 */

public class AddMaterial implements IAddMaterial {

    @Override
    public void addMaterial(Context context, String bunkersID, MaterialSql sql, OnAddMaterialCallBackListener onAddMaterialCallBackListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setView()
    }
}
