package com.example.shareiceboxms.views.fragments.product;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.UpLoadGoodsRecordListAdapter;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by Administrator on 2017/12/12.
 */

public class UpLoadGoodsRecordFragment extends BaseFragment {
    private View contentView;
    private EditText mSearchKeyword;
    private ImageView mDoSearch;
    private LinearLayout mShowDatePick;
    private TextView mDateRange;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mUpLoadGoodsList;
    private UpLoadGoodsRecordListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.uploadgoodsrecordfragment));
            initview();
            inidata();
            initListener();
        }
        return contentView;
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void inidata() {
    }

    public void addFrameFragment() {
        ProductFragment productFragment = (ProductFragment) getParentFragment();
        productFragment.addFrameLayout(new UpLoadGoodsDetailsFragment());
    }

    private void initview() {
        mSearchKeyword = (EditText) contentView.findViewById(R.id.searchKeyword);
        mDoSearch = (ImageView) contentView.findViewById(R.id.doSearch);
        mShowDatePick = (LinearLayout) contentView.findViewById(R.id.showDatePick);
        mDateRange = (TextView) contentView.findViewById(R.id.dateRange);
        mRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        mUpLoadGoodsList = (RecyclerView) contentView.findViewById(R.id.upLoadGoodsList);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
        mAdapter = new UpLoadGoodsRecordListAdapter(getActivity(), this);
        new MyViewFactory(getActivity())
                .BuildRecyclerViewRule(mUpLoadGoodsList, new LinearLayoutManager(getContext()), new DefaultItemAnimator(), true)
                .setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, Constants.REFREASH_DELAYED_TIME);
    }
}
