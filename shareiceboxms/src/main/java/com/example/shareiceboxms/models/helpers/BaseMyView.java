package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public abstract class BaseMyView extends View {

    public BaseMyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    private Mythread mythread;

    //	public BaseMyView(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//	}
    private String TAG = "BaseMyView";

    protected abstract void drawSub(Canvas canvas);

    protected abstract void logic();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mythread == null) {
            mythread = new Mythread();
            mythread.start();
        } else {
            drawSub(canvas);
        }

    }

    private boolean isRunning = true;

    protected void onDetachedFromWindow() {

        isRunning = false;
        Log.e(TAG, "isRunning=" + isRunning);
        super.onDetachedFromWindow();
    }

    public void StopGames() {
        isRunning = false;
    }

    public void ResartGames() {

        isRunning = true;
        new Mythread().start();
        // mythread.start();
     /*   mythread = new Mythread();
        mythread.start();*/
        Log.e(TAG, "isRunning=" + isRunning);

    }

    class Mythread extends Thread {
        @Override
        public void run() {


            while (isRunning) {
                Log.e(TAG, "isRunning=" + isRunning);
                logic();
                try {
                    Thread.sleep(10);
                    postInvalidate();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
