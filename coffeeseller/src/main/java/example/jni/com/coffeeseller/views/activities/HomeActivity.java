package example.jni.com.coffeeseller.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.views.fragments.BaseFragment;
import example.jni.com.coffeeseller.views.fragments.BuyFragment;

/**
 * Created by Administrator on 2018/3/20.
 */

public class HomeActivity extends AppCompatActivity {
    private static HomeActivity mInstance;
    private FragmentManager mManager;
    public BaseFragment mCurFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        turnFragment(mCurFragment);
    }

    public static HomeActivity getInstance() {

        return mInstance;
    }

    private void init() {
        initViews();
        initDatas();
    }

    private void initDatas() {
        if (mCurFragment == null) {
            mCurFragment = new BuyFragment();
        }
    }

    private void initViews() {
        mInstance = this;
        mManager = getSupportFragmentManager();

    }

    public void turnFragment(BaseFragment fragment) {
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
    }
}
