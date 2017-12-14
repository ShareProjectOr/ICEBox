package com.example.shareiceboxms.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountDetailFragment;

/**
 * Created by WH on 2017/11/27.
 */

public class BaseFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, DoubleDatePickerDialog.OnDateSetListener, HomeActivity.OnBackPressListener {
    public static BaseFragment curFragment;
    public static TradeAccountDetailFragment tradeAccountDetailFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = null;
        if (containerView == null) {
            int layoutid = ((savedInstanceState == null) ? 0 : savedInstanceState.getInt("layout_id"));
            containerView = inflater.inflate(layoutid, null, false);
        }
        ViewGroup parent = (ViewGroup) containerView.getParent();
        if (parent != null) {
            parent.removeView(containerView);
        }
        return containerView;
    }

    @Override
    public void onClick(View v) {

    }

    public void addFrameLayout(BaseFragment fragment, int frameId) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

    public void removeFrame(BaseFragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment);
        }

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {

    }

    @Override
    public void OnBackDown() {

    }
}
