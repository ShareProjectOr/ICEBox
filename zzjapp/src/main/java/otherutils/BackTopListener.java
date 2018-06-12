package otherutils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/2/10.
 */

public class BackTopListener implements View.OnTouchListener{
    private int lastY = 0;
    private int scrollY = 0;// 标记上次滑动位置
    private ScrollView scrollView;
    private int touchEventId = -9983761;
    private Button  backtop;

    public BackTopListener(ScrollView scrollView, Button  backtop) {
        this.scrollView = scrollView;
        this. backtop =  backtop;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View scroller = (View) msg.obj;
            if (msg.what == touchEventId) {
                if (lastY == scroller.getScrollY()) {
                    handleStop(scroller);
                } else {
                    handler.sendMessageDelayed(handler.obtainMessage(
                            touchEventId, scroller), 5);
                    lastY = scroller.getScrollY();
                }
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            handler.sendMessageDelayed(
                    handler.obtainMessage(touchEventId, v), 5);
        }
        return false;
    }
    private void handleStop(Object view) {

        //   Log.i("", "handleStop");
        ScrollView scroller = (ScrollView) view;
        scrollY = scroller.getScrollY();

        doOnBorderListener();
    }
    private void doOnBorderListener() {
        // 底部判断
        /*if (contentView != null
                && contentView.getMeasuredHeight() <=  scrollView.getScrollY()
                +  scrollView.getHeight()) {
            backtop.setVisibility(View.VISIBLE);
            Log.i("", "bottom");
        }*/
        // 顶部判断
        /* if ( scrollView.getScrollY() == 0) {

            Log.i("", "top");
            backtop.setVisibility(View.GONE);
        } else*/
        Log.i("下滑高度",String.valueOf(scrollView.getScrollY()));
        if (scrollView.getScrollY() > 1200) {
            backtop.setVisibility(View.VISIBLE);
            // Log.i("", "test");
        } else if (scrollView.getScrollY() <= 1000) {
            backtop.setVisibility(View.GONE);
        }

    }

}
