package example.jni.com.coffeeseller.model;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.ContainerConfig;
import cof.ac.inter.MachineState;
import cof.ac.inter.Result;
import cof.ac.inter.StateEnum;
import example.jni.com.coffeeseller.MachineConfig.CoffeeMakeState;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.CoffeeMakeStateRecorder;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.databases.DealOrderInfoManager;
import example.jni.com.coffeeseller.model.listeners.MkCoffeeListenner;
import example.jni.com.coffeeseller.utils.DensityUtil;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by WH on 2018/4/25.
 */

public class NewMkCoffee {
    private static String TAG = "MkCoffee";
    private long MAX_TOTAL_MK_TIME = 180000;
    private long MAX_STATE_TIME = 5000;
    private long VIEW_SHOW_TIME = 120 * 1000;
    private int MAX_PROGRESS = 100;
    private int MAX_MAKING_PROGRESS = 92;
    private int INIT_PROGRESS = 5;
    private int CONTAIN_MAKING_PROGRESS_TIME = 550;
    private Context context;

    private View view;
    private TextView mModou, mLuobei, mLiangzao, mFuliao, mZhuangbei, mWancheng, mErrCaseTip, mRestTime;
    private ProgressBar mProgressBar;

    private CoffMsger coffMsger;
    private CoffeeMakeStateRecorder coffeeMakeStateRecorder;

    private CoffeeFomat coffeeFomat;
    private DealRecorder dealRecorder;
    private MkCoffeeListenner mkCoffeeListenner;
    private Handler handler;
    private Timer countTimeTimer;
    private TimerTask countTimeTimerTask;

    private boolean isStartMaking = false;
    private boolean makeSuccess = false;
    private boolean isCalculateMaterial = true;
    private boolean isSendMkingComdSuccess = false;
    private boolean isMkingOver = false;

    private long lastStateTime;
    private long totalMakingTime = 0;
    private int curTimeCount = 0;
    private int perProgressUnit;//间隔进度条长度
    private int perTimePaddingLeft = 10;//时间左边距

    private StringBuffer buffer;
    private AsyncTask updateAnimProgressTask;

    public NewMkCoffee(Context context, CoffeeFomat coffeeFomat, DealRecorder dealRecorder, MkCoffeeListenner mkCoffeeListenner, Handler handler) {
        this.context = context;
        this.coffeeFomat = coffeeFomat;
        this.dealRecorder = dealRecorder;
        this.mkCoffeeListenner = mkCoffeeListenner;
        this.handler = handler;
        init();
    }

    public void init() {

        initView();
        initData();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.new_progressbar, null);
        mLuobei = (TextView) view.findViewById(R.id.luobei);
        mModou = (TextView) view.findViewById(R.id.modou);
        mLiangzao = (TextView) view.findViewById(R.id.liangzao);
        mFuliao = (TextView) view.findViewById(R.id.fuliao);
        mZhuangbei = (TextView) view.findViewById(R.id.zhuangbei);
        mWancheng = (TextView) view.findViewById(R.id.wancheng);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mErrCaseTip = (TextView) view.findViewById(R.id.errCaseTip);
        mRestTime = (TextView) view.findViewById(R.id.restTime);

        mProgressBar.setProgress(INIT_PROGRESS);

