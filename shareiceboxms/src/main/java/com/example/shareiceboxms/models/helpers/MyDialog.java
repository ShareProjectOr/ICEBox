package com.example.shareiceboxms.models.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.shareiceboxms.views.activities.LoginActivity;

/**
 * Created by WH on 2017/11/30.
 */

public class MyDialog {

    public static AlertDialog getLogoutDialog(final Context context) {
        return new AlertDialog.Builder(context).setMessage("您确定退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setClass(context, LoginActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
                //清空缓存数据
            }
        }).setNegativeButton("取消", null).create();
    }

}
