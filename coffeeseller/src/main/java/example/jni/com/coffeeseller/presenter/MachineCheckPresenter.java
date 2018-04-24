package example.jni.com.coffeeseller.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import example.jni.com.coffeeseller.MachineConfig.MachineCheckState;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.MachineCheck;
import example.jni.com.coffeeseller.model.listeners.IMachineCheck;
import example.jni.com.coffeeseller.model.listeners.OnMachineCheckCallBackListener;
import example.jni.com.coffeeseller.views.viewinterface.ICheckMachineView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MachineCheckPresenter {
    private IMachineCheck mIMachineCheck;
    private ICheckMachineView mICheckMachineView;
    private Context mContext;
    private Handler mHandler;

    public MachineCheckPresenter(ICheckMachineView mICheckMachineView, Context mContext) {
        this.mICheckMachineView = mICheckMachineView;
        this.mContext = mContext;
        mHandler = new Handler(mContext.getMainLooper());
        mIMachineCheck = new MachineCheck(mContext);
    }

    public void toLoginFragment() {
        mICheckMachineView.ChangePage(FragmentEnum.LoginFragment);
    }

    public void checkMachine() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mICheckMachineView.SetButtonState(true);
            }
        });

        mIMachineCheck.MachineCheck(new OnMachineCheckCallBackListener() {
            @Override
            public void OpenMainCrilSuccess() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.MAINCTRLCHECK, true);
                    }
                });

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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.MACHINECODECHECK, true);
                    }
                });

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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.GETMATERIAL, true);
                    }
                });

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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.SUBMQTT, true);
                    }
                });


            }

            @Override
            public void MQTTSubcribeFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mICheckMachineView.ChangeProgressBar(MachineCheckState.SUBMQTT, false);
                        mICheckMachineView.showTips(3, "通信连接失败");
                    }
                });

            }

            @Override
            public void MachineCheckEnd(boolean isCheckSuccess) {
                mICheckMachineView.endCheck();

                if (isCheckSuccess) {
                    mICheckMachineView.ChangePage(FragmentEnum.ChooseCupNumFragment);
                } else {
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