        coffMsger = CoffMsger.getInstance();
        buffer = new StringBuffer();

    }

    public void initData() {

        if (coffeeFomat != null && dealRecorder != null) {

            dealRecorder.getContainerConfigs().clear();
            dealRecorder.getContainerConfigs().addAll(coffeeFomat.getContainerConfigs());


            /*
            * 便于查看步骤
            * */
            int waterTotal = 0;
            for (int i = 0; i < dealRecorder.getContainerConfigs().size(); i++) {
                ContainerConfig containerConfig = dealRecorder.getContainerConfigs().get(i);

                if (containerConfig.getWater_capacity() != 0) {
                    waterTotal += containerConfig.getWater_capacity();
                    if (containerConfig.getMaterial_time() == 0) {
                        CONTAIN_MAKING_PROGRESS_TIME += 5 * 1000;//出水运作5s

                    } else {
                        CONTAIN_MAKING_PROGRESS_TIME += 5 * 1000;//出水运作5s
                        CONTAIN_MAKING_PROGRESS_TIME += containerConfig.getMaterial_time() * 10 + 10 * 1000;//每个步骤机器运作大概8s
                    }

                } else {
                    CONTAIN_MAKING_PROGRESS_TIME += containerConfig.getMaterial_time() * 10 + 10 * 1000;//每个步骤机器运作大概8s
                }
                MyLog.d(TAG, "getContainer=" + containerConfig.getContainer());
                MyLog.d(TAG, "getWater_capacity=" + containerConfig.getWater_capacity());
                MyLog.d(TAG, "getMaterial_time=" + containerConfig.getMaterial_time());
                MyLog.d(TAG, "getWater_type=" + containerConfig.getWater_type());
                MyLog.d(TAG, "getContainer_id=" + containerConfig.getContainer_id());
                MyLog.d(TAG, "getRotate_speed=" + containerConfig.getRotate_speed());
                MyLog.d(TAG, "getStir_speed=" + containerConfig.getStir_speed());
                MyLog.d(TAG, "getWater_interval=" + containerConfig.getWater_interval());
            }
            CONTAIN_MAKING_PROGRESS_TIME += waterTotal * 10 / 10;

            //    CONTAIN_MAKING_PROGRESS_TIME += 30 * 1000;
            CONTAIN_MAKING_PROGRESS_TIME += 20 * 1000; //落杯过程中的10s

            MyLog.d(TAG, "CONTAIN_MAKING_PROGRESS_TIME= " + CONTAIN_MAKING_PROGRESS_TIME);

            String tasteNameAndRadio = "";
            for (int i = 0; i < coffeeFomat.getTasteNameRatio().size(); i++) {
                tasteNameAndRadio += (coffeeFomat.getTasteNameRatio().get(i) + ",");
            }
            dealRecorder.setTasteRadio(tasteNameAndRadio);
            dealRecorder.setRqTempFormat(coffeeFomat.getWaterType() + "");
            DealOrderInfoManager.getInstance(context).update(dealRecorder);
        }


        perProgressUnit = mProgressBar.getWidth() / 100;
        perTimePaddingLeft = mProgressBar.getWidth() / MAX_PROGRESS;
        mLuobei.setSelected(true);

        MyLog.W(TAG, "enter mkCoffee page");

        startMkCoffee();


    }

    public View getView() {
        return view;
    }

    public void startMkCoffee() {
        if (coffeeMakeStateRecorder == null) {
            coffeeMakeStateRecorder = new CoffeeMakeStateRecorder();
        }
        coffeeMakeStateRecorder.init();
        if (coffeeFomat != null && coffeeFomat.getContainerConfigs().size() >= 0) {
            if (buffer != null) {
                buffer.setLength(0);
            }

            mkCoffee();

            countDownTime();

            mRestTime.setVisibility(View.VISIBLE);
        } else {
            if (buffer != null) {
                buffer.append("\n");
                buffer.append("没有设置配方");
            }

            showErr(true);
            dealRecorder.setMakeSuccess(false);
            if (mkCoffeeListenner != null) {
                mkCoffeeListenner.getMkResult(dealRecorder, false, isCalculateMaterial);
            }
            stopCountTimer();
            MyLog.W(TAG, "containerConfigs is null");
        }
    }

    public void updateProgress() {

        if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_CUP && !isStartMaking) {

            isStartMaking = true;
            updateProgressAnim(false);

        } else if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_ISMAKING && !isStartMaking) {

            isStartMaking = true;
            updateProgressAnim(false);

        } else if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_POWER && !isStartMaking) {

            isStartMaking = true;
            updateProgressAnim(false);

        } else if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN && isStartMaking) {

            isStartMaking = false;
            makeSuccess = true;
            updateProgressAnim(true);

        } else if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEMAKING_FAILED) {

            isStartMaking = false;
            makeSuccess = true;
            updateProgressAnim(true);
        }
    }

    /*
    * 制作咖啡
    * */
    private void mkCoffee() {

        MyLog.W(TAG, "start making");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (buffer != null) {
                    buffer.setLength(0);
                    buffer.append("提示：");
                }
                MachineState machineState = null;
                lastStateTime = System.currentTimeMillis();
                totalMakingTime = System.currentTimeMillis();
                while (true) {

                    machineState = coffMsger.getLastMachineState();

                    if (isMkTimeOut()) {

                        if (coffeeMakeStateRecorder.state == null
                                || coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN
                                || coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEFINISHED_CUPISTAKEN) {
                            MyLog.W(TAG, " make successed but time is too long ");


                        } else {
                            if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_CUP || coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_MAKE_INIT) {

                                MyLog.W(TAG, " make failed and time is too long," + coffeeMakeStateRecorder.state + "  state is remaining ");

                                dealRecorder.setMakeSuccess(false);
                                isCalculateMaterial = false;
                                if (!isMkingOver) {
                                    if (mkCoffeeListenner != null)
                                        mkCoffeeListenner.getMkResult(dealRecorder, false, isCalculateMaterial);
                                    stopCountTimer();
                                }
                                break;

                            } else if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_ISMAKING
                                    || coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_POWER) {

                                MyLog.W(TAG, " make failed and time is too long,other state is remaining ");

                                if (machineState.hasCupOnShelf()) {
                                    dealRecorder.setMakeSuccess(false);
                                } else {
                                    dealRecorder.setMakeSuccess(true);
                                }
                                isCalculateMaterial = true;
                                if (!isMkingOver && mkCoffeeListenner != null) {
                                    mkCoffeeListenner.getMkResult(dealRecorder, dealRecorder.isMakeSuccess(), isCalculateMaterial);
                                    stopCountTimer();
                                }

                                break;

                            }
                        }

                        break;
                    }
                    if (DealMachineState(machineState)) {

                        if (machineState.getMajorState().getCurStateEnum() == StateEnum.HAS_ERR) {

                            /*
                            * 如果接收到0a时，出现的状态是落杯或初始化
                            * */
                            if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_CUP || coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_MAKE_INIT) {
                                isCalculateMaterial = false;

                            }
                            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEMAKING_FAILED;

                            MyLog.W(TAG, "err return : " + machineState.getResult().getReturn_bytes());

                            if (buffer != null) {
                                buffer.append("\n");
                                buffer.append("制作过程中接收到0a : " + machineState.getMajorState().getHighErr_byte() + machineState.getMajorState().getLowErr_byte());//getHighErr_byte
                            }
                        }
                    } else {

                        continue;
                    }


                    if (coffeeMakeStateRecorder.state == null && !isSendMkingComdSuccess) {


                        Result result = coffMsger.mkCoffee(coffeeFomat.getContainerConfigs());

                        MyLog.d(TAG, "send mkCoffee comd : ");//+ result.getReturn_bytes()

                        if (result.getCode() == Result.SUCCESS) {

                            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_MAKE_INIT;

                            isSendMkingComdSuccess = true;

                            MyLog.W(TAG, " cur state is init");

                        } else {

                            MyLog.W(TAG, "send mkCoffee comd result = " + result.getCode());

                            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEMAKING_FAILED;
                            isCalculateMaterial = false;
                            if (buffer != null) {
                                buffer.append("/n");
                                buffer.append("发送咖啡制作指令，返回" + result.getErrDes());

                                MyLog.W(TAG, "send mkCoffee comd err return : " + machineState.getResult().getReturn_bytes());
                            }
                        }

                    }

                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_MAKE_INIT) {

                        dealStateMakeInit(machineState);


                        continue;
                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_CUP) {

                        dealStateDownCup(machineState);

                        updateProgress();

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
                        dealRecorder.setMakeSuccess(true);
                        if (mkCoffeeListenner != null) {
                            mkCoffeeListenner.getMkResult(dealRecorder, true, isCalculateMaterial);
                        }

                        stopCountTimer();
                        isMkingOver = true;

                        break;

                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEFINISHED_CUPISTAKEN) {

                        coffeeMakeStateRecorder.state = null;

                        isMkingOver = false;

//                        MyLog.d(TAG, "cur state is take cup ");

                        break;

                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEMAKING_FAILED) {

                        updateProgress();

                        showErr(true);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                dealRecorder.setMakeSuccess(false);
                                if (mkCoffeeListenner != null) {
                                    mkCoffeeListenner.getMkResult(dealRecorder, false, isCalculateMaterial);
                                }
                                stopCountTimer();
                                isMkingOver = true;

                            }
                        }, 4000);


                        MyLog.d(TAG, "cur state is failed ");
                        break;
                    }
                }
            }
        };
        new Thread(runnable).start();

    }


    protected void dealStateDownCup(MachineState machineState) {

        if (machineState.getMajorState().getCurStateEnum() == StateEnum.MAKING) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_ISMAKING;

            MyLog.W(TAG, " cur state is making");
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.DOWN_POWER) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_DOWN_POWER;

            MyLog.W(TAG, " cur state is down power");
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.FINISH) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN;

            MyLog.W(TAG, " cur state is finish");
        }

    }

    private boolean dealMakingState(MachineState machineState) {

        if (machineState.getMajorState().getCurStateEnum() == StateEnum.DOWN_POWER) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_DOWN_POWER;

            MyLog.W(TAG, " cur state is down power");
            return true;
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.FINISH) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN;

            MyLog.W(TAG, " cur state is finish");
            return true;
        }

        return false;
    }

    protected void dealStateDownPower(MachineState machineState) {

        if (machineState.getMajorState().getCurStateEnum() == StateEnum.MAKING) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_ISMAKING;

            MyLog.W(TAG, " cur state is making");
        } else if (machineState.getMajorState().getCurStateEnum() == StateEnum.FINISH) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN;

            MyLog.W(TAG, " cur state is finish");
        }
    }

    protected void dealStateMakeInit(MachineState machineState) {
        if (machineState.getMajorState().getCurStateEnum() == StateEnum.DOWN_CUP) {

            coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEE_DOWN_CUP;

            MyLog.W(TAG, " cur state is down cup");
        }
    }

    private boolean DealMachineState(MachineState curState) {

        if (curState.getResult().getCode() == Result.SUCCESS) {

            lastStateTime = System.currentTimeMillis();
            return true;
        } else {
            if (isTimeOut()) {

                coffeeMakeStateRecorder.state = CoffeeMakeState.COFFEEMAKING_FAILED;
                isCalculateMaterial = false;
            }
            return false;
        }
    }

    private boolean isTimeOut() {

        long curTime = System.currentTimeMillis();
        if (curTime - lastStateTime > MAX_STATE_TIME) {

            if (buffer != null) {
                buffer.append("\n");
                buffer.append("5s内接收的状态没有更新");
            }
            return true;
        }
        return false;
    }

    private boolean isMkTimeOut() {

        if (System.currentTimeMillis() - totalMakingTime > MAX_TOTAL_MK_TIME) {

            return true;
        }
        return false;
    }

    /*
    * 制作过程的倒计时
    * */

    private void countDownTime() {

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

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mRestTime.setVisibility(View.GONE);
                            }
                        });

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
    }

    public int initLeftMargin = 0;

    public void updateProgressAnim(boolean isFinish) {

        if (isFinish) {
            if (updateAnimProgressTask != null) {
                updateAnimProgressTask.cancel(true);
                updateAnimProgressTask = null;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    setProgress(100);
                    updateTimeCountText(100, perProgressUnit);
                }
            });
        } else {

            if (updateAnimProgressTask == null) {
                perProgressUnit = mProgressBar.getWidth() / 100;
                updateAnimProgressTask = new AsyncTask<Void, Integer, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        if (!makeSuccess) {
                            for (int i = INIT_PROGRESS; i <= MAX_MAKING_PROGRESS; i++) {

                                publishProgress(i);

                                if (i == MAX_MAKING_PROGRESS) {
                                    break;
                                }
                                if (makeSuccess) {
                                    publishProgress(MAX_PROGRESS);
                                    break;
                                }
                                Waiter.doWait(CONTAIN_MAKING_PROGRESS_TIME / MAX_MAKING_PROGRESS);
                            }
                        } else {
                            publishProgress(MAX_PROGRESS);
                        }

                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(final Integer... values) {

                        final int progress = values[0];

                        setProgress(progress);

                        //  mRestTime.setLayoutParams(params);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateTimeCountText(progress, perProgressUnit);
                            }
                        });

                        super.onProgressUpdate(values);

                    }

                    @Override
                    protected void onCancelled() {

                        if (updateAnimProgressTask != null) {
                            updateAnimProgressTask.cancel(true);
                            updateAnimProgressTask = null;
                        }
                        super.onCancelled();

                    }

                    @Override
                    protected void onPostExecute(Integer integer) {

                        if (updateAnimProgressTask != null) {
                            updateAnimProgressTask.cancel(true);
                            updateAnimProgressTask = null;
                        }

                        super.onPostExecute(integer);
                    }
                }.execute();
            }
        }


    }

    private void updateTimeCountText(int progress, int leveProgress) {
        if (progress == 5 || progress * perProgressUnit - 100 < 0) {

        } else if (progress == 100) {
            mRestTime.setPadding(mProgressBar.getWidth() - 100, 0, 0, 0);

        } else {
            initLeftMargin += perProgressUnit;

            mRestTime.setPadding(perProgressUnit * progress - 100, 0, 0, perProgressUnit);

        }

        mRestTime.invalidate();
    }

    public void setProgress(int progress) {

        if (progress == MAX_PROGRESS) {
            mProgressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.new_layer_list_progressbar_finish));
        }
        mProgressBar.setProgress(progress);
        if (progress <= perProgressUnit) {
            mLuobei.setSelected(true);
            if (progress == perProgressUnit) {
                mModou.setSelected(true);
            }

            return;
        }
        if (progress > perProgressUnit && progress <= 2 * perProgressUnit) {
            mModou.setSelected(true);
            if (progress == 2 * perProgressUnit) {
                mLiangzao.setSelected(true);
            }
            return;
        }

        if (progress > 2 * perProgressUnit && progress <= 3 * perProgressUnit) {
            mLiangzao.setSelected(true);
            if (progress == 3 * perProgressUnit) {
                mFuliao.setSelected(true);
            }
            return;
        }

        if (progress > 3 * perProgressUnit && progress <= 4 * perProgressUnit) {
            mFuliao.setSelected(true);
            if (progress == 4 * perProgressUnit) {
                mZhuangbei.setSelected(true);
            }
            return;
        }


        if (progress > 4 * perProgressUnit && progress <= MAX_PROGRESS) {
            mZhuangbei.setSelected(true);
            if (progress == MAX_PROGRESS) {
                mWancheng.setSelected(true);
            }
            return;
        }

    }

    public void showErr(boolean isErr) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                mWancheng.setText("制作失败");
                mWancheng.setTextColor(ContextCompat.getColor(context, R.color.red));
                mWancheng.setTextColor(ContextCompat.getColor(context, R.color.red));

                MyLog.W(TAG, "making err order : " + dealRecorder.getOrder());
                if (buffer != null) {
                    mErrCaseTip.setText("失败" + buffer.toString());
                    mErrCaseTip.setVisibility(View.GONE);
                    MyLog.W(TAG, "making err: " + buffer.toString());

                    MachineConfig.setErrCode(buffer.toString());
                }


            }
        });
    }
}
