package example.jni.com.coffeeseller.model.listeners;

import example.jni.com.coffeeseller.parse.PayResult;

/**
 * Created by WH on 2018/5/7.
 */

public interface TaskServiceListener {

    void payListenner(PayResult payResult);

    void formulaUpdate(String formulaId);
}
