package com.example.shareiceboxms.models.helpers;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by WH on 2017/11/28.
 */

public class LoginAnimHelper {
    public static int LOGIN_ANIM = 0;
    public static int LOGIN_ANIM_REVERSE = 1;

    /*
   * 登录时输入框动画
   * */
    public void loginEditAnim(final int mode, final View editLayout, final View loginLoading) {
        float startMargin = 0;
        float endMargin = 150f;
        float startScale = 1f;
        float endScale = 0.8f;
        if (mode == LOGIN_ANIM) {
        } else if (mode == LOGIN_ANIM_REVERSE) {
            startMargin = 150f;
            endMargin = 0;
            startScale = 0.8f;
            endScale = 1f;
        }
        //为editLayout不断设置margin
        ValueAnimator vAnimator = ValueAnimator.ofFloat(startMargin, endMargin);
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) editLayout.getLayoutParams();
                params.rightMargin = (int) value;
                params.leftMargin = (int) value;
                editLayout.setLayoutParams(params);

            }
        });
        ObjectAnimator oAnimator = ObjectAnimator.ofFloat(editLayout, "scaleX", startScale, endScale);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(300);
        animatorSet.play(vAnimator).with(oAnimator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mode == LOGIN_ANIM_REVERSE) {
                    editLayout.setVisibility(View.VISIBLE);
                    loginLoading.setVisibility(View.INVISIBLE);
                    loginBarAnimReverse(loginLoading);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mode == LOGIN_ANIM) {
                    loginLoading.setVisibility(View.VISIBLE);
                    editLayout.setVisibility(View.INVISIBLE);
                    loginLoading.setAlpha(1f);
                    loginBarAnim(loginLoading);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    /*
           * 登录时进度条框动画
           * */
    private void loginBarAnim(View loginLoading) {
        PropertyValuesHolder xValuesHolder = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1f);
        PropertyValuesHolder yValuesHolder = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(loginLoading, xValuesHolder, yValuesHolder);
        animator.setInterpolator(new JellyInterpolator());
        animator.setDuration(300);
        animator.start();
    }

    /*
           * 登录失败时进度条消失
           * */
    private void loginBarAnimReverse(View loginLoading) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(loginLoading, "alpha", 1f, 0f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    private class JellyInterpolator implements TimeInterpolator {
        float factor = 0.15f;

        @Override
        public float getInterpolation(float v) {
            return (float) (Math.pow(2, -10 * v)
                    * Math.sin((v - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }
}