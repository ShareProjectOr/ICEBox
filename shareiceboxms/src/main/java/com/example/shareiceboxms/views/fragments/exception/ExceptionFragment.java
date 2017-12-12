package com.example.shareiceboxms.views.fragments.exception;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.example.shareiceboxms.R;

import com.example.shareiceboxms.models.adapters.ExceptionListAdapter;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.ActionItem;
import com.example.shareiceboxms.models.helpers.MenuPop;
import com.example.shareiceboxms.models.helpers.TitlePopup;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by WH on 2017/11/27.
 * Edit by LYU on 2017/12/11.
 */

public class ExceptionFragment extends BaseFragment implements HomeActivity.OnBackPressListener,CompoundButton.OnCheckedChangeListener {
    private View containerView;
    private BaseFragment curFrameFragment;
    private FrameLayout tradeDetailLayout;
    private HomeActivity homeActivity;
    private ImageView drawerIcon, showPop;
    private ExceptionListAdapter mRecycleAdapter;
    private RecyclerView exceptionList;
    private Context mContext;
    //  private TitlePopup mTilePopup;
    private TextView exceptionType;
    private ListPopupWindow mTilePopup;
    private Switch chooseIsDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_exception));
            initViews();
            initListener();
            initDatas();
        }
        return containerView;
    }

    private void initListener() {
        drawerIcon.setOnClickListener(this);
        showPop.setOnClickListener(this);
        chooseIsDetails.setOnCheckedChangeListener(this);
    }

    private void initDatas() {
        homeActivity = (HomeActivity) mContext;
        homeActivity.setOnBackPressListener(this);
        new MyViewFactory(mContext).BuildRecyclerViewRule(exceptionList, new LinearLayoutManager(mContext), new DefaultItemAnimator(), true).setAdapter(mRecycleAdapter);
    }

    private void initViews() {
        tradeDetailLayout = (FrameLayout) containerView.findViewById(R.id.detailFrameLayout);
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        exceptionList = (RecyclerView) containerView.findViewById(R.id.exception_list);
        mContext = getActivity();
        showPop = (ImageView) containerView.findViewById(R.id.showpup);
        chooseIsDetails = (Switch) containerView.findViewById(R.id.Is_details);
        mRecycleAdapter = new ExceptionListAdapter(mContext);
        exceptionType = (TextView) containerView.findViewById(R.id.exception_type);
        mTilePopup = MenuPop.CreateMenuPop(mContext, exceptionType, Constants.EXCEPTION_LV_TITLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
            case R.id.showpup:
                mTilePopup.show();
                break;
        }
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


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
           chooseIsDetails.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_switch_open));
            chooseIsDetails.setTrackDrawable(ContextCompat.getDrawable(mContext,R.drawable.shape_switch_open));
        }else {
            chooseIsDetails.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_switch));
            chooseIsDetails.setTrackDrawable(ContextCompat.getDrawable(mContext,R.drawable.shape_switch));

        }
    }
}
