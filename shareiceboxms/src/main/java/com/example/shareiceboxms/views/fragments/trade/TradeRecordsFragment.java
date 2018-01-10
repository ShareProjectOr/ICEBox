package com.example.shareiceboxms.views.fragments.trade;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeRecordListAdapter;
import com.example.shareiceboxms.models.beans.trade.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.models.helpers.MenuPop;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/11/27.
 * 交易记录
 * 将被预加载，故此处不在使用dialog，否则统计页面会出现dialog叠加现象
 */

public class TradeRecordsFragment extends BaseFragment implements LoadMoreHelper.LoadMoreListener {
    private static String TAG = "TradeRecordsFragment";
    private View containerView;
    private ImageView tradeSearch, tradeTypeIcon;
    private TextView tradeTypeText, timeSelector;
    private EditText tradeNo;
    private RelativeLayout tradeType, selectTime;
    private android.support.v4.widget.SwipeRefreshLayout recordRefresh;
    private android.support.v7.widget.RecyclerView tradeRecordList;
    private boolean isTypeClicked = false;
    private TradeRecordListAdapter adapter;
    private List<ItemTradeRecord> itemTradeRecords;
    private LoadMoreHelper loadMoreHelper;
    private DoubleDatePickerDialog datePickerDialog;
    private ListPopupWindow mTilePopup;
    private HomeActivity homeActivity;
    private Map<String, Object> params;
    private String[] time;
    private Dialog dialog;
    private int curPage, requestNum, totalNum, totalPage;
    private boolean searchClicked = false;

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
        tradeNo = (EditText) containerView.findViewById(R.id.tradeNo);
        tradeType = (RelativeLayout) containerView.findViewById(R.id.tradeType);
        selectTime = (RelativeLayout) containerView.findViewById(R.id.selectTime);
        timeSelector = (TextView) containerView.findViewById(R.id.timeSelector);
        tradeTypeText = (TextView) containerView.findViewById(R.id.tradeTypeText);
        tradeTypeIcon = (ImageView) containerView.findViewById(R.id.tradeTypeIcon);
        recordRefresh = (SwipeRefreshLayout) containerView.findViewById(R.id.recordRefresh);
        tradeType.setOnClickListener(this);
        recordRefresh.setOnRefreshListener(this);
        tradeSearch.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        recordRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
        tradeNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tradeNo.getText().toString().length() <= 0 && searchClicked) {
                    if (params.containsKey("tradeCode")) {
                        params.remove("tradeCode");
                    }
                    itemTradeRecords.clear();
                    getDatas(params);
                    searchClicked = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDatas() {
        itemTradeRecords = new ArrayList<>();
        params = new HashMap<>();
        homeActivity = (HomeActivity) getActivity();
        datePickerDialog = new DoubleDatePickerDialog(getContext(), 0, this
                , Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE), true);
        mTilePopup = MenuPop.CreateMenuPop(getContext(), tradeType, Constants.TradeStateTitle);
        mTilePopup.setOnItemClickListener(this);
        if (FragmentFactory.getInstance().getTradeChildFragments().size() == 1) {
            dialog = MyDialog.loadDialog(getContext());
        }
        RecyclerView tradeRecordList = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.tradeRecordList);
        adapter = new TradeRecordListAdapter(getContext(), itemTradeRecords, this);
        new MyViewFactory(getContext()).BuildRecyclerViewRule(tradeRecordList,
                new LinearLayoutManager(getContext()), null, true).setAdapter(adapter);
        loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(adapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(tradeRecordList)
                .setVisibleThreshold(1);
        time = RequestParamsContants.getInstance().getTimeSelectorParams();
        time = SecondToDate.getDateParams(SecondToDate.TODAY_CODE);
        timeSelector.setText(SecondToDate.getDateUiShow(time));
        tradeTypeText.setText(Constants.TradeStateTitle[0]);
        getDatas(getParams());
    }

    private Map<String, Object> getParams() {
        params = RequestParamsContants.getInstance().getTradeRecordsParams();
//        params.put("createTime", RequestParamsContants.getInstance().getSelectTime(SecondToDate.getDateParams(SecondToDate.TODAY_CODE)));
        return params;
    }

    private void getDatas(final Map<String, Object> params) {
        TradeRecordsTask task = new TradeRecordsTask(params);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /*
* 设置能够滑动加载
* */
    private void setCanLoad() {
        if (loadMoreHelper != null) {
            loadMoreHelper.setLoading(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tradeSearch:
                //模糊查询订单号
                params.put("tradeCode", tradeNo.getText().toString());
                getDatas(params);
                searchClicked = true;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tradeTypeText.setText(Constants.TradeStateTitle[position]);
        mTilePopup.setSelection(tradeTypeText.getText().toString().length());
        switch (position) {
            case 0://
                if (params.containsKey("payState")) {
                    params.remove("payState");
                }
                break;
            case 1://已支付
                params.put("payState", 1);
                break;
            case 2://未支付
                params.put("payState", 0);
                break;
        }
        itemTradeRecords.clear();
        getDatas(params);
        mTilePopup.dismiss();
    }

    @Override
    public void onRefresh() {
        if (itemTradeRecords != null) {
            itemTradeRecords.clear();
            initPage();
            adapter.notifyDataSetChanged();
        }
        getDatas(params);
        recordRefresh.setRefreshing(false);
    }

    private void initPage() {
        curPage = 1;
        requestNum = 1;
        totalNum = 0;
    }

    public void addFrameFragment() {
        TradeFragment tradeFragment = (TradeFragment) getParentFragment();
        tradeFragment.addFrameLayout(new TradeRecordDetailFragment());
    }

    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {
        Log.d("-----totalPage-----", "----loadMore---" + totalPage);
        //拉取数据
        if (itemTradeRecords.size() < totalNum && curPage < totalPage) {
//            Map<String, Object> params = getParams();
//            if (mTilePopup.getSelectedItemPosition() != 0) {//如果是全部就不设置
//                params.put("payState", mTilePopup.getSelectedItemPosition() == 1 ? 1 : 0);
//            }
//            params.put("payState", mTilePopup.getSelectedItemPosition() == 1 ? 1 : 0);
            params.put("createTime", RequestParamsContants.getInstance().getSelectTime(time));
            params.put("p", curPage + 1);
            getDatas(params);
        }
    }

    @Override
    public void clearDates() {
        params.put("createTime", null);
        initPage();
        itemTradeRecords.clear();
        timeSelector.setText("");
        getDatas(params);
    }

    @Override
    public String[] onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
        time = super.onDateSet(startDatePicker, startYear, startMonthOfYear, startDayOfMonth, endDatePicker, endYear, endMonthOfYear, endDayOfMonth);
        timeSelector.setText(SecondToDate.getDateUiShow(time));
        params.put("createTime", RequestParamsContants.getInstance().getSelectTime(time));
        itemTradeRecords.clear();
        getDatas(params);

        return null;
    }

    //获取交易记录列表异步任务
    private class TradeRecordsTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private Map<String, Object> params;
        private List<ItemTradeRecord> tradeRecords;

        TradeRecordsTask(Map<String, Object> params) {
            this.params = params;
            tradeRecords = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            if (dialog != null) {
                dialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e(TAG, " request url: " + HttpRequstUrl.TRADE_RECORDS_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_RECORDS_URL, JsonUtil.mapToJson(this.params));
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                Log.e(TAG, "response :" + response);
                tradeRecords = ItemTradeRecord.bindTradeRecordsList(JsonDataParse.getInstance().getArrayList(response.toString()));
                totalNum = JsonDataParse.getInstance().getTotalNum();
                curPage = JsonDataParse.getInstance().getCurPage();
                requestNum = JsonDataParse.getInstance().getRequestNum();
                totalPage = JsonDataParse.getInstance().getTotalPage();
                return true;
            } catch (IOException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.JSONException_Tip;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog != null) {
                dialog.dismiss();
//                dialog = null;//第一次弹出dialog后，后续加载不在弹出
            }
            if (success) {
                if (itemTradeRecords.size() > 0) {
                    if (itemTradeRecords.get(itemTradeRecords.size() - 1) == null) {
                        itemTradeRecords.remove(itemTradeRecords.size() - 1);
                    }
                }
                itemTradeRecords.addAll(tradeRecords);
                if (itemTradeRecords.size() < totalNum) {
                    itemTradeRecords.add(null);
                    setCanLoad();
                }
                adapter.notifyDataSetChanged();
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                }
                /*
                *获得数据为空时，提示
                * */
                if (tradeRecords.size() <= 0 && TextUtils.equals(err, "")) {
                    Toast.makeText(homeActivity, "没有数据！", Toast.LENGTH_SHORT).show();
                    itemTradeRecords.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(homeActivity, err, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
