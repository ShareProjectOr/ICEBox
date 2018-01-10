package com.example.shareiceboxms.presentors;

import android.view.View;

import com.example.shareiceboxms.models.helpers.LoginAnimHelper;

/**
 * Created by WH on 2017/12/6.
 */

public class LoginAnimPresentor {

    public static void loginAnim(View hideView, View showView,LoginLisenner loginLisenner) {
        LoginAnimHelper helper = new LoginAnimHelper();
        helper.setLoginLisenner(loginLisenner);
        helper.loginEditAnim(LoginAnimHelper.LOGIN_ANIM, hideView, showView);
    }

    public static void loginAnimReverse(View showView, View hideView) {
        LoginAnimHelper helper = new LoginAnimHelper();
        helper.loginEditAnim(LoginAnimHelper.LOGIN_ANIM_REVERSE, showView, hideView);
    }

    public interface LoginLisenner {
        void login();
    }
}
