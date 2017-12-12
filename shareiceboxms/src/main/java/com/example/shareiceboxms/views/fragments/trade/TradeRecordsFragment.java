package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeRecordListAdapter;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/11/27.
 * 交易记录
 */

public class TradeRecordsFragment extends BaseFragment {
    private View containerView;
    private ImageView tradeSearch, tradeTypeIcon;
    private TextView tradeNo, tradeTypeText;
    private RelativeLayout tradeType, selectTime;
    private android.support.v4.widget.SwipeRefreshLayout recordRefresh;
    private android.support.v7.widget.RecyclerView tradeRecordList;
    private boolean isTypeClicked = false;
    private boolean isLoading = false;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值 
    private int visibleThreshold = 5;
    private TradeRecordListAdapter adapter;
    private List<ItemTradeRecord> itemTradeRecords;
    private LoadMoreListener loadMoreListener;

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
        tradeSearch = (ImageView) containerView.findViewById(R.id.tradeSearch);
        tradeNo = (TextView) containerView.findViewById(R.id.tradeNo);
        tradeType = (RelativeLayout) containerView.findViewById(R.id.tradeType);
        selectTime = (RelativeLayout) containerView.findViewById(R.id.selectTime);
        tradeTypeText = (TextView) containerView.findViewById(R.id.tradeTypeText);
        tradeTypeIcon = (ImageView) containerView.findViewById(R.id.tradeTypeIcon);
        recordRefresh = (android.support.v4.widget.SwipeRefreshLayout) containerView.findViewById(R.id.recordRefresh);
        tradeRecordList = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.tradeRecordList);
        tradeType.setOnClickListener(this);
        recordRefresh.setOnRefreshListener(this);
        recordRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));

    }

    private void initDatas() {
        itemTradeRecords = new ArrayList<>();

        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(null);


        loadMoreListener = new LoadMoreListener() {
            @Override
            public void loadMore() {
                //拉取数据
//                itemTradeRecords.remove(itemTradeRecords.size() - 1);
                Toast.makeText(getContext(), "111'", Toast.LENGTH_SHORT).show();
                isLoading = false;
                adapter.notifyDataSetChanged();
            }
        };
        tradeRecordList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        tradeRecordList.setLayoutManager(mLayoutManager);
        adapter = new TradeRecordListAdapter(getContext(), itemTradeRecords);
        tradeRecordList.setAdapter(adapter);
        tradeRecordList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= lastVisibleItemPosition + visibleThreshold) {
                    loadMoreListener.loadMore();
                    isLoading = true;
                }
            }
        });

        //联网刷新数据
//        String dataJson = OkHttpUtil.post(url, params);
//        itemTradeTotal = JsonUtil.jsonToJavaBean(dataJson, itemTradeTotal);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tradeSearch:
                //模糊查询订单号
                break;
            case R.id.tradeType:
                isTypeClicked = !isTypeClicked;
                //弹出POPUPlistwindow
                Toast.makeText(getContext(), "111'", Toast.LENGTH_SHORT).show();
                tradeTypeIcon.setSelected(isTypeClicked);
                break;
            case R.id.selectTime:
                //弹出日期选择
                break;
        }
    }

    @Override
    public void onRefresh() {
        adapter.notifyDataSetChanged();
        recordRefresh.setRefreshing(false);
    }

    public interface LoadMoreListener {
        void loadMore();
    }
}
