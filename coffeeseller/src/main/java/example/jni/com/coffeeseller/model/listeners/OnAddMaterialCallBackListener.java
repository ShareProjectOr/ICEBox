package example.jni.com.coffeeseller.model.listeners;

import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;

/**
 * Created by Administrator on 2018/4/24.
 */

public interface OnAddMaterialCallBackListener {
    void addEnd(MaterialSql sql);

}
