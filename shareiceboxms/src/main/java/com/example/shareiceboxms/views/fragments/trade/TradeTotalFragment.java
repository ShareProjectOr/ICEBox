package com.example.shareiceboxms.views.fragments.trade;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeTotalListAdapter;
import com.example.shareiceboxms.models.beans.trade.ItemTradeTotal;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.machine.MachineFragment;
import com.squareup.okhttp.Call;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by WH on 2017/11/27.
 * 交易统计
 */

public class TradeTotalFragment extends BaseFragment {
    private static String TAG = "TradeTotalFragment";
    private View containerView;
    private TextView timeSelector;
    private RadioGroup dateGroup;
    private RecyclerView tradeTotalList;
    private RelativeLayout selectTime;
    private TradeTotalListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ItemTradeTotal itemTradeTotal;
    private Dialog dialog;
    private String[] time;

    private DoubleDatePickerDialog datePickerDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_total));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        selectTime = (RelativeLayout) containerView.findViewById(R.id.selectTime);
        timeSelector = (TextView) containerView.findViewById(R.id.timeSelector);
        tradeTotalList = (RecyclerView) containerView.findViewById(R.id.tradeTotalList);
        refreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.refresh);
        dateGroup = (RadioGroup) containerView.findViewById(R.id.dateGroup);

        selectTime.setOnClickListener(this);

        dateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.todayDate:
                        time = SecondToDate.getDateParams(SecondToDate.TODAY_CODE);
                        break;
                    case R.id.weekDate:
                        time = SecondToDate.getDateParams(SecondToDate.WEEK_CODE);
                        break;
                    case R.id.monthDate:
                        time = SecondToDate.getDateParams(SecondToDate.MONTH_CODE);
                        break;
                    case R.id.yearDate:
                        time = SecondToDate.getDateParams(SecondToDate.YEAR_CODE);
                        break;
                }
                timeSelector.setText(SecondToDate.getDateUiShow(time));
                getDatas();
            }
        });
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
    }

    private void initDatas() {
        time = RequestParamsContants.getInstance().getTimeSelectorParams();
        dialog = MyDialog.loadDialog(getContext());
        tradeTotalList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        tradeTotalList.setLayoutManager(mLayoutManager);
        itemTradeTotal = new ItemTradeTotal();
        adapter = new TradeTotalListAdapter(getContext(), itemTradeTotal);
        tradeTotalList.setAdapter(adapter);
        datePickerDialog = new DoubleDatePickerDialog(getContext(), 0, this
                , Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE), true);
        dateGroup.check(R.id.todayDate);
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = RequestParamsContants.getInstance().getTradeTotalParams();
        params.put("searchTime", RequestParamsContants.getInstance().getSelectTime(time));
        return params;
    }

    private void getDatas() {
        TradeTotalTask task = new TradeTotalTask(getParams());
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRefresh() {
        time = SecondToDate.getDateParams(SecondToDate.TODAY_CODE);
        timeSelector.setText(SecondToDate.getDateUiShow(time));
        getDatas();
        refreshLayout.setRefreshing(false);//关闭刷新
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectTime:
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (dialog != null) {
            dialog = null;
        }
        super.onDestroy();
    }

    @Override
    public String[] onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
        time = super.onDateSet(startDatePicker, startYear, startMonthOfYear, startDayOfMonth, endDatePicker, endYear, endMonthOfYear, endDayOfMonth);
        timeSelector.setText(SecondToDate.getDateUiShow(time));
        getDatas();
        return null;
    }

    //获取交易统计异步任务
    private class TradeTotalTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private ItemTradeTotal tradeTotal;
        private Map<String, Object> params;

        TradeTotalTask(Map<String, Object> params) {
            this.params = params;

        }

        @Override
        protected void onPreExecute() {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e(TAG, "request URL: " + HttpRequstUrl.TRADE_TOTAL_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_TOTAL_URL, JsonUtil.mapToJson(this.params));
                Log.e(TAG, "response" + response.toString());
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                tradeTotal = ItemTradeTotal.bindTradeTotal(JsonDataParse.getInstance().getSingleObject(response.toString()));

                return true;
            } catch (IOException e) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                err = RequstTips.JSONException_Tip;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
//                dialog = null;
            }
            if (success) {
                itemTradeTotal = tradeTotal;
                adapter.notifyDataSetChanged();
            } else {
            }
        }
    }

}
