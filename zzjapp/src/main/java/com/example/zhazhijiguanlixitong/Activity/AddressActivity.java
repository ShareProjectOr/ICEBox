package com.example.zhazhijiguanlixitong.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.zhazhijiguanlixitong.R;

import fragment.FragmentAddress;

public class AddressActivity extends AppCompatActivity {
    private FragmentTransaction transaction;
    private FragmentAddress fragmentAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_provence);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_address);
        fragmentAddress = FragmentAddress.newInstance();
        transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.address, fragmentAddress);
        transaction.commit();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AddressCallBack callBack = fragmentAddress.getCallBack();
        callBack.onKeyDown();
        return true;
    }

    public interface AddressCallBack {
        boolean onKeyDown();
    }
}
