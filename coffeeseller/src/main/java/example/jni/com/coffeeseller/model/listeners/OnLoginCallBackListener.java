package example.jni.com.coffeeseller.model.listeners;


import example.jni.com.coffeeseller.bean.User;

/**
 * Created by Administrator on 2018/3/1.
 */

public interface OnLoginCallBackListener {
    void loginSuccess();

    void loginFailed(String erro);
}
