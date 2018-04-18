package example.jni.com.coffeeseller.views.viewinterface;

import cof.ac.inter.ContainerType;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface IDebugDropMaterialView {
    int getContainerID();

    void ShowDropResult(String result);

    int getTime();

    void setButtonSate(boolean b);

}
