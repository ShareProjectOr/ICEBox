package com.example.shareiceboxms.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenDoorFailFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_open_door_fail));
    }

}
