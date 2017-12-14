package com.example.shareiceboxms.views.fragments.machine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MachineItemAddView;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/11/27.
 */

public class MachineDetailFragment extends BaseFragment{
    private View containerView;
    private ScrollView scrollLayout;
    private android.support.design.widget.TabLayout machineTabLayout;
    private LinearLayout itemLayout;

    private ImageView drawerIcon, saoma;
    private TextView title;

    private TextView machineAddr, machineName, isOnLine, isException, managerName, machienCode;
    HomeActivity homeActivity;
    MachineItemAddView machineItemAddView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_machine_detail));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        scrollLayout = (ScrollView) containerView.findViewById(R.id.scrollLayout);
        machineTabLayout = (android.support.design.widget.TabLayout) containerView.findViewById(R.id.machineTabLayout);
        itemLayout = (LinearLayout) containerView.findViewById(R.id.itemLayout);
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        title = (TextView) containerView.findViewById(R.id.title);
        title = (TextView) containerView.findViewById(R.id.title);

        machineAddr = (TextView) containerView.findViewById(R.id.machineAddr);
        machineName = (TextView) containerView.findViewById(R.id.machineName);
        isOnLine = (TextView) containerView.findViewById(R.id.isOnLine);
        isException = (TextView) containerView.findViewById(R.id.isException);
        managerName = (TextView) containerView.findViewById(R.id.managerName);
        machienCode = (TextView) containerView.findViewById(R.id.machienCode);

        title.setText("机器详情");
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
        machineTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        machineItemAddView.addStateControlView(itemLayout);
                        break;
                    case 1:
                        machineItemAddView.addTeleControlView(itemLayout);
                        break;
                    case 2:
                        List<ItemProduct> itemProducts = new ArrayList<ItemProduct>();
                        itemProducts.add(new ItemProduct());
                        itemProducts.add(new ItemProduct());
                        itemProducts.add(new ItemProduct());
                        itemProducts.add(new ItemProduct());
                        itemProducts.add(new ItemProduct());
                        itemProducts.add(new ItemProduct());
                        itemProducts.add(new ItemProduct());
                        itemProducts.add(new ItemProduct());
                        itemProducts.add(new ItemProduct());
                        machineItemAddView.addStockProductView(itemLayout, itemProducts);
                        break;
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

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        machineItemAddView = new MachineItemAddView(homeActivity);
        for (int i = 0; i < Constants.MachineItemOperator.length; i++) {
            machineTabLayout.addTab(machineTabLayout.newTab().setText(Constants.MachineItemOperator[i]));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
        }
    }
}
