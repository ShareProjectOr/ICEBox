package example.jni.com.coffeeseller.model.new_listenner;

import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.model.new_helper.ViewPagerLayout;

/**
 * Created by WH on 2018/3/21.
 * GirdView item 点击事件
 */

public interface CoffeeItemSelectedListener {

    void getView(Coffee coffee, int page, int position, ViewPagerLayout.ViewHolder viewHolder);
}
