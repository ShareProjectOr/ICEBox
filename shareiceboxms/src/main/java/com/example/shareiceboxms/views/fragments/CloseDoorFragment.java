package com.example.shareiceboxms.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareiceboxms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseDoorFragment extends BaseFragment {

    private String callbackMsg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_close_door, container, false);
    }

}
