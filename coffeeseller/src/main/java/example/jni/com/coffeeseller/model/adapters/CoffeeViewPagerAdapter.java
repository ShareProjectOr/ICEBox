package example.jni.com.coffeeseller.model.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;

/**
 * Created by WH on 2018/3/20.
 */

public class CoffeeViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<Coffee> coffees;
    private List<GridView> gridViews;
    private GridViewItemListener gridViewItemListener;
    private int onePageCount;//偶数
    private int gridViewNum;

    public CoffeeViewPagerAdapter(Context context, List<Coffee> coffees, int onePageCount, GridViewItemListener gridViewItemListener) {
        this.context = context;
        this.coffees = coffees;
        this.onePageCount = onePageCount;
        this.gridViewItemListener = gridViewItemListener;
        this.gridViews = new ArrayList<>();
        getGridViews();
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
            final List<Coffee> gridCoffees;
            if (i != gridViewNum - 1) {
                gridCoffees = coffees.subList(i * onePageCount, (i + 1) * onePageCount);
            } else {
                gridCoffees = coffees.subList(i * onePageCount, coffees.size());
            }
            View view = LayoutInflater.from(context).inflate(R.layout.coffee_grid_layout, null, false);
            final GridView gridView = (GridView) view.findViewById(R.id.coffeeGrid);
            gridView.setNumColumns(onePageCount / 2);
            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (gridViewItemListener != null) {
                        gridViewItemListener.onGridItemClick(parent, view, position, id);
                    }
                    Animation anim = AnimationUtils.loadAnimation(context, R.anim.set_snake);
                    if (view != null) {
                        view.startAnimation(anim);
                    }
                }
            });
            gridView.setAdapter(new CoffeeGridAdapter(context, gridCoffees));
            gridViews.add(gridView);
        }
    }
}
