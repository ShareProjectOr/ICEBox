package com.example.shareiceboxms.views.fragments.trade;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemPerson;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.beans.trade.ItemTradeTongji;
import com.example.shareiceboxms.models.beans.trade.ItemTradeTotal;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.models.helpers.GetAgentsToCreateAccountHelper;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.models.widget.ChoosePopupWindow;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/11/27.
 * 财务明细
 */

public class TradeTongJiFragment extends BaseFragment {
    private static String TAG = "TradeTotalFragment";
    private View containerView;
    private HomeActivity activity;
    private TextView timeSelector;
    private TextView incomeMoney, costomerNum, refundMoney, notPayMoney, totalMoneyNum;
    private RadioGroup dateGroup;
    private RelativeLayout selectTime;
    private SwipeRefreshLayout refreshLayout;
    private ItemTradeTongji itemTradeTongji;
    private TradeTongJiFragment tradeTotalFragment;
    private String[] time;

    private DoubleDatePickerDialog datePickerDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_tongji));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        selectTime = (RelativeLayout) containerView.findViewById(R.id.selectTime);
        timeSelector = (TextView) containerView.findViewById(R.id.timeSelector);
        refreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.refresh);
        dateGroup = (RadioGroup) containerView.findViewById(R.id.dateGroup);

        totalMoneyNum = (TextView) containerView.findViewById(R.id.totalMoneyNum);
        notPayMoney = (TextView) containerView.findViewById(R.id.notPayMoney);
        refundMoney = (TextView) containerView.findViewById(R.id.refundMoney);
        costomerNum = (TextView) containerView.findViewById(R.id.costomerNum);
        incomeMoney = (TextView) containerView.findViewById(R.id.incomeMoney);

        selectTime.setOnClickListener(this);
        dateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.todayDate:
                        time = SecondToDate.getDateParams(SecondToDate.TODAY_CODE);
                        Log.d("todayDate=", time.toString());
                        break;
                    case R.id.weekDate:
                        time = SecondToDate.getDateParams(SecondToDate.WEEK_CODE);
                        Log.d("weekDate=", time.toString());
                        break;
                    case R.id.monthDate:
                        time = SecondToDate.getDateParams(SecondToDate.MONTH_CODE);
                        Log.d("monthDate=", time.toString());

                        break;
                    case R.id.yearDate:
                        time = SecondToDate.getDateParams(SecondToDate.YEAR_CODE);
                        Log.d("yearDate=", time.toString());
                        break;
                }
                timeSelector.setText(SecondToDate.getDateUiShow(time));
                getDatas(getParams());
            }
        });
        dateGroup.check(R.id.todayDate);
        refreshLayout.setOnRefreshListener(this);

        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
    }

    private void initDatas() {
        activity = (HomeActivity) getActivity();
        tradeTotalFragment = this;
        time = RequestParamsContants.getInstance().getTimeSelectorParams();
        time = SecondToDate.getDateParams(SecondToDate.TODAY_CODE);//默认为今天的
        itemTradeTongji = new ItemTradeTongji();
        datePickerDialog = new DoubleDatePickerDialog(getContext(), 0, this
                , Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE), true);
    }

    public Map<String, Object> getParams() {
        Map<String, Object> params = RequestParamsContants.getInstance().getTradeTongjiParams();
        params.put("createTime", RequestParamsContants.getInstance().getSelectTime(time));
        return params;
    }

    public void getDatas(Map<String, Object> params) {
        TradeTotalTask task = new TradeTotalTask(params);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRefresh() {
        getDatas(getParams());
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
        super.onDestroy();
    }

    @Override
    public String[] onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
        time = super.onDateSet(startDatePicker, startYear, startMonthOfYear, startDayOfMonth, endDatePicker, endYear, endMonthOfYear, endDayOfMonth);
        timeSelector.setText(SecondToDate.getDateUiShow(time));
        getDatas(getParams());
        return null;
    }

    private void updateUi() {
        totalMoneyNum.setText(itemTradeTongji.totalMoney);
        notPayMoney.setText(itemTradeTongji.unreceiveMoney);
        refundMoney.setText(itemTradeTongji.refundMoney);
        incomeMoney.setText(itemTradeTongji.receiveMoney);
        costomerNum.setText(itemTradeTongji.totalCustomer + "人");
    }

    //获取交易统计异步任务
    private class TradeTotalTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private ItemTradeTongji tradeTotal;
        private Map<String, Object> params;
        private Dialog dialog;

        TradeTotalTask(Map<String, Object> params) {
            this.params = params;

        }

        @Override
        protected void onPreExecute() {
            dialog = MyDialog.loadDialog(getContext());
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e(TAG, "request URL: " + HttpRequstUrl.TRADE_TONGJI_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_TONGJI_URL, JsonUtil.mapToJson(this.params));
                Log.e(TAG, "response" + response.toString());
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                tradeTotal = ItemTradeTongji.bindTradeTongji(JsonDataParse.getInstance().getSingleObject(response.toString()));

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
            }
            if (success) {
                itemTradeTongji = tradeTotal;
                updateUi();
            } else {
            }
        }
    }

}
