package example.jni.com.coffeeseller.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.presenter.AddFragmentPresenter;
import example.jni.com.coffeeseller.views.fragments.BasicFragment;
import example.jni.com.coffeeseller.views.viewinterface.IAddFragmentView;

import static example.jni.com.coffeeseller.factory.FragmentEnum.ChooseCupNumFragment;
import static example.jni.com.coffeeseller.factory.FragmentEnum.MachineCheckFragment;

/**
 * Created by Administrator on 2018/3/20.
 */

public class HomeActivity extends AppCompatActivity implements IAddFragmentView {
    private static HomeActivity mInstance;
    private AddFragmentPresenter mAddFragmentPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    public static synchronized HomeActivity getInstance() {
        return mInstance;
    }

    private void init() {
        initViews();
        initDatas();
    }

    private void initDatas() {

    }

    private void initViews() {
        mInstance = this;
        FragmentFactory.curPage = MachineCheckFragment;
        mAddFragmentPresenter = new AddFragmentPresenter(this);
        mAddFragmentPresenter.AddFragment();

    }


    public void replaceFragment(FragmentEnum currentPage, FragmentEnum toPage) {
        FragmentFactory.lastPage = currentPage;
        FragmentFactory.curPage = toPage;
        mAddFragmentPresenter.AddFragment();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public BasicFragment getFragment() {
        return FragmentFactory.getInstance().getFragment(FragmentFactory.curPage);
    }


    @Override
    public int getLayoutId() {
        return R.id.frag_container;
    }
}
