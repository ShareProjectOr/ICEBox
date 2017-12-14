package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.shareiceboxms.R;

/**
 * Created by Administrator on 2017/12/13.
 */

public class saomatiaoView extends BaseMyView {
    private Paint mPaint;
    private Rect rect;
    private Context mContext;
    private int inittop = 0;
    private int size = 10;

    public saomatiaoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        this.mContext = context;
        mPaint.setStrokeWidth(5);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.blue));
    }

    //执行动画
    @Override
    protected void drawSub(Canvas canvas) {
        invalidate();
    }

    @Override
    protected void logic() {
        inittop += 1;
     /*   if (inittop < 150) {

        } else {
            inittop = 0;
        }*/
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect = new Rect(0, inittop, Util.getScreenWidth(mContext), inittop + size);
        canvas.drawRect(rect, mPaint);
    }
}
