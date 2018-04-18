package example.jni.com.coffeeseller.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

/**
 * Created by WH on 2018/3/22.
 */

public class MakeDialog extends Dialog implements View.OnClickListener {
    private static String TAG = "MakeDialog";
    private Context context;
    private MakingViewHolder makingViewHolder;
    private CoffMsger coffMsger;
    private List<ContainerConfig> containerConfigs;
    private CoffeeMakeStateRecorder coffeeMakeStateRecorder;
    private Handler handler;
    private boolean isStartMaking = false;
    private long START_MAKING_TIME = 0;
    private long WAIT_TIME = 120000;


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
        initView();

        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setTitle(null);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wl.width = ScreenUtil.getScreenWidth(context) / 2;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.alpha = 0.9f;
        window.setAttributes(wl);
        handler = new Handler();
    }

    public void initData(List<ContainerConfig> containerConfigs) {
        this.containerConfigs = containerConfigs;
        coffMsger = CoffMsger.getInstance();
        if (coffeeMakeStateRecorder == null) {
            coffeeMakeStateRecorder = new CoffeeMakeStateRecorder();
        }
        coffeeMakeStateRecorder.init();
        if (coffMsger != null) {
            if (this.containerConfigs != null && this.containerConfigs.size() >= 0) {
                judgeMakeState();
            } else {
                MyLog.d(TAG, "containerConfigs is null");
            }
        } else {
            MyLog.d(TAG, "coffMsger is null");
        }


    }

    public void updateProgress() {
        if (!isStartMaking) {
            makingViewHolder.progressbarWithText.updateProgressAnim();
        }
    }

    /*
    * 制作咖啡
    * */
    private void judgeMakeState() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MyLog.d(TAG, "coffeeMakeStateRecorder.state = " + coffeeMakeStateRecorder.state);

                Result result = coffMsger.mkCoffee(containerConfigs);
                if (!isResultVlide(result)) {
                    MyLog.d(TAG, "make coffee failed");
                    coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEEMAKING_FAILED);
                    return;
                }
                while (true) {
                    MachineState machineState = coffMsger.getCurState();
                    Result stateResult = machineState.getResult();
                    if (!isResultVlide(stateResult)) {
                        coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEEMAKING_FAILED);
                        continue;
                    }

                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEMAKING_ERR) {
                        break;
                    }

                    if (coffeeMakeStateRecorder.state == null || coffeeMakeStateRecorder.state != CoffeeMakeState.COFFEEMAKING_FINISHED) {
                        MajorState majorState = machineState.getMajorState();

                        if (majorState != null) {
                            StateEnum curStateEnum = majorState.getCurState();

                            changeRecorderState(curStateEnum);

                        } else {
                            coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEEMAKING_FAILED);
                            MyLog.d(TAG, "MajorState is null");
                            continue;
                        }
                    }
                    if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEEMAKING_FINISHED) {
                        if (machineState.isCupShelfRightPlace()) {
                            if (System.currentTimeMillis() - START_MAKING_TIME > WAIT_TIME) {

                                MyLog.d(TAG, "coffee has not been taken cup ,time is too long,over 120s !");
                                MyLog.W(TAG, "coffee mk time is too long!");
                                coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN);
                                break;
                            } else {
                                continue;
                            }
                        } else {
                            MyLog.W(TAG, "coffee has been taken cup !");
                            coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEEFINISHED_CUPNOTAKEN);
                            break;
                        }

                    }
                }
            }
        };
        MachineState machineState = coffMsger.getCurState();
        if (machineState != null) {
            MajorState majorState = machineState.getMajorState();
            if (majorState != null) {
                if (majorState.getCurState() == StateEnum.IDLE) {

                    handler.post(runnable);
                }
            } else {
                MyLog.d(TAG, "majorState is null before mkCoffee");
            }
        } else {
            MyLog.d(TAG, "machinestate is null before mkCoffee");
        }

    }

    private void changeRecorderState(StateEnum stateEnum) {

        MyLog.d(TAG, "stateEnum= " + stateEnum);
        switch (stateEnum) {
            case UNKNOW_STATE:
                break;
            case DOWN_CUP:
                coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEE_DOWN_CUP);
                MyLog.d(TAG, "machine current operator is  DOWN_CUP");
                MyLog.W(TAG, "machine current operator is  DOWN_CUP");
                break;
            case MAKING:
                if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_CUP) {
                    updateProgress();
                    isStartMaking = true;
                    coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEE_ISMAKING);

                } else if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_DOWN_POWER) {

                    coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEE_ISMAKING);
                }
                MyLog.d(TAG, "machine current operator is  MAKING");
                MyLog.W(TAG, "machine current operator is  MAKING");

                break;
            case DOWN_POWER:
                coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEE_DOWN_POWER);
                MyLog.d(TAG, "machine current operator is  DOWN_POWER");
                MyLog.W(TAG, "machine current operator is  DOWN_POWER");
                break;
            case FINISH:
                MyLog.d(TAG, "machine current operator is  making FINISH");
                MyLog.W(TAG, "machine current operator is  making FINISH");
                if (coffeeMakeStateRecorder.state == CoffeeMakeState.COFFEE_ISMAKING) {
                    isStartMaking = false;
                    makingViewHolder.progressbarWithText.makeSuccess = true;
                    updateProgress();
                    coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEEMAKING_FINISHED);
                    START_MAKING_TIME = System.currentTimeMillis();
                }

            case HAS_ERR:
                MyLog.d(TAG, "last state is " + coffeeMakeStateRecorder.state + ", but cur state is HAS_ERR");
                MyLog.W(TAG, "last state is " + coffeeMakeStateRecorder.state + ", but cur state is HAS_ERR");
                coffeeMakeStateRecorder.setState(CoffeeMakeState.COFFEEMAKING_ERR);
                makingViewHolder.mErrTip.setVisibility(View.VISIBLE);
                makingViewHolder.progressbarWithText.setVisibility(View.GONE);
                break;
        }
    }

    private boolean isResultVlide(Result result) {
        if (result == null) {

            MyLog.d(TAG, "result=" + result);
            MyLog.W(TAG, "sent coffeeMking but comm failed instantly");
            return false;
        } else if (result.getCode() != Result.SUCCESS) {

            MyLog.d(TAG, "result invilide error =" + result.getErrDes());
            MyLog.W(TAG, "sent coffeeMking but resultMsg is " + result.getErrDes());
            return false;
        }
        return true;
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (makingViewHolder == null) {
            makingViewHolder = new MakingViewHolder();
        }

        setContentView(makingViewHolder.view);
        setCanceledOnTouchOutside(false);
    }


    public void showDialog() {

        show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }
    }

    class MakingViewHolder {
        public View view;
        public TextView mErrTip;
        public ProgressbarWithText progressbarWithText;
        public TextView mProgressTextOne;
        public TextView mProgressTextTwo;
        public TextView mProgressTextThree;

        public ProgressBar mProgressBar;

        public MakingViewHolder() {
            initView();
        }

        private void initView() {
            view = LayoutInflater.from(context).inflate(R.layout.make_out_layout, null);
            mErrTip = (TextView) view.findViewById(R.id.errTip);
            progressbarWithText = (ProgressbarWithText) view.findViewById(R.id.progressTextLayout);
            mProgressTextOne = (TextView) progressbarWithText.findViewById(R.id.tipOne);
            mProgressTextTwo = (TextView) progressbarWithText.findViewById(R.id.tipTwo);
            mProgressTextThree = (TextView) progressbarWithText.findViewById(R.id.tipThress);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressbarWithText.init();
            progressbarWithText.setProgress(0);
        }

    }
}
