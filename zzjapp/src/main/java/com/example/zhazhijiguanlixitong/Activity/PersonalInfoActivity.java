package com.example.zhazhijiguanlixitong.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import ViewUtils.ChangePerInfo;

import contentprovider.UserMessage;
import fragment.Fragment_Personal_info;
import fragment.Fragment_RepairPassword;
import httputil.Constance;

import httputil.ConstanceMethod;

import httputil.JsonParseUtil;
import httputil.Sql;
import httputil.XUtils;


public class PersonalInfoActivity extends AppCompatActivity {
    private Fragment_Personal_info fragment_personal_info;
    private int CurrentPageTag = 1;
    private Fragment_RepairPassword fragment_repairPassword;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
    }

    private void initView() {
        fragment_personal_info = new Fragment_Personal_info();
        fragment_repairPassword = new Fragment_RepairPassword();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.real_container, fragment_personal_info);
        transaction.commit();
    }


    public void ReplaceToFragment_Personal_info() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.real_container, new Fragment_Personal_info());
        transaction.commit();
        CurrentPageTag = 1;
    }


    public void ReplaceToFragment_Repair_Password() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.real_container, fragment_repairPassword);
        transaction.commit();
        CurrentPageTag = 2;
    }

    @Override
    public void onBackPressed() {
        switch (CurrentPageTag) {
            case 1:
                finish();
                break;
            case 2:
                ReplaceToFragment_Personal_info();
                break;
        }
    }
}
