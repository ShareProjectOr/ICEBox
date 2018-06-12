package com.example.zhazhijiguanlixitong.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zhazhijiguanlixitong.R;

import fragment.Fragment_SearchAccuntLog;

public class SearchAccountLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_account_log);
        AddFragment();
    }

    private void AddFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.real_container, new Fragment_SearchAccuntLog());
        transaction.commit();
    }
}
