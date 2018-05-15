package example.jni.com.coffeeseller.model.new_adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class MyViewPagerAdapter extends PagerAdapter {

    private List<LinearLayout> layouts;
    private SparseArray<LinearLayout> mViews;
    protected Context mContext;

    protected boolean hasAppend;
    protected int appendCounts;

    private String TAG = "AbstractViewPagerAdapter";

    private int count;

    public MyViewPagerAdapter(List<LinearLayout> layouts) {
        this.layouts = layouts;
        int size = layouts.size();
        if (size > 1 && size < 4) {//
            int aftAddSize = 4;
            Object[] appendDatas = null;

            if (size == 2) {//追加到4条,保证数据循环aftAddSize = 4;
            } else if (size == 3) {//追加到6条,保证数据循环aftAddSize = 6;
            }

            appendDatas = new Object[aftAddSize - size];
            for (int i = 0; i < (aftAddSize - size); i++) {
                appendDatas[i] = layouts.get(i % size);
            }

            for (int i = 0; i < (aftAddSize - size); i++) {
                layouts.add((LinearLayout) appendDatas[i]);
            }
            mViews = new SparseArray<LinearLayout>(aftAddSize);
            appendCounts = aftAddSize - size;
            hasAppend = true;
        } else {
            mViews = new SparseArray<LinearLayout>(layouts.size());
        }
    }

    @Override
    public int getCount() {
        if (layouts.size() <= 1) {
            return layouts.size();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int size = layouts.size();

        int realPos = position % size;

        Log.d(TAG, "instantiateItem,position=" + position + ",realPos=" + realPos);

        LinearLayout view = mViews.get(realPos);
        if (view == null) {
            view = newView(realPos);
            mViews.put(realPos, view);
        }

        try {
            container.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "instantiateItem,current container.childCount=" + container.getChildCount());
        return view;
/*
        LinearLayout layout = layouts.get(position);

        ViewGroup parent = (ViewGroup) layout.getParent();
        if (parent != null) {
            parent.removeView(layout);
        }
        container.addView(layout);
        return layout;*/
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int size = layouts.size();
        int realPos = position % size;

        Log.d(TAG, "destroyItem,position=" + position + ",realPos=" + realPos);

        Log.d(TAG, "destroyItem,before destroyItem current container.childCount=" + container.getChildCount());

        container.removeView(mViews.get(realPos));

        Log.d(TAG, "destroyItem,after destroyItem current container.childCount=" + container.getChildCount());
        // container.removeView(layouts.get(position));
    }

    public LinearLayout newView(int position) {
        return layouts.get(position);

    }
}
