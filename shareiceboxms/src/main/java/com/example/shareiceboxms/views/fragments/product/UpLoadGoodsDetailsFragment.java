package com.example.shareiceboxms.views.fragments.product;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.UpLoadDetailsGoodListAdapter;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.widget.ListViewForScrollView;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpLoadGoodsDetailsFragment extends BaseFragment {
    private View contentView;
    private String upLoadGoodsId;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ImageView mDrawerIcon;
    private ImageView mSaoma;
    private ListViewForScrollView mUpLoadDetailsGoodsList;
    private UpLoadDetailsGoodListAdapter mAdapter;
    private HomeActivity homeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_up_load_goods));
            upLoadGoodsId = FragmentFactory.getInstance().getSavedBundle().getString("upLoadGoodsId");
            initview();
            initdata();
            iniListener();
        }
        return contentView;
    }

    private void iniListener() {
        mDrawerIcon.setOnClickListener(this);
        mSaoma.setOnClickListener(this);
    }

    private void initdata() {
    }

    private void initview() {
        mAppBarLayout = (AppBarLayout) contentView.findViewById(R.id.appBarLayout);
        mToolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        mDrawerIcon = (ImageView) contentView.findViewById(R.id.drawerIcon);
        mSaoma = (ImageView) contentView.findViewById(R.id.saoma);
        mAdapter = new UpLoadDetailsGoodListAdapter(getActivity());
        homeActivity = (HomeActivity) getActivity();
        mUpLoadDetailsGoodsList = (ListViewForScrollView) contentView.findViewById(R.id.upLoadDetailsGoodsList);
        mUpLoadDetailsGoodsList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saoma:
                homeActivity.jumpActivity(null, null);
                break;
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
        }
    }
}
