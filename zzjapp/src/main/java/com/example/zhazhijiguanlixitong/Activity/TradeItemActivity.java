package com.example.zhazhijiguanlixitong.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.zhazhijiguanlixitong.R;

import fragment.Fragment_TradeItem;

public class TradeItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_item);
        //   getWindow().setWindowAnimations(R.style.scaleAnimal);
        addFragment();
    }

    private void addFragment() {
        FragmentManager mg = getSupportFragmentManager();
        FragmentTransaction transaction = mg.beginTransaction();
        transaction.add(R.id.real_container, new Fragment_TradeItem());
        transaction.commit();
    }
}
