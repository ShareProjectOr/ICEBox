package example.jni.com.coffeeseller.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.CoffeeMakeStateRecorder;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.ScreenUtil;
import example.jni.com.coffeeseller.utils.Waiter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by WH on 2018/3/22.
 */

public class MakeDialog extends Dialog implements View.OnClickListener {
    private static String TAG = "MakeDialog";
    private long MAX_TOTAL_MK_TIME = 180000;
    private long MAX_STATE_TIME = 5000;
    private int MAX_PROGRESS = 100;
    private int MAX_MAKING_PROGRESS = 92;
    private int CONTAIN_MAKING_PROGRESS_TIME = 550;
    private int NOT_CONTAIN_MAKING_PROGRESS_TIME = 200;
    private Context context;
    private MakingViewHolder makingViewHolder;
    private CoffMsger coffMsger;
    private List<ContainerConfig> containerConfigs;
    private CoffeeMakeStateRecorder coffeeMakeStateRecorder;
    private Handler handler;
    private boolean isStartMaking = false;
    private boolean makeSuccess = false;

    private long lastStateTime;
    private long totalMakingTime;
    private StringBuffer buffer;


    public MakeDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MakeDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    public void init() {

        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setTitle(null);
        window.setWindowAnimations(R.style.dialogWindowAnim);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
        wl.width = ScreenUtil.getScreenWidth(context) /2;//ScreenUtil.getScreenWidth(context) / 2
//        wl.height = ScreenUtil.getScreenHeight(context) * 3 / 4;
        wl.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        wl.alpha = 0.3f;
        window.setAttributes(wl);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        handler = new Handler();
        coffMsger = CoffMsger.getInstance();
        buffer = new StringBuffer();
    }

    public void initData(List<ContainerConfig> containerConfigs) {
        this.containerConfigs = containerConfigs;
        initView();
        startMkCoffee();
    }

    public void startMkCoffee() {
        if (coffeeMakeStateRecorder == null) {
            coffeeMakeStateRecorder = new CoffeeMakeStateRecorder();
        }
        coffeeMakeStateRecorder.init();
        if (coffMsger != null) {
            if (this.containerConfigs != null && this.containerConfigs.size() >= 0) {
                if (isCanMaking()) {
                    makingViewHolder.mIsMakeLayout.setVisibility(View.VISIBLE);
                } else {
                    disDialog(true);
                }
            } else {
                MyLog.d(TAG, "containerConfigs is null");
            }
        } else {
            MyLog.d(TAG, "coffMsger is null");
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

                        disDialog(false);
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

                        Result result = coffMsger.mkCoffee(containerConfigs);
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

                        disDialog(false);

                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEFINISHED_CUPISTAKEN) {

                        coffeeMakeStateRecorder.state = null;

                        disDialog(false);
                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEMAKING_FAILED) {

                        disDialog(true);
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

    private boolean isCanMaking() {
        buffer.setLength(0);
        MachineState machineState = coffMsger.getLastMachineState();


        if (!checkMaterial(machineState)) {

            return false;
        } else {

            MajorState majorState = machineState.getMajorState();

            MyLog.d(TAG, "majorState.getCurStateEnum() ==" + majorState.getCurStateEnum());
            if (majorState.getCurStateEnum() == StateEnum.FINISH) {

                buffer.append("\n");
                buffer.append("杯架上有杯子未取走");

                return false;
            } else if (majorState.getCurStateEnum() != StateEnum.IDLE) {
                buffer.append("\n");
                switch (majorState.getCurStateEnum()) {
                    case DOOR_OPNE:
                        buffer.append("错误:升降门未落下");
                        break;
                    case DOWN_CUP:
                        buffer.append("错误:正在落杯中");
                        break;
                    case DOWN_POWER:
                        buffer.append("错误:正在落粉中");
                        break;
                    case HEAT_POT:
                        buffer.append("错误:锅炉加热中");
                        break;
                    case UNKNOW_STATE:
                        buffer.append("错误:未知错误");
                        break;

                }


                return false;
            }
        }
        return true;
    }

    //检查其他状态,原料状态
    private boolean checkMaterial(MachineState machineState) {
        if (machineState == null) {
            return false;
        }

        MyLog.d(TAG, "----------" + machineState.isCupShelfRightPlace());
        MyLog.d(TAG, "----------" + machineState.hasCupOnShelf());
        MyLog.d(TAG, "----------" + machineState.isLittleDoorOpen());

        boolean isCheckCanMake = true;
        //  StringBuffer buffer = new StringBuffer();
        buffer.append("提示：");
        if (machineState.getPotTemp() > 130) {

            isCheckCanMake = false;
            buffer.append("\n");
            buffer.append("锅炉温度大于130");

        }
        if (machineState.getPotPressure() > 1500) {

            isCheckCanMake = false;
            buffer.append("\n");
            buffer.append("锅炉压力大于1500");

        }
        if (!machineState.isBeanEnough()) {

           /* isCheckCanMake = false;
            buffer.append("\n");
            buffer.append("咖啡豆不足");*/

        }
        if (machineState.isWasteContainerFull()) {

          /*  isCheckCanMake = false;
            buffer.append("\n");
            buffer.append("污水仓已满");*/

        }
        if (machineState.isLittleDoorOpen()) {
            Log.d(TAG, "前门未关");
            isCheckCanMake = false;
            buffer.append("\n");
            buffer.append("前门未关");

        }
        if (!machineState.isCupShelfRightPlace()) {
            Log.d(TAG, "杯架未在初始状态");
            isCheckCanMake = false;
            buffer.append("\n");
            buffer.append("杯架未在初始状态");

        }
        if (machineState.hasCupOnShelf()) {
            Log.d(TAG, "杯架上有杯子未取走");
            isCheckCanMake = false;
            buffer.append("\n");
            buffer.append("杯架上有杯子未取走");

        }
        if (!isCheckCanMake) {
            //     makingViewHolder.mErrTip.setText(buffer.toString());

            MyLog.W(TAG, "err tip : " + buffer.toString());
        }

        return isCheckCanMake;
    }

    private void initView() {
        if (makingViewHolder == null) {
            makingViewHolder = new MakingViewHolder();
        }
        makingViewHolder.mMakeBtn.setOnClickListener(this);
        makingViewHolder.mNotMakeBtn.setOnClickListener(this);
        setContentView(makingViewHolder.view);
        setCanceledOnTouchOutside(false);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                makeSuccess = false;
            }
        });
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

