package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by WH on 2017/11/27.
 * 交易统计
 */

public class TradeTotalFragment extends BaseFragment {
    private View containerView;
    private TextView timeSelector;
    private RadioGroup dateGroup;
    private ListView tradeTotalList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_total));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        timeSelector = (TextView) containerView.findViewById(R.id.timeSelector);
        tradeTotalList = (ListView) containerView.findViewById(R.id.tradeTotalList);
        dateGroup = (RadioGroup) containerView.findViewById(R.id.dateGroup);
        dateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.todayDate:
                        Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.weekDate:
                        Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.monthDate:
                        Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.yearDate:
                        Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        dateGroup.check(R.id.todayDate);
    }

    private void initDatas() {
    }

}
