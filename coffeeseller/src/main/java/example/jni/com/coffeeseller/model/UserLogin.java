package example.jni.com.coffeeseller.model;

import example.jni.com.coffeeseller.model.listeners.IUserLogin;
import example.jni.com.coffeeseller.model.listeners.OnLoginCallBackListener;

public class UserLogin implements IUserLogin {
    private OnLoginCallBackListener mOnLoginCallBackListener;

    @Override
    public void login(final String username, final String password, final OnLoginCallBackListener loginCallBackListener) {
        this.mOnLoginCallBackListener = loginCallBackListener;
        if (username.equals("")) {
            if (password.equals("")) {
                mOnLoginCallBackListener.loginSuccess();
            } else {
                mOnLoginCallBackListener.loginFailed("密码错误");
            }
        } else {
            mOnLoginCallBackListener.loginFailed("用户名不存在");
        }
    }



}
