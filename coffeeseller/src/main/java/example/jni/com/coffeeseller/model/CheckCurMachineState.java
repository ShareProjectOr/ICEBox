package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.DebugAction;
import cof.ac.inter.MachineState;
import cof.ac.inter.MajorState;
import cof.ac.inter.Result;
import cof.ac.inter.StateEnum;
import cof.ac.util.DataSwitcher;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
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
    private boolean isHotting = false;//加热中
    private boolean isClearing = false;//清洗中
    private boolean isDoorOpen = false;//门是否开启着
    private boolean hasCupOnShelf = false;//门是否开启着
    private boolean isCupShelfRightPlace = true;//杯架是否在初始位置
    private boolean isDownTemp = false;//是否在降温中
    private boolean isDownPress = false;//是否在降压中

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

    public boolean isCupEnghou(Context context) {
        MaterialSql materialSql = new MaterialSql(context);
        String cupBunkerId = materialSql.getBunkerIDByContainerID("8");
        String cupNum = materialSql.getStorkByBunkersID(cupBunkerId);

        MyLog.d(TAG, "cupNum = " + cupNum);
        if (!TextUtils.isEmpty(cupNum)) {
            int cupNumInt = Integer.parseInt(cupNum);
            if (cupNumInt > 5) {
                return true;
            } else {
                mBuffer.append("杯子数量不足");
                return false;
            }
        } else {
            mBuffer.append("杯子数量不足");
            return false;
        }
    }

    public boolean isCanMaking() {

        init();
        MyLog.d(TAG, "isCanMaking");

        mBuffer.setLength(0);
        mCoffMsger = CoffMsger.getInstance();
        MachineState machineState = mCoffMsger.getLastMachineState();

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

                if (majorState.getCurStateEnum() == StateEnum.HEAT_POT) {

                    isHotting = true;
                    return true;
                } else if (majorState.getCurStateEnum() == StateEnum.CLEANING) {

                    isClearing = true;
                    return true;
                } else {
                    init();
                    checkCurState(majorState);
                    return false;
                }
            } else {
                init();
            }
        }
        isHotting = false;
        isClearing = false;
        return true;
    }

    public void checkCurState(MajorState majorState) {
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
//                mBuffer.append("错误:锅炉加热中");

                break;
            case MAKING:
                mBuffer.append("错误:制作中");
                break;

            case HAS_ERR:
                MyLog.d(TAG, "---------" + DataSwitcher.byte2Hex(majorState.getHighErr_byte()));
                mBuffer.append("机器有故障: " + majorState.getHighErr_byte());
                break;

            case WARNING:
                mBuffer.append("机器有警告信息");
                break;

            case STERILIZING:
                mBuffer.append("机器消毒中");
                break;
            case CLEANING:
                mBuffer.append("机器清洗中");
                isClearing = true;
                break;

            case TESTING:
                mBuffer.append("机器测试中");
                break;

            case UNKNOW_STATE:
                mBuffer.append("未知错误");
                break;

        }
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
            mBuffer.append("降温中...");
            isDownTemp = true;

            MyLog.W(TAG, "锅炉温度大于130");

        }
        if (machineState.getPotPressure() > 1500 * 10) {

            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("降压中...");
            isDownPress = true;
            MyLog.W(TAG, "锅炉压力大于15000");


        }
        if (!machineState.isWaterEnough()) {
            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("水量不足");
        }
        if (!machineState.isBeanEnough()) {

            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("咖啡豆不足");

        }
       /* if (machineState.isWasteContainerFull()) {

            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("污水仓已满");

        }*/
        if (machineState.isLittleDoorOpen()) {
            Log.d(TAG, "前门未关");
            isCheckCanMake = false;
            isDoorOpen = machineState.isLittleDoorOpen();
            mBuffer.append("\n");
            mBuffer.append("前门未关");

        }
        if (!machineState.isCupShelfRightPlace()) {
         /*   Log.d(TAG, "杯架未在初始状态");
            isCheckCanMake = false;
            mBuffer.append("\n");
            mBuffer.append("杯架未在初始状态");*/
            isCupShelfRightPlace = machineState.isCupShelfRightPlace();

        }
        if (machineState.hasCupOnShelf()) {
            Log.d(TAG, "杯架上有杯子未取走");
            isCheckCanMake = false;
            hasCupOnShelf = machineState.hasCupOnShelf();
            mBuffer.append("\n");
            mBuffer.append("杯架上有杯子未取走");

        }
        if (!isCheckCanMake) {

            MyLog.W(TAG, "err tip : " + mBuffer.toString());
        }

        return isCheckCanMake;
    }

    public boolean canShowQrCode() {
        isCanMaking();
        MyLog.d(TAG, "canShowQrCode  isClearing = " + isClearing);
        MyLog.d(TAG, "canShowQrCode  isHotting = " + isHotting);
        MyLog.d(TAG, "canShowQrCode  isDownTemp = " + isDownTemp);
        MyLog.d(TAG, "canShowQrCode  isDownPress = " + isDownPress);
        MyLog.d(TAG, "canShowQrCode  isCupShelfRightPlace = " + isCupShelfRightPlace);
        return !isClearing && !isHotting && !isDownTemp && !isDownPress && isCupShelfRightPlace;
    }

    public boolean isHottingCheck() {
        return mCoffMsger.getLastMachineState().getMajorState().getCurStateEnum() == StateEnum.HEAT_POT;
    }

    public boolean isClearingCheck() {
        return mCoffMsger.getLastMachineState().getMajorState().getCurStateEnum() == StateEnum.CLEANING;
    }

    public boolean isShelfRightPlace() {
        return !mCoffMsger.getLastMachineState().isCupShelfRightPlace();
    }

    /*
    * 判断机器是否正在忙
    * */
    public boolean isMachineBusy() {

        return false;
    }

    public boolean isHotting() {
        return isHotting;
    }

    public boolean isClearing() {
        return isClearing;
    }

    public boolean isDoorOpen() {
        return isDoorOpen;
    }

    public boolean isHasCupOnShelf() {
        return hasCupOnShelf;
    }

    public boolean isCupShelfRightPlace() {
        return isCupShelfRightPlace;
    }

    public boolean isCupShelfRightPlaceClearMachineTest() {
        CoffMsger coffMsger = CoffMsger.getInstance();
        MachineState machineState = coffMsger.getLastMachineState();
        return machineState.isCupShelfRightPlace();
    }

    public boolean isDoorCloseMachineTest() {
        CoffMsger coffMsger = CoffMsger.getInstance();
        MachineState machineState = coffMsger.getLastMachineState();
        return !machineState.isLittleDoorOpen();
    }

    /*
        * 是否发送关门指令
        * */
    public boolean isSendCloseDoorComd() {//门开、杯架未在初始状态、没有杯子
        return isDoorOpen && (!hasCupOnShelf);
    }

    /*
    * 是否将杯架移入
    * */
    public boolean isMoveInTray() {//杯架未在初始状态、没有杯子
        return (!isCupShelfRightPlace) && (!hasCupOnShelf);
    }

    /*
    * 发送关门指令,应单片机要求
    * */
    public void sendCloseDoorComd() {
        if (isSendCloseDoorComd()) {
            CoffMsger coffMsger = CoffMsger.getInstance();
            Result result = coffMsger.Debug(DebugAction.CTR_LITTLEDOOR, 0, 0);

            if (result.getCode() == Result.SUCCESS) {

                MyLog.W(TAG, "sendCloseDoorComd  send close door code success!");

            } else {

                MyLog.W(TAG, "sendCloseDoorComd  send  close door failed, because " + result.getErrDes());

            }
        }
        if (isMoveInTray()) {
            CoffMsger coffMsger = CoffMsger.getInstance();
            Result result = coffMsger.Debug(DebugAction.MOVE_TRAY, 0, 0);

            if (result.getCode() == Result.SUCCESS) {

                MyLog.W(TAG, "sendCloseDoorComd  send move tray code success!");

            } else {

                MyLog.W(TAG, "sendCloseDoorComd  send  move tray failed, because " + result.getErrDes());

            }
        }
    }

    public void init() {
        isHotting = false;//加热中
        isClearing = false;//清洗中
        isDoorOpen = false;//门是否开启着
        hasCupOnShelf = false;//门是否开启着
        isCupShelfRightPlace = true;//杯架是否在初始位置
        isDownPress = false;
        isDownTemp = false;
    }
}
