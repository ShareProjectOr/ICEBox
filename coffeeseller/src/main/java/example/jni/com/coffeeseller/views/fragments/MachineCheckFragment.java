package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cof.ac.inter.CoffMsger;
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
    final int STEP_INITDATA = 1;
    final int STEP_MAINCTL = 2;
    final int STEP_IC = 3;
    final int STEP_NET = 4;
    final int NORMAL_REFRESH = 5;
    final int RANDOM_REFRESH = 6;
    Thread checkThread;
    int checkFlag = 0;
    private MachineCheckPresenter mMachineCheckPresenter;
    boolean isChecking = false;
    Handler mhHandler = new Handler();
    private HomeActivity homeActivity;
    Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.machinecheck_fragment_layout));
        initview();
        FragmentFactory.curPage = FragmentEnum.MachineCheckFragment;
        startMachineCheck();
        return mView;
    }

    private void initview() {
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
    public void SetButtonState(boolean isHide) {
        if (isHide) {

        } else {

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
        Toast toast = Toast.makeText(getActivity(), tips, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void StartTimeCount() {
        doCountDown = true;
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
