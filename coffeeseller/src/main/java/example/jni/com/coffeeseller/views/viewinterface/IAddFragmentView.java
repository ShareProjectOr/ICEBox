package example.jni.com.coffeeseller.views.viewinterface;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import example.jni.com.coffeeseller.views.fragments.BasicFragment;

/**
 * Created by Administrator on 2018/3/2.
 */

public interface IAddFragmentView {
    AppCompatActivity getActivity();

    BasicFragment getFragment();

    int getLayoutId();
}
