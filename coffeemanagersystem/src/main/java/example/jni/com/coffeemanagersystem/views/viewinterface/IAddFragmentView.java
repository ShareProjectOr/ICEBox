package example.jni.com.coffeemanagersystem.views.viewinterface;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import rx.Observable;

/**
 * Created by Administrator on 2018/3/2.
 */

public interface IAddFragmentView {
    AppCompatActivity getActivity();

    Fragment getFragment();

    int getLayoutId();
}
