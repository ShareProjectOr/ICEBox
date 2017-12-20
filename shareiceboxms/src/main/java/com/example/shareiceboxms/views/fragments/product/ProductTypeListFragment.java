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
import android.widget.Button;
import android.widget.EditText;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.ProductListAdapter;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contentprovider.ProductListData;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordDetailFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LYU on 2017/12/12.
 * 品类列表视图管理类
 */

public class ProductTypeListFragment extends BaseFragment {
    private View contentView;
    private EditText mMachineSearchInput;
    private Button mDoSearch;
    private RecyclerView mProductList;
    private SwipeRefreshLayout mRefreashLayout;
    private ProductListData contentprovider;
    private ProductListAdapter productAdapter;
    private Map<String, Object> initPostBody = new HashMap<>();
    private int currentPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.product_typelist_fragment));
            initview();
            inidata();
            initListener();
        }
        return contentView;
    }

    private void initListener() {
        mDoSearch.setOnClickListener(this);
        if (!mRefreashLayout.isRefreshing()) {
            mRefreashLayout.setOnRefreshListener(this);
        }
    }

    private void inidata() {

    }

    public void addFrameFragment() {
        ProductFragment productFragment = (ProductFragment) getParentFragment();
        productFragment.addFrameLayout(new ProductDetailsFragment());
    }

    private void initview() {
        initPostBody.put("n", 10);
        initPostBody.put("p", currentPage);
        initPostBody.put("appUserID", PerSonMessage.userId);
        mMachineSearchInput = (EditText) contentView.findViewById(R.id.machineSearchInput);
        mDoSearch = (Button) contentView.findViewById(R.id.doSearch);
        mProductList = (RecyclerView) contentView.findViewById(R.id.productList);
        mRefreashLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        mRefreashLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
        productAdapter = new ProductListAdapter(getActivity(), this);
        contentprovider = productAdapter.getContentProvider();
        new MyViewFactory(getContext()).BuildRecyclerViewRule(mProductList, new LinearLayoutManager(getActivity()), null, true).setAdapter(productAdapter);
        contentprovider.getData(HttpRequstUrl.PRODUCT_TYPE_LIST_URL, initPostBody, true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doSearch:
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                contentprovider.getData(HttpRequstUrl.PRODUCT_TYPE_LIST_URL, initPostBody, true);
                mRefreashLayout.setRefreshing(false);
            }
        }, Constants.REFREASH_DELAYED_TIME);
    }
}
