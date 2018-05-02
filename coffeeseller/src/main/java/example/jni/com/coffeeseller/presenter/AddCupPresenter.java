package example.jni.com.coffeeseller.presenter;

import example.jni.com.coffeeseller.model.AddCup;
import example.jni.com.coffeeseller.model.listeners.IAddCup;
import example.jni.com.coffeeseller.model.listeners.OnAddCupCallBackListener;
import example.jni.com.coffeeseller.views.viewinterface.IAddCupView;

/**
 * Created by Administrator on 2018/4/29.
 */

public class AddCupPresenter {
    private IAddCupView iAddCupView;
    private IAddCup iAddCup;

    public AddCupPresenter(IAddCupView iAddCupView) {
        iAddCup = new AddCup();
        this.iAddCupView = iAddCupView;
    }

    public void AddCup() {
        iAddCup.addCup(iAddCupView.getThis(), new OnAddCupCallBackListener() {
            @Override
            public void AddSuccess() {
                iAddCupView.showResultAndUpdateView();
            }
        });
    }
}
