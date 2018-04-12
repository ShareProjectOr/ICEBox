package example.jni.com.coffeeseller.presenter;

import example.jni.com.coffeeseller.model.listeners.IUserLogin;
import example.jni.com.coffeeseller.model.listeners.OnLoginCallBackListener;
import example.jni.com.coffeeseller.model.UserLogin;
import example.jni.com.coffeeseller.views.viewinterface.Iloginview;

/**
 * Created by Administrator on 2018/3/20.
 */

public class UserLoginPresenter {
    private IUserLogin iUserLogin;
    private Iloginview iLoginview;

    public UserLoginPresenter(Iloginview iLoginview) {
        this.iUserLogin = new UserLogin();
        this.iLoginview = iLoginview;
    }

    public void PresenterLogin() {
        iUserLogin.login(iLoginview.getUserName(), iLoginview.getPassword(), new OnLoginCallBackListener() {
            @Override
            public void loginSuccess() {
                iLoginview.toSettingFragment();
            }

            @Override
            public void loginFailed(String erro) {
                iLoginview.showFailedError(erro);
            }
        });
    }
}
