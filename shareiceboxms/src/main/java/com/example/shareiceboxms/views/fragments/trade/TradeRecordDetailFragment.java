package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by WH on 2017/11/27.
 */

public class TradeRecordDetailFragment extends BaseFragment {
    private View containerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.nav_header_home));
        Log.d("------DetailFragment-", FragmentFactory.getInstance().getSavedBundle().getString("machineCode"));
        return containerView;
    }

}
