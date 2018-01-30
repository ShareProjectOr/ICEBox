package com.example.shareiceboxms.models.factories;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.machine.MachineFragment;
import com.example.shareiceboxms.views.fragments.exception.ExceptionFragment;
import com.example.shareiceboxms.views.fragments.product.ProductFragment;
import com.example.shareiceboxms.views.fragments.product.ProductTypeListFragment;
import com.example.shareiceboxms.views.fragments.product.UpLoadGoodsRecordFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordsFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeTotalFragment;

/**
 * Created by WH on 2017/11/27.
 */

public class FragmentFactory {
    public static FragmentFactory mInstance;
    public static Bundle mSavedInstanceState;

    public static synchronized FragmentFactory getInstance() {
        if (mInstance == null) {
            mInstance = new FragmentFactory();
            if (mSavedInstanceState == null) {
                mSavedInstanceState = new Bundle();
            }
        }
        return mInstance;
    }

    public Bundle putLayoutId(int layoutId) {
        mSavedInstanceState.putInt("layout_id", layoutId);
        return mSavedInstanceState;
    }

    public Bundle getSavedBundle() {
        return mSavedInstanceState;
    }

    public List<BaseFragment> getTradeChildFragments() {
        List<BaseFragment> mFragments = new ArrayList<>();
        mFragments.add(new TradeTotalFragment());
        mFragments.add(new TradeRecordsFragment());

        switch (PerSonMessage.userType) {
            case Constants.SUPER_MANAGER://'系统管理员,运营商'
                mFragments.add(new TradeAccountFragment());
                break;
            case Constants.SYSTEM_MANAGER:
                mFragments.add(new TradeAccountFragment());
                break;
            case Constants.AGENT_MANAGER://'代理商'
                mFragments.add(new TradeAccountFragment());
                break;
            case Constants.MACHINE_MANAGER://'机器管理员'
                break;
            default:
                break;
        }


        return mFragments;
    }

    public List<BaseFragment> getProductChildFragments() {
        List<BaseFragment> mFragments = new ArrayList<>();
        String userRole = PerSonMessage.userType + "0";
        if (PerSonMessage.userType == 2) {//能看品类
            mFragments.add(new ProductTypeListFragment());
        } else if (PerSonMessage.userType == 3 && PerSonMessage.role.equals("0")) {
            mFragments.add(new ProductTypeListFragment());
        }

        mFragments.add(new UpLoadGoodsRecordFragment());
        return mFragments;
    }

    public Class<?>[] getClassFragment() {
        return new Class<?>[]{
                TradeFragment.class, MachineFragment.class, ExceptionFragment.class, ProductFragment.class
        };
    }
}
