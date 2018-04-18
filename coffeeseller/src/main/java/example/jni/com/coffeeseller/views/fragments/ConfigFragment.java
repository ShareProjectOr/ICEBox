package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.factory.FragmentFactory;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ConfigFragment extends BasicFragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.config_fragment_layout));
        return mView;
    }
}

