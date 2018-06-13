package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.WaterType;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.bean.Step;
import example.jni.com.coffeeseller.bean.Taste;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.model.listeners.MsgTransListener;
import example.jni.com.coffeeseller.model.new_adapter.TasteGridAdapter;
import example.jni.com.coffeeseller.parse.ParseRQMsg;
import example.jni.com.coffeeseller.parse.PayResult;
import example.jni.com.coffeeseller.utils.DensityUtil;
import example.jni.com.coffeeseller.utils.ImageUtil;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.QRMaker;
import example.jni.com.coffeeseller.utils.TextUtil;

/**
 * Created by WH on 2018/4/25.
 */

public class NewChooseCup implements View.OnClickListener, MsgTransListener {
    private static String TAG = "NewChooseCup";
    private static int NO_PAY = 0;
    private static int PAYING = 1;
    private static int PAYED = 2;
    private Context mContext;
    private View mView;
    private LinearLayout mTasteLayout, mPayLayout;
    private ImageView mQrImg;
    private TextView mPaying;
    private TextView mRequestQRTxt;
    private ProgressBar mLoadingBar;
    private TextView mPayPrice, mClose, mPayTip;
    private Coffee mCoffee;
    public DealRecorder mDealRecorder;
    public CoffeeFomat mCoffeeFomat;
    private ChooseCupListenner mChooseCupListener;
    private MsgTransListener mMsgTransListener;
    private Handler mHandler;
    private ChooseAndMking mChooseAndMking;
    private TradeMsgRequest tradeMsgRequest;

    private Timer countTimeTimer;
    private TimerTask countTimeTimerTask;


    private Bitmap qrBitmap;
    private int curPayState = NO_PAY;
    private String curOrder = null;
    private long clearMachineWaitTime;


    public NewChooseCup(Context context, Coffee coffee, ChooseCupListenner listenner, Handler handler, ChooseAndMking chooseAndMking) {
        mContext = context;
        mCoffee = coffee;
        mChooseCupListener = listenner;
        mHandler = handler;
        mChooseAndMking = chooseAndMking;

        init();
    }

    private void init() {
        initView();

        initData();

    }

    public View getView() {
        return mView;
    }

    public String getOrder() {
        return curOrder;
    }

    private void initView() {

        mView = LayoutInflater.from(mContext).inflate(R.layout.new_taste_choose, null);
        mTasteLayout = (LinearLayout) mView.findViewById(R.id.tasteLayout);
        mQrImg = (ImageView) mView.findViewById(R.id.qrImg);
        mPaying = (TextView) mView.findViewById(R.id.paying);
        mRequestQRTxt = (TextView) mView.findViewById(R.id.requestQRTxt);
        mLoadingBar = (ProgressBar) mView.findViewById(R.id.loadingBar);
        mPayPrice = (TextView) mView.findViewById(R.id.payPrice);
        mPayTip = (TextView) mView.findViewById(R.id.payTip);
        mClose = (TextView) mView.findViewById(R.id.close);
        mPayLayout = (LinearLayout) mView.findViewById(R.id.payLayout);
        mClose.setOnClickListener(this);

        mCoffeeFomat = new CoffeeFomat();
        mDealRecorder = new DealRecorder();
        tradeMsgRequest = new TradeMsgRequest();
        mMsgTransListener = this;

//        mCoffeeFomat.setWaterType(WaterType.HOT_WATER);//默认为热饮

        mPayLayout.setVisibility(View.GONE);
        mPayTip.setVisibility(View.GONE);
    }


    public void initData() {

        MyLog.d(TAG, "coffee= " + mCoffee);
        if (mCoffee == null) {
            return;
        }
        /*
        * 初始化数据
        * */
//        mDealRecorder.setStep(mCoffee.);
        mDealRecorder.setRqcup(1);
        mDealRecorder.setFormulaID(mCoffee.getFormulaID());
        BigDecimal bigDecimal = new BigDecimal(Float.parseFloat(mCoffee.price));
        float pay = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        mPayPrice.setText(pay + "");
        mRequestQRTxt.setOnClickListener(this);

        TaskService.getInstance().SetOnMsgListener(mMsgTransListener);


        initTaste(mCoffee.getStepList());

//        tradeMsgRequest.requestQRCode(this, mDealRecorder, mCoffee);
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

            MyLog.d(TAG, "containerConfig.setWater_type= =  " + step.getContainerConfig().getWater_type());


            mCoffeeFomat.getContainerConfigs().add(i, containerConfig);

            addView(step, i);
        }

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

