package example.jni.com.coffeeseller.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import cof.ac.inter.CoffMsger;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.model.ChooseCup;
import example.jni.com.coffeeseller.model.MkCoffee;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.model.listeners.MkCoffeeListenner;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.ScreenUtil;
import example.jni.com.coffeeseller.views.fragments.BuyFragment;

/**
 * Created by WH on 2018/3/22.
 */

public class BuyDialog extends Dialog implements ChooseCupListenner, MkCoffeeListenner {
    private static String TAG = "BuyDialog";
    private static BuyDialog mInstance;
    private Context context;
    private Coffee coffee;
    private Handler handler;
    private ChooseCupListenner chooseCupListenner;
    private MkCoffeeListenner mkCoffeeListenner;

    public static BuyDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BuyDialog(context, R.style.dialog);
        }
        return mInstance;
    }

    public BuyDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BuyDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    public void init() {

        initView();
        initData();
    }

    private void initView() {

        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setTitle(null);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wl.width = ScreenUtil.getScreenWidth(context) / 2;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.alpha = 0.9f;
        window.setAttributes(wl);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
    }

    private void initData() {
        handler = new Handler();
        coffee = BuyFragment.curSelectedCoffee;
        chooseCupListenner = this;
        mkCoffeeListenner = this;
    }

    public void setInitView() {
        ChooseCup chooseCup = new ChooseCup(context, coffee, chooseCupListenner, handler);
        setContentView(chooseCup.getView());
    }


    public void showDialog() {
        show();
    }

    public void disDialog() {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override
    public void cancle(String order) {
        //通知服务器取消订单

        MyLog.d(TAG, "choosecup is cancle");

        disDialog();
    }

    @Override
    public void hasPay(final CoffeeFomat coffeeFomat, final DealRecorder dealRecorder) {
        final CoffeeFomat fomat = coffeeFomat;
        handler.post(new Runnable() {
            @Override
            public void run() {
                MkCoffee mkCoffee = new MkCoffee(context, fomat,dealRecorder, mkCoffeeListenner);
                setContentView(mkCoffee.getView());
            }
        });

    }

    @Override
    public void getMkResult(DealRecorder dealRecorder, boolean makeSuccess) {

        //向数据库中添加交易记录

        //上报交易结果给服务器

        //上报原料使用量给服务器

        //更新BuyFragment ui
    }
}
