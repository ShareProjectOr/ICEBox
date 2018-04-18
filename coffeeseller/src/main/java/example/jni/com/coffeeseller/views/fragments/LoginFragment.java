package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import example.jni.com.coffeeseller.R;

import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.login_fragment_layout));
        initview();
        return view;
    }

    private void initview() {

        UserName = (EditText) view.findViewById(R.id.userName);
        PassWord = (EditText) view.findViewById(R.id.passWord);
        login = (Button) view.findViewById(R.id.login);
        homeActivity = HomeActivity.getInstance();
        login.setOnClickListener(this);
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
      homeActivity.replaceFragment(FragmentEnum.LoginFragment,FragmentEnum.ConfigFragment);
    }

    @Override
    public void showFailedError(String err) {
        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                mUserLoginPresenter.PresenterLogin();
                break;
        }
    }
}
