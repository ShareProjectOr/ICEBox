package example.jni.com.coffeeseller.model.listeners;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import example.jni.com.coffeeseller.views.fragments.BasicFragment;

/**
 * Created by Administrator on 2018/3/2.
 */

public interface IAddFragment {
    void addFragment(AppCompatActivity activity, BasicFragment fragment, int Layout);

    void removeFragment(AppCompatActivity activity, BasicFragment fragment);

    void replaceFragment(AppCompatActivity activity, BasicFragment fragment, int Layout);
}
