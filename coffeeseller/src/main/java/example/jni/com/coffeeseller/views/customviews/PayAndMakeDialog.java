package example.jni.com.coffeeseller.views.customviews;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.math.BigDecimal;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.utils.ObjectAnimatorUtil;
import example.jni.com.coffeeseller.utils.ScreenUtil;

/**
 * Created by WH on 2018/3/22.
 */

public class PayAndMakeDialog extends Dialog implements View.OnClickListener {
    private static final int PAYING = 0;
    private static final int START = 1;
    private static final int MAKING = 2;
    private static final int MAKE_SUCCESS = 3;

    private static final int MAKING_COFFEE_MAX_TIME = 60;//60s

    private int curViewID = PAYING;
    private Context context;
    private MakingViewHolder makingViewHolder;


    public PayAndMakeDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PayAndMakeDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    public void init() {
        initView();

        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setTitle(null);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wl.width = ScreenUtil.getScreenWidth(context) / 2;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.alpha = 0.9f;
        window.setAttributes(wl);
    }

    public void initData() {

    }

    public void updateProgress(int progress) {
        makingViewHolder.mProgressBar.setSecondaryProgress(progress);

    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (makingViewHolder == null) {
            makingViewHolder = new MakingViewHolder();
        }

        setContentView(makingViewHolder.view);
    }


    public void showDialog() {

        show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }
    }

    class MakingViewHolder {
        public View view;
        public ProgressbarWithText progressbarWithText;
        public TextView mProgressTextOne;
        public TextView mProgressTextTwo;
        public TextView mProgressTextThree;

        public ProgressBar mProgressBar;

        public MakingViewHolder() {
            initView();
        }

        private void initView() {
            view = LayoutInflater.from(context).inflate(R.layout.make_out_layout, null);
            progressbarWithText = (ProgressbarWithText) view.findViewById(R.id.progressTextLayout);
            mProgressTextOne = (TextView) progressbarWithText.findViewById(R.id.tipOne);
            mProgressTextTwo = (TextView) progressbarWithText.findViewById(R.id.tipTwo);
            mProgressTextThree = (TextView) progressbarWithText.findViewById(R.id.tipThress);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressbarWithText.init();
            progressbarWithText.setProgress(0);
        }

    }
}
