package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.WaterType;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.MachineConfig.QRMsger;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.bean.Step;
import example.jni.com.coffeeseller.bean.Taste;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.model.listeners.MsgTransListener;
import example.jni.com.coffeeseller.parse.ParseRQMsg;
import example.jni.com.coffeeseller.parse.PayResult;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.QRMaker;
import example.jni.com.coffeeseller.utils.TextUtil;
import example.jni.com.coffeeseller.views.customviews.BuyDialog;

import static android.R.attr.button;
import static android.R.attr.track;

/**
 * Created by WH on 2018/4/25.
 */

public class ChooseCup implements View.OnClickListener, MsgTransListener {
    private static String TAG = "ChooseCup";
    private static int VIEW_SHOW_TIME = 2 * 60 * 1000;
    private Context mContext;
    private View mView;
    private ViewHolder mViewHolder;
    private Coffee mCoffee;
    private DealRecorder mDealRecorder;
    private CoffeeFomat mCoffeeFomat;
    private ChooseCupListenner mChooseCupListener;
    private MsgTransListener mMsgTransListener;
    private CountDownTimer countDownTimer;
    private Handler mHandler;
    private TradeMsgRequest tradeMsgRequest;
    private Bitmap qrBitmap;


    public ChooseCup(Context context, Coffee coffee, ChooseCupListenner listenner, Handler handler) {
        mContext = context;
        mCoffee = coffee;
        mChooseCupListener = listenner;
        mHandler = handler;
        init();
    }

    private void init() {
        initView();
        if (isMachineRight()) {
            initData();
        }
        countDownTime();
    }

    public View getView() {
        return mView;
    }

    private boolean isMachineRight() {
        if (CheckCurMachineState.getInstance().isCanMaking()) {
            MyLog.d(TAG, "machine is ok ");
            mViewHolder.mContentLayout.setVisibility(View.VISIBLE);
            mViewHolder.mQrLayout.setVisibility(View.VISIBLE);
            mViewHolder.mErrTip.setVisibility(View.GONE);
            mViewHolder.mFailedLayout.setVisibility(View.GONE);
            return true;
        } else {
            MyLog.d(TAG, "machine is has error ");
            mViewHolder.mErrTip.setVisibility(View.VISIBLE);
            mViewHolder.mContentLayout.setVisibility(View.GONE);
            mViewHolder.mQrLayout.setVisibility(View.GONE);
            mViewHolder.mErrTip.setText(CheckCurMachineState.getInstance().getStateTip());
            mViewHolder.mFailedLayout.setVisibility(View.VISIBLE);
            return false;
        }

    }

