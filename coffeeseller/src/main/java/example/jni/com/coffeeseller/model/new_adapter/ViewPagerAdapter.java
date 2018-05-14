package example.jni.com.coffeeseller.model.new_adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by WH on 2018/5/10.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private List<LinearLayout> layouts;

    public ViewPagerAdapter(List<LinearLayout> layouts) {
        this.layouts = layouts;
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LinearLayout layout = layouts.get(position);

        ViewGroup parent = (ViewGroup) layout.getParent();
        if (parent != null) {
            parent.removeView(layout);
        }
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(layouts.get(position));
    }
}