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
import android.widget.RadioButton;

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
    private static final int MAKING = 1;
    private static final int MAKE_SUCCESS = 2;
    private int curViewID = PAYING;
    private Context context;
    private PayViewHolder payViewHolder;
    private MakingViewHolder makingViewHolder;
    private MakeSuccessViewHolder makeSuccessViewHolder;

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

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (payViewHolder == null) {
            payViewHolder = new PayViewHolder();
        }
        if (makingViewHolder == null) {
            makingViewHolder = new MakingViewHolder();
        }
        if (makeSuccessViewHolder == null) {
            makeSuccessViewHolder = new MakeSuccessViewHolder();
        }
        changeView();

    }

    private void changeView() {
        switch (curViewID) {
            case PAYING:
                setContentView(payViewHolder.view);
                setCanceledOnTouchOutside(true);
                break;
            case MAKING:
                setContentView(makingViewHolder.view);
                setCanceledOnTouchOutside(false);
                break;
            case MAKE_SUCCESS:
                setContentView(makeSuccessViewHolder.view);
                setCanceledOnTouchOutside(false);
                break;
        }
    }

    public void showDialog() {

        show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.coffeeNumAdd:

                break;
            case R.id.coffeeNumSub:


                break;
            case R.id.ok:


                break;
            case R.id.giveUp:

                giveUp();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1300);
                break;
        }
    }

    private void giveUp() {
        final View view = LayoutInflater.from(context).inflate(R.layout.make_out_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.outImg);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(view, layoutParams);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimatorUtil.scaleXAnimator(imageView, 4f, 1f);
        ObjectAnimator scaleY = ObjectAnimatorUtil.scaleYAnimator(imageView, 4f, 1f);
        ObjectAnimator alpha = ObjectAnimatorUtil.alphaAnimator(imageView, 0f, 1f);
        animatorSet.setDuration(300);
        animatorSet.play(scaleX).with(scaleY).with(alpha);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    class PayViewHolder {
        public View view;

        public PayViewHolder() {
            initView();
        }

        private void initView() {
        }

    }

    class MakingViewHolder {
        public View view;

        public MakingViewHolder() {
            initView();
        }

        private void initView() {
        }

    }

    class MakeSuccessViewHolder {
        public View view;

        public MakeSuccessViewHolder() {
            initView();
        }

        private void initView() {
        }

    }
}
