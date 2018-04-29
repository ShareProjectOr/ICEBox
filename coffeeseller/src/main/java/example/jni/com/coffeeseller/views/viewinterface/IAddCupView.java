package example.jni.com.coffeeseller.views.viewinterface;

import android.content.Context;

/**
 * Created by Administrator on 2018/4/29.
 */

public interface IAddCupView {
    Context getThis();

    void showResultAndUpdateView();
}
