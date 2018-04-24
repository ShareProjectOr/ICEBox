package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cof.ac.inter.CoffMsger;
import example.jni.com.coffeeseller.MachineConfig.MachineCheckState;
import example.jni.com.coffeeseller.MachineConfig.MachineInitState;
import example.jni.com.coffeeseller.MachineConfig.SerialPortInfo;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.presenter.MachineCheckPresenter;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.Waiter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.viewinterface.ICheckMachineView;

/**
 * Created by Administrator on 2018/4/12.
 */

public class MachineCheckFragment extends BasicFragment implements ICheckMachineView {
    private View mView;
    private boolean doCountDown;//循环检测标记
    boolean hasTurned = false;
    private Button tologin;
    private TextView timeDownText;
    Thread checkThread;
    int checkFlag = 0;
    private MachineCheckPresenter mMachineCheckPresenter;
    boolean isChecking = false;
    Handler mhHandler = new Handler();
    private HomeActivity homeActivity;
    Handler handler = new Handler();
    private ProgressBar mCheck_machineCode;
    private TextView mMachineCode_check_Tips;
    private ProgressBar mCheck_mainCtrl;
    private TextView mCheck_mainCtrl_Tips;
    private ProgressBar mSub_Mqtt;
    private TextView mSub_Mqtt_Tips;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // mView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.machinecheck_fragment_layout));
        mView = inflater.inflate(R.layout.machinecheck_fragment_layout, null);
        initview();
        FragmentFactory.curPage = FragmentEnum.MachineCheckFragment;
        startMachineCheck();
        return mView;
    }

    private void initview() {
        mCheck_machineCode = (ProgressBar) mView.findViewById(R.id.check_machineCode);
        mMachineCode_check_Tips = (TextView) mView.findViewById(R.id.machineCode_check_Tips);
        mCheck_mainCtrl = (ProgressBar) mView.findViewById(R.id.check_mainCtrl);
        mCheck_mainCtrl_Tips = (TextView) mView.findViewById(R.id.check_mainCtrl_Tips);
        mSub_Mqtt = (ProgressBar) mView.findViewById(R.id.sub_Mqtt);
        mSub_Mqtt_Tips = (TextView) mView.findViewById(R.id.sub_Mqtt_Tips);
        tologin = (Button) mView.findViewById(R.id.tologin);
        homeActivity = HomeActivity.getInstance();
        tologin.setOnClickListener(this);
        timeDownText = (TextView) mView.findViewById(R.id.timeDownText);
    }


    private void beginCheck() {
        synchronized (this) {

            isChecking = true;
        }
    }

    public boolean isChecking() {

        return isChecking;
    }


    private void startMachineCheck() {
        timeDownText.setVisibility(View.GONE);
        beginCheck();

        mMachineCheckPresenter = new MachineCheckPresenter(this, getActivity());

        if (checkThread == null) {

            Runnable checkRun = new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    Waiter.doWait(500);
                    mMachineCheckPresenter.checkMachine();
                }
            };
            checkThread = new Thread(checkRun);
            checkThread.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tologin:
                mMachineCheckPresenter.toLoginFragment();
                break;
        }
    }

    private void setCountDown(final int count) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (timeDownText != null)
                    timeDownText.setText(count + "s" + "后重新检测");
            }
        };
        mhHandler.post(runnable);
    }

    @Override
    public void endCheck() {
        synchronized (this) {

            isChecking = false;
        }
        checkThread = null;
    }

    @Override
    public void ChangeProgressBar(MachineCheckState state, boolean isSuccess) {
        switch (state) {
            case MACHINECODECHECK:
                if (isSuccess) {
                    mCheck_machineCode.setProgress(30);
                    Waiter.doWait(500);
                    mCheck_machineCode.setProgress(50);
                    Waiter.doWait(300);
                    mCheck_machineCode.setProgress(70);
                    Waiter.doWait(100);
                    mCheck_machineCode.setProgress(100);
                } else {

                }
                break;
            case MAINCTRLCHECK:
                if (isSuccess) {
                    mCheck_mainCtrl.setProgress(30);
                    Waiter.doWait(500);
                    mCheck_mainCtrl.setProgress(50);
                    Waiter.doWait(300);
                    mCheck_mainCtrl.setProgress(70);
                    Waiter.doWait(100);
                    mCheck_mainCtrl.setProgress(100);
                } else {

                }
                break;
            case SUBMQTT:
                if (isSuccess) {
                    mSub_Mqtt.setProgress(30);
                    Waiter.doWait(500);
                    mSub_Mqtt.setProgress(50);
                    Waiter.doWait(300);
                    mSub_Mqtt.setProgress(70);
                    Waiter.doWait(100);
                    mSub_Mqtt.setProgress(100);
                } else {

                }
                break;
        }
    }


    @Override
    public void SetButtonState(boolean isHide) {

        if (isHide) {
            tologin.setVisibility(View.VISIBLE);
        } else {
            tologin.setVisibility(View.GONE);
        }
    }


    @Override
    public void ChangePage(final FragmentEnum fragment) {
        hasTurned = true;
        handler.post(new Runnable() {
            @Override
            public void run() {
                homeActivity.replaceFragment(FragmentEnum.MachineCheckFragment, fragment);
            }
        });

    }


    @Override
    public void showTips(int whichTextView, String tips) {
        switch (whichTextView) {
            case 1:
                mMachineCode_check_Tips.setText(tips);
                break;
            case 2:
                mCheck_mainCtrl_Tips.setText(tips);
                break;
            case 3:
                mSub_Mqtt_Tips.setText(tips);
                break;
        }
       /* Toast toast = Toast.makeText(getActivity(), tips, Toast.LENGTH_LONG);
        toast.show();*/
    }

    @Override
    public void StartTimeCount() {
        doCountDown = true;
        timeDownText.setVisibility(View.VISIBLE);
        Thread countThr = new Thread(new Runnable() {

            @Override
            public void run() {
                int count = 20;
                while (doCountDown) {

                    MyLog.d("machineCheckFragment", "count--" + count);
                    if (count >= 0) {
                        setCountDown(count);
                        count--;
                        Waiter.doWait(1000);
                    } else {
                        if (!hasTurned) {
                            doCountDown = false;
                            startMachineCheck();
                        }

                        break;
                    }
                }
            }
        });
        countThr.start();
    }


}
