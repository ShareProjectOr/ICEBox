package com.example.shareiceboxms.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenDoorFailFragment extends BaseFragment {


    private View containerView;
    private HomeActivity mHomeAcyivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_open_door_fail));
            bindViews();
        }
        return containerView;
    }


    private void bindViews() {
        mHomeAcyivity = (HomeActivity) getActivity();
    }

    public void backhome(View v) {
        mHomeAcyivity.onBackPressed();
    }
}
