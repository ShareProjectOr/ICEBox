package example.jni.com.coffeeseller.model.listeners;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface IShowPopListWindow {
    void ShowPopListWindow(List<String> list, Context mContext,View anchor,OnPopListItemClickListener mOnPopListItemClickListener);
}
