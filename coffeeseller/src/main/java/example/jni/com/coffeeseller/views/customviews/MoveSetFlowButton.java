package example.jni.com.coffeeseller.views.customviews;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import example.jni.com.coffeeseller.R;


public class MoveSetFlowButton {


    int screenWidth = 0;
    int screenHeight = 0;
    int rawY = 0;
    int rawX = 0;
    Button button;
    int startX, startY, dx, dy;
    int statusHeight;

    private static Context mContext = null;
    WindowManager windowManager;
    LayoutParams params;

    private static MoveSetFlowButton instance = null;
    private static boolean isAttachToWindow = false;

    public static MoveSetFlowButton getInstance(Context context) {

        mContext = context.getApplicationContext();
        if (instance == null) {
            instance = new MoveSetFlowButton();
        }
        return instance;
    }

    private MoveSetFlowButton() {

        // 获取系统窗口服务
        windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusHeight = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        params = new LayoutParams();
        // 将button层设置为在所有应用层之上
        params.type = LayoutParams.TYPE_SYSTEM_ALERT;
        params.flags =
                LayoutParams.FLAG_NOT_FOCUSABLE
                        | LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        // 将Button的背景设置成透明/半透明
        params.format = PixelFormat.TRANSLUCENT;
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP | Gravity.LEFT;
    }

    public void showFlowButton() {

        if (!isAttachToWindow) {

            View view = getButton();
//        if (view != null && !view.isAttachedToWindow()) {  //工控机配置低,出现没有该方法
            windowManager.addView(view, params);

//        }

            isAttachToWindow = true;
        }


    }

    public void removeFlowButton() {
        //                if (button.isAttachedToWindow()){

        if (isAttachToWindow) {
            windowManager.removeView(button);

            isAttachToWindow = false;
        }

//                }
    }

    private View getButton() {

        if (button != null) {

            button.setVisibility(Button.VISIBLE);
            return button;
        }
        //初始化按钮
        button = new Button(mContext);
        button.setBackground(mContext.getResources().getDrawable(R.mipmap.coffee_logo));
        button.setTextSize(35);
        button.setWidth(90);
        button.setHeight(90);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG", "---back---");
/*//                if (button.isAttachedToWindow()){
                windowManager.removeView(button);

                isAttachToWindow = false;
//                }*/

                removeFlowButton();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                //display top-level
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("example.jni.com.coffeeseller", "example.jni.com.coffeeseller.views.activities.HomeActivity");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        button.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int[] dir = new int[2];
                v.getLocationOnScreen(dir);
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        startX = dir[0];
                        startY = dir[1];
                        Log.e("Test", "startx=" + startX + "---startY=" + startY);
                        rawX = (int) event.getRawX();
                        rawY = (int) event.getRawY();
                        Log.e("Test", "rawX=" + rawX + "---rawY=" + rawY);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getRawX() - rawX;
                        dy = (int) event.getRawY() - rawY;
                        Log.e("Test", "dx=" + dx + "---dy=" + dy);
                        updateLayout(v);
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return false;
            }
        });

        return button;
    }


    private void updateLayout(View v) {
        params.x = startX + dx;
        params.y = startY + dy - statusHeight;
        Log.e("Test", "params.x=" + params.x + "---params.y=" + params.y);
        windowManager.updateViewLayout(v, params);
    }

}