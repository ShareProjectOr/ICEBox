package example.jni.com.coffeeseller.views.customviews;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
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

public class ChooseCupDialogTest extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Context context;
    private View view;
    private ChooseCupListenner listenner;
    private Coffee coffee;
    private CoffeeFomat coffeeFomat;
    private ViewHolder viewHolder;

    public ChooseCupDialogTest(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ChooseCupDialogTest(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
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

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_pay_layout, null);
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
        }

        viewHolder.mTaste_suger.setOnCheckedChangeListener(this);
        viewHolder.mTaste_milk.setOnCheckedChangeListener(this);
        viewHolder.mCoffeeHot.setOnClickListener(this);
        viewHolder.mCoffeeCold.setOnClickListener(this);
        setContentView(view);
        setCanceledOnTouchOutside(false);
    }

    public void setData(Coffee coffee, ChooseCupListenner listenner) {
        this.coffee = coffee;
        this.listenner = listenner;
        coffeeFomat.setCoffee(coffee);
        initData();
    }

    public void initData() {
        if (coffee == null) {
            return;
        }
        /*
        * 初始化数据
        * */
        BigDecimal bigDecimal = new BigDecimal(Float.parseFloat(coffee.price));
        float pay = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        viewHolder.mCoffeePrice.setText("￥ " + pay + "");

        viewHolder.mCoffeeName.setText(coffee.name);

        checkHotOrCold(true);

        initSugerGroup();

        initMilkGroup();
    }

    protected void setEnable(RadioGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            if (i != 0) {
                RadioButton button = (RadioButton) group.getChildAt(i);
                button.setEnabled(false);
                button.setAlpha(0.8f);
            }
        }
    }

    private void checkHotOrCold(boolean checkHot) {
        if (checkHot) {
            viewHolder.mCoffeeHot.setSelected(true);
            viewHolder.mCoffeeCold.setSelected(false);
        } else {
            viewHolder.mCoffeeHot.setSelected(true);
            viewHolder.mCoffeeCold.setSelected(false);
        }
    }

    private void initSugerGroup() {

        setEnable(viewHolder.mTaste_suger);
    }

    private void initMilkGroup() {

        setEnable(viewHolder.mTaste_milk);

    }

    private void updateTextColor(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton button = (RadioButton) group.getChildAt(i);
            if (button.getId() != checkedId) {
                button.setSelected(false);
            } else {
                button.setSelected(true);
            }
        }
    }


    public void showDialog() {

        show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.coffeeCold:
                viewHolder.mCoffeeCold.setSelected(true);
                viewHolder.mCoffeeHot.setSelected(false);
                break;
            case R.id.coffeeHot:
                viewHolder.mCoffeeCold.setSelected(false);
                viewHolder.mCoffeeHot.setSelected(true);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        if (group.getId() == R.id.taste_suger) {

            updateTextColor(viewHolder.mTaste_suger, checkedId);
        } else if (group.getId() == R.id.taste_milk) {
            updateTextColor(viewHolder.mTaste_milk, checkedId);
        }
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

        public TextView mCoffeeName;
        public TextView mCoffeeCold;
        public TextView mCoffeeHot;
        public TextView mCoffeePrice;
        public ImageView mQrCodeImage;

        public RadioGroup mTaste_suger, mTaste_milk;
        public RadioButton mSuger_no, mSuger_less, mSuger_normal, mSuger_more;
        public RadioButton mMilk_no, mMilk_less, mMilk_normal, mMilk_more;

        public ViewHolder(View view) {
            initView(view);
        }

        private void initView(View view) {
            mCoffeeName = (TextView) view.findViewById(R.id.coffeeName);
            mQrCodeImage = (ImageView) view.findViewById(R.id.qrCodeImage);
            mCoffeeCold = (TextView) view.findViewById(R.id.coffeeCold);
            mCoffeeHot = (TextView) view.findViewById(R.id.coffeeHot);
            mCoffeePrice = (TextView) view.findViewById(R.id.coffeePrice);

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
