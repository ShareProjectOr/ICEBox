package example.jni.com.coffeeseller.model.listeners;

import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;

/**
 * Created by WH on 2018/3/22.
 */

public interface ChooseCupListenner {
    void cancle(String order);

    void hasPay(CoffeeFomat coffeeFomat,DealRecorder dealRecorder);
}
