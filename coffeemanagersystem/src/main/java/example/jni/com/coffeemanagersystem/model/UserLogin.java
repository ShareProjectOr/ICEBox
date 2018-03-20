package example.jni.com.coffeemanagersystem.model;

import java.io.IOException;

import example.jni.com.coffeemanagersystem.bean.User;
import example.jni.com.coffeemanagersystem.httputils.OkHttpUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserLogin implements IUserLogin {

    @Override
    public void login(final String username, final String password, final OnLoginCallBackListener loginCallBackListener) {
      /*  Thread thread = new Thread(new Login(username, password, loginCallBackListener));
        thread.start();*/
        rx.Observable<String> observable = rx.Observable.create(new rx.Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(1000);
                    String response = OkHttpUtil.post("", "{}");
                    subscriber.onNext(response);
                } catch (IOException e) {
                    subscriber.onError(e);
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();

            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loginCallBackListener.loginFailed(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                if (true) {
                    //成功验证
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    loginCallBackListener.loginSuccess(user);
                    unsubscribe();  //完成登录之后取消订阅,释放内存
                } else {
                    //failed
                    loginCallBackListener.loginFailed(s);
                }
            }
        };
        observable.subscribe(subscriber);
    }

/*
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
    }*/

}
