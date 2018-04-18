package example.jni.com.coffeeseller.presenter;

import example.jni.com.coffeeseller.views.viewinterface.OnToLoginPageListener;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ToLoginPagePresenter {
    private OnToLoginPageListener mOnToLoginPageListener;

    public ToLoginPagePresenter(OnToLoginPageListener mOnToLoginPageListener) {
        this.mOnToLoginPageListener = mOnToLoginPageListener;
    }

    public void ToLogin() {
        mOnToLoginPageListener.toLoginPage();
    }
}
