package com.example.shareiceboxms.views.fragments.product;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.ProductListAdapter;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contentprovider.ProductListData;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.views.fragments.BaseFragment;

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
    private SwipeRefreshLayout mRefreashLayout;
    private ProductListData contentprovider;
    private Map<String, Object> initPostBody = new HashMap<>();
    private int currentPage = 1;
    private int pageNum = 5;

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
        initPostBody.put("n", pageNum);
        initPostBody.put("p", currentPage);
        initPostBody.put("appUserID", PerSonMessage.userId);
        mMachineSearchInput = (EditText) contentView.findViewById(R.id.machineSearchInput);
        mDoSearch = (Button) contentView.findViewById(R.id.doSearch);
        RecyclerView mProductList = (RecyclerView) contentView.findViewById(R.id.productList);
        mRefreashLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        mRefreashLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
        ProductListAdapter productAdapter = new ProductListAdapter(getActivity(), this);
        LoadMoreHelper loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(productAdapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(mProductList)
                .setVisibleThreshold(0);
        contentprovider = productAdapter.getContentProvider();
        contentprovider.setCanLoad(loadMoreHelper, currentPage);
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

    //加载更多的回调
    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {
        Log.e("loadmore", "doing");
        if (currentPage < contentprovider.GetMaxPageAccount()) {
            Map<String, Object> postbody = new HashMap<>();
            postbody.put("n", pageNum);
            postbody.put("p", currentPage + 1);
            postbody.put("appUserID", PerSonMessage.userId);
            contentprovider.getData(HttpRequstUrl.PRODUCT_TYPE_LIST_URL, postbody, false);

        } else {
            Toast.makeText(getActivity(), "偷偷告诉你,数据已全部加载完毕...", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 1;
                contentprovider.getData(HttpRequstUrl.PRODUCT_TYPE_LIST_URL, initPostBody, true);

                mRefreashLayout.setRefreshing(false);
            }
        }, Constants.REFREASH_DELAYED_TIME);
    }
}
