package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.ContainerConfig;
import cof.ac.inter.MachineState;
import cof.ac.inter.MajorState;
import cof.ac.inter.Result;
import cof.ac.inter.StateEnum;
import example.jni.com.coffeeseller.MachineConfig.CoffeeMakeState;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.CoffeeMakeStateRecorder;
import example.jni.com.coffeeseller.databases.DealOrderInfoManager;
import example.jni.com.coffeeseller.model.listeners.MkCoffeeListenner;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.Waiter;
import example.jni.com.coffeeseller.views.customviews.BuyDialog;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by WH on 2018/4/25.
 */

public class MkCoffee {
    private static String TAG = "MkCoffee";
    private long MAX_TOTAL_MK_TIME = 180000;
    private long MAX_STATE_TIME = 5000;
    private int MAX_PROGRESS = 100;
    private int MAX_MAKING_PROGRESS = 92;
    private int CONTAIN_MAKING_PROGRESS_TIME = 550;
    private int NOT_CONTAIN_MAKING_PROGRESS_TIME = 200;
    private Context context;
    private MakingViewHolder makingViewHolder;
    private CoffMsger coffMsger;
    private CoffeeMakeStateRecorder coffeeMakeStateRecorder;

    private CoffeeFomat coffeeFomat;
    private DealRecorder dealRecorder;
    private MkCoffeeListenner mkCoffeeListenner;
    private DealOrderInfoManager dealOrderInfoManager;

    private boolean isStartMaking = false;
    private boolean makeSuccess = false;

    private long lastStateTime;
    private long totalMakingTime;
    private StringBuffer buffer;

    public MkCoffee(Context context, CoffeeFomat coffeeFomat, DealRecorder dealRecorder, MkCoffeeListenner mkCoffeeListenner) {
        this.context = context;
        this.coffeeFomat = coffeeFomat;
        this.dealRecorder = dealRecorder;
        this.mkCoffeeListenner = mkCoffeeListenner;
        init();
    }

    public void init() {

        initView();
        initData();
    }

    private void initView() {
        if (makingViewHolder == null) {
            makingViewHolder = new MakingViewHolder();
        }
    }

    public void initData() {

        dealOrderInfoManager = new DealOrderInfoManager(context);
        if (coffeeFomat != null && dealRecorder != null) {

            dealRecorder.getContainerConfigs().clear();
            dealRecorder.getContainerConfigs().addAll(coffeeFomat.getContainerConfigs());


            /*
            * 便于查看步骤
            * */
            for (int i = 0; i < dealRecorder.getContainerConfigs().size(); i++) {
                ContainerConfig containerConfig = dealRecorder.getContainerConfigs().get(i);

                MyLog.d(TAG, "getContainer=" + containerConfig.getContainer());
                MyLog.d(TAG, "getWater_capacity=" + containerConfig.getWater_capacity());
                MyLog.d(TAG, "getMaterial_time=" + containerConfig.getMaterial_time());
                MyLog.d(TAG, "getWater_type=" + containerConfig.getWater_type());
                MyLog.d(TAG, "getContainer_id=" + containerConfig.getContainer_id());
                MyLog.d(TAG, "getRotate_speed=" + containerConfig.getRotate_speed());
                MyLog.d(TAG, "getStir_speed=" + containerConfig.getStir_speed());
                MyLog.d(TAG, "getWater_interval=" + containerConfig.getWater_interval());
            }

            String tasteNameAndRadio = "";
            for (int i = 0; i < coffeeFomat.getTasteNameRatio().size(); i++) {
                tasteNameAndRadio += (coffeeFomat.getTasteNameRatio().get(i) + ",");
            }
            dealRecorder.setTasteRadio(tasteNameAndRadio);
        }
        coffMsger = CoffMsger.getInstance();
        buffer = new StringBuffer();

        DealOrderInfoManager.getInstance(context).update(dealRecorder);

        updateProgressAnim(CONTAIN_MAKING_PROGRESS_TIME);

        if (mkCoffeeListenner != null) {
            mkCoffeeListenner.getMkResult(dealRecorder, true);
        }
//        startMkCoffee();
    }

    public View getView() {
        return makingViewHolder.view;
    }

