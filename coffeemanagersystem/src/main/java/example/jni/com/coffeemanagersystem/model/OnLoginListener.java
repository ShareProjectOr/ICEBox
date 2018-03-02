package example.jni.com.coffeemanagersystem.model;

import example.jni.com.coffeemanagersystem.bean.User;

/**
 * Created by Administrator on 2018/3/1.
 */

public interface OnLoginListener {
    void loginSuccess(User user);

    void loginFailed(String erro);
}
