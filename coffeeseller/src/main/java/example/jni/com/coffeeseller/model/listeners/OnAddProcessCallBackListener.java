package example.jni.com.coffeeseller.model.listeners;

import cof.ac.inter.ContainerConfig;

/**
 * Created by Administrator on 2018/4/17.
 */

public interface OnAddProcessCallBackListener {
    void AddSuccess();

    void editSuccess(ContainerConfig config);
}
