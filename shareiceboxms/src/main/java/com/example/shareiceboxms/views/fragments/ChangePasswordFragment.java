package com.example.shareiceboxms.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;

/**
 * Created by Administrator on 2017/12/12.
 */

public class ChangePasswordFragment extends BaseFragment {
    private ImageView drawerIcon, saoma;
    private HomeActivity homeActivity;
    private View contentView;
    private EditText mNowPassword;
    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private Button mCommit;
    private Button mCancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.change_password_fragment));
            bindViews();
            initListener();
        }
        return contentView;
    }

    private void initListener() {
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
    }

    private void bindViews() {
        mNowPassword = (EditText) contentView.findViewById(R.id.nowPassword);
        mNewPassword = (EditText) contentView.findViewById(R.id.newPassword);
        mConfirmPassword = (EditText) contentView.findViewById(R.id.confirmPassword);
        mCommit = (Button) contentView.findViewById(R.id.commit);
        mCancel = (Button) contentView.findViewById(R.id.cancel);
        drawerIcon = (ImageView) contentView.findViewById(R.id.drawerIcon);
        saoma = (ImageView) contentView.findViewById(R.id.saoma);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
        }
    }
}
