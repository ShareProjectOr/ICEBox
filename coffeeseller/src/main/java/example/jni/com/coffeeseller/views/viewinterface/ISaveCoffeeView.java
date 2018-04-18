package example.jni.com.coffeeseller.views.viewinterface;

import android.content.Context;

import java.util.List;

import cof.ac.inter.ContainerConfig;

/**
 * Created by Administrator on 2018/4/17.
 */

public interface ISaveCoffeeView {
    Context getcontext();

    List<ContainerConfig> getList();
}
