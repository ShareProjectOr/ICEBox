package com.example.shareiceboxms.views.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;

/**
 * Created by Administrator on 2017/12/12.
 */

public class AboutFragment extends BaseFragment {
    private View contentView;
    private TextView version;
    private ImageView drawerIcon, saoma;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.about_fragment));
            bindViews();
            initdata();
            initListener();
        }
        return contentView;
    }

    public String getVersion() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void initdata() {
        version.setText("当前版本:\t\t" + getVersion());
    }

    private void initListener() {
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
    }

    private void bindViews() {
        homeActivity = (HomeActivity) getActivity();
        version = (TextView) contentView.findViewById(R.id.version);
        drawerIcon = (ImageView) contentView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) contentView.findViewById(R.id.saoma);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
        }
    }
}
