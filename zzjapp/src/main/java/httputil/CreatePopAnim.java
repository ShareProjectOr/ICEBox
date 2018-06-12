package httputil;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.zhazhijiguanlixitong.R;

/**
 * Created by WH on 2017/7/14.
 */

public class CreatePopAnim {
    public static AnimatorSet createAnim(Context context, RelativeLayout rootLayout, String managerType) {////
        Button one = (Button) rootLayout.findViewById(R.id.create_sys_manager);
        Button two = (Button) rootLayout.findViewById(R.id.create_agent);
        Button three = (Button) rootLayout.findViewById(R.id.create_machine_manager);
        AnimatorSet animatorSet = new AnimatorSet();
        switch (Integer.parseInt(managerType)) {
            case 0://超级管理员
                animatorSet.play(alphaAnim(two))
                        .with(alphaAnim(one))
                        .with(alphaAnim(three))
                        .with(translateAnim(one, 0))
                        .with(translateAnim(three, 1));
                break;
            case 1://系统管理员
                animatorSet.play(alphaAnim(two))
                        .with(alphaAnim(three))
                        .with(translateAnim(two, 0))
                        .with(translateAnim(three, 1));
                break;
            case 2://代理商
                animatorSet.play(alphaAnim(three));
                break;
            case 3:
                break;
            default:
                break;
        }
        animatorSet.setDuration(1000);
        return animatorSet;
    }

    public static void dropAnim(Context context, RelativeLayout rootLayout, String managerType) {
        Button one = (Button) rootLayout.findViewById(R.id.create_sys_manager);
        Button two = (Button) rootLayout.findViewById(R.id.create_agent);
        Button three = (Button) rootLayout.findViewById(R.id.create_machine_manager);
        AnimatorSet animatorSet = new AnimatorSet();
        switch (Integer.parseInt(managerType)) {
            case 0:
                animatorSet.play(dropAlphaAnim(two))
                        .with(dropAlphaAnim(one))
                        .with(dropAlphaAnim(three))
                        .with(dropTranslateAnim(one, 0))
                        .with(dropTranslateAnim(three, 1));
                break;
            case 1:
                animatorSet.play(dropAlphaAnim(two))
                        .with(dropAlphaAnim(three))
                        .with(dropTranslateAnim(two, 0))
                        .with(dropTranslateAnim(three, 1));
                break;
            case 2:
                animatorSet.play(dropAlphaAnim(three));
                break;
            case 3:
                break;
            default:
                break;
        }
        animatorSet.setDuration(500);
        animatorSet.start();
    }


    public static ObjectAnimator alphaAnim(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 0.8f);
        return objectAnimator;
    }

    public static ObjectAnimator dropAlphaAnim(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.8f, 0f);
        return objectAnimator;
    }

    public static ObjectAnimator translateAnim(View view, int leftOrRight) {
        ObjectAnimator objectAnimator = null;
        switch (leftOrRight) {
            case 0:
                objectAnimator = ObjectAnimator.ofFloat(view, "X", view.getX(), view.getX() - 300);
                break;
            case 1:
                objectAnimator = ObjectAnimator.ofFloat(view, "X", view.getX(), view.getX() + 300);
                break;
            default:
                break;
        }
        if (objectAnimator != null) {
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return objectAnimator;
    }

    public static ObjectAnimator dropTranslateAnim(View view, int leftOrRight) {
        ObjectAnimator objectAnimator = null;
        switch (leftOrRight) {
            case 0:
                objectAnimator = ObjectAnimator.ofFloat(view, "X", view.getX(), view.getX() + 300);
                break;
            case 1:
                objectAnimator = ObjectAnimator.ofFloat(view, "X", view.getX(), view.getX() - 300);
                break;
            default:
                break;
        }
        if (objectAnimator != null) {
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return objectAnimator;
    }
}
