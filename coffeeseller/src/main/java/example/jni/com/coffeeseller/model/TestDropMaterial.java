package example.jni.com.coffeeseller.model;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.ContainerType;
import cof.ac.inter.DebugAction;
import example.jni.com.coffeeseller.model.listeners.ITestDropMaterial;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TestDropMaterial implements ITestDropMaterial {

    @Override
    public void StartDrop(int dropBunker, int time) {
        CoffMsger coffMsger = CoffMsger.getInstance();
       coffMsger.Debug(DebugAction.OUT_INGREDIENT, dropBunker, 5);
    }
}
