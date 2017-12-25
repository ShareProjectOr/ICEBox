package com.example.shareiceboxms.models.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.UserManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.ConstanceMethod;
import com.example.shareiceboxms.models.contants.Sql;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.activities.LoginActivity;

import java.util.Map;

/**
 * Created by WH on 2017/11/30.
 */

public class MyDialog {
    private Context context;

    public MyDialog(Context context) {
        this.context = context;
    }

    public AlertDialog getLogoutDialog(final HomeActivity activity) {
        return new AlertDialog.Builder(activity).setMessage("您确定退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                dialog1.dismiss();
                //清空缓存数据
                Dosql();
                ConstanceMethod.isFirstLogin(activity, true);
                ConstanceMethod.clearActivityFromStack(activity, LoginActivity.class);
                PerSonMessage.isexcit = true;
                activity.finish();
            }
        }).setNegativeButton("取消", null).create();

    }

    private void Dosql() {
        Sql sql = new Sql(context);
        sql.deleteAllContact(sql.getAllCotacts().size());
    }

    public static Dialog loadDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.load_dialog_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //6.0以下
            lp.width = Util.getScreenWidth(context) / 2;

        }
        window.setTitle(null);
        lp.alpha = 0.65f;
        window.setAttributes(lp);//设置透明度
        //设置圆矩背景，dialog是为矩形
        window.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_loading_layout));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public AlertDialog getMachineTeleControlDialog(String showMsg, final String url, final Map<String, Object> params) {
        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(showMsg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TeleControlHelper.getInstance().setContext(context);
                TeleControlHelper.getInstance().getDatas(url, params);
            }
        }).setNegativeButton("取消", null).create();
        return dialog;
    }

    public void showDialog(Dialog dialog) {
        dialog.show();
        if (dialog instanceof AlertDialog) {
            AlertDialog alertDialog = (AlertDialog) dialog;
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.blue));
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        }

    }
}