        View tasteView = LayoutInflater.from(mContext).inflate(R.layout.new_taste_grid, null);
        TextView tasteName = (TextView) tasteView.findViewById(R.id.tasteName);
        GridView gridView = (GridView) tasteView.findViewById(R.id.taste_grid);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        tasteName.setText(material.getName());
        final TasteGridAdapter tasteGridAdapter = new TasteGridAdapter(mContext, step);
        gridView.setAdapter(tasteGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyLog.d(TAG, "onItemClick position=" + position);
                mCoffeeFomat = tasteGridAdapter.updateTasteSelected(mCoffeeFomat, step, position, index);
            }
        });

        /*
        * 设置默认选中
        * */
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCoffeeFomat = tasteGridAdapter.updateTasteSelected(mCoffeeFomat, step, tasteGridAdapter.getDefaultSelectedItem(), index);
            }
        }, 100);


        gridView.setTag(step);

        mTasteLayout.addView(tasteView);

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
                mLoadingBar.clearAnimation();
                mRequestQRTxt.setVisibility(View.GONE);
                mLoadingBar.setVisibility(View.GONE);

                if (bitmap != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(ImageUtil.getCornerBitmap(bitmap, DensityUtil.dp2px(mContext, 10)));
                    mQrImg.setBackgroundDrawable(bitmapDrawable);
                    tradeMsgRequest.mCurRequest = TradeMsgRequest.REQUEST_CHECK_PAY;
                } else {
                    Bitmap failedBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.qr_fault);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(ImageUtil.getCornerBitmap(failedBitmap, DensityUtil.dp2px(mContext, 10)));
                    mQrImg.setBackgroundDrawable(bitmapDrawable);
                    mRequestQRTxt.setVisibility(View.VISIBLE);
                }
                mQrImg.setVisibility(View.VISIBLE);
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

        final String price = parseRqMsg.getPrice();
        MyLog.d(TAG, "getPrice = " + parseRqMsg.getPrice());
        mDealRecorder.setPrice(parseRqMsg.getPrice());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mPayLayout.setVisibility(View.VISIBLE);
                mPayTip.setVisibility(View.VISIBLE);
                mPayPrice.setText(price);
            }
        });


        if (parseRqMsg == null) {
            MyLog.d(TAG, "parseRqMsg is empty");
            setQR_Code(null);
            return;
        }
        mDealRecorder.setRqcup(parseRqMsg.getCupNum());

        QRMaker mQrMaker = new QRMaker();
        Bitmap bitmap = mQrMaker.createBitmap(parseRqMsg.getQrCode(), 350, 350);
        setQR_Code(ImageUtil.getCornerBitmap(bitmap, DensityUtil.px2dp(mContext, 10)));

        if (!mDealRecorder.isPayed() && !TextUtils.isEmpty(mDealRecorder.getOrder())) {

            tradeMsgRequest.beginTaskCheckPay(mMsgTransListener, mDealRecorder);
        }

        MyLog.W(TAG, "dealOrder = " + mDealRecorder.getOrder());
    }

    /*
    * 接收支付检查结果，并处理
    * */
    private void getPayMsg(PayResult msg) {
        MyLog.d(TAG, "msg= " + msg);

        if (curPayState == NO_PAY) {
            if (msg.getPayResult() == PAYING) {

                operationIfPaying();
                curPayState = PAYING;

            }
            if (msg.getPayResult() == PAYED) {

                operationIfPayed(msg);
                curPayState = PAYED;
            }

            MyLog.W(TAG, "check pay result is not pay ");
        }
        if (curPayState == PAYING) {

            if (msg.getPayResult() == PAYED) {

                operationIfPayed(msg);
                curPayState = PAYED;
            }
        }
    }

    /*
    * 正在支付中的操作
    * */
    private void operationIfPaying() {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    mPaying.setVisibility(View.VISIBLE);
                    mQrImg.setVisibility(View.GONE);
                    mLoadingBar.setProgress(View.VISIBLE);
                    mChooseAndMking.setPaying(true);
                    if (mChooseCupListener != null) {

                        mChooseCupListener.paying(true);
                    }
                }
            });
        }
        MyLog.W(TAG, "check pay result is paying ");
    }

    /*
    * 如果已经检测到支付成功
    * */
    private void operationIfPayed(PayResult msg) {
        if (mDealRecorder.isPayed()) {
            return;
        }
        MyLog.W(TAG, " check pay result has payed");

        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    mPaying.setText("支付完成");
                    mQrImg.setVisibility(View.GONE);
                    mLoadingBar.setProgress(View.VISIBLE);
                    mChooseAndMking.setPayed(true);

                  /*  if (System.currentTimeMillis() - mChooseAndMking.getLastMkTime() > ChooseAndMking.TIME_BEFORE_MK_TO_CLEAR) {
                        setClearText(-1);
                    }*/

                }
            });
        }

        getFinalContainerConfig();

        mDealRecorder.setPayed(true);
        mDealRecorder.setPayTime(msg.getPayTime());

        stopTaskCheckPay();

                /*
                * 重置倒计时
              * */
        mChooseAndMking.setCurTimeCount(0);

