package example.jni.com.coffeeseller.views.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.model.adapters.CoffeeViewPagerAdapter;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;
import example.jni.com.coffeeseller.model.listeners.ViewpagerPageChangedListener;
import example.jni.com.coffeeseller.utils.GridViewTransformation;
import example.jni.com.coffeeseller.views.customviews.ChooseCupDialog;

/**
 * Created by Administrator on 2018/3/20.
 */

public class HomeActivity extends AppCompatActivity implements GridViewItemListener {
    private static int DEFAULT_ONEPAGE_NUM = 12;
    private LinearLayout mViewPagerParentLayout;
    private ViewPager mViewPager;
    private CoffeeViewPagerAdapter mPagerAdapter;
    private List<Coffee> coffees;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initViews();
        initDatas();
    }

    private void initDatas() {
        Log.d("----------------", "initDatas");
        coffees = new ArrayList<>();
        Coffee coffee = new Coffee();
        coffee.name = "wertyhj";
        coffee.price = "12.4";
        coffees.add(coffee);
        coffees.add(coffee);
        coffees.add(coffee);
        coffees.add(coffee);
        coffees.add(coffee);
        Coffee coffee2 = new Coffee();
        coffee2.name = "wertyhj";
        coffee2.price = "12.4";
        coffee2.isOver = true;
        coffees.add(coffee2);
        coffees.add(coffee2);
        coffees.add(coffee2);
        coffees.add(coffee2);
        coffees.add(coffee2);

        coffees.add(coffee);
        coffees.add(coffee);
        coffees.add(coffee);
        coffees.add(coffee);
        coffees.add(coffee);

        coffees.add(coffee2);
        coffees.add(coffee2);
        coffees.add(coffee2);
        coffees.add(coffee2);
        coffees.add(coffee2);

        mPagerAdapter = new CoffeeViewPagerAdapter(this, coffees, DEFAULT_ONEPAGE_NUM, this);
        mViewPager.addOnPageChangeListener(new ViewpagerPageChangedListener());
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initViews() {

        mViewPagerParentLayout = (LinearLayout) findViewById(R.id.viewPagerParentLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setPageTransformer(true, new GridViewTransformation());
        mViewPagerParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
    }

    @Override
    public void onGridItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChooseCupDialog dialog = new ChooseCupDialog(HomeActivity.this);
        dialog.setListenner(null);
        dialog.showDialog();
    }
}
