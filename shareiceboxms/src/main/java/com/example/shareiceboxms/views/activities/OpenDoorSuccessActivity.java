package com.example.shareiceboxms.views.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.views.fragments.OpenDoorSuccessFragment;

public class OpenDoorSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_door_success);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.realcontent, new OpenDoorSuccessFragment());
        transaction.commit();
    }
}
