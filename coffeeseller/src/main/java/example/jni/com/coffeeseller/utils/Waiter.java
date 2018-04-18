package example.jni.com.coffeeseller.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/4/16.
 */

public class Waiter {
    public static void doWait(long millSecond) {

        try {

            TimeUnit.MILLISECONDS.sleep(millSecond);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
