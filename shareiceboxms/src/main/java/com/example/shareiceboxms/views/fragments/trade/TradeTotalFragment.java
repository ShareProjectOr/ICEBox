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
 * 交易统计
 */

public class TradeTotalFragment extends BaseFragment {
    private static String TAG = "TradeTotalFragment";
    private View containerView;
    private HomeActivity activity;
    private TextView timeSelector;
    private LinearLayout agentHas;
    private TextView totalMoneyNum, hasPayNum, notPayNum;

    private TextView totalMoneyNum_2, hasPayNum_2, notPayNum_2;

    private TextView totalMoneyNum_3, hasPayNum_3, notPayNum_3;

    private TextView totalMoneyNum_4, hasPayNum_4, notPayNum_4;

    private TextView settledMoney, unsettleMoney, incomeMoney;

    private Button choosePerson;
    private RadioGroup dateGroup;
    private RelativeLayout selectTime;
    private SwipeRefreshLayout refreshLayout;
    private ItemTradeTotal itemTradeTotal;
    private TradeTotalFragment tradeTotalFragment;
    private Dialog dialog;
    private String[] time;
    private int companyID = -1;

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
        choosePerson = (Button) containerView.findViewById(R.id.choosePerson);
        timeSelector = (TextView) containerView.findViewById(R.id.timeSelector);
        refreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.refresh);
        dateGroup = (RadioGroup) containerView.findViewById(R.id.dateGroup);

        totalMoneyNum = (TextView) containerView.findViewById(R.id.totalMoneyNum);
        hasPayNum = (TextView) containerView.findViewById(R.id.hasPayNum);
        notPayNum = (TextView) containerView.findViewById(R.id.notPayNum);

        totalMoneyNum_2 = (TextView) containerView.findViewById(R.id.totalMoneyNum_2);
        hasPayNum_2 = (TextView) containerView.findViewById(R.id.hasPayNum_2);
        notPayNum_2 = (TextView) containerView.findViewById(R.id.notPayNum_2);

        totalMoneyNum_3 = (TextView) containerView.findViewById(R.id.totalMoneyNum_3);
        hasPayNum_3 = (TextView) containerView.findViewById(R.id.hasPayNum_3);
        notPayNum_3 = (TextView) containerView.findViewById(R.id.notPayNum_3);

        hasPayNum_4 = (TextView) containerView.findViewById(R.id.hasPayNum_4);
        notPayNum_4 = (TextView) containerView.findViewById(R.id.notPayNum_4);

        settledMoney = (TextView) containerView.findViewById(R.id.settledMoney);
        unsettleMoney = (TextView) containerView.findViewById(R.id.unsettleMoney);
        incomeMoney = (TextView) containerView.findViewById(R.id.incomeMoney);

        agentHas = (LinearLayout) containerView.findViewById(R.id.agentHas);

        if (PerSonMessage.userType >= Constants.MACHINE_MANAGER) {
            agentHas.setVisibility(View.GONE);
        }

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
                getDatas(getParams());
            }
        });
        refreshLayout.setOnRefreshListener(this);
        if (PerSonMessage.userType <= Constants.SYSTEM_MANAGER) {
            choosePerson.setOnClickListener(this);
        } else {
            choosePerson.setVisibility(View.GONE);
        }

        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
    }

    private void initDatas() {
        activity = (HomeActivity) getActivity();
        tradeTotalFragment = this;
        time = RequestParamsContants.getInstance().getTimeSelectorParams();
        dialog = MyDialog.loadDialog(getContext());
        itemTradeTotal = new ItemTradeTotal();
        datePickerDialog = new DoubleDatePickerDialog(getContext(), 0, this
                , Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE), true);
        dateGroup.check(R.id.todayDate);
    }

    public Map<String, Object> getParams() {
        Map<String, Object> params = RequestParamsContants.getInstance().getTradeTotalParams();
        params.put("searchTime", RequestParamsContants.getInstance().getSelectTime(time));
      /*  switch (PerSonMessage.userType) {
            case Constants.AGENT_MANAGER:
                if (PerSonMessage.company != null) {
                    params.put("companyID", PerSonMessage.company.companyID);
                }
                break;
            case Constants.MACHINE_MANAGER:
                params.put("userID", PerSonMessage.userId);
                break;
            case Constants.SYSTEM_MANAGER:
                params.put("companyID", "");
                break;
        }*/
        return params;
    }

    public void getDatas(Map<String, Object> params) {
        TradeTotalTask task = new TradeTotalTask(params);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void switchAgentName(String name) {
        choosePerson.setText(name);
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    @Override
    public void onRefresh() {
//        time = SecondToDate.getDateParams(SecondToDate.TODAY_CODE);
//        timeSelector.setText(SecondToDate.getDateUiShow(time));
        getDatas(getParams());
        refreshLayout.setRefreshing(false);//关闭刷新
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectTime:
                datePickerDialog.show();
                break;
            case R.id.choosePerson:
                if (PerSonMessage.childPerson.size() <= 0) {
                    GetAgentsToCreateAccountHelper.getInstance().setContext(activity);
                    GetAgentsToCreateAccountHelper.getInstance().setGetAgentsLisenner(new GetAgentsToCreateAccountHelper.GetAgentsLisenner() {
                        @Override
                        public void getAgents(List<ItemPerson> agents) {
                            if (PerSonMessage.childPerson != null) {
                                PerSonMessage.childPerson.clear();
                                PerSonMessage.childPerson.add(null);
                                PerSonMessage.childPerson.addAll(agents);
                                ChoosePopupWindow.setAdapter(PerSonMessage.childPerson, activity);
                                ChoosePopupWindow.getAdapter().notifyDataSetChanged();
                                ChoosePopupWindow.showPopFormBottom(containerView, activity, tradeTotalFragment);
                            }
                        }
                    });
                    GetAgentsToCreateAccountHelper.getInstance().getDatas();
                } else {
                    ChoosePopupWindow.showPopFormBottom(containerView, activity, tradeTotalFragment);
                }

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
        getDatas(getParams());
        return null;
    }

    private void updateUi() {

        //    totalMoney:1500.00,//Number 交易总金额
        //    unreceiveMoney:400.00,//Number 未收到的交易总额
        //    receiveMoney:1100.00,//Number 已收到的交易总额金额

        //    serviceCharge:21,//Number 手续费
        //    alipayPoundage:6.00,//Number 支付宝手续费
        //    wechatPoundage:0.00,//Number 微信手续费

        //    refundMoney:100.00,//Number  交易退款金额
        //    realRefundMoney:45415.2,//Number 实退金额
        //    refundServiceCharge:0.03,//Number 退回手续费


        //    offsetedMoney:10.00,//Number 已冲抵金额
        //    unoffsetMoney:29.76,//Number 待冲抵金额

        //    settledMoney:300.00,//Number 已结算金额
        //    unsettleMoney:534.96,//Number 待结算金额


        //    incomeMoney:994.00,//Number 净利润

        totalMoneyNum.setText(itemTradeTotal.totalMoney);
        hasPayNum.setText(itemTradeTotal.receiveMoney);
        notPayNum.setText(itemTradeTotal.unreceiveMoney);

        totalMoneyNum_2.setText(itemTradeTotal.serviceCharge);
        hasPayNum_2.setText(itemTradeTotal.alipayPoundage);
        notPayNum_2.setText(itemTradeTotal.wechatPoundage);

        totalMoneyNum_3.setText(itemTradeTotal.realRefundMoney);
      /*  hasPayNum_3.setText(itemTradeTotal.realRefundMoney);
        notPayNum_3.setText(itemTradeTotal.refundServiceCharge);*/

        if (PerSonMessage.userType < Constants.MACHINE_MANAGER) {

            hasPayNum_4.setText(itemTradeTotal.offsettedMoney);
            notPayNum_4.setText(itemTradeTotal.unoffsetMoney);

            settledMoney.setText(itemTradeTotal.settledMoney);
            unsettleMoney.setText(itemTradeTotal.unsettleMoney);
        }
        incomeMoney.setText(itemTradeTotal.incomeMoney);
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
                updateUi();
            } else {
            }
        }
    }

}
