package example.jni.com.coffeeseller.views.customviews;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
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
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.bean.Step;
import example.jni.com.coffeeseller.bean.Taste;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.utils.CoffeeFomatInterface;
import example.jni.com.coffeeseller.utils.ObjectAnimatorUtil;
import example.jni.com.coffeeseller.utils.ScreenUtil;

/**
 * Created by WH on 2018/3/22.
 */

public class ChooseCupDialogTest extends Dialog implements View.OnClickListener {
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

        initTaste(coffee.getStepList());
    }


    private void checkHotOrCold(boolean checkHot) {
        if (checkHot) {
            viewHolder.mCoffeeHot.setSelected(true);
            viewHolder.mCoffeeCold.setSelected(false);
            coffeeFomat.setTemp(CoffeeFomatInterface.HOT);
        } else {
            viewHolder.mCoffeeHot.setSelected(true);
            viewHolder.mCoffeeCold.setSelected(false);
            coffeeFomat.setTemp(CoffeeFomatInterface.COLD);
        }
    }

    private void initTaste(List<Step> steps) {
        if (steps == null || steps.size() <= 0) {
            return;
        }

        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            viewHolder.addView(step);
        }

    }

    private void updateTextColor(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton button = (RadioButton) group.getChildAt(i);
            if (button.getId() != checkedId) {
                button.setSelected(false);
            } else {
                button.setSelected(true);
             //   coffeeFomat.set
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

    class ViewHolder {

        public TextView mCoffeeName;
        public TextView mCoffeeCold;
        public TextView mCoffeeHot;
        public TextView mCoffeePrice;
        public ImageView mQrCodeImage;
        public LinearLayout mTastLayout;

        public ViewHolder(View view) {
            initView(view);
        }

        private void initView(View view) {
            mCoffeeName = (TextView) view.findViewById(R.id.coffeeName);
            mQrCodeImage = (ImageView) view.findViewById(R.id.qrCodeImage);
            mCoffeeCold = (TextView) view.findViewById(R.id.coffeeCold);
            mCoffeeHot = (TextView) view.findViewById(R.id.coffeeHot);
            mCoffeePrice = (TextView) view.findViewById(R.id.coffeePrice);
            mTastLayout = (LinearLayout) view.findViewById(R.id.tast_layout);
        }

        public void addView(final Step step) {
            if (step == null) {
                return;
            }
            List<Taste> tastes = step.getTastes();
            Material material = step.getMaterial();
            if (tastes == null || tastes.size() <= 0 || material == null) {
                return;
            }

            View tasteView = LayoutInflater.from(context).inflate(R.layout.coffee_taste_suger_choose, null);
            TextView tasteName = (TextView) tasteView.findViewById(R.id.taste_name);
            RadioGroup group = (RadioGroup) tasteView.findViewById(R.id.taste_suger);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    updateTextColor(group, checkedId);
                }
            });
            tasteName.setText(material.getName());
            for (int i = 0; i < tastes.size(); i++) {
                Taste taste = tastes.get(i);
                View childView = LayoutInflater.from(context).inflate(R.layout.radio_btn, null);
                RadioButton radioButton = (RadioButton) childView.findViewById(R.id.tasteChild);
                radioButton.setText(taste.getRemark());
                group.addView(childView);
            }
            setEnable(group, step);
            mTastLayout.addView(tasteView);
        }
    }

    /*
    * 根据数据库剩余量计算RadioButton是否可用
    * */
    private void setEnable(RadioGroup group, Step step) {

        List<Taste> tastes = step.getTastes();
        Material material = step.getMaterial();
        if (tastes == null || tastes.size() <= 0 || material == null) {
            return;
        }

        MaterialSql table = new MaterialSql(context);
        Cursor cursor = table.getDataCursor(table.MATERIALS_COLUMN_MATERIALID, material.getMaterialID());
        cursor.moveToFirst();
        String stock = cursor.getString(cursor.getColumnIndex(table.MATERIALS_COLUMN_MATERIALSTOCK));
        if (TextUtils.isEmpty(stock)) {
            return;
        }

        int stockInt = Integer.parseInt(stock);

        for (int i = 0; i < group.getChildCount(); i++) {

            Taste taste = tastes.get(i);
            if (taste == null) {
                continue;
            }
      //      int useMaterial = taste.getAmount() / 100 * step.getMaterial_time();
          /*  if (stockInt < useMaterial) {
                RadioButton button = (RadioButton) group.getChildAt(i);
                button.setEnabled(false);
                button.setAlpha(0.8f);
            }*/
        }
    }
}
