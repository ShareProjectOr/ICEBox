package example.jni.com.coffeemanagersystem.views.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import example.jni.com.coffeemanagersystem.R;
import example.jni.com.coffeemanagersystem.model.ISetAndGetObservable;
import example.jni.com.coffeemanagersystem.presenter.AddFragmentPresenter;
import example.jni.com.coffeemanagersystem.views.fragment.LoginFragment;
import example.jni.com.coffeemanagersystem.views.viewinterface.IAddFragmentView;
import rx.Observable;
import rx.Subscriber;

public class LoginActivity extends AppCompatActivity implements IAddFragmentView {
    private AddFragmentPresenter fragmentPresenter = new AddFragmentPresenter(this);
    private Observable<String> observable;
    private ISetAndGetObservable mSetAndGetObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentPresenter.AddFragment();
        observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("获取到了登录的Fragment");
                subscriber.onCompleted();
            }
        });
    }

    public void setOnISetAndGetObservable(ISetAndGetObservable mSetAndGetObservable) {
        if (mSetAndGetObservable != null) {
            this.mSetAndGetObservable = mSetAndGetObservable;
        }
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public Fragment getFragment() {
        mSetAndGetObservable.getObservable(observable);
        return new LoginFragment();
    }

    @Override
    public int getLayoutId() {
        return R.id.realcontent;
    }


}
