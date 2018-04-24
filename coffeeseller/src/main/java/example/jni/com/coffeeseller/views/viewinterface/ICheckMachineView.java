package example.jni.com.coffeeseller.views.viewinterface;

import example.jni.com.coffeeseller.MachineConfig.MachineCheckState;
import example.jni.com.coffeeseller.factory.FragmentEnum;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface ICheckMachineView {

    void SetButtonState(boolean isHide);

    void ChangePage(FragmentEnum fragment);

    void endCheck();

    void ChangeProgressBar(MachineCheckState state, boolean isSuccess);

    void showTips(int whichTextView, String tips);

    void StartTimeCount();
}
