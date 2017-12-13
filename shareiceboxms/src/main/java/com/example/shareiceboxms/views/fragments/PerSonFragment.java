package com.example.shareiceboxms.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;

/**
 * Created by Administrator on 2017/12/12.
 */

public class PerSonFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.personfragment));
    }
}
