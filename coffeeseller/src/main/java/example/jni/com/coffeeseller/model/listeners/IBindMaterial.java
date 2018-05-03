package example.jni.com.coffeeseller.model.listeners;

import android.content.Context;
import android.widget.TextView;

import example.jni.com.coffeeseller.views.viewinterface.IBindMaterialView;

/**
 * Created by Administrator on 2018/4/27.
 */

public interface IBindMaterial {
    void bindMaterial(IBindMaterialView iBindMaterialView,Context context, TextView textView, String bunkerID,String bunkerType, OnBindMaterialCallBackListener onBindMaterialCallBackListener);
}
