package example.jni.com.coffeeseller.model.new_adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/5/15.
 */

public class MyViewPager extends PagerAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
   /* protected List<T> mData;
    private SparseArray<View> mViews;
    protected Context mContext;

    protected boolean hasAppend;
    protected int appendCounts;

    private String TAG = "AbstractViewPagerAdapter";

    private int count;

    public MyViewPager(List<T> data, Context context) {
        mData = data;
        mContext = context;

        int size = mData.size();
        if (size > 1 && size < 4) {//
            int aftAddSize = 4;
            Object[] appendDatas = null;

            if (size == 2) {//追加到4条,保证数据循环aftAddSize = 4;
            } else if (size == 3) {//追加到6条,保证数据循环aftAddSize = 6;
            }

            appendDatas = new Object[aftAddSize - size];
            for (int i = 0; i < (aftAddSize - size); i++) {
                appendDatas[i] = mData.get(i % size);
            }

            for (int i = 0; i < (aftAddSize - size); i++) {
                mData.add((T) appendDatas[i]);
            }
            mViews = new SparseArray<View>(aftAddSize);
            appendCounts = aftAddSize - size;
            hasAppend = true;
        } else {
            mViews = new SparseArray<View>(data.size());
        }
    }

    @Override
    public int getCount() {
        if (mData.size() <= 1) {
            return mData.size();
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

      //  int size = mData.size();

        int realPos = position % size;

        Log.d(TAG, "instantiateItem,position=" + position + ",realPos=" + realPos);

        View view = mViews.get(realPos);
        if (view == null) {
        //    view = newView(realPos);
            mViews.put(realPos, view);
        }

        try {
            container.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "instantiateItem,current container.childCount=" + container.getChildCount());
        return view;
    }

   // public abstract View newView(int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int size = mData.size();
        int realPos = position % size;

        Log.d(TAG, "destroyItem,position=" + position + ",realPos=" + realPos);

        Log.d(TAG, "destroyItem,before destroyItem current container.childCount=" + container.getChildCount());

        container.removeView(mViews.get(realPos));

        Log.d(TAG, "destroyItem,after destroyItem current container.childCount=" + container.getChildCount());
    }

    public T getItem(int position) {
        int size = mData.size();
        if (hasAppend) {
            size = size - appendCounts;
        }
        int realPos = position % size;
        return mData.get(realPos);
    }*/
}