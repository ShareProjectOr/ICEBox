package example.jni.com.coffeeseller.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import example.jni.com.coffeeseller.factory.FragmentEnum;
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

    public MachineCheckPresenter(ICheckMachineView mICheckMachineView, Context mContext) {
        this.mICheckMachineView = mICheckMachineView;
        this.mContext = mContext;
        mIMachineCheck = new MachineCheck(mContext);
    }

    public void toLoginFragment() {
        mICheckMachineView.ChangePage(FragmentEnum.LoginFragment);
    }

    public void checkMachine() {
        mICheckMachineView.SetButtonState(true);
        mIMachineCheck.MachineCheck(new OnMachineCheckCallBackListener() {
            @Override
            public void OpenMainCrilSuccess() {

            }

            @Override
            public void OpenMainCrilFailed(String response) {
                mICheckMachineView.showTips(1, response);
            }


            @Override
            public void MachineCodeCheckSuccess() {

            }

            @Override
            public void MachineCodeCheckFailed(String response) {

            }

            @Override
            public void MaterialGroupGetSuccess() {

            }

            @Override
            public void MaterialGroupGetFailed(String response) {

            }

            @Override
            public void MQTTSubcribeSuccess() {

            }

            @Override
            public void MQTTSubcribeFailed() {

            }

            @Override
            public void MachineCheckEnd(boolean isCheckSuccess) {
                mICheckMachineView.endCheck();
                if (isCheckSuccess) {
                    mICheckMachineView.ChangePage(FragmentEnum.ChooseCupNumFragment);
                } else {
                    mICheckMachineView.SetButtonState(false);
                    mICheckMachineView.StartTimeCount();
                }
            }


        });

    }
}
