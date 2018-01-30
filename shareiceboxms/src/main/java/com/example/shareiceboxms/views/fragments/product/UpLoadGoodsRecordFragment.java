package com.example.shareiceboxms.views.fragments.product;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.UpLoadGoodsRecordListAdapter;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contentprovider.UpLoadRecordListData;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    private UpLoadRecordListData contentProvider;
    private int currentPage = 1;
    private int pageNum = 5;
    private String[] operationTime = null;
    private DoubleDatePickerDialog datePickerDialog;

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
        if (!mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setOnRefreshListener(this);
        }
        mShowDatePick.setOnClickListener(this);
        mDoSearch.setOnClickListener(this);
    }

    private void inidata() {
    }

    public void addFrameFragment() {
        ProductFragment productFragment = (ProductFragment) getParentFragment();
        productFragment.addFrameLayout(new UpLoadGoodsDetailsFragment());
    }

    private void initview() {
        Calendar c = Calendar.getInstance();
        mSearchKeyword = (EditText) contentView.findViewById(R.id.searchKeyword);
        mDoSearch = (ImageView) contentView.findViewById(R.id.doSearch);
        mShowDatePick = (LinearLayout) contentView.findViewById(R.id.showDatePick);
        mDateRange = (TextView) contentView.findViewById(R.id.dateRange);
        mRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        RecyclerView mUpLoadGoodsList = (RecyclerView) contentView.findViewById(R.id.upLoadGoodsList);
        datePickerDialog = new DoubleDatePickerDialog(getActivity(), 0, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
        UpLoadGoodsRecordListAdapter mAdapter = new UpLoadGoodsRecordListAdapter(getActivity(), this);
        LoadMoreHelper loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(mAdapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(mUpLoadGoodsList)
                .setVisibleThreshold(0);
        new MyViewFactory(getActivity())
                .BuildRecyclerViewRule(mUpLoadGoodsList, new LinearLayoutManager(getContext()), new DefaultItemAnimator(), true)
                .setAdapter(mAdapter);
        contentProvider = mAdapter.getContentProvider();
        contentProvider.setCanLoad(loadMoreHelper, currentPage);//绑定列表页数状态
        contentProvider.getData(HttpRequstUrl.UPLOAD_RECORD_LIST_URL, RequestParamsContants.getInstance().getUploadListParams(), true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showDatePick:
                datePickerDialog.show();
                break;
            case R.id.doSearch:
                getDatas(true);
                break;

        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 1;
                getDatas(true);
                mRefreshLayout.setRefreshing(false);
            }
        }, Constants.REFREASH_DELAYED_TIME);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    private void getDatas(boolean b) {
        Map<String, Object> map = new HashMap<>();
        map.put("n", pageNum);
        map.put("p", currentPage);
        map.put("appUserID", PerSonMessage.userId);
        //  map.put("userID", PerSonMessage.userId);
        map.put("operationTime", RequestParamsContants.getInstance().getSelectTime(operationTime));
        contentProvider.getData(HttpRequstUrl.UPLOAD_RECORD_LIST_URL, map, b);
    }

    @Override
    public String[] onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
        mDateRange.setText(startYear + "-" + String.valueOf(startMonthOfYear + 1) + "-" + startDayOfMonth + "至" + endYear + "-" + String.valueOf(endMonthOfYear + 1) + "-" + endDayOfMonth);
        operationTime = super.onDateSet(startDatePicker, startYear, startMonthOfYear, startDayOfMonth, endDatePicker, endYear, endMonthOfYear, endDayOfMonth);
        currentPage = 1;
        getDatas(true);
        return null;
    }

    @Override
    public void clearDates() {
        mDateRange.setText("");
        operationTime = null;
        currentPage = 1;
        getDatas(true);
    }

    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {
        Log.e("loadmore", "doing" + "currentPage=" + currentPage + "最大页数=" + contentProvider.GetMaxPageAccount());

        if (currentPage < contentProvider.GetMaxPageAccount()) {
            Map<String, Object> map = new HashMap<>();
            map.put("n", pageNum);
            map.put("p", currentPage + 1);
            map.put("appUserID", PerSonMessage.userId);
            map.put("operationTime", RequestParamsContants.getInstance().getSelectTime(operationTime));
            contentProvider.getData(HttpRequstUrl.UPLOAD_RECORD_LIST_URL, map, false);
        } else {
            Toast.makeText(getActivity(), "偷偷告诉你,数据已全部加载完毕...", Toast.LENGTH_SHORT).show();
        }
    }
}
