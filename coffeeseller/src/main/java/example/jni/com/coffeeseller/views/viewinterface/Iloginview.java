package example.jni.com.coffeeseller.views.viewinterface;

/**
 * Created by Administrator on 2018/3/20.
 */

public interface Iloginview {
    String getUserName();

    String getPassword();

    void showLoading();

    void hideLoading();

    void toSettingFragment();

    void showFailedError(String err);
}
