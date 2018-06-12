package com.example.zhazhijiguanlixitong.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.rey.material.app.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import contentprovider.UserMessage;
import fragment.FinanceManagerFragment;
import fragment.Fragment_Home;
import fragment.Fragment_SearchTrade;
import fragment.UserManagerFragment;
import httputil.Constance;
import httputil.ConstanceMethod;
import httputil.Sql;


public class HomeActivity extends AppCompatActivity implements OnItemClickListener {
    private FragmentManager mManager;
    private FragmentTransaction mTransaction;
    private Context mContext;
    private AlertView mAlertView;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        SaveUserMessager();
        checktheupdata();
        BottomSheetDialog  dialog = new BottomSheetDialog(this);
       // dialog.
       // dialog.setContentView();
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

    private void initView() {
        mContext = this;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.framecontent, new Fragment_Home());
        transaction.commit();
    }


    private void checktheupdata() {
        final RequestParams params = new RequestParams();
        params.addBodyParameter("version", getVersion());
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(4000);
        http.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, Constance.GET_APP_VERSION_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    Log.e("SSS", "SS" + responseInfo.result);
                    final JSONObject object = new JSONObject(responseInfo.result);

                    if (!getVersion().equals(object.getJSONObject("d").getString("version"))) {
                        new AlertView(getString(R.string.tishi), getString(R.string.jianchedaoxinbanben), getString(R.string.zhanshibuyong), new String[]{getString(R.string.queding)}, null, HomeActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if (position == 0) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = null;
                                    try {
                                        content_url = Uri.parse(object.getJSONObject("d").getString("fileUrl"));
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplication(), R.string.huoquxiazaidizhicuowu, Toast.LENGTH_LONG).show();
                                    }
                                    intent.setData(content_url);
                                    startActivity(intent);
                                }
                            }
                        }).setCancelable(true).show();
                    }
                } catch (JSONException ignored) {
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    public void swicthFragmentItem(int position) {
        switch (position) {
            case 0:
                switch (UserMessage.getManagerType()) {
                    case "2":
                        replaceFragment(new UserManagerFragment());
                        currentPage = 1;
                        break;
                    case "3":
                        ConstanceMethod.startIntent(HomeActivity.this, PersonalInfoActivity.class, 0);
                        currentPage = 0;
                        break;
                }


                break;
            case 1:
                ConstanceMethod.startIntent(HomeActivity.this, SearchMachineActivity.class, 0);
                currentPage = 0;
                break;
            case 2:
                replaceFragment(new Fragment_SearchTrade());
                currentPage = 3;
                break;
            case 3:
                ConstanceMethod.startIntent(this, TotalActivity.class, null);
                currentPage = 0;
                break;
            case 4:
                if (UserMessage.getManagerType().equals("3")) {
                    new AlertView(getString(R.string.tishi), getString(R.string.yuequan), null, new String[]{getString(R.string.queding)}, null, this, AlertView.Style.Alert, null).show();
                    return;
                }
                replaceFragment(new FinanceManagerFragment());
                currentPage = 5;
                break;
            default:
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framecontent, fragment);
        transaction.commit();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentPage == 0) {
                if (mAlertView == null) {
                    mAlertView = new AlertView(getString(R.string.tishi), getString(R.string.tuichuguanlixitong), getString(R.string.quexiao), new String[]{getString(R.string.queding)}, null, this, AlertView.Style.ActionSheet, this).setCancelable(true);
                } else {
                    mAlertView.show();
                }
            } else {
                replaceFragment(new Fragment_Home());
                currentPage = 0;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void DoSql() {
        Sql sql = new Sql(this);
        boolean isupdated = false;
        for (String saves : sql.getAllManagerId()) {
            if (UserMessage.getManagerId().equals(saves)) {
                Log.d("debug", "managerId" + UserMessage.getManagerId());
                sql.updateContact(UserMessage.getManagerId(), UserMessage.getManagerNum(), UserMessage.getManagerPass());
                isupdated = true;
                break;
            }
        }
        if (!isupdated) {
            sql.insertContact(UserMessage.getManagerId(), UserMessage.getManagerNum(), UserMessage.getManagerPass());
        }

    }

    //记录登录状态
    private void SaveUserMessager() {
        ConstanceMethod.isFirstLogin(mContext, false);
        DoSql();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mManager != null) {
            mManager = null;
        }
        if (mTransaction != null) {
            mTransaction = null;
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position == 0) {
            currentPage = 0;
            onBackPressed();
        } else if (position == 1) {
            mAlertView.dismiss();
        }
    }
}
