package com.example.shareiceboxms.views.fragments.machine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.MachineListAdapter;
import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MachineItemAddView;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/11/27.
 */

public class MachineDetailFragment extends BaseFragment {
    private View containerView;
    private ScrollView scrollLayout;
    private android.support.design.widget.TabLayout machineTabLayout;
    private LinearLayout itemLayout;

    private ImageView drawerIcon, saoma;
    private TextView title;

    private TextView machineAddr, machineName, isOnLine, isException, managerName, machienCode;
    HomeActivity homeActivity;
    MachineItemAddView machineItemAddView;
    ItemMachine itemMachine;
    int curTabPosition = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_machine_detail));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        scrollLayout = (ScrollView) containerView.findViewById(R.id.scrollLayout);
        machineTabLayout = (android.support.design.widget.TabLayout) containerView.findViewById(R.id.machineTabLayout);
        itemLayout = (LinearLayout) containerView.findViewById(R.id.itemLayout);
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        title = (TextView) containerView.findViewById(R.id.title);
        title = (TextView) containerView.findViewById(R.id.title);

        machineAddr = (TextView) containerView.findViewById(R.id.machineAddr);
        machineName = (TextView) containerView.findViewById(R.id.machineName);
        isOnLine = (TextView) containerView.findViewById(R.id.isOnLine);
        isException = (TextView) containerView.findViewById(R.id.isException);
        managerName = (TextView) containerView.findViewById(R.id.managerName);
        machienCode = (TextView) containerView.findViewById(R.id.machienCode);

        title.setText("机器详情");
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
        machineTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                curTabPosition = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        machineItemAddView.addStateControlView(itemLayout);
                        if (itemMachine != null) {
                            machineItemAddView.updateStateControlUi(itemMachine);
                        }
                        break;
                    case 1:
                        machineItemAddView.addTeleControlView(itemLayout);
                        if (itemMachine != null) {
                            machineItemAddView.updateTeleControlUi(itemMachine);
                        }
                        break;
                    case 2:
                        machineItemAddView.addStockProductView(itemLayout, itemMachine);
                        break;
                }
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
        machineItemAddView = new MachineItemAddView(homeActivity);
        for (int i = 0; i < Constants.MachineItemOperator.length; i++) {
            machineTabLayout.addTab(machineTabLayout.newTab().setText(Constants.MachineItemOperator[i]));
        }
        getDatas();
    }

    private void getDatas() {
        MachineDetailTask machineListTask = new MachineDetailTask(RequestParamsContants.getInstance().getMachineDetailParams());
        machineListTask.execute();
    }

    /*
    * 更新UI
    * */
    private void updateUi() {
        machineAddr.setText(itemMachine.machineAddress);
        machineName.setText(itemMachine.machineName);
        isOnLine.setText(Constants.MachineOnLineState[itemMachine.networkState]);
        isOnLine.setTextColor(ContextCompat.getColor(getContext(), Constants.MachineStateColor[itemMachine.networkState]));
        isException.setText(itemMachine.faultState == 0 ? Constants.MachineFaultState[0] : Constants.MachineFaultState[1]);
        isException.setTextColor(ContextCompat.getColor(getContext(), itemMachine.faultState == 0 ? Constants.MachineStateColor[1] : Constants.MachineStateColor[0]));
        managerName.setText(itemMachine.itemManager.name);
        machienCode.setText(itemMachine.machineCode);
        switch (curTabPosition) {
            case 0:
                machineItemAddView.updateStateControlUi(itemMachine);
                break;
            case 1:
                machineItemAddView.updateTeleControlUi(itemMachine);
                break;
            case 2:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
        }
    }

    //获取机器详情异步任务
    private class MachineDetailTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "net_work_err";
        private Map<String, Object> params;
        private ItemMachine machine;

        MachineDetailTask(Map<String, Object> params) {
            this.params = params;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.MACHINE_DETAIL_URL, JsonUtil.mapToJson(this.params));
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject jsonD = jsonObject.getJSONObject("d");
                machine = ItemMachine.bindMachine(jsonD);
                Log.e("response", response.toString());
                return true;
            } catch (IOException e) {
                Log.e("erro", e.toString());
            } catch (JSONException e) {
                Log.e("erro", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                itemMachine = machine;
                updateUi();
            } else {
                Log.e("request error :", response + "");
                if (response == null) {
                    Toast.makeText(getContext(), "网络请求超时", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        protected void onCancelled() {

        }


    }
}
