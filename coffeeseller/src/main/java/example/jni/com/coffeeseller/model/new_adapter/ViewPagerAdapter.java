package example.jni.com.coffeeseller.model.new_adapter;

import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.utils.MyLog;

/**
 * Created by WH on 2018/5/10.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private List<LinearLayout> viewLayouts;

    public ViewPagerAdapter(List<LinearLayout> layouts) {

        if (this.viewLayouts == null) {
            this.viewLayouts = new ArrayList<>();
        }

        for (int i = 0; i < layouts.size(); i++) {
            this.viewLayouts.add(layouts.get(i));
        }


        MyLog.d("-------------------------", viewLayouts.size() + "");

    }

    @Override
    public int getCount() {

        return viewLayouts.size() <= 1 ? viewLayouts.size() : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LinearLayout layout = viewLayouts.get(position % viewLayouts.size());

        MyLog.d("-------------------------", position + " , " + position % viewLayouts.size());

        ViewGroup parent = (ViewGroup) layout.getParent();
        if (parent != null) {
            parent.removeView(layout);
        }
        container.addView(layout);

        return layout;

    /*    if (viewLayouts.size() < 3) {
            LinearLayout layout = viewLayouts.get(position % viewLayouts.size());
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
            container.addView(layout);

            return layout;

        } else {

            container.addView(viewLayouts.get(position % viewLayouts.size()));

            return viewLayouts.get(position % viewLayouts.size());
        }*/
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        if (viewLayouts.size() <= 3) {

        } else {
            container.removeView(viewLayouts.get(position % viewLayouts.size()));
        }
    }
}