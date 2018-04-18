package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.views.activities.HomeActivity;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ConfigFragment extends BasicFragment {
    private View mView;
    private Button toDebug;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.config_fragment_layout));
        initView();
        return mView;
    }

    private void initView() {
        toDebug = (Button) mView.findViewById(R.id.debug_machine);
        homeActivity = HomeActivity.getInstance();
        toDebug.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.debug_machine:
                homeActivity.replaceFragment(FragmentEnum.ConfigFragment, FragmentEnum.DebugFragment);
                break;
        }
    }
}

