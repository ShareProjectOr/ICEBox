package com.example.zhazhijiguanlixitong.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import contentprovider.MachineContentProvider;
import fragment.Fragment_EditMachine;
import fragment.Fragment_MachineContent;

public class MachineContentActivity extends AppCompatActivity {
    public static final String action = "updata";
    private int CurrentPageTag = 1;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_content);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.real_container, new Fragment_MachineContent());
        transaction.commit();
        initview();
    }

    private void initview() {
        title = (TextView) findViewById(R.id.title);
    }



    public void ReplaceToEditPage() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.real_container, new Fragment_EditMachine()).addToBackStack(null).commit();
        CurrentPageTag = 2;
        title.setText(R.string.edit_machine_message);
    }

    public void ReplaceToContentPage() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.real_container, new Fragment_MachineContent()).addToBackStack(null).commit();
        CurrentPageTag = 1;
        title.setText(R.string.machine_content);
    }

    @Override
    public void onBackPressed() {
        switch (CurrentPageTag) {
            case 1:
                finish();
                break;
            case 2:
                ReplaceToContentPage();
                break;
        }
    }
}
