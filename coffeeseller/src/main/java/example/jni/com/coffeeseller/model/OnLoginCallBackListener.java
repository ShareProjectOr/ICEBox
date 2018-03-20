package example.jni.com.coffeeseller.model;


import example.jni.com.coffeeseller.bean.User;

/**
 * Created by Administrator on 2018/3/1.
 */

public interface OnLoginCallBackListener {
    void loginSuccess(User user);

    void loginFailed(String erro);
}
