package example.jni.com.coffeeseller.model.new_adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WH on 2018/5/10.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private List<LinearLayout> layouts;

    public ViewPagerAdapter(List<LinearLayout> layouts) {
        this.layouts = layouts;
        if (this.layouts == null) {
            this.layouts = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {

        return layouts.size() ;// == 0 ? layouts.size() : Integer.MAX_VALUE
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (layouts.size() < 3) {
            LinearLayout layout = layouts.get(position % layouts.size());
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
            container.addView(layout);

            return layout;

        } else {
            container.addView(layouts.get(position % layouts.size()));

            return layouts.get(position % layouts.size());
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        if (layouts.size() < 3) {

        } else {
            container.removeView(layouts.get(position % layouts.size()));
        }
    }
}