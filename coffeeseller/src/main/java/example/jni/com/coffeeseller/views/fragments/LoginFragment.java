package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import example.jni.com.coffeeseller.R;

import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.presenter.UserLoginPresenter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.viewinterface.Iloginview;

/**
 * Created by Administrator on 2018/4/11.
 */

public class LoginFragment extends BasicFragment implements Iloginview {
    private View view;
    private UserLoginPresenter mUserLoginPresenter;
    private EditText UserName, PassWord;
    private Button login;
    private HomeActivity homeActivity;
    private TextView backToCheck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment_layout, null);
        initview();
        return view;
    }

    private void initview() {

        UserName = (EditText) view.findViewById(R.id.userName);
        PassWord = (EditText) view.findViewById(R.id.passWord);
        login = (Button) view.findViewById(R.id.login);
        backToCheck = (TextView) view.findViewById(R.id.backToCheck);
        homeActivity = HomeActivity.getInstance();
        login.setOnClickListener(this);
        backToCheck.setOnClickListener(this);
        mUserLoginPresenter = new UserLoginPresenter(getActivity(), this);
    }

    @Override
    public String getUserName() {
        return UserName.getText().toString();
    }

    @Override
    public String getPassword() {
        return PassWord.getText().toString();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toSettingFragment() {
        homeActivity.replaceFragment(FragmentEnum.LoginFragment, FragmentEnum.ConfigFragment);
    }

    @Override
    public void showFailedError(String err) {
        Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                mUserLoginPresenter.PresenterLogin();
                break;
            case R.id.backToCheck:
                homeActivity.replaceFragment(FragmentEnum.LoginFragment, FragmentEnum.MachineCheckFragment);
                break;
        }
    }
}
