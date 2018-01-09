package com.example.shareiceboxms.views.fragments.trade;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.example.shareiceboxms.models.beans.ItemPerson;
import com.example.shareiceboxms.models.beans.trade.ItemTradeAccount;
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
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by WH on 2017/12/13.
 */

public class CreateAccountFragment extends BaseFragment {
    private static String TAG = "CreateAccountFragment";
    private View containerView;
    private Button createAccountSubmit;
    private ImageView drawerIcon, saoma;
    private RelativeLayout selectTime;
    private TextView timeSelector, title;
    private DoubleDatePickerDialog datePickerDialog;
    private Dialog dialog;
    HomeActivity homeActivity;
    CreateAccountLisenner createAccountLisenner;

    private String[] time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_create_account));
            initViews();
            initLisenner();
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

    }

    private void initLisenner() {
        createAccountSubmit.setOnClickListener(this);
        drawerIcon.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        saoma.setOnClickListener(this);
    }

    private void initDatas() {
        homeActivity = (HomeActivity) getActivity();
        datePickerDialog = new DoubleDatePickerDialog(getContext(), 0, this
                , Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)
                , Calendar.getInstance().get(Calendar.DATE), true);
        title.setText("创建结算工单");
        dialog = MyDialog.loadDialog(homeActivity);
    }

    public void setCreateAccountLisenner(CreateAccountLisenner createAccountLisenner) {
        this.createAccountLisenner = createAccountLisenner;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccountSubmit:
                if (TextUtils.equals(timeSelector.getText().toString(), "")) {
                    Toast.makeText(getContext(), "时间范围必须选择", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<ItemPerson> agentIDs = (ArrayList<ItemPerson>) FragmentFactory.getInstance().getSavedBundle().getSerializable("CREATE_AGENTS_ACCOUNT");
                for (int i = 0; i < agentIDs.size(); i++) {
                    //提交数据
                    Map<String, Object> params = RequestParamsContants.getInstance().getCreateJieSuanParams();
                    params.put("agentID", agentIDs.get(i).userID);
                    params.put("createTime", RequestParamsContants.getInstance().getSelectTime(time));
                    TradeCreateJieSuanTask task = new TradeCreateJieSuanTask(params);
                    task.execute();
                }
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
        private String err = "";
        private ItemTradeAccount itemTradeAccount;
        private Map<String, Object> params;

        TradeCreateJieSuanTask(Map<String, Object> params) {
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
                Log.e(TAG, "request url: " + HttpRequstUrl.TRADE_CREATE_JIESUAN_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_CREATE_JIESUAN_URL, JsonUtil.mapToJson(this.params));
                Log.e(TAG, "response" + response);
                if (response == null) {
                    return false;
                } else {
                    err = JsonDataParse.getInstance().getErr(response);
                    if ((!TextUtils.equals(err, "")) && !err.equals("null")) {
                        return false;
                    }
                }
                itemTradeAccount = ItemTradeAccount.bindTradeAccount(JsonDataParse.getInstance().getSingleObject(response));
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
            if (createAccountLisenner != null) {
                createAccountLisenner.createSuccess(success, err);
            }
           /* if (success) {
                Toast.makeText(homeActivity, "创建成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(homeActivity, "创建失败，请重试！" + err, Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    public interface CreateAccountLisenner {
        void createSuccess(boolean success, String err);
    }
}
