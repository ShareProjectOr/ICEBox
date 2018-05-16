package example.jni.com.coffeeseller.model;

import java.util.ArrayList;
import java.util.List;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.ContainerConfig;
import cof.ac.inter.ContainerType;
import cof.ac.inter.DebugAction;
import cof.ac.inter.Result;
import example.jni.com.coffeeseller.MachineConfig.CoffeeMakeState;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by WH on 2018/5/9.
 * 清洗机器
 */

public class ClearMachine {
    public static String TAG = "ClearMachine";
    public final static int BurstBubble = 0;//冲泡器
    public final static int TeaInfuser = 1;//泡茶器
    public final static int Beater_1 = 2;//1号搅拌器
    public final static int Beater_2 = 3;//2号搅拌器
    public final static int Beater_3 = 4;//3号搅拌器
    public final static int Beater_4 = 5;//4号搅拌器

    /*
    * 机器清洗
    * */
    public static boolean clearMachineByAll() {

        CoffMsger coffMsger = CoffMsger.getInstance();
        Result result = coffMsger.Debug(DebugAction.WASH_MACHINE, 0, 0);
        if (result.getCode() == Result.SUCCESS) {

            MyLog.W(TAG, "clearMachineByAll  send clear code success!");
            return true;
        } else {

            MyLog.W(TAG, "clearMachineByAll  send clear code failed, because " + result.getErrDes());
            return false;
        }

    }

    /*
    * 出热水
    * */
    public static boolean clearMachineByHotWater(int waterCapacityL, int waterCapacityH) {

        CoffMsger coffMsger = CoffMsger.getInstance();
        Result result = coffMsger.Debug(DebugAction.OUT_HOTWATER, waterCapacityL, waterCapacityH);//10ml
        if (result.getCode() == Result.SUCCESS) {

            MyLog.W(TAG, "clearMachineByHotWater  send clear code success!");
            return true;
        } else {

            MyLog.W(TAG, "clearMachineByHotWater  send clear code failed, because " + result.getErrDes());
            return false;
        }
    }

    /*
    * 模块清洗
    * moduleId:模块编号
    * count :冲洗次数
    * 0xAA: 冲泡器
    * 0xCC: 泡茶器
    * 1-4: 1号1~4号搅拌器
    * 目前只清洗1-4
    * */
    public static boolean clearMechineByModuleID(int moduleId, int count) {

        CoffMsger coffMsger = CoffMsger.getInstance();
        Result result = null;
        switch (moduleId) {
            case BurstBubble:
                //发送清洗指令
                result = coffMsger.Debug(DebugAction.CLEAR_MODULE, 0xaa, count);

                break;
            case TeaInfuser:
                //发送清洗指令
                result = coffMsger.Debug(DebugAction.CLEAR_MODULE, 0xcc, count);
                break;
            case Beater_1:
                //发送清洗指令
                result = coffMsger.Debug(DebugAction.CLEAR_MODULE, 1, count);
                break;
            case Beater_2:
                //发送清洗指令
                result = coffMsger.Debug(DebugAction.CLEAR_MODULE, 2, count);
                break;
            case Beater_3:
                //发送清洗指令
                result = coffMsger.Debug(DebugAction.CLEAR_MODULE, 3, count);
                break;
            //发送清洗指令
            case Beater_4:

                break;
        }
        if (result == null) {

            MyLog.W(TAG, "clearMechineByModuleID   send clear code failed, because result==null ");
            return false;
        }
        if (result.getCode() == Result.SUCCESS) {

            MyLog.W(TAG, "clearMechineByModuleID  send clear code success!");
            return true;
        } else {

            MyLog.W(TAG, "clearMechineByModuleID  send clear code failed, because " + result.getErrDes());
            return false;
        }
    }


    public static int getModuleId(ContainerConfig containerConfig) {
        if (containerConfig == null) {
            return -1;
        }
        ContainerType containerType = containerConfig.getContainer();

        MyLog.d(TAG, "containerType =" + containerType);

        if (containerType == ContainerType.NO_ONE || containerType == ContainerType.NO_TOW) {//清洗1号模块

            return Beater_1;
        } else if (containerType == ContainerType.NO_THREE || containerType == ContainerType.NO_FOUR) {//清洗2号模块

            return Beater_2;
        } else if (containerType == ContainerType.NO_FIVE || containerType == ContainerType.NO_SIX) {//清洗3号模块

            return Beater_3;

        } else if (containerType == ContainerType.BEAN_CONTAINER) {

            return BurstBubble;
        }
        return -1;
    }

    public static int clearMachineAllModule(List<ContainerConfig> containerConfigs) {

        int time = 0;
        MyLog.W(TAG, "clearMachineAllModule called!");
        if (containerConfigs == null) {
            return time;
        }

        List<Integer> moduleIds = new ArrayList<>();
        for (int i = 0; i < containerConfigs.size(); i++) {
            ContainerConfig containerConfig = containerConfigs.get(i);

            int moduleId = getModuleId(containerConfig);
            if (moduleIds.contains(moduleId)) {
                continue;
            } else {
                moduleIds.add(moduleId);
            }
        }
        MyLog.d(TAG, "moduleIds .size= " + moduleIds.size());
        for (int i = 0; i < moduleIds.size(); i++) {
            int moduleId = moduleIds.get(i);
            boolean isCanSend = (moduleId == -1 ? false : true);
            MyLog.d(TAG, "isCanSend= " + isCanSend);
            if (isCanSend) {

                boolean isSendOk = clearMechineByModuleID(moduleId, 1);

                if (moduleId == BurstBubble) {
                    time += 10 * 1000 + 1000;
                } else {
                    time += 5 * 1000 + 1000;
                }
                MyLog.W(TAG, "clear module!");
                Waiter.doWait(5 * 1000 + 1 * 1000);//再次发送清洗指令必须在5s后
            } else {
                continue;
            }
            if (i + 1 == moduleIds.size()) {
                return time;
            }
        }
        return time;
    }
}
