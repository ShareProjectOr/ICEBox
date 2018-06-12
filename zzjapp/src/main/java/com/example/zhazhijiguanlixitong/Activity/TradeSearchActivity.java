package com.example.zhazhijiguanlixitong.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zhazhijiguanlixitong.R;

import fragment.Fragment_SearchTrade;

public class TradeSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_search);
        AddFragment();
    }

    private void AddFragment() {
        FragmentTransaction transasction = getSupportFragmentManager().beginTransaction();
        transasction.add(R.id.real_container, new Fragment_SearchTrade());
        transasction.commit();
    }


}
