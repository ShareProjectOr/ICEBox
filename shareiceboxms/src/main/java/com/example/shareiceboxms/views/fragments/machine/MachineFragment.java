package com.example.shareiceboxms.views.fragments.machine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.shareiceboxms.R;

import com.example.shareiceboxms.models.adapters.ViewPagerAdapter;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by WH on 2017/11/27.
 */

public class MachineFragment extends BaseFragment implements HomeActivity.OnBackPressListener {
    private View containerView;
    BaseFragment curFrameFragment;
    private FrameLayout tradeDetailLayout;
    HomeActivity homeActivity;
    private ImageView drawerIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_machine));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        homeActivity.setOnBackPressListener(this);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.clickIconToOpenDrawer();
            }
        });
    }

    private void initViews() {
        tradeDetailLayout = (FrameLayout) containerView.findViewById(R.id.detailFrameLayout);
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
    }

    public void addFrameLayout(BaseFragment fragment) {
        curFrameFragment = fragment;
        super.addFrameLayout(fragment, R.id.detailFrameLayout);
        tradeDetailLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnBackDown() {
        if (curFrameFragment != null && curFrameFragment.isAdded()) {
            removeFrame(curFrameFragment);
            tradeDetailLayout.setVisibility(View.GONE);
            curFrameFragment = null;
        } else {
            homeActivity.finishActivity();
        }

    }
}
