package example.jni.com.coffeeseller.crash;

import android.app.Application;

/**
 * Created by Administrator on 2017/8/9.
 */

public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }
}
