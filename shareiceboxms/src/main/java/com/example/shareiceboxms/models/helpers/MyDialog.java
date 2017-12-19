package com.example.shareiceboxms.models.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.UserManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.ConstanceMethod;
import com.example.shareiceboxms.models.contants.Sql;
import com.example.shareiceboxms.views.activities.LoginActivity;

/**
 * Created by WH on 2017/11/30.
 */

public class MyDialog {
    private Context mContext;

    public AlertDialog getLogoutDialog(final Activity activity) {
        mContext = activity;
        return new AlertDialog.Builder(activity).setMessage("您确定退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //清空缓存数据
                Dosql();
                ConstanceMethod.isFirstLogin(mContext, true);
                ConstanceMethod.clearActivityFromStack(activity, LoginActivity.class);
                PerSonMessage.isexcit = true;
                activity.finish();
            }
        }).setNegativeButton("取消", null).create();
    }

    private void Dosql() {
        Sql sql = new Sql(mContext);
        sql.deleteAllContact(sql.getAllCotacts().size());
    }

    public static Dialog loadDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.load_dialog_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.65f;
        window.setAttributes(lp);//设置透明度

        window.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_loading_layout));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
