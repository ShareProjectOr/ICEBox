package example.jni.com.coffeeseller.model.listeners;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/4/27.
 */

public interface IBindMaterial {
    void bindMaterial(Context context, TextView textView,String bunkerID, OnBindMaterialCallBackListener onBindMaterialCallBackListener);
}
