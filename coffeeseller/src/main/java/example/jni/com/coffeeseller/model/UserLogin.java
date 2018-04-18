package example.jni.com.coffeeseller.model;

import android.content.Context;

import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.model.listeners.IUserLogin;
import example.jni.com.coffeeseller.model.listeners.OnLoginCallBackListener;

public class UserLogin implements IUserLogin {
    private OnLoginCallBackListener mOnLoginCallBackListener;
    private Context mContext;

    public UserLogin(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void login(final String username, final String password, final OnLoginCallBackListener loginCallBackListener) {
        this.mOnLoginCallBackListener = loginCallBackListener;

        if (username.equals(SharedPreferencesManager.getInstance(mContext).getLoginAccount())) {
            if (password.equals(SharedPreferencesManager.getInstance(mContext).getLoginPassword())) {
                mOnLoginCallBackListener.loginSuccess();
            } else {
                mOnLoginCallBackListener.loginFailed("密码错误");
            }
        } else {
            mOnLoginCallBackListener.loginFailed("用户名不存在");
        }
    }


}
