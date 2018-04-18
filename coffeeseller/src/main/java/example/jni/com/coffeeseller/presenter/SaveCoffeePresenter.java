package example.jni.com.coffeeseller.presenter;

import android.widget.Toast;

import cof.ac.inter.CoffMsger;
import example.jni.com.coffeeseller.model.SaveCoffee;
import example.jni.com.coffeeseller.model.listeners.ISaveCoffee;
import example.jni.com.coffeeseller.model.listeners.OnSaveCoffeeCallBackListener;
import example.jni.com.coffeeseller.views.viewinterface.ISaveCoffeeView;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SaveCoffeePresenter {
    private ISaveCoffee iSaveCoffee;
    private ISaveCoffeeView iSaveCoffeeView;

    public SaveCoffeePresenter(ISaveCoffeeView iSaveCoffeeView) {
        iSaveCoffee = new SaveCoffee();
        this.iSaveCoffeeView = iSaveCoffeeView;
    }

    public void Save() {
        iSaveCoffee.saveCoffee(iSaveCoffeeView.getcontext(), iSaveCoffeeView.getList(), new OnSaveCoffeeCallBackListener() {
            @Override
            public void saveSuccess() {
                Toast.makeText(iSaveCoffeeView.getcontext(), "保存成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void make() {
        CoffMsger msger = CoffMsger.getInstance();
        msger.mkCoffee(iSaveCoffeeView.getList());
    }
}
