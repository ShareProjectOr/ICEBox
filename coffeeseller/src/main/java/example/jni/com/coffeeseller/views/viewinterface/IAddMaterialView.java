package example.jni.com.coffeeseller.views.viewinterface;

import android.content.Context;

import example.jni.com.coffeeseller.contentprovider.MaterialSql;

/**
 * Created by Administrator on 2018/4/25.
 */

public interface IAddMaterialView {
    MaterialSql getSql();

    Context getcontext();

    String getBunkersID();

    void notifySetDataChange(MaterialSql sql);
}
