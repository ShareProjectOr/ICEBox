package ViewUtils;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Administrator on 2017/7/20.
 */

public class ObjectAnimatorUtil {
    public static ObjectAnimator MoveAnimator(Object view, String direction, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translation" + direction, from, to);
        return animator;
    }

    public static ObjectAnimator FadeInOutAnimator(Object view, float from, float to, float Final) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", from, to, Final);
        return animator;
    }

    public static ObjectAnimator RotateAnimator(Object view, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", from, to);
        return animator;
    }

    public static ObjectAnimator ScaleAnimator(Object view, String type, float from, float to, float Final) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scale" + type, from, to, Final);
        return animator;
    }
}