    private void initView() {

        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_pay_layout, null);
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder(mView);
        }

        mViewHolder.mCoffeeHot.setOnClickListener(this);
        mViewHolder.mCoffeeCold.setOnClickListener(this);
        mViewHolder.mClose.setOnClickListener(this);

        mCoffeeFomat = new CoffeeFomat();
        mDealRecorder = new DealRecorder();
        tradeMsgRequest = new TradeMsgRequest();
        mMsgTransListener = this;

        mViewHolder.mCoffeeName.setText(mCoffee.name);
        mViewHolder.mCoffeeName.setTextSize(TextUtil.textSize(mCoffee.name));

    }

    public void initData() {

        MyLog.d(TAG, "coffee= " + mCoffee);
        if (mCoffee == null) {
            return;
        }
        /*
        * 初始化数据
        * */

        mDealRecorder.setRqcup(1);
        mDealRecorder.setFormulaID(mCoffee.getFormulaID());

        BigDecimal bigDecimal = new BigDecimal(Float.parseFloat(mCoffee.price));
        float pay = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        mViewHolder.mCoffeePrice.setText("￥ " + pay + "");

        mViewHolder.mCoffeeName.setText(mCoffee.name);
        mViewHolder.mCoffeeName.setTextSize(TextUtil.textSize(mCoffee.name));

        mViewHolder.mRequestQRTxt.setOnClickListener(this);

        TaskService.getInstance().SetOnMsgListener(mMsgTransListener);

        checkHotOrCold(true);

        initTaste(mCoffee.getStepList());

        tradeMsgRequest.requestQRCode(this, mDealRecorder, mCoffee);

    }


    private void checkHotOrCold(boolean checkHot) {

        MyLog.d(TAG, "checkHotOrCold");
        if (checkHot) {
            mViewHolder.mCoffeeHot.setSelected(true);
            mViewHolder.mCoffeeCold.setSelected(false);
            mCoffeeFomat.setWaterType(WaterType.HOT_WATER);
        } else {
            mViewHolder.mCoffeeHot.setSelected(true);
            mViewHolder.mCoffeeCold.setSelected(false);
            mCoffeeFomat.setWaterType(WaterType.COLD_WATER);
        }
    }

    private void initTaste(List<Step> steps) {
        MyLog.d(TAG, "initTaste");
        if (steps == null || steps.size() <= 0) {
            return;
        }

        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);

            mCoffeeFomat.getContainerConfigs().add(i, step.getContainerConfig());

            mViewHolder.addView(step, i);
        }

    }


    /**
     * 将二维码设置为loadingView的背景
     *
     * @param bitmap
     */
    void setQR_Code(final Bitmap bitmap) {
        qrBitmap = bitmap;
        Runnable mRun = new Runnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mViewHolder.mLoadingBar.clearAnimation();
                mViewHolder.mRequestQRTxt.setVisibility(View.GONE);
                mViewHolder.mLoadingBar.setVisibility(View.GONE);

                if (bitmap != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    mViewHolder.mQrCodeImage.setBackgroundDrawable(bitmapDrawable);
                } else {
                    mViewHolder.mQrCodeImage.setBackgroundResource(R.mipmap.qr_fault);
                    mViewHolder.mRequestQRTxt.setVisibility(View.VISIBLE);
                }
                mViewHolder.mQrCodeImage.setVisibility(View.VISIBLE);
            }
        };
        mHandler.post(mRun);
    }

    /*
* 根据返回信息，设置二维码
* */
    private void getQrMsg(ParseRQMsg parseRqMsg) {

        MyLog.W(TAG, "QR msg come now going to deal this msg");

        mDealRecorder.setOrder(parseRqMsg.getTradeCode());
        mDealRecorder.setPrice(parseRqMsg.getPrice());
        if (parseRqMsg == null) {
            MyLog.d(TAG, "parseRqMsg is empty");
            setQR_Code(null);
            return;
        }
        mDealRecorder.setRqcup(parseRqMsg.getCupNum());

        QRMaker mQrMaker = new QRMaker();
        Bitmap bitmap = mQrMaker.createBitmap(parseRqMsg.getQrCode(), 350, 350);
        setQR_Code(bitmap);

//        beginTaskCheckPay();
        tradeMsgRequest.beginTaskCheckPay(mMsgTransListener, mDealRecorder);
        MyLog.W(TAG, "dealOrder = " + mDealRecorder.getOrder());
    }

    /*
    * 接收支付检查结果，并处理
    * */
    private void getPayMsg(PayResult msg) {
        MyLog.d(TAG, "msg= " + msg);

        if (msg.getPayResult() == 2) {

            MyLog.W(TAG, " check pay result has payed");

            mDealRecorder.setPayed(true);
            mDealRecorder.setPayTime(msg.getPayTime());
            MyLog.d(TAG, "msg.getPayTime()= " + mDealRecorder.getPayTime());

            if (mChooseCupListener != null) {

                tradeMsgRequest.stopTaskCheckPay();
                getFinalContainerConfig();
                mChooseCupListener.hasPay(mCoffeeFomat, mDealRecorder);
            }

        } else if (msg.getPayResult() == 1) {
            mDealRecorder.setPayed(false);
            MyLog.W(TAG, "check pay result is paying ");
        } else if (msg.getPayResult() == 0) {
            mDealRecorder.setPayed(false);
            MyLog.W(TAG, "check pay result is not pay ");
        }
    }

    private void updateTextColor(LinearLayout tasteLayout, View checkedView, Step step, int index) {

        ContainerConfig containerConfig = mCoffeeFomat.getContainerConfigs().get(index);

        for (int i = 0; i < tasteLayout.getChildCount(); i++) {
            LinearLayout childLayout = (LinearLayout) tasteLayout.getChildAt(i);
            TextView textView = (TextView) childLayout.getChildAt(0);
            if (textView != checkedView) {
                textView.setSelected(false);
            } else {
                textView.setSelected(true);
                double materialTime = new BigDecimal((float) step.getTastes().get(i).getAmount() / 100)
                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                int useMaterial = (int) (materialTime * containerConfig.getMaterial_time());

                MyLog.d(TAG, "amount= " + step.getTastes().get(i).getAmount() + "getMaterial_time= " + useMaterial + " ----i= " + i);

                containerConfig.setMaterial_time(useMaterial);
                MyLog.d(TAG, "containerConfig.setMaterial_time= " + containerConfig.getMaterial_time());

                mCoffeeFomat.getContainerConfigs().add(index, containerConfig);

                MyLog.d(TAG, "containerConfig.setMaterial_time= " + mCoffeeFomat.getContainerConfigs().get(index).getMaterial_time());

                mCoffeeFomat.getTasteNameRatio().add(step.getTastes().get(i).getRemark() + "-" + step.getTastes().get(i).getAmount());

            }
        }
    }

    /*
   * 倒计时检测
   * */
    private void countDownTime() {

        stopCountDownTimer();

        countDownTimer = new CountDownTimer(VIEW_SHOW_TIME, 1000) {
            @Override
            public void onTick(final long millisUntilFinished) {

                mViewHolder.mCountDownTime.setText(millisUntilFinished / 1000 + " s");

                if (mDealRecorder.isPayed()) {

                    tradeMsgRequest.stopTaskCheckPay();
                    stopCountDownTimer();
                    return;
                }

            }

            @Override
            public void onFinish() {
                if (!mDealRecorder.isPayed()) {

                    if (mChooseCupListener != null) {

                        stopCountDownTimer();
                        tradeMsgRequest.stopTaskCheckPay();
                        mChooseCupListener.cancle(mDealRecorder.getOrder());
                    }
                }
            }
        };
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    /*
    * 获取最终的配方步骤
    * */
    private List<ContainerConfig> getFinalContainerConfig() {

        for (int i = 0; i < mCoffeeFomat.getContainerConfigs().size(); i++) {
            if (mCoffeeFomat.getContainerConfigs().get(i).getWater_capacity() != 0)
                mCoffeeFomat.getContainerConfigs().get(i).setWater_type(mCoffeeFomat.getWaterType());
        }
        return mCoffeeFomat.getContainerConfigs();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.requestQRTxt:
                if (qrBitmap != null) {
                    return;
                } else {

                    mViewHolder.mRequestQRTxt.setVisibility(View.GONE);
                    mViewHolder.mLoadingBar.setVisibility(View.VISIBLE);
                    tradeMsgRequest.requestQRCode(this, mDealRecorder, mCoffee);
                }

                break;

            case R.id.coffeeCold:
                mViewHolder.mCoffeeCold.setSelected(true);
                mViewHolder.mCoffeeHot.setSelected(false);
                mCoffeeFomat.setWaterType(WaterType.COLD_WATER);
                break;
            case R.id.coffeeHot:
                mViewHolder.mCoffeeCold.setSelected(false);
                mViewHolder.mCoffeeHot.setSelected(true);
                mCoffeeFomat.setWaterType(WaterType.HOT_WATER);
                break;
            case R.id.close:

                if (mChooseCupListener != null) {

                    stopCountDownTimer();
                    tradeMsgRequest.stopTaskCheckPay();
                    mChooseCupListener.cancle(mDealRecorder.getOrder());
                }
                break;
        }
    }

    @Override
    public void onMsgArrived(Object msg) {
        MyLog.d(TAG, "one msg arrived");

        if (msg != null) {

            if (msg instanceof ParseRQMsg && tradeMsgRequest.mCurRequest == TradeMsgRequest.REQUEST_QR_CODE) {

                ParseRQMsg parseRqMsg = (ParseRQMsg) msg;
                getQrMsg(parseRqMsg);

                MyLog.d(TAG, parseRqMsg.getQrCode());

            }
            if (msg instanceof PayResult && tradeMsgRequest.mCurRequest == TradeMsgRequest.REQUEST_CHECK_PAY) {
                PayResult aMsg = (PayResult) msg;
                getPayMsg(aMsg);
            }
        } else {
            if (tradeMsgRequest.mCurRequest == TradeMsgRequest.REQUEST_QR_CODE) {
                setQR_Code(null);
                MyLog.W(TAG, "no message come in reqQR");
            }
            if (tradeMsgRequest.mCurRequest == TradeMsgRequest.REQUEST_CHECK_PAY) {
                MyLog.W(TAG, "check pay msg is null");
            }

        }
    }

    class ViewHolder {

        public TextView mCoffeeName;
        public TextView mCoffeeCold;
        public TextView mCoffeeHot;
        public TextView mCoffeePrice;
        public ImageView mQrCodeImage;
        public LinearLayout mTastLayout;
        public ProgressBar mLoadingBar;
        public ImageView mClose;
        public TextView mCountDownTime;
        public TextView mErrTip;
        public LinearLayout mContentLayout;
        private LinearLayout mQrLayout;
        public LinearLayout mFailedLayout;
        public TextView mRequestQRTxt;


        public ViewHolder(View view) {
            initView(view);
        }

        private void initView(View view) {
            mCoffeeName = (TextView) view.findViewById(R.id.coffeeName);
            mQrCodeImage = (ImageView) view.findViewById(R.id.qrCodeImage);
            mCoffeeCold = (TextView) view.findViewById(R.id.coffeeCold);
            mCoffeeHot = (TextView) view.findViewById(R.id.coffeeHot);
            mRequestQRTxt = (TextView) view.findViewById(R.id.requestQRTxt);
            mCoffeePrice = (TextView) view.findViewById(R.id.coffeePrice);
            mTastLayout = (LinearLayout) view.findViewById(R.id.tast_layout);
            mLoadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);
            mClose = (ImageView) view.findViewById(R.id.close);
            mCountDownTime = (TextView) view.findViewById(R.id.countDownTime);
            mErrTip = (TextView) view.findViewById(R.id.errTip);
            mContentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);
            mQrLayout = (LinearLayout) view.findViewById(R.id.qr_layout);
            mFailedLayout = (LinearLayout) view.findViewById(R.id.failed_layout);


        }

        public void addView(final Step step, final int index) {
            if (step == null) {
                return;
            }
            List<Taste> tastes = step.getTastes();
            Material material = step.getMaterial();
            if (tastes == null || tastes.size() <= 0 || material == null) {
                return;
            }

            View tasteView = LayoutInflater.from(mContext).inflate(R.layout.coffee_taste_suger_choose, null);
            TextView tasteName = (TextView) tasteView.findViewById(R.id.taste_name);
            LinearLayout tasteLayout = (LinearLayout) tasteView.findViewById(R.id.taste_suger);

            tasteName.setText(material.getName());

            LinearLayout layout = setTast(tasteLayout, tastes, step, index);

            setEnable(layout, step);

            mTastLayout.addView(tasteView);

        }

        /*
          * 根据数据库剩余量计算RadioButton是否可用
          * */
        private void setEnable(LinearLayout tasteLayout, Step step) {

            List<Taste> tastes = step.getTastes();
            Material material = step.getMaterial();
            if (tastes == null || tastes.size() <= 0 || material == null) {
                return;
            }

            MaterialSql table = new MaterialSql(mContext);
            Cursor cursor = table.getDataCursor(table.MATERIALS_COLUMN_MATERIALID, material.getMaterialID());
            cursor.moveToFirst();
            String stock = cursor.getString(cursor.getColumnIndex(table.MATERIALS_COLUMN_MATERIALSTOCK));
            if (TextUtils.isEmpty(stock)) {
                return;
            }

            int stockInt = Integer.parseInt(stock);

            for (int i = 0; i < tasteLayout.getChildCount(); i++) {

                Taste taste = tastes.get(i);
                if (taste == null) {
                    continue;
                }
                int useMaterial = (int) (((float) taste.getAmount() / 100) * step.getContainerConfig().getMaterial_time());
                LinearLayout layout = (LinearLayout) tasteLayout.getChildAt(i);
                TextView textView = (TextView) layout.getChildAt(0);
                if (stockInt < useMaterial) {
                    textView.setEnabled(false);
                    textView.setAlpha(0.7f);
                    textView.setClickable(false);
                }
            }
        }
    }

    private LinearLayout setTast(LinearLayout layout, List<Taste> tastes, final Step step, final int index) {
        final LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(linearLayoutParams);
        for (int i = 0; i < tastes.size(); i++) {
            Taste taste = tastes.get(i);

            final LinearLayout childLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            childLayout.setLayoutParams(childLayoutParams);

            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewParams.setMargins(10, 20, 10, 10);
            textView.setPadding(20, 10, 20, 10);
            textView.setBackgroundResource(R.drawable.selector_taste_bng_shape);
            textView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_taste_text_color));
            textView.setText(taste.getRemark());
            textView.setTextSize(22);
            textView.setLayoutParams(textViewParams);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTextColor(linearLayout, v, step, index);
                }
            });

            if (taste.getAmount() == 100) {
                textView.setSelected(true);
            }

            childLayout.addView(textView);

            linearLayout.addView(childLayout);
        }
        layout.addView(linearLayout);
        return linearLayout;
    }
}
