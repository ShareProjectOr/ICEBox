package example.jni.com.coffeemanagersystem.model;

import java.io.IOException;

import example.jni.com.coffeemanagersystem.bean.User;
import example.jni.com.coffeemanagersystem.httputils.OkHttpUtil;

/**
 * Created by Administrator on 2018/3/1.
 */

public class UserLogin implements IUserLogin {

    @Override
    public void login(String username, String password, OnLoginCallBackListener loginCallBackListener) {
        Thread thread = new Thread(new Login(username, password, loginCallBackListener));
        thread.start();
    }

    private class Login implements Runnable {
        private String username, password, response;
        private OnLoginCallBackListener callBackListener;

        Login(String username, String password, OnLoginCallBackListener loginCallBackListener) {
            this.username = username;
            this.password = password;
            this.callBackListener = loginCallBackListener;
        }

        @Override
        public void run() {
            try {
                //do login
                response = OkHttpUtil.post("", "{}");
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (true) {
                //成功验证
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                callBackListener.loginSuccess(user);
            } else {
                //failed
                callBackListener.loginFailed(response);
            }
        }
    }

}
