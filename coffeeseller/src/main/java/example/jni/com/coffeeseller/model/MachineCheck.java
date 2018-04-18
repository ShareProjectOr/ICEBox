package example.jni.com.coffeeseller.model;

import cof.ac.inter.CoffMsger;
import example.jni.com.coffeeseller.MachineConfig.MachineInitState;
import example.jni.com.coffeeseller.model.listeners.IMachineCheck;
import example.jni.com.coffeeseller.model.listeners.OnMachineCheckCallBackListener;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MachineCheck implements IMachineCheck {
    private OnMachineCheckCallBackListener mOnMachineCheckCallBackListener;
    private CoffMsger mCoffmsger;

    public MachineCheck() {
        MachineInitState.init();
        mCoffmsger = CoffMsger.getInstance();
    }

    @Override
    public void MachineCheck(OnMachineCheckCallBackListener onMachineCheckCallBackListener) {
        this.mOnMachineCheckCallBackListener = onMachineCheckCallBackListener;
        Thread mcheckThread = new Thread(new checkRunnnable());
        mcheckThread.start();

    }

    class checkRunnnable implements Runnable {

        @Override
        public void run() {
            if (mCoffmsger.init()) {
                mOnMachineCheckCallBackListener.OpenMainCrilSuccess();
                MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.NORMAL;
            } else {
                mOnMachineCheckCallBackListener.OpenMainCrilFailed();
                MachineInitState.CHECK_OPENMAINCTRL = MachineInitState.UNNORMAL;
            }

            if (MachineInitState.CHECK_OPENMAINCTRL == MachineInitState.NORMAL) {
                mOnMachineCheckCallBackListener.MachineCheckEnd(true);
            }else {
                mOnMachineCheckCallBackListener.MachineCheckEnd(false);
            }
        }
    }
}
