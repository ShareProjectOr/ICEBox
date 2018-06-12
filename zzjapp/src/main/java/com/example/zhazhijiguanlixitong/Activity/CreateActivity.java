package com.example.zhazhijiguanlixitong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.zhazhijiguanlixitong.R;

import fragment.FragmentCreate;

public class CreateActivity extends AppCompatActivity {
    private FragmentTransaction transaction;
    private int flags = 0;
    private FragmentCreate fragmentCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_create);
        Intent intent = getIntent();
        flags = intent.getFlags();
        fragmentCreate = FragmentCreate.newInstance(String.valueOf(flags));
        transaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.create, fragmentCreate);
        transaction.commit();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String address = intent.getStringExtra("address");
        String id = intent.getStringExtra("id");
        fragmentCreate.setAddressValue(address, id);
    }
}
