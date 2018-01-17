package com.example.shareiceboxms.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenDoorSuccessFragment extends BaseFragment {
    private HomeActivity homeActivity;
    private View containerView;
    private Button backhome;

    public OpenDoorSuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_open_door_success));
            initView();
            initData();
        }
        return containerView;
    }

    private void initView() {
        backhome = (Button) containerView.findViewById(R.id.backhome);
        backhome.setOnClickListener(this);
    }

    private void initData() {
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backhome:
                homeActivity.onBackPressed();
                break;
        }
    }
}
