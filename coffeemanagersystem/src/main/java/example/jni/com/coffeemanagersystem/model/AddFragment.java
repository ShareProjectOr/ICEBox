package example.jni.com.coffeemanagersystem.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2018/3/2.
 */

public class AddFragment implements IAddFragment {
    @Override
    public void addFragment(AppCompatActivity activity, Fragment fragment, int Layout) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(Layout, fragment);
        ft.commit();
    }

    @Override
    public void removeFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
    }

    @Override
    public void replaceFragment(AppCompatActivity activity, Fragment fragment, int Layout) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(Layout, fragment);
    }

}
