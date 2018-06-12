package otherutils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import ViewUtils.ScreenUtil;

/**
 * Created by Administrator on 2017/2/16.
 */

public class ListviewBackTopListener implements AbsListView.OnScrollListener {
    private ListView listView;// List数据列表
    private Button toTopBtn;// 返回顶部的按钮
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private Activity activity;
    private Scroller mScroller;

    public interface Scroller {
        void ScrollerBottom();
        void ScrollerTop();
    }

    public ListviewBackTopListener(ListView listView, Button button, Activity activity) {
        this.listView = listView;
        toTopBtn = button;
        this.activity = activity;
    }

    public void getScoller(Scroller scroller) {
        mScroller = scroller;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            // 当不滚动时
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                scrollFlag = false;
                // 判断滚动到底部
                Log.i("最后一项", String.valueOf(listView.getLastVisiblePosition()));
                Log.i("总项数", String.valueOf(listView.getChildCount()));
                if (listView.getLastVisiblePosition() == (listView
                        .getChildCount() - 1)) {
                    toTopBtn.setVisibility(View.VISIBLE);
                    if (mScroller != null) {
                        mScroller.ScrollerBottom();
                    }
                }
                // 判断滚动到顶部
                if (view.getFirstVisiblePosition() == 0) {
                    toTopBtn.setVisibility(View.GONE);
                    if (mScroller != null) {
                        mScroller.ScrollerTop();
                    }
                }
                if (getScrollY() >= 1200) {
                    toTopBtn.setVisibility(View.VISIBLE);
                } else if (getScrollY() <= 1000) {
                    toTopBtn.setVisibility(View.GONE);
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                scrollFlag = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                scrollFlag = false;
                break;
        }
    }

    public int getScrollY() {

        View c = listView.getChildAt(0);

        if (c == null) {

            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();

        int top = c.getTop();

        return -top + firstVisiblePosition * c.getHeight();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
// 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
        if (scrollFlag
                && ScreenUtil.getScreenViewBottomHeight(listView) >= ScreenUtil
                .getScreenHeight(activity)) {
            if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                toTopBtn.setVisibility(View.VISIBLE);
            } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                toTopBtn.setVisibility(View.GONE);
            } else {
                return;
            }
            lastVisibleItemPosition = firstVisibleItem;
        }
    }

    /**
     * 滚动ListView到指定位置
     *
     * @param pos
     */
    public void setListViewPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            listView.smoothScrollToPosition(pos);
        } else {
            listView.setSelection(pos);
        }
    }

}