package com.example.zhazhijiguanlixitong.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zhazhijiguanlixitong.R;

import fragment.Fragment_DetailsDivider;
import fragment.Fragment_SearchException;

public class DetailsDividerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_divider);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.real_container,new Fragment_DetailsDivider());
        transaction.commit();
    }
}
