package com.example.shareiceboxms.models.factories;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.machine.MachineFragment;
import com.example.shareiceboxms.views.fragments.exception.ExceptionFragment;
import com.example.shareiceboxms.views.fragments.product.ProductFragment;
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
        mFragments.add(new TradeAccountFragment());
        return mFragments;
    }

    public Class<?>[] getClassFragment() {
        return new Class<?>[]{
                TradeFragment.class, MachineFragment.class, ExceptionFragment.class, ProductFragment.class
        };
    }
}
