package example.jni.com.coffeeseller.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import example.jni.com.coffeeseller.model.listeners.IAddFragment;
import example.jni.com.coffeeseller.views.fragments.BasicFragment;

/**
 * Created by Administrator on 2018/3/2.
 */

public class AddFragment implements IAddFragment {
    @Override
    public void addFragment(AppCompatActivity activity, BasicFragment fragment, int Layout) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(Layout, fragment);
        ft.commit();
    }

    @Override
    public void removeFragment(AppCompatActivity activity, BasicFragment fragment) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
    }

    @Override
    public void replaceFragment(AppCompatActivity activity, BasicFragment fragment, int Layout) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(Layout, fragment);
    }

}
