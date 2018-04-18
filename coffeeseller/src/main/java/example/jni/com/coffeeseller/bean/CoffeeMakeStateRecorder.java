package example.jni.com.coffeeseller.bean;

import example.jni.com.coffeeseller.MachineConfig.CoffeeMakeState;

/**
 * Created by WH on 2018/4/18.
 */

public class CoffeeMakeStateRecorder {
    public CoffeeMakeState state = null;
    public CoffeeMakeState last_state = null;

    public void init() {

        state = null;
        last_state = null;
    }

    public void setState(CoffeeMakeState curState) {

        last_state = state;
        state = curState;
    }

}
