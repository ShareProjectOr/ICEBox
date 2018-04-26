package example.jni.com.coffeeseller.model.listeners;

import example.jni.com.coffeeseller.MachineConfig.DealRecorder;

/**
 * Created by WH on 2018/4/26.
 */

public interface MkCoffeeListenner {
    void getMkResult(DealRecorder dealRecorder, boolean makeSuccess);
}
