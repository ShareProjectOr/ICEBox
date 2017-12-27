package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.ViewPagerAdapter;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by WH on 2017/11/27.
 */

public class TradeFragment extends BaseFragment implements HomeActivity.OnBackPressListener {
    private View containerView = null;
    HomeActivity homeActivity;
    private TabLayout tabLayout;
    private ImageView drawerIcon, saoma;
    private ViewPager viewPager;
    private FrameLayout tradeDetailLayout;
    ViewPagerAdapter adapter;
    BaseFragment curFrameFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {

        tabLayout = (TabLayout) containerView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) containerView.findViewById(R.id.viewpager);
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        tradeDetailLayout = (FrameLayout) containerView.findViewById(R.id.detailFrameLayout);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentFactory.getInstance().getTradeChildFragments(), Constants.TradeTabTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        homeActivity.setOnBackPressListener(this);
    }

    public void addFrameLayout(BaseFragment fragment) {
        curFrameFragment = fragment;
        super.addFrameLayout(fragment, R.id.detailFrameLayout);
        tradeDetailLayout.setVisibility(View.VISIBLE);
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

    @Override
    public void OnBackDown() {
        if (BaseFragment.tradeAccountDetailFragment != null) {
            removeFrame(BaseFragment.tradeAccountDetailFragment);
            //绑定查询ID；
            addFrameLayout(new TradeAccountDetailFragment());
            BaseFragment.tradeAccountDetailFragment = null;
            return;
        }
        if (curFrameFragment != null && curFrameFragment.isAdded()) {
            removeFrame(curFrameFragment);
            tradeDetailLayout.setVisibility(View.GONE);
            curFrameFragment = null;
        } else {
            homeActivity.finishActivity();
        }

    }
}
