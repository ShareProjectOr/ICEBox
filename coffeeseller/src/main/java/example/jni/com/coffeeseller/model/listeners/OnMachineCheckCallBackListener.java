package example.jni.com.coffeeseller.model.listeners;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface OnMachineCheckCallBackListener {
    void OpenMainCrilSuccess();

    void OpenMainCrilFailed(String response);

    void MachineCodeCheckSuccess();

    void MachineCodeCheckFailed(String response);


    void MaterialGroupGetSuccess();

    void MaterialGroupGetFailed(String response);

    void MQTTSubcribeSuccess();

    void MQTTSubcribeFailed();

    void MachineCheckEnd(boolean isCheckSuccess);
}
