package example.jni.com.coffeeseller.views.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by WH on 2018/4/17.
 */

public class ProgressbarWithText extends RelativeLayout {
    private Context context;
    private int offset = 5;
    private int maxProgress = 100;
    private int maxMakingProgress = 98;
    private int perWidthOfPerProgress;
    private int paddingTop = 20;
    private int onePointProgress = offset;
    private int twoPointProgress;
    private int threePointProgress;

    private ProgressBar progressBar;
    private LinearLayout textLayout;
    private LinearLayout imageLayout;

    private int firstNoteImageCenterPosition;
    private int secondNoteImageCenterPosition;
    private int thiredNoteImageCenterPosition;

    public boolean makeSuccess = false;


    public ProgressbarWithText(Context context) {
        super(context);
        this.context = context;
    }

    public ProgressbarWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ProgressbarWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void init() {
        progressBar = (ProgressBar) getChildAt(0);
        textLayout = (LinearLayout) getChildAt(1);
        onePointProgress = progressBar.getProgress();
        twoPointProgress = onePointProgress + (maxProgress - onePointProgress) / 2;
        threePointProgress = maxProgress;
        offset = progressBar.getProgress();
    }

    public void setProgress(int progress) {

        if (textLayout == null || textLayout.getChildCount() != 3) {
            return;
        }
        int totalProgress = progress + onePointProgress;
        Log.d("progress ", "progress=" + totalProgress);
        TextView tipOne = (TextView) textLayout.getChildAt(0);
        TextView tipTwo = (TextView) textLayout.getChildAt(1);
        TextView tipThree = (TextView) textLayout.getChildAt(2);
        if (totalProgress < twoPointProgress) {
            tipOne.setVisibility(VISIBLE);
            tipTwo.setVisibility(GONE);
            tipThree.setVisibility(GONE);
        } else if (totalProgress >= twoPointProgress && totalProgress < threePointProgress) {
            tipOne.setVisibility(GONE);
            tipTwo.setVisibility(VISIBLE);
            tipThree.setVisibility(GONE);
        } else {
            tipOne.setVisibility(GONE);
            tipTwo.setVisibility(GONE);
            tipThree.setVisibility(VISIBLE);
        }
        progressBar.setSecondaryProgress(totalProgress);
    }

    public void updateProgressAnim() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = offset + 1; i < maxMakingProgress; i++) {
                    if (!makeSuccess) {
                        setProgress(i);
                        Waiter.doWait(300);
                    } else {
                        setProgress(maxProgress);
                        break;
                    }
                }
            }
        }).start();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width;
        int height;
        progressBar = (ProgressBar) getChildAt(0);
        textLayout = (LinearLayout) getChildAt(1);
        imageLayout = (LinearLayout) getChildAt(2);
        offset = progressBar.getProgress();
        this.measureChild(progressBar, widthMeasureSpec, heightMeasureSpec);
        int textChildCount = textLayout.getChildCount();
        for (int i = 0; i < textChildCount; i++) {
            View view = textLayout.getChildAt(i);
            this.measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        this.measureChild(textLayout, widthMeasureSpec, heightMeasureSpec);
        int imageChildCount = imageLayout.getChildCount();
        for (int i = 0; i < imageChildCount; i++) {
            View view = imageLayout.getChildAt(i);
            this.measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        this.measureChild(imageLayout, widthMeasureSpec, heightMeasureSpec);
//        width = progressBar.getMeasuredWidth() + textLayout.getChildAt(0).getMeasuredWidth();
        height = imageLayout.getChildAt(0).getMeasuredHeight() + textLayout.getChildAt(0).getHeight() + paddingTop;

        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);


        int textViewWidth = textLayout.getChildAt(0).getMeasuredWidth();
        int textViewHeight = textLayout.getChildAt(0).getMeasuredHeight();
        int imageViewWidth = imageLayout.getChildAt(0).getMeasuredWidth();
        int imageViewHeight = imageLayout.getChildAt(0).getMeasuredHeight();
        int progressbarWidth = progressBar.getMeasuredWidth() - progressBar.getPaddingLeft() - progressBar.getPaddingRight();

        perWidthOfPerProgress = progressBar.getMeasuredWidth() / maxProgress;
        int progressWidth = progressBar.getProgress() * perWidthOfPerProgress;
        firstNoteImageCenterPosition = perWidthOfPerProgress * offset + textViewWidth / 2 + progressBar.getPaddingLeft();

        int offset = firstNoteImageCenterPosition - progressWidth;
        secondNoteImageCenterPosition = (progressbarWidth - firstNoteImageCenterPosition - offset) / 2 + firstNoteImageCenterPosition;//(progressBar.getMeasuredWidth() - offset) / 2 + firstNoteImageCenterPosition - getPaddingLeft();
        thiredNoteImageCenterPosition = progressbarWidth + progressBar.getPaddingRight() - textViewWidth / 2;

        int textChildCount = textLayout.getChildCount();
        for (int i = 0; i < textChildCount; i++) {
            View view = textLayout.getChildAt(i);
            if (i == 0) {
                view.layout(firstNoteImageCenterPosition - textViewWidth / 2, 0, firstNoteImageCenterPosition + textViewWidth / 2, textViewHeight);
            }
            if (i == 1) {
                view.layout(secondNoteImageCenterPosition - textViewWidth / 2, 0, secondNoteImageCenterPosition + textViewWidth / 2, view.getMeasuredHeight());
            }

            if (i == 2) {
                view.layout(thiredNoteImageCenterPosition - textViewWidth / 2, 0, thiredNoteImageCenterPosition + textViewWidth / 2, view.getMeasuredHeight());
            }
        }
        int imageChildCount = imageLayout.getChildCount();

        for (int i = 0; i < imageChildCount; i++) {
            View view = imageLayout.getChildAt(i);
            if (i == 0) {
                view.layout(firstNoteImageCenterPosition - imageViewWidth / 2, textViewHeight + paddingTop, firstNoteImageCenterPosition + imageViewWidth / 2, getHeight());//textViewHeight + paddingTop + view.getMeasuredHeight()
            }
            if (i == 1) {
                view.layout(secondNoteImageCenterPosition - imageViewWidth / 2, textViewHeight + paddingTop, secondNoteImageCenterPosition + imageViewWidth / 2, textViewHeight + paddingTop + view.getMeasuredHeight());
            }

            if (i == 2) {
                view.layout(thiredNoteImageCenterPosition - imageViewWidth / 2, textViewHeight + paddingTop, thiredNoteImageCenterPosition + imageViewWidth / 2, textViewHeight + paddingTop + view.getMeasuredHeight());
            }
        }
        progressBar.layout(textViewWidth / 2, getHeight() - progressBar.getMeasuredHeight(), getWidth() - textViewWidth / 2, getHeight() - imageViewHeight / 2 + progressBar.getMeasuredHeight() / 2);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
