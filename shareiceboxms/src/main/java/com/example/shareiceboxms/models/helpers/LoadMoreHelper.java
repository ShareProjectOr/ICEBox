package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by WH on 2017/12/12.
 */

public class LoadMoreHelper {
    private Context context;
    private boolean isLoading = false;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private LoadMoreListener loadMoreListener;
    private RecyclerView recyclerView;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值 
    private int visibleThreshold = 1;


    public LoadMoreHelper() {
    }

    public LoadMoreHelper setContext(Context context) {
        this.context = context;
        return this;
    }

    public LoadMoreHelper setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.adapter = adapter;
        return this;
    }

    public LoadMoreHelper setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
        return this;
    }

    public LoadMoreHelper setLoadMoreListenner(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
        return this;
    }

    public LoadMoreHelper bindScrollListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= lastVisibleItemPosition + visibleThreshold) {
                        loadMoreListener.loadMore(adapter, recyclerView);
                        isLoading = true;
                    }
                }
            }
        });
        return this;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }

    public LoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public int getLastVisibleItemPosition() {
        return lastVisibleItemPosition;
    }

    public int getVisibleThreshold() {
        return visibleThreshold;
    }

    public interface LoadMoreListener {
        void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView);
    }
}
