package com.example.shareiceboxms.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.presentors.LoginAnimPresentor;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.R;

import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WH on 2017/11/28.
 * LYU Repair on 2017/12/15
 */

public class LoginFragment extends BaseFragment {
    private LoginActivity loginActivity;
    private View containerView;
    private EditText accountEdit, passEdit;
    private CheckBox isRemember;
    private Button loginBnt;
    private ImageView isClose, isSee;
    private ProgressBar loginBar;
    private LinearLayout editLayout, barLayout;
    private RelativeLayout passLayout;
    private boolean isSeeClicked = false;
    private boolean isPassEditFoucsed = false;
    private UserLoginTask mAuthTask = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_login));
        initViews();
        initDatas();
        return containerView;
    }

    private void initDatas() {

    }

    private void initViews() {
        loginActivity = (LoginActivity) getActivity();
        accountEdit = (EditText) containerView.findViewById(R.id.accountEdit);
        isClose = (ImageView) containerView.findViewById(R.id.isClose);
        passEdit = (EditText) containerView.findViewById(R.id.passEdit);
        isSee = (ImageView) containerView.findViewById(R.id.isSee);
        isRemember = (CheckBox) containerView.findViewById(R.id.isRemember);
        loginBnt = (Button) containerView.findViewById(R.id.login);
        loginBar = (ProgressBar) containerView.findViewById(R.id.loginBar);
        editLayout = (LinearLayout) containerView.findViewById(R.id.editLayout);
        barLayout = (LinearLayout) containerView.findViewById(R.id.barLayout);
        passLayout = (RelativeLayout) containerView.findViewById(R.id.passLayout);
        isClose.setOnClickListener(this);
        isSee.setOnClickListener(this);
        loginBnt.setOnClickListener(this);
        passEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isPassEditFoucsed = hasFocus;
            }
        });
        isRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loginBnt.setVisibility(View.VISIBLE);
                passLayout.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
//                mAuthTask = new UserLoginTask(accountEdit.getText().toString(), passEdit.getText().toString());
//                mAuthTask.execute();
//                LoginAnimPresentor.loginAnim(editLayout, barLayout);
//                loginBnt.setVisibility(View.GONE);
//                passLayout.setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.setClass(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.isClose:
                accountEdit.setText("");
                break;
            case R.id.isSee:
                isSeeClicked = !isSeeClicked;
                setCursorAfterContent();
                if (isSeeClicked) {
                    isSee.setImageResource(R.mipmap.see);
                    passEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//设置密码可见
                } else {
                    isSee.setImageResource(R.mipmap.unsee_pass);
                    passEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);//设置密码不可见
                }
                break;
        }
    }

    /*
    *  获得焦点时将光标设置在后面
    * */
    private void setCursorAfterContent() {
        passEdit.setCursorVisible(false);
        if (isPassEditFoucsed) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Editable ea = passEdit.getText();
                    passEdit.setSelection(ea.length());
                    passEdit.setCursorVisible(true);
                }
            }, 100);
        }
    }

    //登录异步任务
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String response;
        private String err = "net_work_err";
        private String tsy;
        //        private Map<String, String> msgMap;
        private String userJson;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
//            msgMap = new HashMap<>();
            userJson = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Map<String, Object> body = new HashMap<>();
//            body.put("loginAccount", mEmail);
//            body.put("loginPassword", mPassword);
            body.put("loginAccount", " 刘江东");
            body.put("loginPassword", "123456");


            try {
                response = OkHttpUtil.post(HttpRequstUrl.LOGIN_URL, JsonUtil.mapToJson(body));
                Log.e("login_response", response.toString());
            } catch (IOException e) {
                Log.e("erro", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            //loginAnim.destoryDialog();
            if (success) {
                loginActivity.jumpActivity(HomeActivity.class, null);

            } else {
//                Log.e("login_response", response);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }


    }

    @Override
    public void OnBackDown() {
        super.OnBackDown();
    }
}
