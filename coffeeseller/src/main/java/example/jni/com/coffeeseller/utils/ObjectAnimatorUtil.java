package example.jni.com.coffeeseller.utils;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by WH on 2018/3/17.
 */

public class ObjectAnimatorUtil {
    /*
    * 透明度动画
    * */
    public static ObjectAnimator alphaAnimator(View view, float startAlpha, float centerAlpha, float endAlpha) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, centerAlpha, endAlpha);
        return animator;
    }

    public static ObjectAnimator alphaAnimator(View view, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", from, to);
        return animator;
    }

    /*
    * 旋转动画：围绕x轴旋转
    * */
    public static ObjectAnimator rotateXAnimator(View view, float startX, float centerX, float endX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationX", startX, centerX, endX);
        return animator;
    }

    public static ObjectAnimator rotateXAnimator(View view, float fromX, float toX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationX", fromX, toX);
        return animator;
    }

    /*
 * 旋转动画：围绕Y轴旋转
 * */
    public static ObjectAnimator rotateYAnimator(View view, float startY, float centerY, float endY) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", startY, centerY, endY);
        return animator;
    }

    public static ObjectAnimator rotateYAnimator(View view, float fromY, float toY) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", fromY, toY);
        return animator;
    }

    public static ObjectAnimator rotateAnimator(View view, float fromX, float toX, float fromY, float toY) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", fromX, toX, fromY, toY);
        return animator;
    }

    /*
    * 缩放动画：在x轴缩放
    * */
    public static ObjectAnimator scaleXAnimator(View view, float startX, float centerX, float endX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", startX, centerX, endX);
        return animator;
    }

    public static ObjectAnimator scaleXAnimator(View view, float fromX, float toX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", fromX, toX);
        return animator;
    }

    /*
    * 缩放动画：在y轴上缩放
    * */
    public static ObjectAnimator scaleYAnimator(View view, float fromY, float centerY, float toY) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", fromY, centerY, toY);
        return animator;
    }

    public static ObjectAnimator scaleYAnimator(View view, float fromY, float toY) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", fromY, toY);
        return animator;
    }

    /*
  * 平移动画:在y轴上平移
  * */
    public static ObjectAnimator translateYAnimator(View view, float fromX, float toX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", fromX, toX);
        return animator;
    }

    /*
* 平移动画:在x轴上平移
* */
    public static ObjectAnimator translateXAnimator(View view, float fromX, float toX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", fromX, toX);
        return animator;
    }

    public static ObjectAnimator alphaAnimator(Object view, float from, float to, float Final) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", from, to, Final);
        return animator;
    }

    public static ObjectAnimator alphaAnimator(Object view, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", from, to);
        return animator;
    }

    public static ObjectAnimator rotateAnimator(Object view, String type, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation" + type, from, to);
        return animator;
    }

    public static ObjectAnimator scaleAnimator(Object view, String type, float from, float to, float Final) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scale" + type, from, to, Final);
        return animator;
    }

    public static ObjectAnimator scaleAnimator(Object view, String type, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scale" + type, from, to);
        return animator;
    }

    public static ObjectAnimator transactionAnimator(Object view, String type, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translation" + type, from, to);
        return animator;
    }
}
