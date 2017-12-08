package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by WH on 2017/11/27.
 * 交易记录
 */

public class TradeRecordsFragment extends BaseFragment {
    private View containerView;
    private ImageView iconTabLayout;
    private String str = "fdsafds";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_records));
            initViews();
            initDatas();
        }

        return containerView;
    }

    private void initViews() {
        iconTabLayout = (ImageView) containerView.findViewById(R.id.iconTabLayout);
        iconTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeFragment tradeFragment = (TradeFragment) getParentFragment();
                FragmentFactory.getInstance().getSavedBundle().putString("machineCode", str);
                tradeFragment.addFrameLayout(new TradeRecordDetailFragment());
            }
        });
    }

    private void initDatas() {
    }

}
