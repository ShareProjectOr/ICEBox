package example.jni.com.coffeeseller.model.listeners;

import example.jni.com.coffeeseller.MachineConfig.DealRecorder;

/**
 * Created by WH on 2018/4/26.
 */

public interface MkCoffeeListenner {
    /*
    * success :是否制作成功
    * isCalculateMaterial：是否需要更新原料库
    * */
    void getMkResult(DealRecorder dealRecorder, boolean success, boolean isCalculateMaterial);
}
