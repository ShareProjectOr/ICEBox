package com.example.shareiceboxms.views.fragments.trade;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeRecordDetailAdapter;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
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
    private View containerView;
    private ScrollView scrollView;
    private android.support.design.widget.TabLayout productTabLayout;
    private CheckBox allProductCB;
    private com.example.shareiceboxms.models.widget.ListViewForScrollView productList;

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
        title.setText("交易详情");
        productTabLayout.setTabMode(TabLayout.MODE_FIXED);
        productTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                curTabSelect = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        if (adapter != null) {
                            adapter.setItemProductList(itemBuyProducts);
                        }
                        break;
                    case 1:
                        if (adapter != null) {
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

//    /*
//    *
//    * 切换tab时拉去响应的数据,开始时设置商品绑定的machine为null(看执行的先后顺序)，详情数据拉去后，为每个商品设置machine
//    * */
//    private void loadProductForSell(List<ItemSellProduct> itemProducts, int value) {
//
//        if (itemProducts.size() <= 0) {//第一次加载时拉去数据
//            Map<String, Object> params = RequestParamsContants.getInstance().getTradeRecoredDetailProductParams();
//            recordDetailProductHelper.setMachine(itemTradeRecord.machine);
//            recordDetailProductHelper.getDatas(params);
//        }
//
//    }

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
        payState.setTextColor(ContextCompat.getColor(getContext(), itemTradeRecord.payState == 0 ? R.color.red : R.color.sucessgreen));
        machineNameAddr.setText(itemTradeRecord.machine.machineAddress);
        customerId.setText(itemTradeRecord.consumer.userID);
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
        allProductCB.setChecked(adapter.isAllChecked());
        chooseItemCount.setText(adapter.getTotalCheckedCount() + "");
        chooseItemMoney.setText(adapter.getTotalCheckedMoney() + "");
    }

    private void getDatas() {
        TradeRecordDetailTask task = new TradeRecordDetailTask(RequestParamsContants.getInstance().getTradeRecoredDetailParams());
        task.execute();
    }

    private void getProductDatas() {
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
                if (adapter.getTotalCheckedCount() > 0) {
                    List<ItemSellProduct> wellBeRefund = new ArrayList<>();
                    for (int i = 0; i < itemBuyProducts.size(); i++) {
                        ItemSellProduct itemSellProduct = itemBuyProducts.get(i);
                        if (itemSellProduct.isChecked()) {
                            wellBeRefund.add(itemSellProduct);
                        }
                    }
                    MyDialog.getRefundDialog(wellBeRefund, homeActivity).show();
                } else {
                    Toast.makeText(homeActivity, "没有退款项", Toast.LENGTH_SHORT).show();
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
        private String err = "net_work_err";
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
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_RECOR_DETAIL_URL, JsonUtil.mapToJson(this.params));
                tradeRecord = ItemTradeRecord.bindTradeRecord(JsonDataParse.getInstance().getSingleObject(response.toString()));
                Log.e("response", response.toString());
                return true;
            } catch (IOException e) {
                err = RequstTips.getErrorMsg(e.getMessage());
                Log.e("erro", e.toString());
            } catch (JSONException e) {
                err = RequstTips.JSONException_Tip;
                Log.e("erro", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                itemTradeRecord = tradeRecord;
                updateUi();
                /*
                * 为拉取到的商品绑定machine
                * */
                recordDetailProductHelper.setMachine(itemTradeRecord.machine);
            } else {
                Log.e("request error :", response + "");
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

}
