package com.example.shareiceboxms.models.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.shareiceboxms.R;

import java.util.Random;

/**
 * Created by Administrator on 2018/1/24.
 */

public class CustromSwitch extends View {
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private boolean isChecked = false;
    private Paint unCheckLargeRectF;//未选中时大矩形画笔
    private Paint CheckLargeRectF;//选中时大矩形画笔
    private Paint unCheckSmailRectF;//未选中时小矩形画笔
    private Paint CheckSmailRectF;//选中时小矩形画笔
    private Context mContext;
    private float X;
    private float Y;
    private int WRAP_WIDTH = 60;
    private int WRAP_HEIGHT = 30;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        if (mOnCheckedChangeListener != null) {
            this.mOnCheckedChangeListener = mOnCheckedChangeListener;
        }
    }

    public CustromSwitch(Context context) {
        super(context);
    }

    public CustromSwitch(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        Log.e("CustromSwitch", "doing");
        this.mContext = context;

        unCheckLargeRectF = new Paint();
        CheckLargeRectF = new Paint();
        unCheckSmailRectF = new Paint();
        CheckSmailRectF = new Paint();
        // unCheckLargeRectF.setStrokeWidth(5);
        unCheckLargeRectF.setStyle(Paint.Style.FILL);
        CheckLargeRectF.setStyle(Paint.Style.FILL);
        unCheckSmailRectF.setStyle(Paint.Style.FILL);
        CheckSmailRectF.setStyle(Paint.Style.FILL);
       /* CheckLargeRectF.setStrokeWidth(5);
        unCheckSmailRectF.setStrokeWidth(5);
        CheckSmailRectF.setStrokeWidth(5);*/
        unCheckLargeRectF.setColor(ContextCompat.getColor(mContext, R.color.red));//
        unCheckSmailRectF.setColor(ContextCompat.getColor(mContext, R.color.white));//;
        CheckLargeRectF.setColor(ContextCompat.getColor(mContext, R.color.sucessgreen));
        CheckSmailRectF.setColor(ContextCompat.getColor(mContext, R.color.white));//;
    }

    public CustromSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);


    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        super.setBackgroundColor(color);
        setBackgroundColor(R.color.gray);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (isChecked) {
                    setBackgroundColor(R.color.gray);
                    isChecked = false;
                } else {
                    isChecked = true;
                    setBackgroundColor(R.color.sucessgreen);
                }

                mOnCheckedChangeListener.onCheckedChanged(isChecked);

                invalidate();
                break;
        }
        Log.e("当前选中状态", isChecked + "");
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Log.e("XYZ", "getLeft=" + getLeft() + "getttop=" + getTop() + "getright=" + getRight() + "getbottom=" + getBottom());
        if (isChecked) {
            canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), CheckLargeRectF);
            // canvas.drawRect((getLeft() - 5) / 2, getTop() - 5, (getRight() - 5), getBottom() - 5, CheckSmailRectF);
        } else {
            canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), unCheckLargeRectF);
            //  canvas.drawRect(getLeft() +5 5, getTop() - 5, (getRight() - 5) / 2, getBottom() - 5, unCheckSmailRectF);
        }
        super.onDraw(canvas);

        //  canvas.drawRect();

    }


}
