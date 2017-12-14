package com.example.shareiceboxms.views.fragments.machine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;

import com.example.shareiceboxms.models.adapters.MachineListAdapter;
import com.example.shareiceboxms.models.adapters.TradeRecordListAdapter;
import com.example.shareiceboxms.models.adapters.ViewPagerAdapter;
import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.shareiceboxms.R.id.refreshLayout;

/**
 * Created by WH on 2017/11/27.
 */

public class MachineFragment extends BaseFragment implements HomeActivity.OnBackPressListener, LoadMoreHelper.LoadMoreListener {
    private View containerView;
    BaseFragment curFrameFragment;
    private FrameLayout tradeDetailLayout;
    HomeActivity homeActivity;
    private ImageView drawerIcon, saoma;
    private CoordinatorLayout machineContainer;
    private android.support.v4.widget.SwipeRefreshLayout machineRefresh;
    private android.support.v7.widget.RecyclerView machineRecycler;
    private FrameLayout detailFrameLayout;
    private EditText inputMachineName;
    private Button search;
    private TextView title;
    List<ItemMachine> itemMachines;
    MachineListAdapter adapter;
    LoadMoreHelper loadMoreHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_machine));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        homeActivity.setOnBackPressListener(this);
        itemMachines = new ArrayList<>();
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(null);

        machineRecycler = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.machineRecycler);
        adapter = new MachineListAdapter(getContext(), itemMachines, this);
        new MyViewFactory(getContext()).BuildRecyclerViewRule(machineRecycler,
                new LinearLayoutManager(getContext()), null, true).setAdapter(adapter);
        loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(adapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(machineRecycler)
                .setVisibleThreshold(1);
    }

    private void initViews() {
        machineContainer = (CoordinatorLayout) containerView.findViewById(R.id.machineContainer);
        tradeDetailLayout = (FrameLayout) containerView.findViewById(R.id.detailFrameLayout);
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        machineRefresh = (android.support.v4.widget.SwipeRefreshLayout) containerView.findViewById(R.id.machineRefresh);
        machineRecycler = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.machineRecycler);
        detailFrameLayout = (FrameLayout) containerView.findViewById(R.id.detailFrameLayout);
        inputMachineName = (EditText) containerView.findViewById(R.id.inputMachineName);
        search = (Button) containerView.findViewById(R.id.search);
        title = (TextView) containerView.findViewById(R.id.title);
        title.setText("机器列表");
        machineRefresh.setOnRefreshListener(this);
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
        machineRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
    }

    public void addFrameLayout(BaseFragment fragment) {
        curFrameFragment = fragment;
        super.addFrameLayout(fragment, R.id.detailFrameLayout);
        tradeDetailLayout.setVisibility(View.VISIBLE);
        machineContainer.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                break;
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadMoreHelper.getAdapter().notifyDataSetChanged();
        machineRefresh.setRefreshing(false);
    }

    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {
        //拉取数据
        if (itemMachines.size() > 0) {
            itemMachines.remove(itemMachines.size() - 1);
        }
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        itemMachines.add(new ItemMachine());
        Toast.makeText(getContext(), "111'", Toast.LENGTH_SHORT).show();
        if (loadMoreHelper != null) {
            loadMoreHelper.setLoading(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnBackDown() {
        Toast.makeText(getContext(), "fadsfdsa", Toast.LENGTH_SHORT).show();
        if (curFrameFragment != null && curFrameFragment.isAdded()) {
            removeFrame(curFrameFragment);
            tradeDetailLayout.setVisibility(View.GONE);
            machineContainer.setVisibility(View.VISIBLE);
            curFrameFragment = null;
        } else {
            homeActivity.finishActivity();
        }

    }


}
