package com.example.shareiceboxms.models.widget;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.shareiceboxms.models.helpers.LoadMoreHelper;

/**
 * Created by Administrator on 2017/11/15.
 * 解决scrollview滑动留白
 * 动态设置listView高度
 */
public class ListViewForScrollView extends ListView {// implements NestedScrollingChild
    public static int listViewHeight;

    public ListViewForScrollView(Context context) {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, RelativeLayout relativeLayout) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        ScrollView scrollView = (ScrollView) relativeLayout.getChildAt(1);
        LinearLayout childLayout = (LinearLayout) scrollView.getChildAt(0);

        LinearLayout layout = (LinearLayout) relativeLayout.getChildAt(0);

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + childLayout.getChildAt(0).getHeight() + layout.getHeight();
/*
* layout.getHeight()在上面使用时是0，因为视图正在绘制，可以使用延时更新
* */
        Log.d("--layout.getHeight()--", layout.getHeight() + "");
        Log.d("--totalHeight--", totalHeight + "");
        Log.d("--childLayou--", childLayout.getChildAt(0).getHeight() + "");

        // layout.getChildAt(0).getMeasuredHeight():scrollView中不仅包含ListView
        // layout.getChildAt(0).getHeight():空白比上一个小
        listView.setLayoutParams(params);
    }


    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, ScrollView scrollView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        LinearLayout layout = (LinearLayout) scrollView.getChildAt(0);
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + layout.getChildAt(0).getHeight();
        // layout.getChildAt(0).getMeasuredHeight():scrollView中不仅包含ListView
        // layout.getChildAt(0).getHeight():空白比上一个小
        listView.setLayoutParams(params);
        listViewHeight = listView.getMeasuredHeight();
        Log.d("-------------", "-------listView.size=" + listView.getMeasuredHeight());
        Log.d("-------------", "-------listView.LinearLayout.size=" + listView.getLayoutParams().height);
    }

    /**
     * 动态设置ListView的高度，虚拟机8.0需要设置
     *
     * @param listView
     */
    public static void sub(ListView listView) {
        if (listView == null) return;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
        params.height = listViewHeight;
        listView.setLayoutParams(params);
    }
}