package com.example.shareiceboxms.views.fragments.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by Administrator on 2017/12/12.
 */

public class ModifyPriceFragment extends BaseFragment {
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.modify_price_fragment));
        }
        return contentView;
    }
}