    public void startMkCoffee() {
        if (coffeeMakeStateRecorder == null) {
            coffeeMakeStateRecorder = new CoffeeMakeStateRecorder();
        }
        coffeeMakeStateRecorder.init();
        if (coffeeFomat != null && coffeeFomat.getContainerConfigs().size() >= 0) {
            mkCoffee();
        } else {

            buffer.append("\n");
            buffer.append("没有设置配方");

            showErr(true);

            if (mkCoffeeListenner != null) {
                dealRecorder.setMakeSuccess(false);
                mkCoffeeListenner.getMkResult(dealRecorder, false);
            }


//            disDialog(true);
            MyLog.d(TAG, "containerConfigs is null");
        }
    }

    public void updateProgress() {

        if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_ISMAKING && !isStartMaking) {

            isStartMaking = true;
            updateProgressAnim(CONTAIN_MAKING_PROGRESS_TIME);

        } else if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_POWER && !isStartMaking) {

            isStartMaking = true;
            updateProgressAnim(NOT_CONTAIN_MAKING_PROGRESS_TIME);

        } else if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN && isStartMaking) {

            isStartMaking = false;
            makeSuccess = true;
            updateProgressAnim(0);

        }
    }

    /*
    * 制作咖啡
    * */
    private void mkCoffee() {


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                //   StringBuffer buffer = new StringBuffer();
                buffer.append("提示：");
                MachineState machineState = null;
                lastStateTime = System.currentTimeMillis();
                totalMakingTime = System.currentTimeMillis();
                while (true) {


                    machineState = coffMsger.getLastMachineState();

                    if (isMkTimeOut()) {

                        if (coffeeMakeStateRecorder.state != null
                                && coffeeMakeStateRecorder.state != CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN
                                && coffeeMakeStateRecorder.state != CoffeeMakeState.COFFEEFINISHED_CUPISTAKEN) {

                            if (mkCoffeeListenner != null) {
                                dealRecorder.setMakeSuccess(false);
                                mkCoffeeListenner.getMkResult(dealRecorder, false);
                            }
                        }

                        //     disDialog(false);
                        break;
                    }
                    if (DealMachineState(machineState)) {

                        if (machineState.getMajorState().getCurStateEnum() == StateEnum.HAS_ERR) {

                            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEMAKING_FAILED;
                            buffer.append("/n");
                            buffer.append("制作过程中接收到0a");
                        }
                    } else {

                        continue;
                    }


                    if (coffeeMakeStateRecorder.state == null) {

                        Result result = coffMsger.mkCoffee(coffeeFomat.getContainerConfigs());
                        if (result.getCode() == Result.SUCCESS) {

                            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_MAKE_INIT;
                        } else {

                            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEMAKING_FAILED;
                            buffer.append("/n");
                            buffer.append("发送咖啡制作指令，返回" + result.getErrDes());
                        }

                    }

                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_MAKE_INIT) {

                        dealStateMakeInit(machineState);
                        continue;
                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_CUP) {

                        dealStateDownCup(machineState);

                        continue;
                    }

                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_ISMAKING) {

                        dealMakingState(machineState);

                        updateProgress();

                        continue;
                    }

                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_POWER) {

                        dealStateDownPower(machineState);

                        updateProgress();

                        continue;
                    }

                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN) {
                        //上报交易记录

                        updateProgress();

                        if (mkCoffeeListenner != null) {
                            dealRecorder.setMakeSuccess(true);
                            mkCoffeeListenner.getMkResult(dealRecorder, true);
                        }

                        //disDialog(false);

                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEFINISHED_CUPISTAKEN) {

                        coffeeMakeStateRecorder.state = null;

                        //  disDialog(false);
                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEMAKING_FAILED) {

                        if (mkCoffeeListenner != null) {
                            dealRecorder.setMakeSuccess(false);
                            mkCoffeeListenner.getMkResult(dealRecorder, false);
                        }


                        // disDialog(true);
                        showErr(true);
                    }
                }
            }
        };
        new Thread(runnable).start();

    }


    protected void dealStateDownCup(MachineState machineState) {

        if (machineState.getMajorState().getCurStateEnum() == StateEnum.MAKING) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_ISMAKING;
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.DOWN_POWER) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_DOWN_POWER;
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.FINISH) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN;
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.HAS_ERR) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEMAKING_FAILED;

        }
    }

    private boolean dealMakingState(MachineState machineState) {

        if (machineState.getMajorState().getCurStateEnum() == StateEnum.DOWN_POWER) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_DOWN_POWER;
            return true;
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.FINISH) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN;
            return true;
        }
        return false;
    }

    protected void dealStateDownPower(MachineState machineState) {

        if (machineState.getMajorState().getCurStateEnum() == StateEnum.MAKING) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_ISMAKING;
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.FINISH) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN;
        }
    }

    protected void dealStateMakeInit(MachineState machineState) {
        if (machineState.getMajorState().getCurStateEnum() == StateEnum.DOWN_CUP) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_DOWN_CUP;
        }
    }

    private boolean DealMachineState(MachineState curState) {

        if (curState.getResult().getCode() == Result.SUCCESS) {

            lastStateTime = System.currentTimeMillis();
            return true;
        } else {
            if (isTimeOut()) {

                coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEMAKING_FAILED;

            }
            return false;
        }
    }

    private boolean isTimeOut() {

        long curTime = System.currentTimeMillis();
        if (curTime - lastStateTime > MAX_STATE_TIME) {

            return true;
        }
        return false;
    }

    private boolean isMkTimeOut() {

        long curTime = System.currentTimeMillis();
        if (curTime - totalMakingTime > MAX_TOTAL_MK_TIME) {

            return true;
        }
        return false;
    }

    public void updateProgressAnim(final int waitTime) {

        new AsyncTask<Void, Integer, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                if (!makeSuccess) {
                    for (int i = 0; i <= MAX_MAKING_PROGRESS; i++) {

                        publishProgress(i);

                        if (i == MAX_MAKING_PROGRESS) {
                            break;
                        }
                        if (makeSuccess) {
                            publishProgress(MAX_PROGRESS);
                            break;
                        }
                        Waiter.doWait(waitTime);
                    }
                } else {
                    publishProgress(MAX_PROGRESS);
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {

                setProgress(values[0]);
                super.onProgressUpdate(values);

            }
        }.execute();
    }

    public void setProgress(int progress) {

        makingViewHolder.mProgressBar.setProgress(progress);
        if (progress < MAX_PROGRESS / 2) {
            makingViewHolder.mTipOne.setVisibility(VISIBLE);
            makingViewHolder.mTipTwo.setVisibility(View.INVISIBLE);
            makingViewHolder.mTipThress.setVisibility(View.INVISIBLE);
        } else if (progress >= MAX_PROGRESS / 2 && progress < MAX_PROGRESS) {
            makingViewHolder.mTipOne.setVisibility(View.INVISIBLE);
            makingViewHolder.mTipTwo.setVisibility(View.VISIBLE);
            makingViewHolder.mTipThress.setVisibility(View.INVISIBLE);
            makingViewHolder.mMakeCenterImage.setImageResource(R.mipmap.making);
        } else {
            makingViewHolder.mTipOne.setVisibility(View.INVISIBLE);
            makingViewHolder.mTipTwo.setVisibility(View.INVISIBLE);
            makingViewHolder.mTipThress.setVisibility(View.VISIBLE);
            makingViewHolder.mMakeCenterImage.setImageResource(R.mipmap.make_finish);

        }
    }

    public void showErr(boolean isErr) {
        makingViewHolder.mErrTip.setVisibility(VISIBLE);
        makingViewHolder.mProgressBarLayout.setVisibility(GONE);
        makingViewHolder.mErrTip.setText(buffer.toString());
    }

    class MakingViewHolder {
        public View view;
        public TextView mErrTip;
        public LinearLayout mIsMakeLayout;
        public ImageView mMakeCenterImage;

        public LinearLayout mProgressBarLayout;
        public ProgressBar mProgressBar;
        public LinearLayout mTextLayout;
        public LinearLayout mTipOne;
        public LinearLayout mTipTwo;
        public LinearLayout mTipThress;


        public MakingViewHolder() {
            initView();
        }

        private void initView() {
            view = LayoutInflater.from(context).inflate(R.layout.make_out_layout, null);
            mErrTip = (TextView) view.findViewById(R.id.errTip);
            mMakeCenterImage = (ImageView) view.findViewById(R.id.makeCenterImage);

            mProgressBarLayout = (LinearLayout) view.findViewById(R.id.progressBarLayout);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mTextLayout = (LinearLayout) view.findViewById(R.id.textLayout);
            mTipOne = (LinearLayout) view.findViewById(R.id.tipOne);
            mTipTwo = (LinearLayout) view.findViewById(R.id.tipTwo);
            mTipThress = (LinearLayout) view.findViewById(R.id.tipThress);

            mTipTwo.setVisibility(View.INVISIBLE);
            mTipThress.setVisibility(View.INVISIBLE);

        }

    }
}
