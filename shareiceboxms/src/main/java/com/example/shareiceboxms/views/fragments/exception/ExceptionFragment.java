package com.example.shareiceboxms.views.fragments.exception;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;

import com.example.shareiceboxms.models.adapters.ExceptionListAdapter;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.ActionItem;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.models.helpers.MenuPop;
import com.example.shareiceboxms.models.helpers.TitlePopup;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.Calendar;

/**
 * Created by WH on 2017/11/27.
 * Edit by LYU on 2017/12/11.
 */

public class ExceptionFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    private View containerView;
    private BaseFragment curFrameFragment;
    private HomeActivity homeActivity;
    private ImageView drawerIcon, showPop, saoma;
    private ExceptionListAdapter mRecycleAdapter;
    private RecyclerView exceptionList;
    private Context mContext;
    //  private TitlePopup mTilePopup;
    private TextView exceptionType;
    private ListPopupWindow mTilePopup;
    private Switch chooseIsDetails;
    private LinearLayout exceptionTypeLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout selectTimeLayout;
    private DoubleDatePickerDialog datePickerDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_exception));
            initViews();
            initListener();
            initDatas();
        }
        return containerView;
    }

    private void initListener() {
        drawerIcon.setOnClickListener(this);
        showPop.setOnClickListener(this);
        chooseIsDetails.setOnCheckedChangeListener(this);
        selectTimeLayout.setOnClickListener(this);
        if (!mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setOnRefreshListener(this);
        }
        saoma.setOnClickListener(this);
    }

    private void initDatas() {
        homeActivity = (HomeActivity) mContext;
        homeActivity.setOnBackPressListener(this);
        new MyViewFactory(mContext).BuildRecyclerViewRule(exceptionList, new LinearLayoutManager(mContext), new DefaultItemAnimator(), true).setAdapter(mRecycleAdapter);
    }

    private void initViews() {
        mContext = getActivity();
        Calendar c = Calendar.getInstance();
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        exceptionList = (RecyclerView) containerView.findViewById(R.id.exception_list);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        selectTimeLayout = (RelativeLayout) containerView.findViewById(R.id.selectTime);
        exceptionTypeLayout = (LinearLayout) containerView.findViewById(R.id.exception_type_layout);
        datePickerDialog = new DoubleDatePickerDialog(mContext, 0, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
        showPop = (ImageView) containerView.findViewById(R.id.showpup);
        chooseIsDetails = (Switch) containerView.findViewById(R.id.Is_details);
        mRecycleAdapter = new ExceptionListAdapter(mContext);
        exceptionType = (TextView) containerView.findViewById(R.id.exception_type);
        mTilePopup = MenuPop.CreateMenuPop(mContext, exceptionTypeLayout, Constants.EXCEPTION_LV_TITLE);
        mRefreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.blue));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
            case R.id.showpup:
                mTilePopup.show();
                break;
            case R.id.selectTime:
                datePickerDialog.show();
                break;
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, Constants.REFREASH_DELAYED_TIME);
    }

    @Override
    public void OnBackDown() {
        Log.e("ExceptionFragment", "Onback");
        homeActivity.finishActivity();


    }

    @Override
    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {

        //  int a = startYear;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            chooseIsDetails.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_switch_open));
            chooseIsDetails.setTrackDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_switch_open));
        } else {
            chooseIsDetails.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_switch));
            chooseIsDetails.setTrackDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_switch));

        }
    }
}
