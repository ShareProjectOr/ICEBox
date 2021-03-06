package com.example.shareiceboxms.views.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shareiceboxms.R;

import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.views.fragments.LoginFragment;
import com.example.shareiceboxms.views.fragments.WelcomeFragment;

import org.zackratos.ultimatebar.UltimateBar;

public class LoginActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        if (PerSonMessage.isexcit){
            login();
        }else {
            welcome();
        }

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
        ft.commit();
    }

    private void initViews() {

    }

    @Override
    public void jumpActivity(Class<?> activitycalss, Bundle intentData) {
        super.jumpActivity(activitycalss, intentData);
        finish();
    }
}
