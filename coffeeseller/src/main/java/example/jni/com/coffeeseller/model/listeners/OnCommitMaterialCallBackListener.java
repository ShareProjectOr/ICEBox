package example.jni.com.coffeeseller.model.listeners;

/**
 * Created by Administrator on 2018/4/27.
 */

public interface OnCommitMaterialCallBackListener {
    void commitSuccess();

    void commitFailed(String response);
}
