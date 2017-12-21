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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeRecordDetailAdapter;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;
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

    private TextView tradeTime, totalMoney, payState, machineNameAddr, customerId, tradeNo;
    private ImageView payIcon;
    private TextView jiesuanMoney, jiesuanReal, jiesuanRefund;

    private TextView title;
    private ImageView drawerIcon, saoma;

    private TextView chooseItemCount, chooseItemMoney, moreRefund;


    private List<ItemProduct> itemBuyProducts, itemRefundProducts;
    TradeRecordDetailAdapter adapter;
    HomeActivity homeActivity;


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

        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        title = (TextView) containerView.findViewById(R.id.title);

        chooseItemCount = (TextView) containerView.findViewById(R.id.chooseItemCount);
        chooseItemMoney = (TextView) containerView.findViewById(R.id.chooseItemMoney);
        moreRefund = (TextView) containerView.findViewById(R.id.moreRefund);


        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
        moreRefund.setOnClickListener(this);

        allProductCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && adapter.getTotalCheckedCount() != adapter.getCount()) {
                    return;
                }
                adapter.checkAllItem(isChecked);
                changeCheckedCount();
            }
        });
        title.setText("交易详情");

    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        itemBuyProducts = new ArrayList<>();
        itemRefundProducts = new ArrayList<>();

        itemRefundProducts.add(new ItemProduct());
        itemRefundProducts.add(new ItemProduct());
        itemRefundProducts.add(new ItemProduct());
        itemRefundProducts.add(new ItemProduct());
        itemRefundProducts.add(new ItemProduct());
        itemRefundProducts.add(new ItemProduct());
        itemRefundProducts.add(new ItemProduct());


        itemBuyProducts.add(new ItemProduct());
        itemBuyProducts.add(new ItemProduct());
        itemBuyProducts.add(new ItemProduct());
        itemBuyProducts.add(new ItemProduct());


        productTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < Constants.TradeRecordDetailTitle.length; i++) {
            TabLayout.Tab tab = productTabLayout.newTab();
            //设置拉取到的数据在标题中
            tab.setText(Constants.TradeRecordDetailTitle[i]);
            productTabLayout.addTab(tab);
        }
        adapter = new TradeRecordDetailAdapter(getContext(), this);
        adapter.setItemProductList(itemBuyProducts);
        productList.setAdapter(adapter);
        productTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    adapter.setItemProductList(itemBuyProducts);
                } else {
                    adapter.setItemProductList(itemRefundProducts);
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

    /*
* 修改选中数量和价格
* */
    public void changeCheckedCount() {
        allProductCB.setChecked(adapter.isAllChecked());
        chooseItemCount.setText(adapter.getTotalCheckedCount() + "");
        chooseItemMoney.setText(adapter.getTotalCheckedMoney() + "");
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
            case R.id.moreRefund:
                //请求网络退款操作
                break;
        }
    }
}
