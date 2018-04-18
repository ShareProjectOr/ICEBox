package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.model.adapters.CoffeeViewPagerAdapter;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;
import example.jni.com.coffeeseller.model.listeners.ViewpagerPageChangedListener;
import example.jni.com.coffeeseller.utils.GridViewTransformation;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.customviews.PayAndMakeDialog;

/**
 * Created by WH on 2018/3/30.
 */

public class BuyFragment extends BasicFragment implements GridViewItemListener, ChooseCupListenner, View.OnClickListener {
    private View content;
    private HomeActivity homeActivity;
    public static int DEFAULT_ONEPAGE_NUM = 12;
    private LinearLayout mViewPagerParentLayout;
    private ViewPager mViewPager;
    private LinearLayout mPointGroup;
    private CoffeeViewPagerAdapter mPagerAdapter;
    private List<Coffee> mCoffees;
    private List<CoffeeFomat> mCoffeeFomats;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        content = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_buy));
        init();
        return content;
    }

    private void init() {
        initViews();
        initDatas();
    }

    private void initViews() {

        homeActivity = HomeActivity.getInstance();
        mViewPagerParentLayout = (LinearLayout) content.findViewById(R.id.viewPagerParentLayout);
        mViewPager = (ViewPager) content.findViewById(R.id.viewPager);
        mPointGroup = (LinearLayout) content.findViewById(R.id.pointGroup);
        mViewPager.setPageTransformer(true, new GridViewTransformation());
        mViewPagerParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changePoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initDatas() {
        Log.d("----------------", "initDatas");
        mCoffees = new ArrayList<>();
        mCoffeeFomats = new ArrayList<>();
        Coffee coffee = new Coffee();
        coffee.name = "美式咖啡";
        coffee.price = "12.4";
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        Coffee coffee2 = new Coffee();
        coffee2.name = "美式咖啡2";
        coffee2.price = "12.4";
        coffee2.isOver = true;
        mCoffees.add(coffee2);
        mCoffees.add(coffee2);
        mCoffees.add(coffee2);
        mCoffees.add(coffee2);
        mCoffees.add(coffee2);

        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);

        mCoffees.add(coffee2);
        mCoffees.add(coffee2);
        mCoffees.add(coffee2);
        mCoffees.add(coffee2);
        mCoffees.add(coffee2);

        mPagerAdapter = new CoffeeViewPagerAdapter(homeActivity, mCoffees, this);
        mViewPager.addOnPageChangeListener(new ViewpagerPageChangedListener());
        mViewPager.setAdapter(mPagerAdapter);
    }

    public void addPoint(View pointView, final int index) {
        final TextView point = (TextView) pointView.findViewById(R.id.point);
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePoint(index);
            }
        });
        mPointGroup.addView(pointView);
    }

    private void changePoint(int index) {

        for (int i = 0; i < mPointGroup.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) mPointGroup.getChildAt(i);
            TextView point = (TextView) layout.findViewById(R.id.point);
            if (i == index) {
                point.setSelected(true);
                mViewPager.setCurrentItem(index, true);
            } else {
                point.setSelected(false);
            }
        }
    }


    @Override
    public void onGridItemClick(AdapterView<?> parent, View view, int position, long id) {
      /*  ChooseCupDialogTest dialog = new ChooseCupDialogTest(homeActivity);
        dialog.setData(mCoffees.get(position), this);
        dialog.showDialog();*/
        PayAndMakeDialog dialog = new PayAndMakeDialog(homeActivity);
        dialog.showDialog();
    }

    @Override
    public void cancle() {
    }

    @Override
    public void getResult(CoffeeFomat coffeeFomat) {
        mCoffeeFomats.add(coffeeFomat);
    }

    @Override
    public void onClick(View v) {

    }
}
