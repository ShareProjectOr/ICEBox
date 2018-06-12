package com.example.zhazhijiguanlixitong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import fragment.Fragment_ManageContent;
import com.example.zhazhijiguanlixitong.R;

public class ManagerContentActivity extends AppCompatActivity {
    public static final String action = "updata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_content);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.real_container, new Fragment_ManageContent());
        transaction.commit();
    }

    public void updata() {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
}
