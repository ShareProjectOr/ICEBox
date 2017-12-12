package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeAccountListAdapter;
import com.example.shareiceboxms.models.adapters.TradeRecordListAdapter;
import com.example.shareiceboxms.models.beans.ItemTradeAccount;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2017/11/27.
 * 结算
 */

public class TradeAccountFragment extends BaseFragment implements LoadMoreHelper.LoadMoreListener {
    private View containerView;
    private Button createAccount;
    private RelativeLayout accountType;
    private TextView accountTypeText;
    private ImageView chooseAccountIcon;
    private android.support.v4.widget.SwipeRefreshLayout accountRefresh;
    private android.support.v7.widget.RecyclerView tradeaccountList;
    private boolean isTypeClicked = false;
    List<ItemTradeAccount> itemTradeAccounts;
    private LoadMoreHelper loadMoreHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_account));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        createAccount = (Button) containerView.findViewById(R.id.createAccount);
        accountType = (RelativeLayout) containerView.findViewById(R.id.accountType);
        accountTypeText = (TextView) containerView.findViewById(R.id.accountTypeText);
        chooseAccountIcon = (ImageView) containerView.findViewById(R.id.chooseAccountIcon);
        accountRefresh = (android.support.v4.widget.SwipeRefreshLayout) containerView.findViewById(R.id.accountRefresh);

        accountType.setOnClickListener(this);
        accountRefresh.setOnRefreshListener(this);
        accountRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
    }

    private void initDatas() {
        itemTradeAccounts = new ArrayList<>();

        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(null);


        RecyclerView tradeaccountList = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.tradeaccountList);
        TradeAccountListAdapter adapter = new TradeAccountListAdapter(getContext(), itemTradeAccounts);
        new MyViewFactory(getContext()).BuildRecyclerViewRule(tradeaccountList,
                new LinearLayoutManager(getContext()), null, true).setAdapter(adapter);
        loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(adapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(tradeaccountList)
                .setVisibleThreshold(1);

        //联网刷新数据
//        String dataJson = OkHttpUtil.post(url, params);
//        itemTradeTotal = JsonUtil.jsonToJavaBean(dataJson, itemTradeTotal);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accountType:
                isTypeClicked = !isTypeClicked;
                //弹出POPUPlistwindow
                Toast.makeText(getContext(), "111'", Toast.LENGTH_SHORT).show();
                chooseAccountIcon.setSelected(isTypeClicked);
                break;
            case R.id.createAccount:
                break;
            default:
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
        itemTradeAccounts.clear();

        loadMoreHelper.getAdapter().notifyDataSetChanged();
        accountRefresh.setRefreshing(false);
    }

    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {
        //拉取数据
        if (itemTradeAccounts.size() > 0) {
            itemTradeAccounts.remove(itemTradeAccounts.size() - 1);
        }
        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(new ItemTradeAccount());

        Toast.makeText(getContext(), "111'", Toast.LENGTH_SHORT).show();
        if (loadMoreHelper != null) {
            loadMoreHelper.setLoading(false);
        }
        adapter.notifyDataSetChanged();
    }
}
