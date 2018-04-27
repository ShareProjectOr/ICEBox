package example.jni.com.coffeeseller.model.listeners;

import java.util.List;

import example.jni.com.coffeeseller.bean.Material;

/**
 * Created by Administrator on 2018/4/27.
 */

public interface OnBindMaterialCallBackListener {
    void BindSuccess(List<Material> list);

    void BindFailed(String reponse);
}
