package example.jni.com.coffeeseller.model.listeners;

/**
 * Created by Administrator on 2018/4/27.
 */

public interface OnCheckVersionCallBackListener {
    void checkSuccess(String loadfileUrl, boolean isupdate);

    void checkFailed(String Result);
}
