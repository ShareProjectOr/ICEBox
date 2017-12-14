package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeAccountDetailAdapter;
import com.example.shareiceboxms.models.adapters.TradeRecordDetailAdapter;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/11/27.
 */

public class TradeAccountDetailFragment extends BaseFragment {
    private View containerView;

    private android.support.design.widget.TabLayout accountTabLayout;
    private com.example.shareiceboxms.models.widget.ListViewForScrollView accountList;

    private TextView jiesuanTime, jiesuanMoney, jiesuanState, jiesuanTimePeriod;

    private ImageView drawerIcon, saoma;
    private TextView title;

    private TextView chongdiRefundMoney, realMoney;


    private List<ItemTradeRecord> itemBuyProducts, itemRefundProducts;
    TradeAccountDetailAdapter adapter;
    HomeActivity homeActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_account_detail));
            initViews();
            initDatas();
        }
        Log.d("------DetailFragment-", FragmentFactory.getInstance().getSavedBundle().getString("machineCode"));
        return containerView;
    }

    private void initViews() {

        accountTabLayout = (android.support.design.widget.TabLayout) containerView.findViewById(R.id.accountTabLayout);
        accountList = (com.example.shareiceboxms.models.widget.ListViewForScrollView) containerView.findViewById(R.id.accountList);


        jiesuanTime = (TextView) containerView.findViewById(R.id.jiesuanTime);
        jiesuanMoney = (TextView) containerView.findViewById(R.id.jiesuanMoney);
        jiesuanState = (TextView) containerView.findViewById(R.id.jiesuanState);
        jiesuanTimePeriod = (TextView) containerView.findViewById(R.id.jiesuanTimePeriod);


        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        title = (TextView) containerView.findViewById(R.id.title);
        title.setText("结算工单详情");

        chongdiRefundMoney = (TextView) containerView.findViewById(R.id.chongdiRefundMoney);
        realMoney = (TextView) containerView.findViewById(R.id.realMoney);


        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        itemBuyProducts = new ArrayList<>();
        itemRefundProducts = new ArrayList<>();

        itemRefundProducts.add(new ItemTradeRecord());
        itemRefundProducts.add(new ItemTradeRecord());
        itemRefundProducts.add(new ItemTradeRecord());
        itemRefundProducts.add(new ItemTradeRecord());
        itemRefundProducts.add(new ItemTradeRecord());
        itemRefundProducts.add(new ItemTradeRecord());
        itemRefundProducts.add(new ItemTradeRecord());


        itemBuyProducts.add(new ItemTradeRecord());
        itemBuyProducts.add(new ItemTradeRecord());
        itemBuyProducts.add(new ItemTradeRecord());
        itemBuyProducts.add(new ItemTradeRecord());


        accountTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < Constants.TradeRecordDetailTitle.length; i++) {
            TabLayout.Tab tab = accountTabLayout.newTab();
            //设置拉取到的数据在标题中
            tab.setText(Constants.TradeAccountDetailTitle[i]);
            accountTabLayout.addTab(tab);
        }
        adapter = new TradeAccountDetailAdapter(getContext(), this);
        adapter.setItemAccountList(itemBuyProducts);
        accountList.setAdapter(adapter);
        accountTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    adapter.setItemAccountList(itemBuyProducts);
                } else {
                    adapter.setItemAccountList(itemRefundProducts);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void addFrameFragment() {
        if (BaseFragment.curFragment != null && BaseFragment.curFragment instanceof TradeFragment) {
            TradeFragment tradeFragment = (TradeFragment) BaseFragment.curFragment;
            tradeFragment.addFrameLayout(new TradeRecordDetailFragment());
            BaseFragment.tradeAccountDetailFragment = this;
        }

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
