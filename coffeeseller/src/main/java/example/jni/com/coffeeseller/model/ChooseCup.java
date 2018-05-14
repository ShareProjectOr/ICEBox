package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.content.DialogInterface;
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

import cof.ac.inter.CoffMsger;
import cof.ac.inter.ContainerConfig;
import cof.ac.inter.ContainerType;
import cof.ac.inter.DebugAction;
import cof.ac.inter.Result;
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
import example.jni.com.coffeeseller.utils.Waiter;
import example.jni.com.coffeeseller.views.customviews.BuyDialog;

import static android.R.attr.button;
import static android.R.attr.track;

/**
 * Created by WH on 2018/4/25.
 */

public class ChooseCup implements View.OnClickListener, MsgTransListener {
    private static String TAG = "ChooseCup";
    private static int VIEW_SHOW_TIME = 2 * 60 * 1000;
    private static long TIME_BEFORE_MK_TO_CLEAR = 2 * 60 * 60 * 1000;
    private static int NO_PAY = 0;
    private static int PAYING = 1;
    private static int PAYED = 2;
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
    private BuyDialog mBuyDialog;
    private Bitmap qrBitmap;
    private int curPayState = NO_PAY;
    private String curOrder = null;
    private long clearMachineWaitTime;


    public ChooseCup(Context context, Coffee coffee, ChooseCupListenner listenner, Handler handler, BuyDialog buyDialog) {
        mContext = context;
        mCoffee = coffee;
        mChooseCupListener = listenner;
        mHandler = handler;
        mBuyDialog = buyDialog;
        init();
    }

    private void init() {
        initView();
        if (isMachineRight()) {//如果正常
            initData();
        } else {//如果不正常

            //是否需要发送关门指令
            CheckCurMachineState.getInstance().sendCloseDoorComd();
        }
        countDownTime();
    }

    public View getView() {
        return mView;
    }

    public String getOrder() {
        return curOrder;
    }

    private boolean isMachineRight() {

        if (CheckCurMachineState.getInstance().isCanMaking() && CheckCurMachineState.getInstance().isCupEnghou(mContext)) {
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

//        tradeMsgRequest.requestQRCode(this, mDealRecorder, mCoffee);
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

            ContainerConfig containerConfig = new ContainerConfig();
            containerConfig.setMaterial_time(step.getContainerConfig().getMaterial_time());
            containerConfig.setWater_interval(step.getContainerConfig().getWater_interval());
            containerConfig.setWater_type(step.getContainerConfig().getWater_type());
            containerConfig.setContainer(step.getContainerConfig().getContainer());
            containerConfig.setRotate_speed(step.getContainerConfig().getRotate_speed());
            containerConfig.setStir_speed(step.getContainerConfig().getStir_speed());
            containerConfig.setWater_capacity(step.getContainerConfig().getWater_capacity());


            MyLog.d(TAG, step.getContainerConfig().getMaterial_time() + "----getMaterial_time= " + containerConfig.getMaterial_time());
            MyLog.d(TAG, step.getContainerConfig().getWater_interval() + "----getWater_interval= " + containerConfig.getWater_interval());
            MyLog.d(TAG, step.getContainerConfig().getWater_type() + "----getWater_type= " + containerConfig.getWater_type());
            MyLog.d(TAG, step.getContainerConfig().getContainer() + "----getContainer= " + containerConfig.getContainer());
            MyLog.d(TAG, step.getContainerConfig().getRotate_speed() + "----getRotate_speed= " + containerConfig.getRotate_speed());
            MyLog.d(TAG, step.getContainerConfig().getStir_speed() + "----getStir_speed= " + containerConfig.getStir_speed());
            MyLog.d(TAG, step.getContainerConfig().getWater_capacity() + "----getWater_capacity= " + containerConfig.getWater_capacity());


            mCoffeeFomat.getContainerConfigs().add(i, containerConfig);

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
                mViewHolder.mCoffeePrice.setText(mDealRecorder.getPrice());

                if (bitmap != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    mViewHolder.mQrCodeImage.setBackgroundDrawable(bitmapDrawable);
                    tradeMsgRequest.mCurRequest = TradeMsgRequest.REQUEST_CHECK_PAY;
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

        curOrder = parseRqMsg.getTradeCode();

        MyLog.d(TAG, "getPrice = " + parseRqMsg.getPrice());

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

        tradeMsgRequest.beginTaskCheckPay(mMsgTransListener, mDealRecorder);
        MyLog.W(TAG, "dealOrder = " + mDealRecorder.getOrder());
    }

    /*
    * 接收支付检查结果，并处理
    * */
    private void getPayMsg(PayResult msg) {
        MyLog.d(TAG, "msg= " + msg);

        if (curPayState == NO_PAY) {
            if (msg.getPayResult() == PAYING) {
                curPayState = PAYING;
            }
            if (msg.getPayResult() == PAYED) {
                curPayState = PAYED;
            }

            MyLog.W(TAG, "check pay result is not pay ");
        }
        if (curPayState == PAYING) {

            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        mViewHolder.mPaying.setVisibility(View.VISIBLE);
                        mViewHolder.mQrCodeImage.setVisibility(View.GONE);

                        if (mChooseCupListener != null) {

                            mChooseCupListener.paying();
                        }
                    }
                });
            }
            MyLog.W(TAG, "check pay result is paying ");

