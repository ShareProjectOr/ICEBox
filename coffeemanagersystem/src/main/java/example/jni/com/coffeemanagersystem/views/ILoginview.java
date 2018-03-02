package example.jni.com.coffeemanagersystem.views;

import example.jni.com.coffeemanagersystem.bean.User;

/**
 * Created by Administrator on 2018/3/1.
 */

public interface ILoginview {
    String getUserName();

    String getPassword();

    void showLoading();

    void hideLoading();

    void toMainActivity(User user);

    void showFailedError(String err);

}
