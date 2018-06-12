package example.jni.com.coffeeseller.views.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WH on 2018/4/23.
 */

public class DialogChooseCupGroup extends ViewGroup {
    private int width, height;
    private Paint paint;
    private String coffeeName;

    public DialogChooseCupGroup(Context context) {
        super(context);
        init();
    }

    public DialogChooseCupGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialogChooseCupGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            this.measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*int textStart = width / 2 - coffeeName.length() / 2;
        canvas.drawText(coffeeName, textStart, width, paint);*/
        canvas.drawRoundRect(100, 100, 800, 800, 10, 10, paint);
    }
}
