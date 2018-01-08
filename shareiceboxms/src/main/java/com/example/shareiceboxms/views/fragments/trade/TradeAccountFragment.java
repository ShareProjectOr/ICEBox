package com.example.shareiceboxms.views.fragments.trade;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.TradeAccountListAdapter;
import com.example.shareiceboxms.models.beans.ItemPerson;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.beans.trade.ItemTradeAccount;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.GetAgentsToCreateAccountHelper;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.models.helpers.MenuPop;
import com.example.shareiceboxms.models.helpers.MyDialog;
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
 * 服务费结算（运营商）
 */

public class TradeAccountFragment extends BaseFragment implements LoadMoreHelper.LoadMoreListener {
    private static String TAG = "TradeAccountFragment";
    private View containerView;
    private Button createAccount;
    private RelativeLayout accountType;
    private TextView accountTypeText;
    private ImageView chooseAccountIcon;
    private android.support.v4.widget.SwipeRefreshLayout accountRefresh;
    private android.support.v7.widget.RecyclerView tradeaccountList;
    private TradeAccountListAdapter adapter;
    private boolean isTypeClicked = false;
    List<ItemTradeAccount> itemTradeAccounts;
    private LoadMoreHelper loadMoreHelper;
    private ListPopupWindow mTilePopup;
    HomeActivity homeActivity;
    private int curPage, requestNum, totalNum, totalPage, curType;
    private Dialog dialog;
    TradeAccountFragment tradeAccountFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_trade_account));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        createAccount = (Button) containerView.findViewById(R.id.createAccount);
        accountType = (RelativeLayout) containerView.findViewById(R.id.accountType);
        accountTypeText = (TextView) containerView.findViewById(R.id.accountTypeText);
        chooseAccountIcon = (ImageView) containerView.findViewById(R.id.chooseAccountIcon);
        accountRefresh = (android.support.v4.widget.SwipeRefreshLayout) containerView.findViewById(R.id.accountRefresh);

        if (PerSonMessage.userType == Constants.AGENT_MANAGER) {
            createAccount.setVisibility(View.GONE);
        } else {
            createAccount.setOnClickListener(this);
        }
        accountType.setOnClickListener(this);
        accountRefresh.setOnRefreshListener(this);
        accountRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        itemTradeAccounts = new ArrayList<>();
        tradeAccountFragment = this;
/*
        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(new ItemTradeAccount());
        itemTradeAccounts.add(null);*/


        mTilePopup = MenuPop.CreateMenuPop(getContext(), accountType, Constants.TradeAccountStateTitle);
        dialog = MyDialog.loadDialog(getContext());
        RecyclerView tradeaccountList = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.tradeaccountList);
        adapter = new TradeAccountListAdapter(getContext(), itemTradeAccounts, this);
        new MyViewFactory(getContext()).BuildRecyclerViewRule(tradeaccountList,
                new LinearLayoutManager(getContext()), null, true).setAdapter(adapter);
        loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(adapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(tradeaccountList)
                .setVisibleThreshold(0);
        mTilePopup.setOnItemClickListener(this);
        accountTypeText.setText(Constants.TradeAccountStateTitle[0]);
        getDatas(getParams());
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = RequestParamsContants.getInstance().getAccountsParams();
//        params.put("createTime", RequestParamsContants.getInstance().getSelectTime(SecondToDate.getDateParams(SecondToDate.TODAY_CODE)));
        return params;
    }

    private void getDatas(Map<String, Object> params) {
        TradeAccountsTask task = new TradeAccountsTask(params);
        task.execute();
    }

    public void addFrameFragment(BaseFragment fragment) {
        TradeFragment tradeFragment = (TradeFragment) getParentFragment();
        tradeFragment.addFrameLayout(fragment);
    }

    private void initPage() {
        curPage = 1;
        requestNum = 1;
        totalNum = 0;
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
            case R.id.accountType:
                isTypeClicked = !isTypeClicked;
                //弹出POPUPlistwindow
                mTilePopup.show();
                chooseAccountIcon.setSelected(isTypeClicked);
                break;
            case R.id.createAccount:
                GetAgentsToCreateAccountHelper.getInstance().setContext(homeActivity);
                GetAgentsToCreateAccountHelper.getInstance().setGetAgentsLisenner(new GetAgentsToCreateAccountHelper.GetAgentsLisenner() {
                    @Override
                    public void getAgents(List<ItemPerson> agents) {
                        MyDialog.getAgentsDialog(homeActivity, agents, tradeAccountFragment).show();
                    }
                });
                GetAgentsToCreateAccountHelper.getInstance().getDatas();
//                addFrameFragment(new CreateAccountFragment());
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        accountTypeText.setText(Constants.TradeAccountStateTitle[position]);
        Map<String, Object> params = getParams();
        switch (position) {
            case 0:
                if (params.containsKey("divideState")) {
                    params.remove("divideState");
                }
                break;
            case 1:
                params.put("divideState", 0);
                break;
            case 2:
                params.put("divideState", 1);
                break;
            case 3:
                params.put("divideState", 2);
                break;
            case 4:
                params.put("divideState", 3);
                break;
            case 5:
                params.put("divideState", 4);
                break;
            case 6:
                params.put("divideState", 5);
                break;
        }
        getDatas(params);
        mTilePopup.dismiss();
    }

    @Override
    public void onRefresh() {
        //联网刷新数据
        if (itemTradeAccounts != null) {
            itemTradeAccounts.clear();
            initPage();
            loadMoreHelper.getAdapter().notifyDataSetChanged();
        }
        getDatas(getParams());
        accountRefresh.setRefreshing(false);
    }


    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {
        Log.d("-----totalPage-----", "----loadMore---" + totalPage);
        //拉取数据
        if (itemTradeAccounts.size() < totalNum && curPage < totalPage) {
            Map<String, Object> params = getParams();
            if (mTilePopup.getSelectedItemPosition() != 0) {//如果是全部就不设置
                params.put("divideState", mTilePopup.getSelectedItemPosition() - 1);
            }
            params.put("p", curPage + 1);
            getDatas(params);
        }
        adapter.notifyDataSetChanged();
    }


    //获取服务费结算列表异步任务
    private class TradeAccountsTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "";
        private Map<String, Object> params;
        private List<ItemTradeAccount> tradeRecords;

        TradeAccountsTask(Map<String, Object> params) {
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
                Log.e(TAG, "request URL: " + HttpRequstUrl.TRADE_JIESUAN_LIST_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_JIESUAN_LIST_URL, JsonUtil.mapToJson(this.params));
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                tradeRecords = ItemTradeAccount.bindTradeAccountsList(JsonDataParse.getInstance().getArrayList(response.toString()));
                totalNum = JsonDataParse.getInstance().getTotalNum();
                curPage = JsonDataParse.getInstance().getCurPage();
                requestNum = JsonDataParse.getInstance().getRequestNum();
                totalPage = JsonDataParse.getInstance().getTotalPage();
                Log.e(TAG, "tradeRecords.size" + tradeRecords.size());
                Log.e(TAG, "response" + response.toString());
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
                if (itemTradeAccounts.size() > 0) {
                    if (itemTradeAccounts.get(itemTradeAccounts.size() - 1) == null) {
                        itemTradeAccounts.remove(itemTradeAccounts.size() - 1);
                    }
                }
                itemTradeAccounts.addAll(tradeRecords);
                if (itemTradeAccounts.size() < totalNum) {
                    itemTradeAccounts.add(null);
                    setCanLoad();
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
