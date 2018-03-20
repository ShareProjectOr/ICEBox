package example.jni.com.coffeemanagersystem.presenter;

import android.os.Handler;

import example.jni.com.coffeemanagersystem.bean.User;
import example.jni.com.coffeemanagersystem.model.IUserLogin;
import example.jni.com.coffeemanagersystem.model.OnLoginCallBackListener;
import example.jni.com.coffeemanagersystem.model.UserLogin;
import example.jni.com.coffeemanagersystem.views.viewinterface.ILoginview;

/**
 * Created by Administrator on 2018/3/1.
 */

public class UserLoginPresenter {
    private IUserLogin iUserLogin;
    private ILoginview iLoginview;
  //  private Handler mHandler = new Handler();

    public UserLoginPresenter(ILoginview iLoginview) {
        this.iUserLogin = new UserLogin();
        this.iLoginview = iLoginview;
    }

    public void PresenterLogin() {
        iLoginview.showLoading();
        iUserLogin.login(iLoginview.getUserName(), iLoginview.getPassword(), new OnLoginCallBackListener() {
            @Override
            public void loginSuccess(final User user) {
                iLoginview.hideLoading();
                iLoginview.toMainActivity(user);
             /*   mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iLoginview.hideLoading();
                        iLoginview.toMainActivity(user);
                    }
                });
*/
            }

            @Override
            public void loginFailed(final String erro) {
                iLoginview.hideLoading();
                iLoginview.showFailedError(erro);
              /*  mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iLoginview.hideLoading();
                        iLoginview.showFailedError(erro);
                    }
                });
*/
            }
        });
    }
}
