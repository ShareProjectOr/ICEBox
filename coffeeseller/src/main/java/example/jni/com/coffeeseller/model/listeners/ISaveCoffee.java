package example.jni.com.coffeeseller.model.listeners;

import android.content.Context;

import java.util.List;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.bean.Coffee;

/**
 * Created by Administrator on 2018/4/17.
 */

public interface ISaveCoffee {
    void saveCoffee(Context context, List<ContainerConfig> list,OnSaveCoffeeCallBackListener onSaveCoffeeCallBackListener);
}
