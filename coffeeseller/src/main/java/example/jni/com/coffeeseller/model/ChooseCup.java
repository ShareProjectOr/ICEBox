package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import example.jni.com.coffeeseller.views.customviews.BuyDialog;

/**
 * Created by WH on 2018/4/25.
 */

public class ChooseCup implements View.OnClickListener, MsgTransListener {
    private static String TAG = "ChooseCup";
    private static int REQUEST_QR_CODE = 0;
    private static int REQUEST_CHECK_PAY = 1;
    private static int VIEW_SHOW_TIME = 2 * 60 * 1000;
    private Context mContext;
    private View mView;
    private ViewHolder mViewHolder;
    private Coffee mCoffee;
    private DealRecorder mDealRecorder;
    private CoffeeFomat mCoffeeFomat;
    private ChooseCupListenner mChooseCupListener;
    private MsgTransListener mMsgTransListener;
    private TimerTask mCheckPayTask = null;
    private Timer mTimer = null;
    private Handler mHandler;
    private int mCurRequest;


    public ChooseCup(Context context, Coffee coffee, ChooseCupListenner listenner, Handler handler) {
        mContext = context;
        mCoffee = coffee;
        mChooseCupListener = listenner;
        mHandler = handler;
        init();
    }

    private void init() {
        initView();
        if (CheckCurMachineState.getInstance().isCanMaking()) {
            MyLog.d(TAG, "machine is ok ");
            mViewHolder.mContentLayout.setVisibility(View.VISIBLE);
            mViewHolder.mErrTip.setVisibility(View.GONE);
        } else {
            MyLog.d(TAG, "machine is has error ");
            mViewHolder.mContentLayout.setVisibility(View.GONE);
            mViewHolder.mErrTip.setVisibility(View.VISIBLE);
            mViewHolder.mErrTip.setText(CheckCurMachineState.getInstance().getStateTip());
        }
//        mViewHolder.mContentLayout.setVisibility(View.VISIBLE);


        initData();
    }

    public View getView() {
        return mView;
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
        mMsgTransListener = this;
    }

