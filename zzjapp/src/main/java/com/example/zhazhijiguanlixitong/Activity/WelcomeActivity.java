package com.example.zhazhijiguanlixitong.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.zackratos.ultimatebar.UltimateBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ViewUtils.ObjectAnimatorUtil;
import contentprovider.UserMessage;
import httputil.Constance;
import httputil.ConstanceMethod;
import httputil.HttpRequest;
import httputil.LoginAnim;
import httputil.Sql;
import otherutils.Tip;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private UserLoginTask mAuthTask;
    private Sql sql;
    private LinearLayout welcome;
    private String tsy;
    private AlphaAnimation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
        initView();
        mSharedPreferences = ConstanceMethod.getSharedPreferences(this, "ShowWelcome");
        IsAutoLogin();

    }

    private void IsAutoLogin() {
        Log.d("debug", "isFirst==========" + mSharedPreferences.getBoolean("isFirst", true));
        if (!mSharedPreferences.getBoolean("isFirst", true)) {
            hasLogined();
        } else {
            isNotLogined();
        }
    }


    private void initView() {
        setContentView(R.layout.activity_welcome);
        sql = new Sql(this);
        welcome = (LinearLayout) findViewById(R.id.welcome);
        animation = new AlphaAnimation(0, 1);
        animation.setDuration(2000);
        welcome.setAnimation(animation);
        animation.start();

//        mLoginAnim = new LoginAnim();
//        dialog = mLoginAnim.createLoginAnim(this);
//        dialog.show();
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void isNotLogined() {
        if (animation != null) {
            animation.cancel();
            welcome.clearAnimation();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private void hasLogined() {
        int id = sql.getAllCotacts().size();
        Cursor cursor = sql.getData(id);
        cursor.moveToFirst();

        String managerNum = cursor.getString(cursor.getColumnIndex(Sql.CONTACTS_COLUMN_MANAGERNUM));
        String managerPass = cursor.getString(cursor.getColumnIndex(Sql.CONTACTS_COLUMN_LOGINPASSWORD));
        Log.e("managerNum", managerNum + managerPass);
        if (managerNum.isEmpty() || managerPass.isEmpty()) {
            isNotLogined();
            return;
        }
        mAuthTask = new UserLoginTask(managerNum, managerPass);
        mAuthTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animation != null) {
            animation.cancel();
            welcome.clearAnimation();
            animation = null;
        }

    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> implements OnItemClickListener {

        private final String mEmail;
        private final String mPassword;

        private String err = getString(R.string.wangluobugeili);
        private String response = getString(R.string.qingqiushibai);
        private JSONObject userMessage;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            //  Map<String, String> body = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            body.put("managerNum", mEmail);
            body.put("logonPassword", mPassword);
            try {
                response = HttpRequest.postString(Constance.LOGIN_URL, body);
                JSONObject jsonResponse = new JSONObject(response);
                err = jsonResponse.getString("err");
                tsy = jsonResponse.getString("tsy");
                if (err.equals("")) {
                    userMessage = jsonResponse.getJSONObject("d");
                    int managerType = Integer.parseInt(userMessage.getString("managerType"));
                    if (managerType < 2) {
                        err = getString(R.string.denglutishi);//"手机端目前只开放代理商和机器管理员使用 \n系统管理员或超级管理员请使用网页版"
                        return false;
                    }
                    Log.d("debug", "fdsfsd___" + userMessage);
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                response = getString(R.string.wangluobugeili);
                return false;
            } catch (JSONException e) {
                response =getString(R.string.fuwuqikaixiaocai_chongshi) + e;
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mLoginAnim.destoryDialog()
            if (animation != null) {
                animation.cancel();
                welcome.clearAnimation();
            }
            if (success) {
                UserMessage.setManagerPass(mPassword);
                UserMessage.setTsy(tsy);
                if (userMessage != null) {
                    UserMessage.setUserMessage(userMessage);
                }
                ConstanceMethod.startIntent(WelcomeActivity.this, HomeActivity.class, null);
                finish();
            } else {
                Toast.makeText(getApplication(), err, Toast.LENGTH_LONG).show();
                ConstanceMethod.startIntent(WelcomeActivity.this, LoginActivity.class, null);
                finish();

            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }


        private Map<String, String> addMessageToMap(JSONObject userMessage) throws JSONException {
            Map<String, String> msgMap = new HashMap<>();
            msgMap.put("managerID", userMessage.getString("managerID"));
            msgMap.put("managerNum", userMessage.getString("managerNum"));
            msgMap.put("managerType", userMessage.getString("managerType"));
            msgMap.put("activatedType", userMessage.getString("activatedType"));
            msgMap.put("managerName", userMessage.getString("managerName"));
            msgMap.put("managerTelephone", userMessage.getString("managerTelephone"));
            msgMap.put("managerEmail", userMessage.getString("managerEmail"));
            msgMap.put("managerAddress", userMessage.getString("managerAddress"));
            msgMap.put("managerCard", userMessage.getString("managerCard"));
            msgMap.put("managerCompany", userMessage.getString("managerCompany"));
            msgMap.put("agentID", userMessage.getString("agentID"));
            msgMap.put("agentName", userMessage.getString("agentName"));
            msgMap.put("managerBankAccount", userMessage.getString("managerBankAccount"));
            msgMap.put("companyAddress", userMessage.getString("companyAddress"));
            msgMap.put("companyNum", userMessage.getString("companyNum"));
            msgMap.put("divideProportion", userMessage.getString("divideProportion"));
            return msgMap;
        }

        @Override
        public void onItemClick(Object o, int position) {
            if (position == 0) {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
