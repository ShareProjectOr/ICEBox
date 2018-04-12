package example.jni.com.coffeeseller.model.listeners;

/**
 * Created by Administrator on 2018/3/1.
 */

public interface IUserLogin {
    void login(String username, String password, OnLoginCallBackListener loginCallBackListener);
}
