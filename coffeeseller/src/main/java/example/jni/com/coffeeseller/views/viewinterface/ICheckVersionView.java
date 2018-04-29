package example.jni.com.coffeeseller.views.viewinterface;

/**
 * Created by Administrator on 2018/4/28.
 */

public interface ICheckVersionView {
    void showLoad(String downLoadUrl);

    void showErroResult(String err);

    String getLocationVersion();
}
