package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/11/27.
 */

public class TradeRecordDetailFragment extends BaseFragment {
    private View containerView;
    private android.support.design.widget.TabLayout productTabLayout;
    private CheckBox allProductCB;
    private com.example.shareiceboxms.models.widget.ListViewForScrollView productList;

    private TextView tradeTime, totalMoney, payState, machineNameAddr, tradeNo, customerId;
    private ImageView payIcon;
    private TextView jiesuanMoney, jiesuanReal, jiesuanRefund;

    private List<ItemProduct> itemBuyProducts, itemRefundProducts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_record_detail));
            initViews();
            initDatas();
        }
        Log.d("------DetailFragment-", FragmentFactory.getInstance().getSavedBundle().getString("machineCode"));
        return containerView;
    }

    private void initViews() {
        productTabLayout = (android.support.design.widget.TabLayout) containerView.findViewById(R.id.productTabLayout);
        allProductCB = (CheckBox) containerView.findViewById(R.id.allProductCB);
        productList = (com.example.shareiceboxms.models.widget.ListViewForScrollView) containerView.findViewById(R.id.productList);

        tradeTime = (TextView) containerView.findViewById(R.id.tradeTime);
        totalMoney = (TextView) containerView.findViewById(R.id.totalMoney);
        payIcon = (ImageView) containerView.findViewById(R.id.payIcon);
        payState = (TextView) containerView.findViewById(R.id.payState);
        machineNameAddr = (TextView) containerView.findViewById(R.id.machineNameAddr);
        tradeNo = (TextView) containerView.findViewById(R.id.tradeNo);
        customerId = (TextView) containerView.findViewById(R.id.customerId);

        jiesuanMoney = (TextView) containerView.findViewById(R.id.jiesuanMoney);
        jiesuanReal = (TextView) containerView.findViewById(R.id.jiesuanReal);
        jiesuanRefund = (TextView) containerView.findViewById(R.id.jiesuanRefund);

    }

    private void initDatas() {
        itemBuyProducts = new ArrayList<>();
        itemRefundProducts = new ArrayList<>();
        for (int i = 0; i < Constants.TradeRecordDetailTitle.length; i++) {
            TabLayout.Tab tab = productTabLayout.newTab();
            tab.setText(Constants.TabTitles[i]);
            productTabLayout.addTab(tab);
        }
        productTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                } else {

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

}
