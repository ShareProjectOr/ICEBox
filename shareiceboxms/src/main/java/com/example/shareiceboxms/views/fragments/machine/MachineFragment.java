package com.example.shareiceboxms.views.fragments.machine;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;

import com.example.shareiceboxms.models.adapters.MachineListAdapter;
import com.example.shareiceboxms.models.adapters.TradeRecordListAdapter;
import com.example.shareiceboxms.models.adapters.ViewPagerAdapter;
import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.ItemTradeRecord;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.LoginFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shareiceboxms.R.id.refreshLayout;

/**
 * Created by WH on 2017/11/27.
 */

public class MachineFragment extends BaseFragment implements HomeActivity.OnBackPressListener, LoadMoreHelper.LoadMoreListener {
    private View containerView;
    BaseFragment curFrameFragment;
    private FrameLayout tradeDetailLayout;
    HomeActivity homeActivity;
    private ImageView drawerIcon, saoma;
    private CoordinatorLayout machineContainer;
    private android.support.v4.widget.SwipeRefreshLayout machineRefresh;
    private android.support.v7.widget.RecyclerView machineRecycler;
    private FrameLayout detailFrameLayout;
    private EditText inputMachineName;
    private Button search;
    private TextView title;
    MachineListAdapter adapter;
    LoadMoreHelper loadMoreHelper;
    Dialog dialog;
    private List<ItemMachine> itemMachines;
    private int curPage, requestNum, totalNum, totalPage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            Log.e("------", "-----------onCreateView_---------");
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_machine));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        homeActivity.setOnBackPressListener(this);
        itemMachines = new ArrayList<>();
        machineRecycler = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.machineRecycler);
        adapter = new MachineListAdapter(getContext(), itemMachines, this);
        new MyViewFactory(getContext()).BuildRecyclerViewRule(machineRecycler,
                new LinearLayoutManager(getContext()), null, true).setAdapter(adapter);
        loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(adapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(machineRecycler)
                .setVisibleThreshold(0);
        initPage();
        dialog = MyDialog.loadDialog(getActivity());
        getDatas(getParams());
    }

    private void getDatas(Map<String, Object> params) {
        MachineListTask machineListTask = new MachineListTask(params);
        machineListTask.execute();
    }

    private void initViews() {
        machineContainer = (CoordinatorLayout) containerView.findViewById(R.id.machineContainer);
        tradeDetailLayout = (FrameLayout) containerView.findViewById(R.id.detailFrameLayout);
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        machineRefresh = (android.support.v4.widget.SwipeRefreshLayout) containerView.findViewById(R.id.machineRefresh);
        machineRecycler = (android.support.v7.widget.RecyclerView) containerView.findViewById(R.id.machineRecycler);
        detailFrameLayout = (FrameLayout) containerView.findViewById(R.id.detailFrameLayout);
        inputMachineName = (EditText) containerView.findViewById(R.id.inputMachineName);
        search = (Button) containerView.findViewById(R.id.search);
        title = (TextView) containerView.findViewById(R.id.title);
        title.setText("机器列表");
        machineRefresh.setOnRefreshListener(this);
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
        search.setOnClickListener(this);
        machineRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue));
    }

    public void addFrameLayout(BaseFragment fragment) {
        curFrameFragment = fragment;
        super.addFrameLayout(fragment, R.id.detailFrameLayout);
        tradeDetailLayout.setVisibility(View.VISIBLE);
        machineContainer.setVisibility(View.GONE);
    }

    private Map<String, Object> getParams() {
        Map<String, Object> body = RequestParamsContants.getInstance().getMachineListParams();
        body.put("n", 2);
        return body;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                Map<String, Object> body = RequestParamsContants.getInstance().getMachineListParams();
                body.put("keyword", inputMachineName.getText().toString());
                itemMachines.clear();
                getDatas(body);
                break;
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
        }
    }

    @Override
    public void onRefresh() {
/*
     添加上时，刷新无效，不发送请求消息
       if (machineRefresh.isRefreshing()) {
            return;
        }*/
        if (itemMachines != null) {
            itemMachines.clear();
            initPage();
            adapter.notifyDataSetChanged();
        }
        getDatas(getParams());
        machineRefresh.setRefreshing(false);
    }

    private void initPage() {
        curPage = 1;
        requestNum = 1;
        totalNum = 0;
    }

    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {

        totalPage = totalNum / requestNum + (totalNum % requestNum > 0 ? 1 : 0);
        Log.d("-----totalPage-----", "----loadMore---" + totalPage);
        //拉取数据
        if (itemMachines.size() < totalNum && curPage < totalPage) {
            Map<String, Object> params = getParams();
            params.put("p", curPage + 1);
            getDatas(params);
        }
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
    public void OnBackDown() {
        if (dialog != null) {
            dialog.dismiss();
            return;
        }
        if (curFrameFragment != null && curFrameFragment.isAdded()) {
            Dialog dialog = ((MachineDetailFragment) curFrameFragment).getDialog();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                return;
            }
            ((MachineDetailFragment) curFrameFragment).leaveToCommit();
            removeFrame(curFrameFragment);
            tradeDetailLayout.setVisibility(View.GONE);
            machineContainer.setVisibility(View.VISIBLE);
            curFrameFragment = null;
        } else {
            homeActivity.finishActivity();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //获取机器列表异步任务
    private class MachineListTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "net_work_err";
        private List<ItemMachine> machines;
        private Map<String, Object> params;

        MachineListTask(Map<String, Object> params) {
            this.params = params;
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
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.MACHINE_LIST_URL, JsonUtil.mapToJson(this.params));
             /*    被移动至JsonDataParse的getArrayList 方法中
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject jsonD = jsonObject.getJSONObject("d");
                totalNum = jsonD.getInt("t");
                curPage = jsonD.getInt("p");
                requestNum = jsonD.getInt("n");
                JSONArray jsonList = jsonD.getJSONArray("list");*/
                machines = ItemMachine.bindMachineList(JsonDataParse.getInstance().getArrayList(response.toString()));
                totalNum = JsonDataParse.getInstance().getTotalNum();
                curPage = JsonDataParse.getInstance().getCurPage();
                requestNum = JsonDataParse.getInstance().getRequestNum();
                Log.e("machines.size==", machines.size() + "");
                Log.e("response", response.toString());
                return true;
            } catch (IOException e) {
                Log.e("erro", e.getMessage());
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.JSONException_Tip;
                Log.e("erro", e.toString());
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
                if (itemMachines.size() > 0) {
                    if (itemMachines.get(itemMachines.size() - 1) == null) {
                        itemMachines.remove(itemMachines.size() - 1);
                    }
                }
                itemMachines.addAll(machines);
                if (itemMachines.size() < totalNum) {
                    itemMachines.add(null);
                    setCanLoad();
                }
                adapter.notifyDataSetChanged();
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("request error :", response + "");
                Toast.makeText(homeActivity, err, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }


    }

}
