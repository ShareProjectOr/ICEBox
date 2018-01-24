package com.example.shareiceboxms.models.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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
    private static final float BASE_SQUARE_UNIT = 72f;
    private float mScaleFactor = 1.0f;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private boolean isChecked = false;
    private Paint unCheckLargeRectF;//未选中时大矩形画笔
    private Paint CheckLargeRectF;//选中时大矩形画笔
    private Paint unCheckSmailRectF;//未选中时小矩形画笔
    private Paint CheckSmailRectF;//选中时小矩形画笔
    private Context mContext;
    private float X;
    private float Y;

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
        this.mContext = context;
        unCheckLargeRectF = new Paint();
        CheckLargeRectF = new Paint();
        unCheckSmailRectF = new Paint();
        CheckSmailRectF = new Paint();
        unCheckLargeRectF.setStrokeWidth(5);
        CheckLargeRectF.setStrokeWidth(5);
        unCheckSmailRectF.setStrokeWidth(5);
        CheckSmailRectF.setStrokeWidth(5);
        unCheckLargeRectF.setColor(ContextCompat.getColor(mContext, R.color.gray));//
        unCheckSmailRectF.setColor(ContextCompat.getColor(mContext, R.color.white));//;
        CheckLargeRectF.setColor(ContextCompat.getColor(mContext, R.color.sucessgreen));
        CheckSmailRectF.setColor(ContextCompat.getColor(mContext, R.color.white));//;
    }

    public CustromSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
        X = getX();
        Y = getY();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (isChecked) {
                    isChecked = false;
                } else {
                    isChecked = true;

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
        super.onDraw(canvas);
        if (isChecked) {
            canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), CheckLargeRectF);
            canvas.drawRect((getLeft() - 5) / 2, getTop() - 5, (getRight() - 5), getBottom() - 5, CheckSmailRectF);
        } else {
            canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), unCheckLargeRectF);
            canvas.drawRect(getLeft() - 5, getTop() - 5, (getRight() - 5) / 2, getBottom() - 5, unCheckSmailRectF);
        }


        //  canvas.drawRect();

    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) (30 * BASE_SQUARE_UNIT * mScaleFactor + getPaddingLeft()
                    + getPaddingRight());
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (BASE_SQUARE_UNIT * mScaleFactor) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
