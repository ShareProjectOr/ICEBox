package com.example.shareiceboxms.views.fragments.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

public class ProductFragment extends BaseFragment implements HomeActivity.OnBackPressListener {
    private View containerView = null;
    HomeActivity homeActivity;
    private TabLayout tabLayout;
    private ImageView drawerIcon;
    private ViewPager viewPager;
    private FrameLayout tradeDetailLayout;
    ViewPagerAdapter adapter;
    BaseFragment curFrameFragment;
    //    private CurrentPageListener mCurrentPageListener;
    private AppBarLayout titleBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_product));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        titleBar = (AppBarLayout) containerView.findViewById(R.id.appBarLayout);
        tabLayout = (TabLayout) containerView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) containerView.findViewById(R.id.viewpager);
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        tradeDetailLayout = (FrameLayout) containerView.findViewById(R.id.detailFrameLayout);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentFactory.getInstance().getProductChildFragments(), Constants.PRODUCT_VIEWPAGER_TITLE);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        homeActivity.setOnBackPressListener(this);
        drawerIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
        }
    }

    public void addFrameLayout(BaseFragment fragment) {
        curFrameFragment = fragment;
        super.addFrameLayout(fragment, R.id.detailFrameLayout);
        titleBar.setVisibility(View.GONE);
        tradeDetailLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnBackDown() {
        if (curFrameFragment != null && curFrameFragment.isAdded()) {
            removeFrame(curFrameFragment);
            tradeDetailLayout.setVisibility(View.GONE);
            titleBar.setVisibility(View.VISIBLE);
            curFrameFragment = null;
        } else {
            homeActivity.finishActivity();
            //  mCurrentPageListener.ListPage();
        }

    }

 /*   public void setCurrentPageListener(CurrentPageListener mCurrentPageListener) {
        if (mCurrentPageListener != null) {
            this.mCurrentPageListener = mCurrentPageListener;
        }
    }

    public interface CurrentPageListener {
        void ListPage();

        void DetailsPage();
    }*/
}
