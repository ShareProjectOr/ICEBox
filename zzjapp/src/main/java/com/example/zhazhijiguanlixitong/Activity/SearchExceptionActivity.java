package com.example.zhazhijiguanlixitong.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zhazhijiguanlixitong.R;

import fragment.Fragment_MachineContent;
import fragment.Fragment_SearchException;

public class SearchExceptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_exception);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.real_container,new Fragment_SearchException());
        transaction.commit();
    }
}