    public void initData() {

        MyLog.d(TAG, "coffee= " + mCoffee);
        if (mCoffee == null) {
            return;
        }
        /*
        * 初始化数据
        * */

        mCoffeeFomat = new CoffeeFomat();
        mDealRecorder = new DealRecorder();
        mMsgTransListener = this;

        BigDecimal bigDecimal = new BigDecimal(Float.parseFloat(mCoffee.price));
        float pay = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        mViewHolder.mCoffeePrice.setText("￥ " + pay + "");

        mViewHolder.mCoffeeName.setText(mCoffee.name);

        TaskService.getInstance().SetOnMsgListener(mMsgTransListener);

        checkHotOrCold(true);

        initTaste(mCoffee.getStepList());

        requestQRCode();

        countDownTime();
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

    /*
* 请求二维码
* */
    private void requestQRCode() {

        MyLog.d(TAG, "requestQRCode");
        mCurRequest = REQUEST_QR_CODE;
        mDealRecorder.setRqcup(1);
        mDealRecorder.setFormulaID(mCoffee.getFormulaID());
        QRMsger qrMsger = new QRMsger(mDealRecorder);
        qrMsger.reqQR(this, null);
    }

    /**
     * 将二维码设置为loadingView的背景
     *
     * @param bitmap
     */
    void setQR_Code(final Bitmap bitmap) {
        Runnable mRun = new Runnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mViewHolder.mLoadingBar.clearAnimation();
                mViewHolder.mLoadingBar.setVisibility(View.GONE);

                if (bitmap != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    mViewHolder.mQrCodeImage.setBackgroundDrawable(bitmapDrawable);
                } else {
                    mViewHolder.mQrCodeImage.setBackgroundResource(R.drawable.ic_launcher);
                }
                mViewHolder.mQrCodeImage.setVisibility(View.VISIBLE);
            }
        };
        mHandler.post(mRun);
    }

    /*
* 开始检查订单是否支付
* */
    private void beginTaskCheckPay() {

        mCurRequest = REQUEST_CHECK_PAY;

        if (mTimer == null) {

            mTimer = new Timer(true);
        }
        if (mCheckPayTask == null) {

            mCheckPayTask = new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    if (!mDealRecorder.isPayed() && mDealRecorder.isVlide()) {
                        QRMsger qrMsger = new QRMsger();
                        qrMsger.checkPay(mMsgTransListener, mDealRecorder.getOrder());
                    } else {
                        stopTaskCheckPay();
                    }
                }
            };

            mTimer.schedule(mCheckPayTask, 20000, 9000);//交易之后20秒进行查询交易状态,每隔5秒查询一次
        }

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

        beginTaskCheckPay();
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

            if (mChooseCupListener != null) {
                stopTaskCheckPay();
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

    private void updateTextColor(RadioGroup group, int checkedId, Step step, int index) {

        ContainerConfig containerConfig = mCoffeeFomat.getContainerConfigs().get(index);

        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton button = (RadioButton) group.getChildAt(i);
            if (button.getId() != checkedId) {
                button.setSelected(false);
            } else {
                button.setSelected(true);
                int useMaterial = (step.getTastes().get(i).getAmount() / 100) * containerConfig.getMaterial_time();
                containerConfig.setMaterial_time(useMaterial);
                mCoffeeFomat.getContainerConfigs().set(index, containerConfig);

                mCoffeeFomat.getTasteNameRatio().add(index, step.getTastes().get(i).getRemark() + "-" + step.getTastes().get(i).getAmount());

            }
        }
    }

    private void stopTaskCheckPay() {

        if (mTimer != null) {

            mTimer.cancel();
        }
        mTimer = null;
        mCheckPayTask = null;
    }

    /*
   * 倒计时检测
   * */
    private void countDownTime() {


        CountDownTimer timer = new CountDownTimer(VIEW_SHOW_TIME, 1000) {
            @Override
            public void onTick(final long millisUntilFinished) {

                mViewHolder.mCountDownTime.setText(millisUntilFinished / 1000 + " s");

            }

            @Override
            public void onFinish() {
                if (!mDealRecorder.isPayed()) {

                    if (mChooseCupListener != null) {
                        stopTaskCheckPay();
                        mChooseCupListener.cancle(mDealRecorder.getOrder());
                    }
                }
            }
        };
        timer.start();

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
            case R.id.coffeeCold:
                mViewHolder.mCoffeeCold.setSelected(true);
                mViewHolder.mCoffeeHot.setSelected(false);
                break;
            case R.id.coffeeHot:
                mViewHolder.mCoffeeCold.setSelected(false);
                mViewHolder.mCoffeeHot.setSelected(true);
                break;
            case R.id.close:

                if (mChooseCupListener != null) {
                    stopTaskCheckPay();
                    mChooseCupListener.cancle(mDealRecorder.getOrder());
                }
                break;
        }
    }

    @Override
    public void onMsgArrived(Object msg) {
        MyLog.d(TAG, "one msg arrived");

        if (msg != null) {

            if (msg instanceof ParseRQMsg && mCurRequest == REQUEST_QR_CODE) {

                ParseRQMsg parseRqMsg = (ParseRQMsg) msg;
                getQrMsg(parseRqMsg);

                MyLog.d(TAG, parseRqMsg.getQrCode());

            }
            if (msg instanceof PayResult && mCurRequest == REQUEST_CHECK_PAY) {
                PayResult aMsg = (PayResult) msg;
                getPayMsg(aMsg);
            }
        } else {
            if (mCurRequest == REQUEST_QR_CODE) {
                setQR_Code(null);
                MyLog.W(TAG, "no message come in reqQR");
            }
            if (mCurRequest == REQUEST_CHECK_PAY) {
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
            mLoadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);
            mClose = (ImageView) view.findViewById(R.id.close);
            mCountDownTime = (TextView) view.findViewById(R.id.countDownTime);
            mErrTip = (TextView) view.findViewById(R.id.errTip);
            mContentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);


            mQrCodeImage.setVisibility(View.GONE);
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
            RadioGroup group = (RadioGroup) tasteView.findViewById(R.id.taste_suger);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    updateTextColor(group, checkedId, step, index);
                }
            });
            tasteName.setText(material.getName());
            for (int i = 0; i < tastes.size(); i++) {
                Taste taste = tastes.get(i);
                View childView = LayoutInflater.from(mContext).inflate(R.layout.radio_btn, null);
                RadioButton radioButton = (RadioButton) childView.findViewById(R.id.tasteChild);
                radioButton.setText(taste.getRemark());
                group.addView(childView);
            }
            setEnable(group, step);
            mTastLayout.addView(tasteView);
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

            MaterialSql table = new MaterialSql(mContext);
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
                int useMaterial = (taste.getAmount() / 100) * step.getContainerConfig().getMaterial_time();
                RadioButton button = (RadioButton) group.getChildAt(i);
                if (stockInt < useMaterial) {
                    button.setEnabled(false);
                    button.setAlpha(0.8f);
                }
           /* else {
                group.check(button.getId());
            }*/
            }
        }
    }
}
