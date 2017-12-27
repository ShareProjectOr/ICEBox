package com.example.shareiceboxms.views.fragments.trade;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemTradeTotal;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by WH on 2017/12/13.
 */

public class CreateAccountFragment extends BaseFragment {
    private View containerView;
    private Button createAccountSubmit;
    private ImageView drawerIcon, saoma;
    private RelativeLayout selectTime;
    private TextView timeSelector, title;
    private DoubleDatePickerDialog datePickerDialog;
    HomeActivity homeActivity;
    private String[] time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_create_account));
            initViews();
            initDatas();
        }
        return containerView;
    }

    private void initViews() {
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        createAccountSubmit = (Button) containerView.findViewById(R.id.createAccountSubmit);
        selectTime = (RelativeLayout) containerView.findViewById(R.id.selectTime);
        timeSelector = (TextView) containerView.findViewById(R.id.timeSelector);
        title = (TextView) containerView.findViewById(R.id.title);
        drawerIcon.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        saoma.setOnClickListener(this);
        title.setText("创建结算工单");
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        datePickerDialog = new DoubleDatePickerDialog(getContext(), 0, this
                , Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE), true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccountSubmit:
                //提交数据
                Map<String, Object> params = RequestParamsContants.getInstance().getCreateJieSuanParams();
                params.put("createTime", RequestParamsContants.getInstance().getSelectTime(time));
                TradeCreateJieSuanTask task = new TradeCreateJieSuanTask(params);
                task.execute();
                break;
            case R.id.selectTime:
                //弹出时间选择框
                datePickerDialog.show();
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
    public String[] onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {

        time = super.onDateSet(startDatePicker, startYear, startMonthOfYear, startDayOfMonth, endDatePicker, endYear, endMonthOfYear, endDayOfMonth);
        timeSelector.setText(SecondToDate.getDateUiShow(time));
        return null;
    }

    //获取创建结算工单异步任务
    private class TradeCreateJieSuanTask extends AsyncTask<Void, Void, Boolean> {

        private String response;
        private String err = "net_work_err";
        private boolean isCommited = false;
        private Map<String, Object> params;

        TradeCreateJieSuanTask(Map<String, Object> params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_TOTAL_URL, JsonUtil.mapToJson(this.params));
                isCommited = (JsonDataParse.getInstance().getSingleObject(response.toString()) != null);
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
                Toast.makeText(homeActivity, "创建成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(homeActivity, "创建失败，请重试！", Toast.LENGTH_SHORT).show();
                Log.e("request error :", response + "");
            }
        }

        @Override
        protected void onCancelled() {

        }


    }
}
