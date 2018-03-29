package example.jni.com.coffeeseller.views.customviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.utils.ScreenUtil;

/**
 * Created by WH on 2018/3/22.
 */

public class ChooseCupDialog extends Dialog implements DialogInterface.OnClickListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Context context;
    private View view;
    private ChooseCupListenner listenner;
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

    public void setListenner(ChooseCupListenner listenner) {
        this.listenner = listenner;
    }

    public void init() {
        initView();
        coffeeFomat = new CoffeeFomat();

        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setTitle(null);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wl.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.alpha = 0.9f;
        window.setAttributes(wl);
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


        setContentView(view);
        setCanceledOnTouchOutside(true);
    }

    public void showDialog() {
        show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_NEGATIVE:
                break;
            case AlertDialog.BUTTON_POSITIVE:
                break;
            case AlertDialog.BUTTON_NEUTRAL:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.coffeeNumAdd:
                coffeeFomat.cupAdd();

                break;
            case R.id.coffeeNumSub:
                coffeeFomat.cupSub();
                break;
            case R.id.ok:
                if (listenner != null) {
                    listenner.getResult(coffeeFomat);
                }
                dismiss();
                break;
            case R.id.giveUp:
                viewHolder.mGiveUp.setEnabled(false);
                final View view = LayoutInflater.from(context).inflate(R.layout.make_out_layout, null);
             /*   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT); */
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.getScreenWidth(context)
                        , ScreenUtil.getScreenHeight(context));
                addContentView(view, layoutParams);
                AnimationSet set = new AnimationSet(true);
//                TranslateAnimation animation = new TranslateAnimation(view.getWidth(), view.getWidth() / 2, view.getHeight(), view.getHeight() / 2);
                ScaleAnimation alphaAnimation = new ScaleAnimation(5f, 1f, 5f, 1f);
                set.addAnimation(alphaAnimation);
//                set.addAnimation(animation);
                set.setDuration(3000);
                view.startAnimation(set);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        dismiss();
                    }
                }, 1000);

//                dismiss();
//                view.setVisibility(View.GONE);
               /* this.cancel();
                dismiss();*/
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

    }

    class ViewHolder {
        public ImageView mCoffeeImage, mGiveUp, mOk, mCoffeeNumSub, mCoffeeNumAdd;
        public TextView mCoffeeName, mCoffeePrice, mTip, mCoffeeNum;
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
            mTip = (TextView) view.findViewById(R.id.tip);
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
