package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.util.Log;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.MachineState;
import cof.ac.inter.MajorState;
import cof.ac.inter.StateEnum;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.views.activities.HomeActivity;

/**
 * Created by WH on 2018/4/25.
 */

public class CheckCurMachineState {
    private static String TAG = "CheckCurMachineState";
    public static CheckCurMachineState mInstance;
    private CoffMsger mCoffMsger;
    private StringBuffer mBuffer;

    public static CheckCurMachineState getInstance() {
        if (mInstance == null) {
            mInstance = new CheckCurMachineState();
        }
        return mInstance;
    }

    public CheckCurMachineState() {
        mCoffMsger = CoffMsger.getInstance();
        mBuffer = new StringBuffer();
    }

    public boolean isCanMaking() {

        MyLog.d(TAG, "isCanMaking");

        mBuffer.setLength(0);
        mCoffMsger = CoffMsger.getInstance();
        MachineState machineState = mCoffMsger.getLastMachineState();
/*

        if (SharedPreferencesManager.getInstance().getCupNum() < 5) {

            mBuffer.append("\n");
            mBuffer.append("杯子数量不足！");
            return false;
        }
*/


        if (!checkMaterial(machineState)) {

            return false;
        } else {

            MajorState majorState = machineState.getMajorState();

            MyLog.d(TAG, "majorState.getCurStateEnum() ==" + majorState.getCurStateEnum());
            if (majorState.getCurStateEnum() == StateEnum.FINISH) {

                mBuffer.append("\n");
                mBuffer.append("杯架上有杯子未取走");

                return false;
            } else if (majorState.getCurStateEnum() != StateEnum.IDLE) {
                mBuffer.append("\n");
                switch (majorState.getCurStateEnum()) {
                    case DOOR_OPNE:
                        mBuffer.append("错误:升降门未落下");
                        break;
                    case DOWN_CUP:
                        mBuffer.append("错误:正在落杯中");
                        break;
                    case DOWN_POWER:
                        mBuffer.append("错误:正在落粉中");
                        break;
                    case HEAT_POT:
                        mBuffer.append("错误:锅炉加热中");
                        break;
                    case MAKING:
                        mBuffer.append("接收到的指令为制作中");
                        break;

                    case HAS_ERR:
                        mBuffer.append("机器有故障");
                        break;

                    case WARNING:
                        mBuffer.append("机器有警告信息");
                        break;

                    case STERILIZING:
                        mBuffer.append("机器消毒中");
                        break;
                    case CLEANING:
                        mBuffer.append("机器清洗中");
                        break;

                    case TESTING:
                        mBuffer.append("机器测试中");
                        break;

                    case UNKNOW_STATE:
                        mBuffer.append("错误:未知错误");
                        break;

                }
                return false;
            }
        }
        return true;
    }

    public String getStateTip() {
        return mBuffer.toString();
    }


    //检查其他状态,原料状态
    private boolean checkMaterial(MachineState machineState) {
        if (machineState == null) {
            mBuffer.append("\n");
            mBuffer.append("机器状态获取失败！");
            MyLog.d(TAG, "machineState= " + machineState);
            return false;
        }

        boolean isCheckCanMake = true;
        mBuffer.append("提示：");
        if (machineState.getPotTemp() > 130) {

            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("锅炉温度大于130");

        }
        if (machineState.getPotPressure() > 1500) {

            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("锅炉压力大于1500");

        }
        if (!machineState.isWaterEnough()) {
            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("水量不足");
        }
  /*      if (!machineState.isBeanEnough()) {

            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("咖啡豆不足");

        }*/
       /* if (machineState.isWasteContainerFull()) {

            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("污水仓已满");

        }*/
        if (machineState.isLittleDoorOpen()) {
            Log.d(TAG, "前门未关");
            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("前门未关");

        }
        if (!machineState.isCupShelfRightPlace()) {
            Log.d(TAG, "杯架未在初始状态");
            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("杯架未在初始状态");

        }
        if (machineState.hasCupOnShelf()) {
            Log.d(TAG, "杯架上有杯子未取走");
            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("杯架上有杯子未取走");

        }
        if (!isCheckCanMake) {

            MyLog.W(TAG, "err tip : " + mBuffer.toString());
        }

        return isCheckCanMake;
    }
}