    public void showDialog() {

        if (!isShowing()) {

            show();
        } else {
            MyLog.d(TAG, "dialog is showing");
        }
    }

    public void disDialog(boolean isErr) {
        if (isErr) {
            makingViewHolder.mErrTip.setVisibility(VISIBLE);
            makingViewHolder.mProgressBarLayout.setVisibility(GONE);
            makingViewHolder.mErrTip.setText(buffer.toString());
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isShowing()) {
                    dismiss();
                    //    buffer.setLength(0);
                }
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.notMake:
                dismiss();
                break;
            case R.id.make:

                makingViewHolder.mIsMakeLayout.setVisibility(GONE);
                makingViewHolder.mProgressBarLayout.setVisibility(VISIBLE);

                mkCoffee();

                break;
        }
    }

    class MakingViewHolder {
        public View view;
        public TextView mErrTip;
        public LinearLayout mIsMakeLayout;
        public ImageView mNotMakeBtn;
        public ImageView mMakeBtn;
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
            mIsMakeLayout = (LinearLayout) view.findViewById(R.id.isMakeLayout);
            mNotMakeBtn = (ImageView) view.findViewById(R.id.notMake);
            mMakeBtn = (ImageView) view.findViewById(R.id.make);
            mMakeCenterImage = (ImageView) view.findViewById(R.id.makeCenterImage);

            mProgressBarLayout = (LinearLayout) view.findViewById(R.id.progressBarLayout);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mTextLayout = (LinearLayout) view.findViewById(R.id.textLayout);
            mTipOne = (LinearLayout) view.findViewById(R.id.tipOne);
            mTipTwo = (LinearLayout) view.findViewById(R.id.tipTwo);
            mTipThress = (LinearLayout) view.findViewById(R.id.tipThress);

            mTipTwo.setVisibility(View.INVISIBLE);
            mTipThress.setVisibility(View.INVISIBLE);
            mProgressBarLayout.setVisibility(GONE);
            mIsMakeLayout.setVisibility(GONE);

        }

    }
}
