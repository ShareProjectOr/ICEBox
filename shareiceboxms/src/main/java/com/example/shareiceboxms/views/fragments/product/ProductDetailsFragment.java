package com.example.shareiceboxms.views.fragments.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.activities.ModifyPriceActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * Created by Lyu on 2017/12/12.
 * 商品详情
 */

public class ProductDetailsFragment extends BaseFragment {
    private View containerView = null;
    private String productId;
    private ImageView mDrawerIcon;
    private ImageView mSaoma;
    private TextView mProductName;
    private TextView mPrice;
    private TextView mSpecialPrice;
    private TextView mProductCode;
    private TextView mStorageTimeLimit;
    private TextView mSellAndUpload;
    private TextView mSpoilageUpload;
    private TextView mSelledPriceTotal;
    private TextView mSelledNum;
    private Button mModifyPrice;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.product_details_fragment));
            productId = FragmentFactory.getInstance().getSavedBundle().getString("productId");
            initViews();
            initDatas();
            initLisener();
        }
        return containerView;
    }

    private void initLisener() {
        mModifyPrice.setOnClickListener(this);
        mDrawerIcon.setOnClickListener(this);
        mSaoma.setOnClickListener(this);
    }

    private void initDatas() {
    }

    private void initViews() {
        mDrawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        mSaoma = (ImageView) containerView.findViewById(R.id.saoma);
        mProductName = (TextView) containerView.findViewById(R.id.productName);
        mPrice = (TextView) containerView.findViewById(R.id.price);
        mSpecialPrice = (TextView) containerView.findViewById(R.id.specialPrice);
        homeActivity = (HomeActivity) getActivity();
        mProductCode = (TextView) containerView.findViewById(R.id.productCode);
        mStorageTimeLimit = (TextView) containerView.findViewById(R.id.StorageTimeLimit);
        mSellAndUpload = (TextView) containerView.findViewById(R.id.sellAndUpload);
        mSpoilageUpload = (TextView) containerView.findViewById(R.id.spoilageUpload);
        mSelledPriceTotal = (TextView) containerView.findViewById(R.id.selledPriceTotal);
        mSelledNum = (TextView) containerView.findViewById(R.id.selledNum);
        mModifyPrice = (Button) containerView.findViewById(R.id.modifyPrice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modifyPrice:
                Bundle bundle = new Bundle();
                bundle.putString("productId", productId);
                homeActivity.jumpActivity(ModifyPriceActivity.class, bundle);
                break;
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
        }
    }
}
