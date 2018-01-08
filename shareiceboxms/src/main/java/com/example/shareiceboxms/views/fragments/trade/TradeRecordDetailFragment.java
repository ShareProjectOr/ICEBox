package com.example.shareiceboxms.views.fragments.trade;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeRecordDetailAdapter;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.beans.trade.ItemTradeRecord;
import com.example.shareiceboxms.models.beans.product.ItemSellProduct;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.helpers.RecordDetailProductHelper;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/11/27.
 */

public class TradeRecordDetailFragment extends BaseFragment implements RecordDetailProductHelper.ProductResponseLisenner {
    private static String TAG = "TradeRecordDetailFragment";
    private View containerView;
    private ScrollView scrollView;
    private android.support.design.widget.TabLayout productTabLayout;
    private CheckBox allProductCB;
    private com.example.shareiceboxms.models.widget.ListViewForScrollView productList;
    private LinearLayout refundMoreLayout;

    private TextView tradeTime, totalMoney, payState, machineNameAddr, customerId, tradeNo;
    private ImageView payIcon;
    private TextView jiesuanMoney, jiesuanReal, jiesuanRefund;

    private TextView title;
    private ImageView drawerIcon, saoma;

    private TextView chooseItemCount, chooseItemMoney, moreRefund;

    private ItemTradeRecord itemTradeRecord;
    private List<ItemSellProduct> itemBuyProducts, itemRefundProducts;
    TradeRecordDetailAdapter adapter;
    HomeActivity homeActivity;
    RecordDetailProductHelper recordDetailProductHelper;
    private int curTabSelect = 0;
    private int checkNum = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_record_detail));
            initViews();
            initDatas();
        }
        Log.d("------DetailFragment-", "" + FragmentFactory.getInstance().getSavedBundle().getInt("tradeID"));
        return containerView;
    }

    private void initViews() {
        scrollView = (ScrollView) containerView.findViewById(R.id.scrollView);
        productTabLayout = (android.support.design.widget.TabLayout) containerView.findViewById(R.id.productTabLayout);
        allProductCB = (CheckBox) containerView.findViewById(R.id.allProductCB);
        productList = (com.example.shareiceboxms.models.widget.ListViewForScrollView) containerView.findViewById(R.id.productList);
        refundMoreLayout = (LinearLayout) containerView.findViewById(R.id.refundMoreLayout);

        tradeTime = (TextView) containerView.findViewById(R.id.tradeTime);
        totalMoney = (TextView) containerView.findViewById(R.id.totalMoney);
        payIcon = (ImageView) containerView.findViewById(R.id.payIcon);
        payState = (TextView) containerView.findViewById(R.id.payState);
        machineNameAddr = (TextView) containerView.findViewById(R.id.machineNameAddr);
        tradeNo = (TextView) containerView.findViewById(R.id.tradeNo);
        customerId = (TextView) containerView.findViewById(R.id.customerId);

        jiesuanMoney = (TextView) containerView.findViewById(R.id.jiesuanMoney);
        jiesuanReal = (TextView) containerView.findViewById(R.id.jiesuanReal);
        jiesuanRefund = (TextView) containerView.findViewById(R.id.jiesuanRefund);

        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        title = (TextView) containerView.findViewById(R.id.title);

        chooseItemCount = (TextView) containerView.findViewById(R.id.chooseItemCount);
        chooseItemMoney = (TextView) containerView.findViewById(R.id.chooseItemMoney);
        moreRefund = (TextView) containerView.findViewById(R.id.moreRefund);


        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
        moreRefund.setOnClickListener(this);
        if (PerSonMessage.userType < Constants.AGENT_MANAGER) {
            allProductCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked && adapter.getTotalCheckedCount() != adapter.getCount()) {
                        return;
                    }
                    adapter.checkAllItem(isChecked);
                    changeCheckedCount();
                }
            });
        } else {
            allProductCB.setVisibility(View.GONE);
        }
        if (PerSonMessage.userType == Constants.AGENT_MANAGER) {
            refundMoreLayout.setVisibility(View.GONE);
        }
        title.setText("交易详情");
        productTabLayout.setTabMode(TabLayout.MODE_FIXED);
        productTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                curTabSelect = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        if (adapter != null) {
                            allProductCB.setVisibility(View.VISIBLE);
                            adapter.setRefundProductSelected(false);
                            adapter.setItemProductList(itemBuyProducts);
                        }
                        break;
                    case 1:
                        if (adapter != null) {
                            allProductCB.setVisibility(View.GONE);
                            adapter.setRefundProductSelected(true);
                            adapter.setItemProductList(itemRefundProducts);
                        }
                        break;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        itemBuyProducts = new ArrayList<>();
        itemRefundProducts = new ArrayList<>();
        adapter = new TradeRecordDetailAdapter(getContext(), this);
        productList.setAdapter(adapter);
        for (int i = 0; i < Constants.TradeRecordDetailTitle.length; i++) {
            TabLayout.Tab tab = productTabLayout.newTab();
            productTabLayout.addTab(tab);
            tab.setText(Constants.TradeRecordDetailTitle[i]);
        }
        initHelper();
        getDatas();
    }

    private void initHelper() {
        recordDetailProductHelper = new RecordDetailProductHelper(getContext(), this);
        getProductDatas();
    }

    /*
    * 更新显示状态
    * */
    private void updateUi() {
        if (itemTradeRecord == null) {
            return;
        }
        tradeTime.setText(itemTradeRecord.closingTime);
        totalMoney.setText(itemTradeRecord.tradeMoney);
        payState.setText(itemTradeRecord.payState == 0 ? Constants.TradeStateTitle[2] : Constants.TradeStateTitle[1]);
        payState.setTextColor(ContextCompat.getColor(getContext(), Constants.TreadIsPayCOLOR[itemTradeRecord.payState]));
        machineNameAddr.setText(itemTradeRecord.machine.machineAddress);
        customerId.setText(itemTradeRecord.consumer.userID + "");
        tradeNo.setText(itemTradeRecord.tradeCode);
        jiesuanMoney.setText(itemTradeRecord.settlementMoney);
        jiesuanReal.setText(itemTradeRecord.realSettlementMoney);
        jiesuanRefund.setText(itemTradeRecord.refundSettlementMoney);
        payIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), Constants.TradeRecordPayICON[itemTradeRecord.payState]));
    }

    /*
* 修改选中数量和价格
* */
    public void changeCheckedCount() {
        checkNum = adapter.getTotalCheckedCount();
        allProductCB.setChecked(adapter.isAllChecked());
        chooseItemCount.setText(adapter.getTotalCheckedCount() + "");
        chooseItemMoney.setText(adapter.getTotalCheckedMoney() + "");
    }

    private void getDatas() {
        TradeRecordDetailTask task = new TradeRecordDetailTask(RequestParamsContants.getInstance().getTradeRecoredDetailParams());
        task.execute();
    }

    public void getProductDatas() {
        chooseItemCount.setText("0");
        chooseItemMoney.setText("0.00");
        itemRefundProducts.clear();
        itemBuyProducts.clear();
        Map<String, Object> params = RequestParamsContants.getInstance().getTradeRecoredDetailProductParams();
        if (itemTradeRecord != null) {
            recordDetailProductHelper.setMachine(itemTradeRecord.machine);
        }
        recordDetailProductHelper.getDatas(params);
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
            case R.id.moreRefund:
                //请求网络退款操作
                if (checkNum > 0) {
                    List<ItemSellProduct> refundProducts = new ArrayList<>();
                    String rfids = "";
                    for (int i = 0; i < itemBuyProducts.size(); i++) {
                        ItemSellProduct itemSellProduct = itemBuyProducts.get(i);
                        if (itemSellProduct.isChecked()) {
                            refundProducts.add(itemSellProduct);
                        }
                    }
                    for (int i = 0; i < refundProducts.size(); i++) {
                        ItemSellProduct itemSellProduct = refundProducts.get(i);
                        rfids += itemSellProduct.rfid;
                        if (i != refundProducts.size() - 1) {
                            rfids += "-";
                        }
                    }
                    MyDialog.getRefundDialog(checkNum, rfids, homeActivity, this).show();
                } else {
                    Toast.makeText(homeActivity, "没有可退款项", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void getProducts(List<ItemSellProduct> itemProducts) {
        for (int i = 0; i < itemProducts.size(); i++) {
            ItemSellProduct itemProduct = itemProducts.get(i);
            if (itemProduct == null) {
                continue;
            }
            if (itemProduct.isRefund == 0) {
                itemBuyProducts.add(itemProduct);
            } else {
                itemRefundProducts.add(itemProduct);
            }
        }
        adapter.setItemProductList(curTabSelect == 0 ? itemBuyProducts : itemRefundProducts);
        adapter.notifyDataSetChanged();
        productTabLayout.getTabAt(0).setText(Constants.TradeRecordDetailTitle[0] + "(" + itemBuyProducts.size() + ")");
        productTabLayout.getTabAt(1).setText(Constants.TradeRecordDetailTitle[1] + "(" + itemRefundProducts.size() + ")");
    }

    //获取交易记录详情异步任务
    private class TradeRecordDetailTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private ItemTradeRecord tradeRecord;
        private Map<String, Object> params;

        TradeRecordDetailTask(Map<String, Object> params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e(TAG, " request url: " + HttpRequstUrl.TRADE_RECOR_DETAIL_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_RECOR_DETAIL_URL, JsonUtil.mapToJson(this.params));
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                tradeRecord = ItemTradeRecord.bindTradeRecord(JsonDataParse.getInstance().getSingleObject(response.toString()));
                return true;
            } catch (IOException e) {
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                err = RequstTips.JSONException_Tip;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.e(TAG, "response:" + response);
            if (success) {
                itemTradeRecord = tradeRecord;
                updateUi();
                /*
                * 为拉取到的商品绑定machine
                * */
                recordDetailProductHelper.setMachine(itemTradeRecord.machine);
            } else {
                Toast.makeText(homeActivity, err, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
