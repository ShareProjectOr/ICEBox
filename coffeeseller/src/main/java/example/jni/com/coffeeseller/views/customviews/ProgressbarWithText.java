package example.jni.com.coffeeseller.views.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
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
    private int maxMakingProgress = 80;
    private int perWidthOfPerProgress;
    private int paddingTop = 20;
    private int imageOffset = 5;
    private int onePointProgress = offset;
    private int twoPointProgress;
    private int threePointProgress;

    private ProgressBar progressBar;
    private LinearLayout textLayout;
    private LinearLayout imageLayout;

    private int firstNoteImageCenterPosition;
    private int secondNoteImageCenterPosition;
    private int thiredNoteImageCenterPosition;

    TextView tipOne;
    TextView tipTwo;
    TextView tipThree;

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
        progressBar =  (ProgressBar) ((LinearLayout) getChildAt(0)).getChildAt(0);;
        textLayout = (LinearLayout) getChildAt(1);
        onePointProgress = progressBar.getProgress();
        twoPointProgress = onePointProgress + (maxProgress - onePointProgress) / 2;
        threePointProgress = maxProgress;
        offset = progressBar.getProgress();
        tipOne = (TextView) textLayout.getChildAt(0);
        tipTwo = (TextView) textLayout.getChildAt(1);
        tipThree = (TextView) textLayout.getChildAt(2);
    }

    public void setProgress(int progress) {

        if (textLayout == null || textLayout.getChildCount() != 3) {
            return;
        }
        Log.d("progress ", "progress=" + progress);
        progressBar.setSecondaryProgress(progress);
        if (progress < twoPointProgress) {
            tipOne.setVisibility(VISIBLE);
            tipTwo.setVisibility(GONE);
            tipThree.setVisibility(GONE);
        } else if (progress >= twoPointProgress && progress < threePointProgress) {
            tipOne.setVisibility(GONE);
            tipTwo.setVisibility(VISIBLE);
            tipThree.setVisibility(GONE);
        } else {
            tipOne.setVisibility(GONE);
            tipTwo.setVisibility(GONE);
            tipThree.setVisibility(VISIBLE);
        }

    }

    public void updateProgressAnim() {
        if (makeSuccess) {
            setProgress(maxProgress);
            return;
        }

        new AsyncTask<Void, Integer, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                for (int i = offset + 1; i <= maxProgress; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    publishProgress(i);
                    Waiter.doWait(300);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (isCancelled()) {
                    return;
                }
                setProgress(values[0]);
            }
        }.execute();

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width;
        int height;
        progressBar = (ProgressBar) ((LinearLayout) getChildAt(0)).getChildAt(0);
        textLayout = (LinearLayout) getChildAt(1);
        imageLayout = (LinearLayout) getChildAt(2);
        offset = progressBar.getProgress();

        int textChildCount = textLayout.getChildCount();
        for (int i = 0; i < textChildCount; i++) {
            View view = textLayout.getChildAt(i);
            this.measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        this.measureChild(textLayout, widthMeasureSpec, textLayout.getChildAt(0).getHeight());
        int imageChildCount = imageLayout.getChildCount();
        for (int i = 0; i < imageChildCount; i++) {
            View view = imageLayout.getChildAt(i);
            this.measureChild(view, widthMeasureSpec, heightMeasureSpec);
            if ((view.getHeight() - progressBar.getHeight()) / 2 < imageOffset) {
                int imageSize = progressBar.getHeight() + 2 * imageOffset;
                this.measureChild(view, imageSize, imageSize);
            } else {
                if (view.getHeight() >= view.getWidth()) {
                    this.measureChild(view, view.getWidth(), view.getWidth());
                } else {
                    this.measureChild(view, view.getHeight(), view.getHeight());
                }
            }
        }
        this.measureChild(imageLayout, widthMeasureSpec, imageLayout.getChildAt(0).getHeight());


        this.measureChild(progressBar, widthMeasureSpec * 2 / 3, heightMeasureSpec);

        width = progressBar.getMeasuredWidth() + textLayout.getChildAt(0).getMeasuredWidth();
        height = imageLayout.getMeasuredHeight() + textLayout.getMeasuredHeight() + paddingTop;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);


        int textViewWidth = textLayout.getChildAt(0).getMeasuredWidth();
        int textViewHeight = textLayout.getChildAt(0).getMeasuredHeight();
        int imageViewWidth = imageLayout.getChildAt(0).getMeasuredWidth();
        int imageViewHeight = imageLayout.getChildAt(0).getMeasuredHeight();

        int progressbarWidth = progressBar.getMeasuredWidth() - progressBar.getPaddingLeft() - progressBar.getPaddingRight();
        perWidthOfPerProgress = progressbarWidth / maxProgress;
        offset = progressBar.getProgress();
        int offsetWidth = offset * perWidthOfPerProgress;
        int threeOfMatchParent = textLayout.getMeasuredWidth() / 3;


        firstNoteImageCenterPosition = threeOfMatchParent / 2;
        secondNoteImageCenterPosition = textLayout.getMeasuredWidth() / 2;
        thiredNoteImageCenterPosition = threeOfMatchParent / 2 * 5;

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
                view.layout(firstNoteImageCenterPosition - imageViewWidth / 2, textViewHeight + paddingTop, firstNoteImageCenterPosition + imageViewWidth / 2, textViewHeight + paddingTop + view.getMeasuredHeight());//textViewHeight + paddingTop + view.getMeasuredHeight()
            }
            if (i == 1) {
                view.layout(secondNoteImageCenterPosition - imageViewWidth / 2, textViewHeight + paddingTop, secondNoteImageCenterPosition + imageViewWidth / 2, textViewHeight + paddingTop + view.getMeasuredHeight());
            }

            if (i == 2) {
                view.layout(thiredNoteImageCenterPosition - imageViewWidth / 2, textViewHeight + paddingTop, thiredNoteImageCenterPosition + imageViewWidth / 2, textViewHeight + paddingTop + view.getMeasuredHeight());
            }
        }

        progressBar.layout(firstNoteImageCenterPosition - offsetWidth, textViewHeight + paddingTop + imageViewHeight / 2 - progressBar.getHeight() / 2
                , thiredNoteImageCenterPosition, getWidth() - imageViewHeight / 2 + progressBar.getHeight() / 2);
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
