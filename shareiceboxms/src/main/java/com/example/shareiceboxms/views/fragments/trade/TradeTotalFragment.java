package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeTotalListAdapter;
import com.example.shareiceboxms.models.beans.ItemTradeTotal;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by WH on 2017/11/27.
 * 交易统计
 */

public class TradeTotalFragment extends BaseFragment {
    private View containerView;
    private TextView timeSelector;
    private RadioGroup dateGroup;
    private RecyclerView tradeTotalList;
    private TradeTotalListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ItemTradeTotal itemTradeTotal;

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
        tradeTotalList = (RecyclerView) containerView.findViewById(R.id.tradeTotalList);
        refreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.refresh);
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
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
    }

    private void initDatas() {
        refreshLayout.setOnRefreshListener(this);
        tradeTotalList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        tradeTotalList.setLayoutManager(mLayoutManager);
        itemTradeTotal = new ItemTradeTotal();
        adapter = new TradeTotalListAdapter(getContext(), itemTradeTotal);
        tradeTotalList.setAdapter(adapter);
        //联网刷新数据
//        String dataJson = OkHttpUtil.post(url, params);
//        itemTradeTotal = JsonUtil.jsonToJavaBean(dataJson, itemTradeTotal);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);//关闭刷新
    }
}
