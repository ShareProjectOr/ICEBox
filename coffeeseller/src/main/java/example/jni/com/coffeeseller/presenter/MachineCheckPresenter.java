package example.jni.com.coffeeseller.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import example.jni.com.coffeeseller.MachineConfig.MachineCheckState;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.MachineCheck;
import example.jni.com.coffeeseller.model.listeners.IMachineCheck;
import example.jni.com.coffeeseller.model.listeners.OnMachineCheckCallBackListener;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.viewinterface.ICheckMachineView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MachineCheckPresenter {
    private IMachineCheck mIMachineCheck;
    private ICheckMachineView mICheckMachineView;
    private HomeActivity mContext;
    private Handler mHandler;
    private String TAG = "MachineCheckPresenter";

    public MachineCheckPresenter(ICheckMachineView mICheckMachineView, HomeActivity mContext) {
        this.mICheckMachineView = mICheckMachineView;
        this.mContext = mContext;
        mHandler = new Handler(mContext.getMainLooper());
        mIMachineCheck = new MachineCheck(mContext);
    }

    public void toLoginFragment() {
        mICheckMachineView.ChangePage(FragmentEnum.LoginFragment);
    }

    public void checkMachine() {


        mIMachineCheck.MachineCheck(new OnMachineCheckCallBackListener() {
            @Override
            public void OpenMainCrilSuccess() {

                mICheckMachineView.ChangeProgressBar(MachineCheckState.MAINCTRLCHECK, true);


            }

            @Override
            public void OpenMainCrilFailed(final String response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.MAINCTRLCHECK, false);
                        mICheckMachineView.showTips(2, response);
                    }
                });

            }


            @Override
            public void MachineCodeCheckSuccess() {

                mICheckMachineView.ChangeProgressBar(MachineCheckState.MACHINECODECHECK, true);


            }

            @Override
            public void MachineCodeCheckFailed(final String response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.MACHINECODECHECK, false);
                        mICheckMachineView.showTips(1, response);
                    }
                });

            }


            @Override
            public void MaterialGroupGetSuccess() {

                mICheckMachineView.ChangeProgressBar(MachineCheckState.GETMATERIAL, true);


            }

            @Override
            public void MaterialGroupGetFailed(final String response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.GETMATERIAL, false);
                        mICheckMachineView.showTips(4, response);
                    }
                });

            }

            @Override
            public void MQTTSubcribeSuccess() {

                mICheckMachineView.ChangeProgressBar(MachineCheckState.SUBMQTT, true);


            }

            @Override
            public void MQTTSubcribeFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "MQtt订阅失败");
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.SUBMQTT, false);
                        mICheckMachineView.showTips(3, "通信连接失败");
                    }
                });

            }

            @Override
            public void MachineCheckEnd(boolean isCheckSuccess) {


                if (isCheckSuccess) {
                    mICheckMachineView.endCheck();
          //          mICheckMachineView.ChangePage(FragmentEnum.ChooseCupNumFragment);
                } else {
                    Log.e(TAG, "check unpass start timeCount");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mICheckMachineView.SetButtonState(false);
                            mICheckMachineView.StartTimeCount();
                        }
                    });

                }
            }


        });

    }
}
