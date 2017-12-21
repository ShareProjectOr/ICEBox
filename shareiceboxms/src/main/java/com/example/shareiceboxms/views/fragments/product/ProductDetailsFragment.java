package com.example.shareiceboxms.views.fragments.product;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemProductType;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.activities.ModifyPriceActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lyu on 2017/12/12.
 * 商品详情
 */

public class ProductDetailsFragment extends BaseFragment {
    private View containerView = null;
    private int categoryID;
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
    private ItemProductType itemProductType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.product_details_fragment));
            categoryID = FragmentFactory.getInstance().getSavedBundle().getInt("categoryID");
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
        final Map<String, Object> map = new HashMap<>();
        map.put("categoryID", categoryID);
        map.put("appUserID", PerSonMessage.userId);
        final Dialog dialog = MyDialog.loadDialog(getActivity());
        dialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            String err;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    JSONObject object = new JSONObject(OkHttpUtil.post(
                            HttpRequstUrl.PRODUCT_TYPE_DETAIL_URL, JsonUtil.mapToJson(map)));
                    err = object.getString("err");
                    if (!err.equals("") && !err.equals("null")) {
                        return false;
                    }
                    itemProductType = new ItemProductType();
                    itemProductType.bindData(object.getJSONObject("d"));

                } catch (JSONException e) {
                    err = RequstTips.JSONException_Tip;
                    return false;
                } catch (IOException e) {
                    err = RequstTips.getErrorMsg(e.getMessage());
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                if (aBoolean) {
                    mModifyPrice.setVisibility(View.VISIBLE);
                    mProductName.setText(itemProductType.categoryName);
                    mPrice.setText("￥" + itemProductType.categoryPrice);
                    mSpecialPrice.setText("￥" + itemProductType.activityPrice);
                    mProductCode.setText(String.valueOf(itemProductType.categoryID));
                    String[] time = SecondToDate.formatLongToTimeStr(Long.parseLong(String.valueOf(itemProductType.storageTimeLimit)));
                    if (!time[0].equals("0")) {
                        mStorageTimeLimit.setText(time[0] + "天" + time[1] + "时" + time[2] + "分" + time[3] + "秒");
                    } else {
                        if (!time[1].equals("0")) {
                            mStorageTimeLimit.setText(time[1] + "时" + time[2] + "分" + time[3] + "秒");
                        } else {
                            mStorageTimeLimit.setText(time[2] + "分" + time[3] + "秒");
                        }
                    }

                    mSellAndUpload.setText(String.valueOf(itemProductType.salingNum) + "/" + String.valueOf(itemProductType.noExhibitNum));
                    //折损率  折损数除以 折损数加上售卖中数量
//                    float breakfloat = itemProductType.breakNum / (itemProductType.breakNum + itemProductType.salingNum);

                /*   mSpoilageUpload.setText(itemProductType.breakNum +
                            "(" + String.valueOf(breakfloat * 100) + "%)");*/
                    mSelledPriceTotal.setText(String.valueOf(itemProductType.soldOutPrice));
                    mSelledNum.setText(String.valueOf(itemProductType.soldOutNum));

                } else {
                    mModifyPrice.setVisibility(View.GONE);
                    Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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
                bundle.putInt("categoryID", categoryID);
                bundle.putString("categoryPrice", itemProductType.categoryPrice);
                bundle.putString("activityPrice", itemProductType.activityPrice);
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
