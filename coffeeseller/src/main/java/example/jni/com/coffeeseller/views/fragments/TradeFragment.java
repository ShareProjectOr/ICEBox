package example.jni.com.coffeeseller.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.databases.DealOrderInfoManager;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.model.adapters.TradeListAdapter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;

/**
 * Created by WH on 2018/4/27.
 */

public class TradeFragment extends BasicFragment implements View.OnClickListener {
    private View content;
    private Button back, clearTrade;
    private ListView listView;
    private TradeListAdapter adapter;
    private HomeActivity homeActivity;
    private List<DealRecorder> dealRecorderList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        content = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade));
        initView();
        initData();
        return content;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView() {
        homeActivity = (HomeActivity) getActivity();
        back = (Button) content.findViewById(R.id.back);
        clearTrade = (Button) content.findViewById(R.id.clearTrade);
        listView = (ListView) content.findViewById(R.id.tradeList);
        dealRecorderList = DealOrderInfoManager.getInstance(homeActivity).getLocalTableDatas();
        adapter = new TradeListAdapter(homeActivity, dealRecorderList);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
        clearTrade.setOnClickListener(this);
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                homeActivity.replaceFragment(FragmentEnum.TradeFragment, FragmentEnum.ConfigFragment);
                break;
            case R.id.clearTrade:
                break;
        }
    }
}
