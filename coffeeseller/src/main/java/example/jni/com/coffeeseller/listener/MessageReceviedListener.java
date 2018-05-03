package example.jni.com.coffeeseller.listener;

/**
 * Created by Administrator on 2018/4/29.
 */

public interface MessageReceviedListener {
    void getMsgType(String msgType);

    void stopSomeOneCoffeeSell(int formulaID);

    void notifyDataSetChange();
}
