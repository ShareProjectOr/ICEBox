package com.example.shareiceboxms.models.factories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/11/21.
 */

public class MyViewFactory {
    private Context mContext;

    public MyViewFactory(Context context) {
        mContext = context;
    }

    public RecyclerView BuildRecyclerViewRule(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, RecyclerView.ItemAnimator itemAnimator, boolean isHasFixedSize) {
        if (recyclerView != null) {
            //layoutManager.set
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(itemAnimator);
            recyclerView.setHasFixedSize(isHasFixedSize);
        }

        return recyclerView;
    }
}
