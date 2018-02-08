package com.example.shareiceboxms.views.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.ConstanceMethod;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.contants.Sql;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.activities.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.zackratos.ultimatebar.UltimateBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WH on 2017/11/28.
 */

public class WelcomeFragment extends BaseFragment {
    private SharedPreferences mSharedPreferences;
    private UserLoginTask mAuthTask;
    private Sql sql;
    private LoginActivity loginActivity;
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            UltimateBar ultimateBar = new UltimateBar(getActivity());
            ultimateBar.setImmersionBar();
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_welcome));
            initViews();
        }

        return contentView;
    }


    private void initViews() {
        mSharedPreferences = ConstanceMethod.getSharedPreferences(getActivity(), "ShowWelcome");
        loginActivity = (LoginActivity) getActivity();
        sql = new Sql(getActivity());
        isAutoLogin();
    }

    private void isAutoLogin() {
        Log.d("debug", "isFirst==========" + mSharedPreferences.getBoolean("isFirst", true));
        if (!mSharedPreferences.getBoolean("isFirst", true)) {
            /*
            * 解析地址
            * */
            // loginActivity.parseAddress();
            hasLogined();
        } else {
            isNotLogined();
        }
    }

    //之前登陆过
    private void hasLogined() {
        int id = sql.getAllCotacts().size();
        Cursor cursor = sql.getData(id);
        cursor.moveToFirst();

        String loginAccount = cursor.getString(cursor.getColumnIndex(Sql.CONTACTS_COLUMN_LOGINACCOUNT));
        String loginPassword = cursor.getString(cursor.getColumnIndex(Sql.CONTACTS_COLUMN_LOGINPASSWORD));
        Log.e("loginAccount", loginAccount + loginPassword);
        if (loginAccount.isEmpty() || loginPassword.isEmpty()) {
            isNotLogined();
            return;
        }
        mAuthTask = new UserLoginTask(loginAccount, loginPassword);
        mAuthTask.execute();
    }

    //第一次登陆
    private void isNotLogined() {
      /*  if (animation != null) {
            animation.cancel();
            welcome.clearAnimation();
        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginActivity.login();
            }
        }, 1000);
    }

    //登录异步任务
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String response;
        private String err = RequstTips.Server_ERROR_404;
        private JSONObject userJson;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
//            msgMap = new HashMap<>();
            userJson = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Map<String, Object> body = new HashMap<>();
            body.put("loginAccount", mEmail);
            body.put("loginPassword", mPassword);

            try {

                response = OkHttpUtil.post(HttpRequstUrl.LOGIN_URL, JsonUtil.mapToJson(body));
                Log.e("login_response", response);
                userJson = new JSONObject(response);
                err = userJson.getString("err");
                if (err.equals("") || err.equals("null")) {
                    return true;
                }
            } catch (IOException e) {
                Log.e("response", e.getMessage() + "");
                err = RequstTips.getErrorMsg(e.getMessage());
                return false;
            } catch (JSONException e) {
                err = RequstTips.JSONException_Tip + e.getMessage();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                loginActivity.jumpActivity(HomeActivity.class, null);
                PerSonMessage.loginPassword = mPassword;
                PerSonMessage.bindMessage(response);
            } else {
                Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                loginActivity.login();
            /*    passEdit.setError(err);
                passEdit.requestFocus();*/
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }


    }
}
