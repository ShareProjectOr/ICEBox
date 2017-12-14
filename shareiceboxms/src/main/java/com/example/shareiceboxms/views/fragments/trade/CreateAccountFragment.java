package com.example.shareiceboxms.views.fragments.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.Calendar;

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
    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {

        //  int a = startYear;
        //textview中添加文本
    }
}
