package httputil;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zhazhijiguanlixitong.R;

import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * Created by WH on 2017/7/13.
 */

public class LoginAnim {
    private int CurrentPosition = 0;
    private boolean isstop = false;
    private static Dialog dialog;

    public void init() {
        isstop = false;
        CurrentPosition = 0;
    }

    public Dialog createLoginAnim(final Activity activity) {
        final Map<String, SoftReference<Drawable>> imageCache;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.anim_login, null);
        final ImageView login_anim_image = (ImageView) view.findViewById(R.id.login_anim_image);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (!isstop) {
                    if (activity == null) {
                        isstop = true;
                        return;
                    }
                    if (CurrentPosition == ConstanceDataForLarge.LOGIN_LOADING_IMAGE.length - 1) {
                        CurrentPosition = 0;
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            login_anim_image.setImageResource(ConstanceDataForLarge.LOGIN_LOADING_IMAGE[CurrentPosition]);
                        }
                    });
                    CurrentPosition++;
                    SystemClock.sleep(30);

                }
            }
        }).start();

        dialog = new Dialog(activity, R.style.dialog);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.setContentView(view, params);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void destoryDialog() {
        isstop = true;
        if (dialog != null) {
            dialog.dismiss();
        }

    }
}
