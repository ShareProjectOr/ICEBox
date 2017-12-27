package com.example.shareiceboxms.models.helpers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.views.activities.HomeActivity;

/**
 * Created by WH on 2017/12/7.
 */

public class NotifySnackbar {
    private static Snackbar.SnackbarLayout snackbarLayout;
    private static Snackbar snackbar;
    private String msg;

    public NotifySnackbar(HomeActivity activity, View bindView, String msg) {
        addNotifySnackbar(activity, bindView,msg);
    }

    public static void addNotifySnackbar(final HomeActivity activity, View coorView, String msg) {
//隐藏虚拟按键栏(否则SnackBar高度很高)
//        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE); //防止点击屏幕时,隐藏虚拟按键栏又弹了出来 //  
        View view = LayoutInflater.from(activity).inflate(R.layout.notify_window, null, false);


        snackbar = Snackbar.make(coorView, msg, Snackbar.LENGTH_INDEFINITE);
        snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        if (snackbarLayout != null) {
            snackbarLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.shape_float_botton));

          /*  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            snackbarLayout.setLayoutParams(layoutParams);  添加后snackbar不能滑动*/


          /*  AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
            alphaAnimation.setDuration(1000);

            snackbarLayout.setAnimation(alphaAnimation);*/

            Button button = (Button) snackbarLayout.findViewById(R.id.snackbar_action);
            // button.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.width = 70;
            params.height = 70;
            button.setBackground(ContextCompat.getDrawable(activity, R.mipmap.notify_close));
            TextView textView = (TextView) snackbarLayout.findViewById(R.id.snackbar_text);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.weight = 1;
            textView.setLayoutParams(params1);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideNotify();
//                    snackbarLayout.setVisibility(View.GONE);
//                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    activity.selectedException();
                }
            });
        }
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
//                snackbarLayout.setVisibility(View.GONE);
                //隐藏SnackBar时记得恢复隐藏虚拟按键栏,不然屏幕底部会多出一块空白布局出来,和难看
//                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
     /*   snackbar.setAction(" ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();*/

        snackbar.show();
    }

    public static void showNotify() {
        if (snackbarLayout != null) {
            snackbarLayout.setVisibility(View.VISIBLE);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(snackbarLayout, "alpha", 0f, 1f);
        objectAnimator.setDuration(200);
        objectAnimator.start();
    }

    public static void hideNotify() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(snackbarLayout, "alpha", 1f, 0f);
        objectAnimator.setDuration(200);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (snackbarLayout != null) {
                    snackbarLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
