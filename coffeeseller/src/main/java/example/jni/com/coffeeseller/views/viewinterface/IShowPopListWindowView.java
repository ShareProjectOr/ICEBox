package example.jni.com.coffeeseller.views.viewinterface;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface IShowPopListWindowView {
    List<String> getPopList();

    Context getContext();

    View getAnchorView();

    void setText(int positon, View view);
}
