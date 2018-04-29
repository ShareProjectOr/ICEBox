package example.jni.com.coffeeseller.presenter;

import example.jni.com.coffeeseller.model.CheckVersion;
import example.jni.com.coffeeseller.model.listeners.ICheckVersion;
import example.jni.com.coffeeseller.model.listeners.OnCheckVersionCallBackListener;
import example.jni.com.coffeeseller.views.viewinterface.ICheckVersionView;

/**
 * Created by Administrator on 2018/4/28.
 */

public class CheckVersionPresenter {
    private ICheckVersion iCheckVersion;
    private ICheckVersionView iCheckVersionView;

    public CheckVersionPresenter(ICheckVersionView iCheckVersionView) {
        this.iCheckVersionView = iCheckVersionView;
        iCheckVersion = new CheckVersion();
    }

    public void CheckVersion() {
        iCheckVersion.CheckVersion(iCheckVersionView.getLocationVersion(), new OnCheckVersionCallBackListener() {
            @Override
            public void checkSuccess(String loadfileUrl, boolean isupdate) {
                iCheckVersionView.showLoad(loadfileUrl);
            }

            @Override
            public void checkFailed(String Result) {
              iCheckVersionView.showErroResult(Result);
            }
        });
    }
}
