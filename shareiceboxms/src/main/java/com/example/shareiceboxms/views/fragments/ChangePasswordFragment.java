package com.example.shareiceboxms.views.fragments;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        mCommit.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    private void bindViews() {
        homeActivity = (HomeActivity) getActivity();
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
            case R.id.cancel:
                homeActivity.onBackPressed();
                break;
            case R.id.commit:
                if (mNowPassword.getText().toString().isEmpty() || mNewPassword.getText().toString().isEmpty() || mConfirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "请将修改信息补充完整", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!mNowPassword.getText().toString().equals(PerSonMessage.loginPassword)) {
                    mNowPassword.setError("旧密码输入错误");
                    mNowPassword.requestFocus();
                    return;
                }

                if (mNewPassword.getText().toString().length() < 6) {
                    mNewPassword.setError("密码至少为6位");
                    mNewPassword.requestFocus();
                    return;
                }
                if (!mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                    mConfirmPassword.setError("两次输入的新密码不一致");
                    mConfirmPassword.requestFocus();
                    return;
                }

                repairPassword();

                break;
        }
    }

    private void repairPassword() {
        final Dialog dialog = MyDialog.loadDialog(getContext());
        dialog.show();
        final Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("userID", PerSonMessage.userId);
        requestbody.put("loginPassword", mNewPassword.getText().toString());
        requestbody.put("appUserID", PerSonMessage.userId);
        new AsyncTask<Void, Void, Boolean>() {
            String response;
            String err;

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    response = OkHttpUtil.post(HttpRequstUrl.REPAIR_PASSWORD_URL, JsonUtil.mapToJson(requestbody));
                    JSONObject object = new JSONObject(response);
                    Log.e("response", response);
                    err = object.getString("err");
                    if (object.getString("err").equals("")) {
                        //  Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_LONG).show();
                        return true;
                        //  homeActivity.onBackPressed();
                    }
                } catch (IOException e) {
                    err = RequstTips.NetWork_ERROR;
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
                    Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_LONG).show();
                    PerSonMessage.loginPassword = mNewPassword.getText().toString();
                    homeActivity.onBackPressed();
                } else {
                    Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                }

            }
        }.execute();

    }
}
