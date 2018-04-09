package example.jni.com.coffeeseller.views.customviews;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.utils.CoffeeFomatInterface;
import example.jni.com.coffeeseller.utils.ObjectAnimatorUtil;
import example.jni.com.coffeeseller.utils.ScreenUtil;

/**
 * Created by WH on 2018/3/22.
 */

public class ChooseCupDialog extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Context context;
    private View view;
    private ChooseCupListenner listenner;
    private Coffee coffee;
    private CoffeeFomat coffeeFomat;
    private ViewHolder viewHolder;

    public ChooseCupDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ChooseCupDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    public void setData(Coffee coffee, ChooseCupListenner listenner) {
        this.coffee = coffee;
        this.listenner = listenner;
        coffeeFomat.setCoffee(coffee);

        initData();
    }

    public void init() {
        initView();
        coffeeFomat = new CoffeeFomat();

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
        /*
        * 初始化数据
        * */
        viewHolder.mCoffeePrice.setText(coffee.price + "");
        changeText();

        /*
        * 没有多奶规格
        * */
        if (coffee.milkFomat == -1) {
            //    viewHolder.mTaste_milk.setVisibility(View.GONE);
        }
        /*
        * 可使用规格不足
        * */
        for (int i = 0; i < viewHolder.mTaste_suger.getChildCount(); i++) {
            if (i != 0) {
                RadioButton button = (RadioButton) viewHolder.mTaste_suger.getChildAt(i);
                button.setEnabled(false);
                button.setAlpha(0.3f);
            }
        }
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.choose_cup_layout, null);
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
        }

        viewHolder.mCoffeeNumAdd.setOnClickListener(this);
        viewHolder.mCoffeeNumSub.setOnClickListener(this);
        viewHolder.mOk.setOnClickListener(this);
        viewHolder.mGiveUp.setOnClickListener(this);
        viewHolder.mTaste_suger.setOnCheckedChangeListener(this);
        viewHolder.mTaste_milk.setOnCheckedChangeListener(this);
        viewHolder.mCoffeeNumSub.setAlpha(0.3f);
        setContentView(view);
        setCanceledOnTouchOutside(false);
    }

    public void showDialog() {

        show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.coffeeNumAdd:

                coffeeFomat.cupAdd();
                changeText();
                if (coffeeFomat.isMax()) {
                    viewHolder.mCoffeeNumAdd.setAlpha(0.3f);
                }
                viewHolder.mCoffeeNumSub.setAlpha(1f);

                break;
            case R.id.coffeeNumSub:

                coffeeFomat.cupSub();
                changeText();
                if (coffeeFomat.isMin()) {
                    viewHolder.mCoffeeNumSub.setAlpha(0.3f);
                }
                viewHolder.mCoffeeNumAdd.setAlpha(1f);
                break;
            case R.id.ok:

                if (listenner != null) {
                    listenner.getResult(coffeeFomat);
                }
                dismiss();
                break;
            case R.id.giveUp:

                giveUp();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listenner != null) {
                            listenner.cancle();
                        }
                    }
                }, 1300);
                break;
        }
    }

    private void changeText() {
        viewHolder.mCoffeeNum.setText(coffeeFomat.getCup() + "");
        viewHolder.mTipCupNum.setText(coffeeFomat.getCup() + "");
        viewHolder.mTipName.setText(coffee.name);
        BigDecimal bigDecimal = new BigDecimal(Float.parseFloat(coffee.price) * coffeeFomat.getCup());
        float pay = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        viewHolder.mTipPayM.setText(pay + "");
    }

    private void giveUp() {
        viewHolder.mGiveUp.setEnabled(false);
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

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.suger_no:
                coffeeFomat.setSurgerFomat(CoffeeFomatInterface.SUGER_NO);
                break;
            case R.id.suger_less:
                coffeeFomat.setSurgerFomat(CoffeeFomatInterface.SUGER_LESS);
                break;
            case R.id.suger_normal:
                viewHolder.mTaste_suger.clearChildFocus(viewHolder.mTaste_suger.getChildAt(2));
                coffeeFomat.setSurgerFomat(CoffeeFomatInterface.SUGER_NORMAL);
                break;
            case R.id.suger_more:

                coffeeFomat.setSurgerFomat(CoffeeFomatInterface.SUGER_MORE);
                break;
            case R.id.milk_no:
                coffeeFomat.setMilkFomat(CoffeeFomatInterface.MILK_NO);
                break;
            case R.id.milk_less:
                coffeeFomat.setMilkFomat(CoffeeFomatInterface.MILK_LESS);
                break;
            case R.id.milk_normal:
                coffeeFomat.setMilkFomat(CoffeeFomatInterface.MILK_NORMAL);
                break;
            case R.id.milk_more:
                coffeeFomat.setMilkFomat(CoffeeFomatInterface.MILK_MORE);
                break;
        }
    }

    class ViewHolder {
        public ImageView mCoffeeImage, mGiveUp, mOk, mCoffeeNumSub, mCoffeeNumAdd;
        public TextView mCoffeeName, mCoffeePrice, mCoffeeNum, mTipCupNum, mTipName, mTipPayM;
        public RadioGroup mTaste_suger, mTaste_milk;
        public RadioButton mSuger_no, mSuger_less, mSuger_normal, mSuger_more;
        public RadioButton mMilk_no, mMilk_less, mMilk_normal, mMilk_more;

        public ViewHolder(View view) {
            initView(view);
        }

        private void initView(View view) {
            mCoffeeImage = (ImageView) view.findViewById(R.id.coffeeImage);
            mCoffeeName = (TextView) view.findViewById(R.id.coffeeName);
            mCoffeePrice = (TextView) view.findViewById(R.id.coffeePrice);
            mTipCupNum = (TextView) view.findViewById(R.id.tipCupNum);
            mTipName = (TextView) view.findViewById(R.id.tipName);
            mTipPayM = (TextView) view.findViewById(R.id.tipPayM);
            mGiveUp = (ImageView) view.findViewById(R.id.giveUp);
            mOk = (ImageView) view.findViewById(R.id.ok);
            mCoffeeNumSub = (ImageView) view.findViewById(R.id.coffeeNumSub);
            mCoffeeNum = (TextView) view.findViewById(R.id.coffeeNum);
            mCoffeeNumAdd = (ImageView) view.findViewById(R.id.coffeeNumAdd);
            mTaste_suger = (RadioGroup) view.findViewById(R.id.taste_suger);
            mSuger_no = (RadioButton) view.findViewById(R.id.suger_no);
            mSuger_less = (RadioButton) view.findViewById(R.id.suger_less);
            mSuger_normal = (RadioButton) view.findViewById(R.id.suger_normal);
            mSuger_more = (RadioButton) view.findViewById(R.id.suger_more);
            mTaste_milk = (RadioGroup) view.findViewById(R.id.taste_milk);
            mMilk_no = (RadioButton) view.findViewById(R.id.milk_no);
            mMilk_less = (RadioButton) view.findViewById(R.id.milk_less);
            mMilk_normal = (RadioButton) view.findViewById(R.id.milk_normal);
            mMilk_more = (RadioButton) view.findViewById(R.id.milk_more);
        }
    }
}