            if (msg.getPayResult() == PAYED) {
                curPayState = PAYED;
            }
        }
        if (curPayState == PAYED) {
            MyLog.W(TAG, " check pay result has payed");

            mDealRecorder.setPayed(true);
            mDealRecorder.setPayTime(msg.getPayTime());

            tradeMsgRequest.stopTaskCheckPay();

            getFinalContainerConfig();

            clearMachineWaitTime = System.currentTimeMillis();
        }
/*
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
            mViewHolder.mPaying.setVisibility(View.VISIBLE);

            MyLog.W(TAG, "check pay result is paying ");
        } else if (msg.getPayResult() == 0) {
            mDealRecorder.setPayed(false);
            MyLog.W(TAG, "check pay result is not pay ");
        }*/
    }

    private void updateTextColor(LinearLayout tasteLayout, View checkedView, Step step, int index) {

        ContainerConfig containerConfig = mCoffeeFomat.getContainerConfigs().get(index);

        for (int i = 0; i < tasteLayout.getChildCount(); i++) {
            LinearLayout childLayout = (LinearLayout) tasteLayout.getChildAt(i);
            TextView textView = (TextView) childLayout.getChildAt(0);
            if (textView != checkedView) {
                textView.setSelected(false);
                textView.setTag(false);
            } else {
                textView.setSelected(true);

                textView.setTag(true);

                float materialTime = ((float) step.getTastes().get(i).getAmount()) / 100;

                int useMaterial = Math.round(materialTime * step.getContainerConfig().getMaterial_time());

                MyLog.d(TAG, "containerConfig.getMaterial_time()= " + step.getContainerConfig().getMaterial_time()
                        + " ,time= " + materialTime + ",getMaterial_time= " + useMaterial + " ----i= " + step.getTastes().get(i).getRemark());

                containerConfig.setMaterial_time(useMaterial);

                mCoffeeFomat.getContainerConfigs().remove(index);

                mCoffeeFomat.getContainerConfigs().add(index, containerConfig);

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

                if (judgeHeapHot(millisUntilFinished)) return;

                if (mDealRecorder.isPayed()) {

                    tradeMsgRequest.stopTaskCheckPay();

                    setClearText((int) (millisUntilFinished / 100 % 3));


                    MyLog.d(TAG, "isCanClear= " + (System.currentTimeMillis() - mBuyDialog.getLastMkTime() > TIME_BEFORE_MK_TO_CLEAR));
                    if (System.currentTimeMillis() - mBuyDialog.getLastMkTime() > TIME_BEFORE_MK_TO_CLEAR) {
                        //清洗机器
                        int waitTime = ClearMachine.clearMachineAllModule(mCoffeeFomat.getContainerConfigs());


                        if (System.currentTimeMillis() - clearMachineWaitTime > waitTime) {

                            if (mChooseCupListener != null) {

                                mChooseCupListener.hasPay(mCoffeeFomat, mDealRecorder);
                                stopCountDownTimer();
                            }
                        }

                        MyLog.d(TAG, "waitTime= " + waitTime);
                    }
                    if (mChooseCupListener != null) {

                        mChooseCupListener.hasPay(mCoffeeFomat, mDealRecorder);
                        stopCountDownTimer();
                    }

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

    public void setClearText(int pointNum) {
        mViewHolder.mRequestQRTxt.setVisibility(View.VISIBLE);
        mViewHolder.mQrCodeImage.setVisibility(View.GONE);
        mViewHolder.mPaying.setVisibility(View.GONE);
        mViewHolder.mLoadingBar.setVisibility(View.VISIBLE);

        String tipText = TextUtil.textPointNum("清洗中", pointNum);

        mViewHolder.mRequestQRTxt.setText(tipText);
    }

    /*
    * 判断锅炉是否正在加热中，如果不在加热中，显示二维码
    * */
    private boolean judgeHeapHot(long millisUntilFinished) {
        if (tradeMsgRequest.mCurRequest != TradeMsgRequest.DEFAULT) {
            return true;
        }

        if (CheckCurMachineState.getInstance().isCanMaking()) {
            if (CheckCurMachineState.getInstance().isHotting()) {
                mViewHolder.mRequestQRTxt.setVisibility(View.VISIBLE);
                mViewHolder.mQrCodeImage.setVisibility(View.GONE);

                String tipText = TextUtil.textPointNum("锅炉加热中", (int) (millisUntilFinished / 1000 % 3));

                mViewHolder.mRequestQRTxt.setText(tipText);
            } else {
                mViewHolder.mRequestQRTxt.setVisibility(View.GONE);

                mViewHolder.mRequestQRTxt.setText("点击重新获取");

                if (tradeMsgRequest.mCurRequest == TradeMsgRequest.DEFAULT) {
                    tradeMsgRequest.requestQRCode(mMsgTransListener, mDealRecorder, mCoffee);
                }
            }
        }
        return false;
    }

    private void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public void stopTimer() {
        tradeMsgRequest.stopTaskCheckPay();
        stopCountDownTimer();
    }

    /*
    * 获取最终的配方步骤
    * */
    private List<ContainerConfig> getFinalContainerConfig() {

        for (int i = 0; i < mCoffeeFomat.getContainerConfigs().size(); i++) {
            if (mCoffeeFomat.getContainerConfigs().get(i).getWater_capacity() != 0)
                mCoffeeFomat.getContainerConfigs().get(i).setWater_type(mCoffeeFomat.getWaterType());
        }

        mCoffeeFomat.setCoffeeName(mCoffee.name);

        int count = mViewHolder.mTastLayout.getChildCount();

        MyLog.d(TAG, "getFinalContainerConfig count= " + count);

        for (int i = 0; i < count; i++) {
            View view = mViewHolder.mTastLayout.getChildAt(i);
            LinearLayout tasteLayout = (LinearLayout) view.findViewById(R.id.taste_suger);
            Step step = (Step) tasteLayout.getTag();
            if (step == null || step.getTastes() == null || step.getTastes().size() <= 0) {
                continue;
            }
            LinearLayout layout = (LinearLayout) tasteLayout.getChildAt(0);
            MyLog.d(TAG, "getFinalContainerConfig layout count= " + step.getTastes().size());

            for (int selectedId = 0; selectedId < step.getTastes().size(); selectedId++) {

                LinearLayout childLayout = (LinearLayout) layout.getChildAt(selectedId);
                TextView textView = (TextView) childLayout.getChildAt(0);

                MyLog.d(TAG, "getFinalContainerConfig layout selected= " + textView.isSelected());

                if (textView.isSelected()) {

                    if (mCoffeeFomat.getTasteNameRatio().size() > i) {
                        mCoffeeFomat.getTasteNameRatio().remove(i);
                    }

                    mCoffeeFomat.getTasteNameRatio().add(i, step.getTastes().get(selectedId).getRemark() + "-" + step.getTastes().get(i).getAmount());
                }
            }
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

                MyLog.d(TAG, "qrcode come " + parseRqMsg.getQrCode());

            }
            if (msg instanceof PayResult && tradeMsgRequest.mCurRequest == TradeMsgRequest.REQUEST_CHECK_PAY) {
                PayResult aMsg = (PayResult) msg;
                getPayMsg(aMsg);

                MyLog.d(TAG, "payResult come !");
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
        public TextView mPaying;


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
            mPaying = (TextView) view.findViewById(R.id.paying);
            mContentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);
            mQrLayout = (LinearLayout) view.findViewById(R.id.qr_layout);
            mFailedLayout = (LinearLayout) view.findViewById(R.id.failed_layout);


        }

        public void addView(final Step step, final int index) {

            MyLog.d(TAG, "addView has been called");

            if (step == null) {
                return;
            }

            MyLog.d(TAG, "addView has been called,and step !=null");

            List<Taste> tastes = step.getTastes();
            Material material = step.getMaterial();

            MyLog.d(TAG, "addView has been called,and step !=null ,and tastes =" + tastes.size());

            if (tastes == null || tastes.size() <= 0 || material == null) {
                return;
            }

            View tasteView = LayoutInflater.from(mContext).inflate(R.layout.coffee_taste_suger_choose, null);
            TextView tasteName = (TextView) tasteView.findViewById(R.id.taste_name);
            LinearLayout tasteLayout = (LinearLayout) tasteView.findViewById(R.id.taste_suger);

            tasteName.setText(material.getName());

            LinearLayout layout = setTast(tasteLayout, tastes, step, index);

            tasteLayout.setTag(step);

            setEnable(layout, step, index);

            mTastLayout.addView(tasteView);

        }

        /*
          * 根据数据库剩余量计算RadioButton是否可用
          * */
        private void setEnable(LinearLayout tasteLayout, Step step, int index) {

            List<Taste> tastes = step.getTastes();
            Material material = step.getMaterial();
            if (tastes == null || tastes.size() <= 0 || material == null) {
                return;
            }


            MaterialSql table = new MaterialSql(mContext);
            String sqlRestMaterial = table.getStorkByMaterialID(step.getMaterial().getMaterialID() + "");

            MyLog.d(TAG, "sqlRestMaterial =" + sqlRestMaterial);


            if (TextUtils.isEmpty(sqlRestMaterial)) {
                return;
            }
            int sqlRestMaterialInt = Integer.parseInt(sqlRestMaterial);

            TextView nomalView = null;
            for (int i = 0; i < tasteLayout.getChildCount(); i++) {

                Taste taste = tastes.get(i);
                if (taste == null) {
                    continue;
                }

                float useMaterial = ((float) taste.getAmount()) / 100 * step.getAmount();

                MyLog.d(TAG, "useMaterial =" + useMaterial);
                LinearLayout layout = (LinearLayout) tasteLayout.getChildAt(i);
                TextView textView = (TextView) layout.getChildAt(0);
                if (sqlRestMaterialInt < useMaterial) {
                    textView.setEnabled(false);
                    textView.setAlpha(0.7f);
                    textView.setClickable(false);
                }

            }

            for (int i = 0; i < tasteLayout.getChildCount(); i++) {
                LinearLayout layout = (LinearLayout) tasteLayout.getChildAt(i);
                TextView textView = (TextView) layout.getChildAt(0);
                if (textView.isEnabled() && tastes.get(i).getAmount() == 100) {
                    updateTextColor(tasteLayout, textView, step, index);
                    break;
                } else {
                    if (textView.isEnabled()) {
                        updateTextColor(tasteLayout, textView, step, index);
                    }
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

            childLayout.addView(textView);

            linearLayout.addView(childLayout);
        }
        layout.addView(linearLayout);
        return linearLayout;
    }
}
