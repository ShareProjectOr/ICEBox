package com.example.shareiceboxms.views.fragments.trade;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeAccountDetailAdapter;
import com.example.shareiceboxms.models.beans.trade.ItemChongdiRefundRecord;
import com.example.shareiceboxms.models.beans.trade.ItemTradeAccount;
import com.example.shareiceboxms.models.beans.trade.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.PageHelper;
import com.example.shareiceboxms.models.helpers.RefundChongdiHelper;
import com.example.shareiceboxms.models.helpers.RefundMoreHelper;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/11/27.
 */

public class TradeAccountDetailFragment extends BaseFragment {
    private static String TAG = "TradeAccountDetailFragment";
    private View containerView;

    private android.support.design.widget.TabLayout accountTabLayout;
    private com.example.shareiceboxms.models.widget.ListViewForScrollView accountList;

    private TextView jiesuanTime, jiesuanMoney, jiesuanState, jiesuanTimePeriod;

    private LinearLayout pageLayout;

    private ImageView drawerIcon, saoma;
    private TextView title, timeTitle, moneyTitle;

    private TextView chongdiRefundMoney, realMoney;


    private List<ItemTradeRecord> itemBuyProducts;
    private List<ItemChongdiRefundRecord> itemRefundProducts;
    TradeAccountDetailAdapter adapter;
    PageHelper pageHelper;
    HomeActivity homeActivity;
    ItemTradeAccount itemTradeAccount;
    Dialog dialog;
    int curPage, requestNum, totalNum, totalPage;
    int curTab = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_account_detail));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {

        accountTabLayout = (android.support.design.widget.TabLayout) containerView.findViewById(R.id.accountTabLayout);
        accountList = (com.example.shareiceboxms.models.widget.ListViewForScrollView) containerView.findViewById(R.id.accountList);


        jiesuanTime = (TextView) containerView.findViewById(R.id.jiesuanTime);
        jiesuanMoney = (TextView) containerView.findViewById(R.id.jiesuanMoney);
        jiesuanState = (TextView) containerView.findViewById(R.id.jiesuanState);
        jiesuanTimePeriod = (TextView) containerView.findViewById(R.id.jiesuanTimePeriod);

        pageLayout = (LinearLayout) containerView.findViewById(R.id.pageLayout);

        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        title = (TextView) containerView.findViewById(R.id.title);
        timeTitle = (TextView) containerView.findViewById(R.id.timeTitle);
        moneyTitle = (TextView) containerView.findViewById(R.id.moneyTitle);

        chongdiRefundMoney = (TextView) containerView.findViewById(R.id.chongdiRefundMoney);
        realMoney = (TextView) containerView.findViewById(R.id.realMoney);


        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        itemTradeAccount = (ItemTradeAccount) FragmentFactory.getInstance().getSavedBundle().getSerializable("ItemTradeAccount");
        itemBuyProducts = new ArrayList<>();
        itemRefundProducts = new ArrayList<>();
        itemBuyProducts.add(new ItemTradeRecord());
        itemBuyProducts.add(new ItemTradeRecord());
        itemBuyProducts.add(new ItemTradeRecord());
        itemBuyProducts.add(new ItemTradeRecord());
        itemBuyProducts.add(new ItemTradeRecord());


        pageHelper = new PageHelper(getContext(), this, pageLayout);
        accountTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < Constants.TradeRecordDetailTitle.length; i++) {
            TabLayout.Tab tab = accountTabLayout.newTab();
            //设置拉取到的数据在标题中
            tab.setText(Constants.TradeAccountDetailTitle[i]);
            accountTabLayout.addTab(tab);
        }
        adapter = new TradeAccountDetailAdapter(getContext(), this);
        adapter.setItemAccountList(itemBuyProducts);
        accountList.setAdapter(adapter);
        accountTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                curTab = tab.getPosition();
                if (tab.getPosition() == 0) {
                    moneyTitle.setText("交易金额");
                    timeTitle.setText("交易时间");
                    adapter.setRefundList(false);
                    adapter.setItemAccountList(itemBuyProducts);
                    pageHelper.setTotalText(totalPage, totalNum);
                    pageHelper.setChongdiRefund(false);
                } else {
                    moneyTitle.setText("应退金额");
                    timeTitle.setText("退回时间");
                    adapter.setRefundList(true);
                    adapter.setItemChongdiAccountList(itemRefundProducts);
                    if (itemRefundProducts.size() <= 0) {
                        getChongdiDatas(1, 6);
                    }
                    pageHelper.setTotalText(RefundChongdiHelper.getInstance().totalPage, RefundChongdiHelper.getInstance().totalNum);
                    pageHelper.setChongdiRefund(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        title.setText("结算工单详情");
        init();
        getRecorDatas(getParams());
    }

    public void getChongdiDatas(int page, int requestNum) {
        Map<String, Object> params = RequestParamsContants.getInstance().getRefundOfChongdiParams();
        params.put("divideID", itemTradeAccount.divideID);
        params.put("p", page);
        RefundChongdiHelper.getInstance().setContext(homeActivity);
        RefundChongdiHelper.getInstance().setAdapter(adapter);
        RefundChongdiHelper.getInstance().setItemRefundProducts(itemRefundProducts);
        RefundChongdiHelper.getInstance().setItemTradeAccount(itemTradeAccount);
        RefundChongdiHelper.getInstance().getDatas(params);
    }

    private void init() {
        if (itemTradeAccount == null) return;
        jiesuanMoney.setText(itemTradeAccount.divideMoney);
        switch (itemTradeAccount.divideState) {
            case 0://待审核状态时显示工单创建时间
                jiesuanTime.setText(itemTradeAccount.createTime);
                break;
            case 1://待确认-审核时间
                jiesuanTime.setText(itemTradeAccount.checkTime);
                break;
            case 2://待转账-确认时间
                jiesuanTime.setText(itemTradeAccount.configTime);
                break;
            case 3://待复审-转账确认时间
                jiesuanTime.setText(itemTradeAccount.configTransferTime);
                break;
            case 4://复审完成-复审完成时间
                jiesuanTime.setText(itemTradeAccount.recheckTime);
                break;
            case 5://背撤销-撤销时间
                jiesuanTime.setText(itemTradeAccount.cancelTime);
                break;
        }
        jiesuanState.setText(Constants.TradeAccountStateTitle[itemTradeAccount.divideState + 1]);
        try {
            jiesuanTimePeriod.setText(SecondToDate.getSubString(itemTradeAccount.startTime, itemTradeAccount.endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        chongdiRefundMoney.setText(itemTradeAccount.offsetMoney);
        realMoney.setText(itemTradeAccount.actualPayment);

    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = RequestParamsContants.getInstance().getTradeRecordsParams();
        params.put("divideID", itemTradeAccount.divideID);
        return params;
    }

    public void addFrameFragment() {
        if (BaseFragment.curFragment != null && BaseFragment.curFragment instanceof TradeFragment) {
            TradeFragment tradeFragment = (TradeFragment) BaseFragment.curFragment;
            tradeFragment.addFrameLayout(new TradeRecordDetailFragment());
            BaseFragment.tradeAccountDetailFragment = this;
        }

    }

    public void getRecorDatas(Map<String, Object> params) {
        TradeRecordsTask task = new TradeRecordsTask(params);
        task.execute();
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
        }
    }

    //获取交易记录列表异步任务
    private class TradeRecordsTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private Map<String, Object> params;
        private List<ItemTradeRecord> tradeRecords;
        private List<ItemChongdiRefundRecord> itemChongdiRefundRecords;

        TradeRecordsTask(Map<String, Object> params) {
            this.params = params;
            tradeRecords = new ArrayList<>();
            itemChongdiRefundRecords = new ArrayList<>();
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
                Log.e(TAG, "request URL: " + HttpRequstUrl.TRADE_RECORDS_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_RECORDS_URL, JsonUtil.mapToJson(this.params));
                Log.e(TAG, "response: " + response);
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                tradeRecords = ItemTradeRecord.bindTradeRecordsList(JsonDataParse.getInstance().getArrayList(response));
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
            if (success) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;//第一次弹出dialog后，后续加载不在弹出
                }
                itemBuyProducts.clear();
                itemBuyProducts.addAll(tradeRecords);
                if (curTab == 0) {
                    pageHelper.setTotalText(totalPage, totalNum);
                }
                adapter.notifyDataSetChanged();
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(homeActivity, err, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
