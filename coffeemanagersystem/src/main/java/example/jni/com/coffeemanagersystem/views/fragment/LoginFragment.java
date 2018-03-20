package example.jni.com.coffeemanagersystem.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import example.jni.com.coffeemanagersystem.R;
import example.jni.com.coffeemanagersystem.bean.User;
import example.jni.com.coffeemanagersystem.model.ISetAndGetObservable;
import example.jni.com.coffeemanagersystem.presenter.UserLoginPresenter;
import example.jni.com.coffeemanagersystem.views.activity.LoginActivity;
import example.jni.com.coffeemanagersystem.views.viewinterface.ILoginview;
import rx.Observable;
import rx.Subscriber;


public class LoginFragment extends Fragment implements ILoginview, View.OnClickListener, ISetAndGetObservable {
    private View view;
    private EditText username, password;
    private ProgressBar mProgressBar;
    private UserLoginPresenter mUserLoginPresenter = new UserLoginPresenter(this);
    private LoginActivity activity;
    private Subscriber<String> subscriber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.login_fragment_layout, container, false);
            initview();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initview() {
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        mProgressBar = view.findViewById(R.id.loading);
        Button login = view.findViewById(R.id.login);
        login.setOnClickListener(this);
        activity = (LoginActivity) getActivity();
        activity.setOnISetAndGetObservable(this);
    }

    @Override
    public String getUserName() {
        return username.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void toMainActivity(User user) {

    }

    @Override
    public void showFailedError(String err) {
        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                mUserLoginPresenter.PresenterLogin();
                break;
        }
    }


    @Override
    public void getObservable(Observable<String> observable) {
        subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            }
        };
        observable.subscribe(subscriber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriber != null) {
            subscriber.unsubscribe();
        }

    }
}
