package example.jni.com.coffeeseller.views.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by WH on 2018/4/17.
 */

public class ProgressbarWithTextTest extends RelativeLayout {
    private Context context;
    private int offset = 5;
    private int maxProgress = 100;
    private int maxMakingProgress = 80;
    private int perWidthOfPerProgress;
    private int paddingTop = 20;
    private int onePointProgress = offset;
    private int twoPointProgress;
    private int threePointProgress;

    private LinearLayout progressBarLayout;
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

    public ProgressbarWithTextTest(Context context) {
        super(context);
        this.context = context;
    }

    public ProgressbarWithTextTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ProgressbarWithTextTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void init() {
        progressBar = (ProgressBar) ((LinearLayout) getChildAt(0)).getChildAt(0);
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

        new AsyncTask<Void, Integer, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                if (!makeSuccess) {
                    for (int i = offset + 1; i <= maxMakingProgress; i++) {

                        publishProgress(i);

                        if (i == maxMakingProgress) {
                            break;
                        }
                        Waiter.doWait(300);
                    }
                } else {
                    publishProgress(maxProgress);
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {

                setProgress(values[0]);
                super.onProgressUpdate(values);

            }
        }.execute();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width;
        int height;
        progressBarLayout = (LinearLayout) getChildAt(0);
        progressBar = (ProgressBar) ((LinearLayout) getChildAt(0)).getChildAt(0);
        textLayout = (LinearLayout) getChildAt(1);
        imageLayout = (LinearLayout) getChildAt(2);
        offset = progressBar.getProgress();
        this.measureChild(progressBar, widthMeasureSpec, heightMeasureSpec);
        this.measureChild(progressBarLayout, widthMeasureSpec, heightMeasureSpec);
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

        perWidthOfPerProgress = progressbarWidth / maxProgress;
        int progressWidth = progressBar.getProgress() * perWidthOfPerProgress;
        progressBarLayout.layout(textViewWidth / 2 - progressBar.getPaddingLeft(), textViewHeight + paddingTop + (imageViewHeight - progressBar.getHeight()) / 2,
                getWidth() - textViewWidth / 2, textViewHeight + paddingTop + (imageViewHeight - progressBar.getHeight()) / 2 + progressBar.getMeasuredHeight());
        progressBar.layout(textViewWidth / 2 - progressBar.getPaddingLeft() + 30, textViewHeight + paddingTop + (imageViewHeight - progressBar.getHeight()) / 2 + 10,
                getWidth() - textViewWidth / 2 - 30, textViewHeight + paddingTop + (imageViewHeight - progressBar.getHeight()) / 2 + progressBar.getMeasuredHeight() + 10);

        firstNoteImageCenterPosition = textViewWidth / 2 + progressWidth;//-progressBar.getPaddingLeft()
        secondNoteImageCenterPosition = (progressbarWidth - progressWidth) / 2 + progressWidth; //+ firstNoteImageCenterPosition;//(progressBar.getMeasuredWidth() - offset) / 2 + firstNoteImageCenterPosition - getPaddingLeft();
        thiredNoteImageCenterPosition = progressbarWidth - textViewWidth / 2;//+ progressBar.getPaddingRight() - textViewWidth / 2;


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

            if (i == 2) {//thiredNoteImageCenterPosition + imageViewWidth / 2
                view.layout(thiredNoteImageCenterPosition - imageViewWidth / 2, textViewHeight + paddingTop, thiredNoteImageCenterPosition + imageViewWidth / 2, textViewHeight + paddingTop + view.getMeasuredHeight());
            }
        }
//        progressBar.layout(textViewWidth / 2, getHeight() - progressBar.getMeasuredHeight(), getWidth() - textViewWidth / 2, getHeight() - imageViewHeight / 2 + progressBar.getMeasuredHeight() / 2);


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
