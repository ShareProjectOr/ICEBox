package example.jni.com.coffeemanagersystem.model;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2018/3/2.
 */

public interface IAddFragment {
    void addFragment(AppCompatActivity activity, Fragment fragment, int Layout);

    void removeFragment(AppCompatActivity activity, Fragment fragment);

    void replaceFragment(AppCompatActivity activity, Fragment fragment, int Layout);
}
