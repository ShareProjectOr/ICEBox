package example.jni.com.coffeeseller.model.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.customviews.HomeGridView;
import example.jni.com.coffeeseller.views.fragments.BuyFragment;

/**
 * Created by WH on 2018/3/20.
 */

public class CoffeeViewPagerAdapter extends PagerAdapter {
    private static String TAG = "CoffeeViewPagerAdapter";
    private HomeActivity homeActivity;
    private List<Coffee> coffees;
    private List<GridView> gridViews;
    private GridViewItemListener gridViewItemListener;
    private int onePageCount;//偶数
    private int gridViewNum;
    private long lastClickTime;

    public CoffeeViewPagerAdapter(HomeActivity homeActivity, List<Coffee> coffees, GridViewItemListener gridViewItemListener) {
        this.homeActivity = homeActivity;
        this.coffees = coffees;
        this.onePageCount = BuyFragment.DEFAULT_ONEPAGE_NUM;
        this.gridViewItemListener = gridViewItemListener;
        this.gridViews = new ArrayList<>();
        getGridViews();


        Log.d("-----------", gridViews.size() + " -------------");
        Log.d("-----------", coffees.size() + " --------coffees-----");
    }

    @Override
    public int getCount() {
        return gridViewNum;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GridView gridView = gridViews.get(position);
        ViewGroup parent = (ViewGroup) gridView.getParent();
        if (parent != null) {
            parent.removeView(gridView);
        }
        container.addView(gridView);
        return gridView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(gridViews.get(position));
    }

    private void getGridViews() {
        gridViewNum = coffees.size() % onePageCount == 0 ? coffees.size() / onePageCount : coffees.size() / onePageCount + 1;
        for (int i = 0; i < gridViewNum; i++) {
            final List<Coffee> gridCoffees = new ArrayList<>();
            if (i != gridViewNum - 1) {
                gridCoffees.addAll(coffees.subList(i * onePageCount, (i + 1) * onePageCount));
            } else {
                gridCoffees.addAll(coffees.subList(i * onePageCount, coffees.size()));
            }
            final int gridIndex = i;
            View view = LayoutInflater.from(homeActivity).inflate(R.layout.coffee_grid_layout, null, false);
            final HomeGridView gridView = (HomeGridView) view.findViewById(R.id.coffeeGrid);
            gridView.setNumColumns(onePageCount / 2);
            gridView.setGravity(Gravity.CENTER);

            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    if (System.currentTimeMillis() - lastClickTime < 500) {
                        return;
                    } else {
                        lastClickTime = System.currentTimeMillis();
                    }

                    if (!view.isEnabled()) {
                        return;
                    }
                    if (gridViewItemListener != null) {
                        MyLog.d(TAG, "gridViewItemListener has been called");
                        gridViewItemListener.onGridItemClick(parent, view, gridIndex * onePageCount + position, id);
                    }
                    Animation anim = AnimationUtils.loadAnimation(homeActivity, R.anim.set_snake);

                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (view != null) {
                                view.clearAnimation();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    if (view != null) {
                        view.startAnimation(anim);
                    }
                }
            });
            gridView.setAdapter(new CoffeeGridAdapter(homeActivity, gridCoffees));
            gridView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            gridViews.add(gridView);
            /*
            * 添加圆点
            * */
            addPoint(i);
        }
    }

    private void addPoint(int i) {
        View pointView = LayoutInflater.from(homeActivity).inflate(R.layout.point, null);
        TextView point = (TextView) pointView.findViewById(R.id.point);
        if (i == 0) {
            point.setSelected(true);
        }
        point.setText((i + 1) + "");
        if (FragmentFactory.curPage == FragmentEnum.ChooseCupNumFragment) {
            ((BuyFragment) FragmentFactory.getInstance().getFragment(FragmentEnum.ChooseCupNumFragment)).addPoint(pointView, i);
        }
    }
}
