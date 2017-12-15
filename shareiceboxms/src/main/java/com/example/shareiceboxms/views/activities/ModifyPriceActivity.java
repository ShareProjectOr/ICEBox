package com.example.shareiceboxms.views.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.views.fragments.product.ModifyPriceFragment;

public class ModifyPriceActivity extends BaseActivity {
    private ImageView mDrawerIcon;
    private ImageView mSaoma;
    private final int SCANNIN_GREQUEST_CODE = 1;
    private static final int CAMERA_OK = 517;

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

    public void openSaoma() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA}, CAMERA_OK);
                //当fragment申请权限是fragment本身申请权限,不需要ActivityCompat.requestPermissions.不然无法执行授权回调
                //当Activity申请权限是Activity本身申请权限,需要ActivityCompat.requestPermissions.来执行授权回调
            } else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
                Intent intent = new Intent();
                intent.setClass(getApplication(), CaptureActivity.class);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        } else {
//这个说明系统版本在6.0之下，不需要动态获取权限。
            Intent intent = new Intent();
            intent.setClass(getApplication(), CaptureActivity.class);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    Intent intent = new Intent();
                    intent.setClass(getApplication(), CaptureActivity.class);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    Toast.makeText(getApplication(), "您拒绝了系统调用摄像头权限,请手动打开相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                onBackPressed();
                break;
            case R.id.saoma:
                openSaoma();
                break;
        }
    }
}
