package example.jni.com.coffeeseller.model.listeners;

/**
 * Created by Administrator on 2018/4/18.
 */

public interface TestDropMaterialCallBackListener {
    void TestSuccess();

    void TestFailed(int ResultCode);

    void TestEnd();
}
