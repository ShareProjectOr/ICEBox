package example.jni.com.coffeeseller.model.listeners;

import example.jni.com.coffeeseller.bean.CoffeeFomat;

/**
 * Created by WH on 2018/3/22.
 */

public interface ChooseCupListenner {
    void cancle();

    void getResult(CoffeeFomat coffeeFomat);
}
