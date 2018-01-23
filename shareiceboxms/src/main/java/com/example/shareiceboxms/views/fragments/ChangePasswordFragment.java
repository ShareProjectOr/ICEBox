package com.example.shareiceboxms.views.fragments;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.ConstanceMethod;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.contants.Sql;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.activities.LoginActivity;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/12.
 */

public class ChangePasswordFragment extends BaseFragment implements View.OnFocusChangeListener {
    private ImageView drawerIcon, saoma;
    private HomeActivity homeActivity;
    private View contentView;
    private EditText mNowPassword;
    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private ImageView oldPassClear, newPassClear, surePassClear;
    private TextView oldErrorTip, newErrorTip, sureErrorTip;
    private Button mCommit;
    private Button mCancel;
    private TextWatcher textWatcher;
    private int tipPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.change_password_fragment));
            bindViews();
            bindDatas();
            initListener();
        }
        return contentView;
    }

    private void bindDatas() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (tipPosition) {
                    case 0:
                        oldErrorTip.setVisibility(View.GONE);
                        break;
                    case 1:
                        newErrorTip.setVisibility(View.GONE);
                        break;
                    case 2:
                        sureErrorTip.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void initListener() {
        drawerIcon.setOnClickListener(this);
        saoma.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        oldPassClear.setOnClickListener(this);
        newPassClear.setOnClickListener(this);
        surePassClear.setOnClickListener(this);
        mNowPassword.addTextChangedListener(textWatcher);
        mNewPassword.addTextChangedListener(textWatcher);
        mConfirmPassword.addTextChangedListener(textWatcher);
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
        oldPassClear = (ImageView) contentView.findViewById(R.id.oldPassClear);
        newPassClear = (ImageView) contentView.findViewById(R.id.newPassClear);
        surePassClear = (ImageView) contentView.findViewById(R.id.surePassClear);
        oldErrorTip = (TextView) contentView.findViewById(R.id.oldErrorTip);
        newErrorTip = (TextView) contentView.findViewById(R.id.newErrorTip);
        sureErrorTip = (TextView) contentView.findViewById(R.id.sureErrorTip);
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
            case R.id.oldPassClear:
                mNowPassword.setText("");
                oldErrorTip.setVisibility(View.GONE);
                break;
            case R.id.newPassClear:
                mNewPassword.setText("");
                newErrorTip.setVisibility(View.GONE);
                break;
            case R.id.surePassClear:
                mConfirmPassword.setText("");
                sureErrorTip.setVisibility(View.GONE);
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
                    oldErrorTip.setVisibility(View.VISIBLE);
                    return;
                }

                if (mNewPassword.getText().toString().length() < 6) {
                    newErrorTip.setVisibility(View.VISIBLE);
                    return;
                }
                if (!mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                    sureErrorTip.setVisibility(View.VISIBLE);
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
                    if (object.getString("err").equals("") || object.getString("err").equals("null")) {
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
                    Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_LONG).show();
                    PerSonMessage.loginPassword = mNewPassword.getText().toString();
                    Dosql();
                    ConstanceMethod.isFirstLogin(getActivity(), true);
                    PerSonMessage.isexcit = true;
                    homeActivity.jumpActivity(LoginActivity.class, null);
                    //清空缓存数据
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                }

            }
        }.execute();

    }

    private void Dosql() {
        Sql sql = new Sql(getActivity());
        sql.deleteAllContact(sql.getAllCotacts().size());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.nowPassword:
                    tipPosition = 0;
                    break;
                case R.id.newPassword:
                    tipPosition = 1;
                    break;
                case R.id.confirmPassword:
                    tipPosition = 2;
                    break;
            }

        }
    }
}
