package com.example.shareiceboxms.views.fragments.product;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/12.
 */

public class ModifyPriceFragment extends BaseFragment {
    private View contentView;
    private TextView mProductName;
    private EditText mProductPrice;
    private EditText mProductSpecPrice;
    private Button mCommit;
    private Button mCancel;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.modify_price_fragment));
            bindViews();
            initDatas();
            initListener();
        }
        return contentView;
    }

    private void initListener() {
        mCommit.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    private void initDatas() {
        bundle = getActivity().getIntent().getBundleExtra("intentdata");
        mProductPrice.setText(bundle.getString("categoryPrice"));
        mProductSpecPrice.setText(bundle.getString("activityPrice"));
    }

    private void bindViews() {

        mProductName = (TextView) contentView.findViewById(R.id.productName);
        mProductPrice = (EditText) contentView.findViewById(R.id.productPrice);
        mProductSpecPrice = (EditText) contentView.findViewById(R.id.productSpecPrice);
        mCommit = (Button) contentView.findViewById(R.id.commit);
        mCancel = (Button) contentView.findViewById(R.id.cancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                if (mProductPrice.getText().toString().isEmpty() || mProductSpecPrice.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "单价或活动价不能为0", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case R.id.cancel:
                getActivity().onBackPressed();
                break;
        }
    }


    private void repairPassword() {
        final Dialog dialog = MyDialog.loadDialog(getContext());
        dialog.show();
        final Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("categoryPrice", mProductPrice.getText().toString());
        requestbody.put("categoryID", bundle.getString("categoryID"));
        requestbody.put("appUserID", PerSonMessage.userId);
        new AsyncTask<Void, Void, Boolean>() {
            String response;
            String err;

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    response = OkHttpUtil.post(HttpRequstUrl.EDIT_PRICE_UERL, JsonUtil.mapToJson(requestbody));
                    JSONObject object = new JSONObject(response);
                    Log.e("response", response);
                    err = object.getString("err");
                    if (object.getString("err").equals("")) {
                        //  Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_LONG).show();
                        return true;
                        //  homeActivity.onBackPressed();
                    }
                } catch (IOException e) {
                    err = RequstTips.getErrorMsg(e.getMessage());
                    return false;
                } catch (JSONException e) {
                    err = RequstTips.JSONException_Tip;
                    return false;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                if (aBoolean) {
                    Toast.makeText(getActivity(), "价格修改成功", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                }

            }
        }.execute();

    }
}
