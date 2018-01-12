package com.example.shareiceboxms.views.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.helpers.ProvenceAndCodeUtil;
import com.example.shareiceboxms.views.fragments.LoginFragment;
import com.example.shareiceboxms.views.fragments.WelcomeFragment;

public class LoginActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    //    welcome();

    }

    private void welcome() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.login_frame, new WelcomeFragment(), "WelcomeFragment");
        ft.commit();
    }

    public void login() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.login_frame, new LoginFragment(), "LoginFragment");
        /*
        * 出现这个问题：java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        * */
        ft.commitAllowingStateLoss();
    }

    private void initViews() {
        if (PerSonMessage.isexcit) {
            login();
        } else {
            welcome();
        }
    }

    /*
    * 解析地址
    * 如果直接再activity中使用，在启动时会出现空白，然后跳转到静态图片
    * 如果再fragment中使用，静态图片显示时间稍长，然后加载相应的fragment，但不会出现留白页面
    * */
    public void parseAddress() {
        ProvenceAndCodeUtil.getInstance(this);
    }

    @Override
    public void jumpActivity(Class<?> activitycalss, Bundle intentData) {
        super.jumpActivity(activitycalss, intentData);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
//        super.onBackPressed();
    }
}
