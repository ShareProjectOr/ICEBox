package example.jni.com.coffeemanagersystem.model;

import example.jni.com.coffeemanagersystem.bean.User;

/**
 * Created by Administrator on 2018/3/1.
 */

public class UserLogin implements IUserLogin {

    @Override
    public void login(String username, String password, OnLoginListener loginListener) {
        Thread thread = new Thread(new Login(username, password, loginListener));
        thread.start();
    }

    private class Login implements Runnable {
        private String username, password;
        private OnLoginListener loginListener;

        public Login(String username, String password, OnLoginListener loginListener) {
            this.username = username;
            this.password = password;
            this.loginListener = loginListener;
        }

        @Override
        public void run() {
            try {
                //do login
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (true){
                //成功验证
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                loginListener.loginSuccess(user);
            }else {
                //failed
                loginListener.loginFailed("");
            }
        }
    }
}
