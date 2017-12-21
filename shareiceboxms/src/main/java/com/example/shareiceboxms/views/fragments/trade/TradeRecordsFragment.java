package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeRecordListAdapter;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.models.helpers.MenuPop;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by WH on 2017/11/27.
 * 交易记录
 */

public class TradeRecordsFragment extends BaseFragment implements LoadMoreHelper.LoadMoreListener {
    private View containerView;
    private ImageView tradeSearch, tradeTypeIcon;
    private TextView tradeNo, tradeTypeText;
    private RelativeLayout tradeType, selectTime;
    private android.support.v4.widget.SwipeRefreshLayout recordRefresh;
    private android.support.v7.widget.RecyclerView tradeRecordList;
    private boolean isTypeClicked = false;
    private TradeRecordListAdapter adapter;
    private List<ItemTradeRecord> itemTradeRecords;
    private LoadMoreHelper loadMoreHelper;
    private DoubleDatePickerDialog datePickerDialog;
    private ListPopupWindow mTilePopup;

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
        recordRefresh = (SwipeRefreshLayout) containerView.findViewById(R.id.recordRefresh);
//        tradeRecordList = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.tradeRecordList);
        tradeType.setOnClickListener(this);
        recordRefresh.setOnRefreshListener(this);
        tradeSearch.setOnClickListener(this);
        selectTime.setOnClickListener(this);
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

        datePickerDialog = new DoubleDatePickerDialog(getContext(), 0, this
                , Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE), true);
        mTilePopup = MenuPop.CreateMenuPop(getContext(), tradeType, Constants.TradeStateTitle);

        RecyclerView tradeRecordList = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.tradeRecordList);
        TradeRecordListAdapter adapter = new TradeRecordListAdapter(getContext(), itemTradeRecords, this);
        new MyViewFactory(getContext()).BuildRecyclerViewRule(tradeRecordList,
                new LinearLayoutManager(getContext()), null, true).setAdapter(adapter);
        loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(adapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(tradeRecordList)
                .setVisibleThreshold(1);

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
                mTilePopup.show();
                tradeTypeIcon.setSelected(isTypeClicked);
                break;
            case R.id.selectTime:
                //弹出日期选择
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onRefresh() {
        //拉取数据
        //联网刷新数据
//        String dataJson = OkHttpUtil.post(url, params);
//        itemTradeTotal = JsonUtil.jsonToJavaBean(dataJson, itemTradeTotal);
//        adapter.notifyDataSetChanged();
        itemTradeRecords.clear();

        loadMoreHelper.getAdapter().notifyDataSetChanged();
        recordRefresh.setRefreshing(false);
    }

    public void addFrameFragment() {
        TradeFragment tradeFragment = (TradeFragment) getParentFragment();
        tradeFragment.addFrameLayout(new TradeRecordDetailFragment());
    }

    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {
        //拉取数据
        if (itemTradeRecords.size() > 0) {
            itemTradeRecords.remove(itemTradeRecords.size() - 1);
        }
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        itemTradeRecords.add(new ItemTradeRecord());
        if (loadMoreHelper != null) {
            loadMoreHelper.setLoading(false);
        }
        adapter.notifyDataSetChanged();
    }
}
