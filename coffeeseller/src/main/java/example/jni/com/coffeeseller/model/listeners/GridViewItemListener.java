package example.jni.com.coffeeseller.model.listeners;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by WH on 2018/3/21.
 * GirdView item 点击事件
 */

public interface GridViewItemListener {
    void onGridItemClick(AdapterView<?> parent, View view, int position, long id);
}
