package com.example.shareiceboxms.views.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.views.fragments.product.ModifyPriceFragment;

public class ModifyPriceActivity extends BaseActivity {
    private ImageView mDrawerIcon;
    private ImageView mSaoma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_price);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.realcontent, new ModifyPriceFragment());
        transaction.commit();
        bindViews();
        initListener();
    }

    private void initListener() {
        mDrawerIcon.setOnClickListener(this);
        mSaoma.setOnClickListener(this);
    }

    private void bindViews() {


        mDrawerIcon = (ImageView) findViewById(R.id.drawerIcon);
        mSaoma = (ImageView) findViewById(R.id.saoma);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                onBackPressed();
                break;
            case R.id.saoma:
              //  Intent
                break;
        }
    }
}
