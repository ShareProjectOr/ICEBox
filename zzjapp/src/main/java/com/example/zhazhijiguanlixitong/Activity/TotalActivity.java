package com.example.zhazhijiguanlixitong.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.zhazhijiguanlixitong.R;

import fragment.Fragment_TodayProfit;
import httputil.ConstanceMethod;

public class TotalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);
        Fragment_TodayProfit fragment = new Fragment_TodayProfit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framecontent, fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ConstanceMethod.getSharedPreferences(this, "time").edit().putString("lastSelectedTime", "").commit();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
