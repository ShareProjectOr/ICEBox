package com.example.shareiceboxms.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;

/**
 * Created by LYU on 2017/12/12.
 * 个人资料界面
 */

public class PerSonFragment extends BaseFragment {
    private View contentView;
    private TextView mPhoneNum;
    private TextView mEmail;
    private TextView mAddress;
    private TextView mIdCard;
    private TextView mLastLoginTime;
    private TextView mLastLoginIp;
    private TextView mPayType;
    private TextView mServerPrecent;
    private TextView mMinSettlement;
    private TextView mCompanyName;
    private TextView mCompanyCode;
    private TextView mPublicAccount;
    private ImageView drawerIcon, saoma;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.personfragment));
            bindViews();
            bindDatas();
            initListener();
        }
        return contentView;
    }

    private void bindDatas() {
        mPhoneNum.setText("电话:\t" + PerSonMessage.tel);
        mEmail.setText("邮箱:\t" + PerSonMessage.email);
        mAddress.setText("所在地址:\t" + PerSonMessage.address);
        mIdCard.setText("身份证号:\t" + PerSonMessage.idCard);
        mLastLoginTime.setText("最后登录时间:\t" + PerSonMessage.lastLoginTime);
        mLastLoginIp.setText("最后登录Ip:\t" + PerSonMessage.loginIP);
        if (PerSonMessage.role.equals("2")) {
            mPayType.setText("结算方式:\t" + Constants.settleWay[PerSonMessage.settleWay]);
            mServerPrecent.setText("服务费比例:\t" + PerSonMessage.settlementProportion);
            mMinSettlement.setText("最低结算金额:\t" + PerSonMessage.minBalance);
        } else {
            mPayType.setVisibility(View.GONE);
            mServerPrecent.setVisibility(View.GONE);
            mMinSettlement.setVisibility(View.GONE);
        }
        Log.e("minBalance",PerSonMessage.minBalance);
        mCompanyName.setText("企业名称:\t" + PerSonMessage.companyName);
        mCompanyCode.setText("信用代码:\t" + PerSonMessage.companyCreditCode);
        if (PerSonMessage.role.equals("2") || PerSonMessage.role.equals("1")) {
            mPublicAccount.setText("对公账号:\t" + PerSonMessage.bankAccount);
        } else {
            mPublicAccount.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
    }

    private void bindViews() {
        homeActivity = (HomeActivity) getActivity();
        mPhoneNum = (TextView) contentView.findViewById(R.id.phoneNum);
        mEmail = (TextView) contentView.findViewById(R.id.Email);
        mAddress = (TextView) contentView.findViewById(R.id.Address);
        mIdCard = (TextView) contentView.findViewById(R.id.IdCard);
        mLastLoginTime = (TextView) contentView.findViewById(R.id.lastLoginTime);
        mLastLoginIp = (TextView) contentView.findViewById(R.id.lastLoginIp);
        mPayType = (TextView) contentView.findViewById(R.id.payType);
        mServerPrecent = (TextView) contentView.findViewById(R.id.serverPrecent);
        mMinSettlement = (TextView) contentView.findViewById(R.id.minSettlement);
        mCompanyName = (TextView) contentView.findViewById(R.id.companyName);
        mCompanyCode = (TextView) contentView.findViewById(R.id.companyCode);
        mPublicAccount = (TextView) contentView.findViewById(R.id.publicAccount);
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