/*        if (System.currentTimeMillis() - mChooseAndMking.getLastMkTime() > ChooseAndMking.TIME_BEFORE_MK_TO_CLEAR) {

            //清洗机器
            int waitTime = ClearMachine.clearMachineAllModule(mCoffeeFomat.getContainerConfigs());

            int newWaitTime = waitTime + 10 * 1000;

            MyLog.d(TAG, "waitTime= " + newWaitTime);

            checkCanSendMkingComd(newWaitTime);

        } else {*/
        hasPay();
        stopTaskCheckPay();
        mChooseAndMking.stopCountTimer();
//        }
    }

    public void checkCanSendMkingComd(final int waitTime) {
        clearMachineWaitTime = waitTime;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hasPay();
                stopTaskCheckPay();
                mChooseAndMking.stopCountTimer();
            }
        }, waitTime);
    }

    public void hasPay() {
        stopTaskCheckPay();
        if (mChooseCupListener != null) {

            mChooseCupListener.hasPay(mCoffeeFomat, mDealRecorder);

        }
    }

    public void cancle() {
        stopTaskCheckPay();
        if (mChooseCupListener != null) {
            mChooseCupListener.cancle(mDealRecorder.getOrder());

        }
    }

    public void stopTaskCheckPay() {
        tradeMsgRequest.stopTaskCheckPay();

    }

    int curTimeCount = 0;
        /*
    * 清洗机器完成的检测倒计时
    * */

/*    private void countDownTime() {

        stopCountTimer();

        if (countTimeTimer == null) {
            countTimeTimer = new Timer();
        }
        if (countTimeTimerTask == null) {
            countTimeTimerTask = new TimerTask() {
                @Override
                public void run() {

                    if (VIEW_SHOW_TIME / 1000 - curTimeCount <= 0) {

                        //如果倒计时结束
                        stopCountTimer();

                        mRestTime.setVisibility(View.GONE);
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mRestTime.setText((VIEW_SHOW_TIME / 1000 - curTimeCount) + " s");
                            }
                        });
                        curTimeCount++;
                    }
                }
            };
        }

        countTimeTimer.schedule(countTimeTimerTask, 0, 1000);
    }

    public void stopCountTimer() {

        if (countTimeTimer != null) {
            countTimeTimer.cancel();
            countTimeTimer = null;
            countTimeTimerTask = null;
        }
        curTimeCount = 0;
    }*/

    public void setClearText(int pointNum) {
        mRequestQRTxt.setVisibility(View.VISIBLE);
        mQrImg.setVisibility(View.GONE);
        mPaying.setVisibility(View.GONE);
        mLoadingBar.setVisibility(View.VISIBLE);
        mRequestQRTxt.setText(TextUtil.textPointNum("清洗中", pointNum));
    }

    /*
    * 判断锅炉是否正在加热中，如果不在加热中，显示二维码
    * */
    public boolean judgeHeapHot(long millisUntilFinished) {
        if (tradeMsgRequest.mCurRequest != TradeMsgRequest.DEFAULT) {

            if (mDealRecorder.isPayed() && clearMachineWaitTime > 0) {

                setClearText((int) (millisUntilFinished % 3));
            }
            return true;
        } else {

            if(!CheckCurMachineState.getInstance().canShowQrCode()){
                mRequestQRTxt.setVisibility(View.VISIBLE);
                mLoadingBar.setProgress(View.VISIBLE);
                mQrImg.setVisibility(View.GONE);

                MyLog.d(TAG, "机器准备中");
                String tipText = TextUtil.textPointNum("机器准备中", (int) (millisUntilFinished % 3));

                mRequestQRTxt.setText(tipText);
            } else {

                mRequestQRTxt.setVisibility(View.GONE);
                mLoadingBar.setProgress(View.GONE);
                mRequestQRTxt.setText("点击重新获取");

                if (tradeMsgRequest.mCurRequest == TradeMsgRequest.DEFAULT) {
                    tradeMsgRequest.requestQRCode(mMsgTransListener, mDealRecorder, mCoffee);
                }
            }

           /* if (CheckCurMachineState.getInstance().isHottingCheck()) {
                mRequestQRTxt.setVisibility(View.VISIBLE);
                mLoadingBar.setProgress(View.VISIBLE);
                mQrImg.setVisibility(View.GONE);

                MyLog.d(TAG, "锅炉加热中");
                String tipText = TextUtil.textPointNum("锅炉加热中", (int) (millisUntilFinished % 3));

                mRequestQRTxt.setText(tipText);
            } else if (CheckCurMachineState.getInstance().isClearingCheck()) {

                setClearText((int) (millisUntilFinished % 3));

            } else {

                mRequestQRTxt.setVisibility(View.GONE);
                mLoadingBar.setProgress(View.GONE);
                mRequestQRTxt.setText("点击重新获取");

                if (tradeMsgRequest.mCurRequest == TradeMsgRequest.DEFAULT) {
                    tradeMsgRequest.requestQRCode(mMsgTransListener, mDealRecorder, mCoffee);
                }
            }*/
            return false;
        }
    }

    /*
    * 获取最终的配方步骤
    * */
    private List<ContainerConfig> getFinalContainerConfig() {
        mCoffeeFomat.setCoffeeName(mCoffee.name);

        int count = mTasteLayout.getChildCount();

        MyLog.d(TAG, "getFinalContainerConfig count= " + count);

        for (int i = 0; i < count; i++) {
            View view = mTasteLayout.getChildAt(i);
            GridView gridView = (GridView) view.findViewById(R.id.taste_grid);

            Step step = (Step) gridView.getTag();
            if (step == null || step.getTastes() == null || step.getTastes().size() <= 0) {
                continue;
            }
            MyLog.d(TAG, "getFinalContainerConfig layout count= " + step.getTastes().size());

            for (int selectedId = 0; selectedId < gridView.getChildCount(); selectedId++) {

                LinearLayout childLayout = (LinearLayout) gridView.getChildAt(selectedId);
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

                    mRequestQRTxt.setVisibility(View.GONE);
                    mLoadingBar.setVisibility(View.VISIBLE);
                    tradeMsgRequest.requestQRCode(this, mDealRecorder, mCoffee);
                }

                break;
            case R.id.close:

                stopTaskCheckPay();

                if (mDealRecorder.isPayed()) {
                    return;
                }

                if (mChooseCupListener != null) {

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

}
