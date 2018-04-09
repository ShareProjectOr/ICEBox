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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.model.adapters.CoffeeViewPagerAdapter;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;
import example.jni.com.coffeeseller.model.listeners.ViewpagerPageChangedListener;
import example.jni.com.coffeeseller.utils.GridViewTransformation;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.customviews.ChooseCupDialog;

/**
 * Created by WH on 2018/3/30.
 */

public class BuyFragment extends BaseFragment implements GridViewItemListener, ChooseCupListenner, View.OnClickListener {
    private View content;
    private HomeActivity homeActivity;
    private static int DEFAULT_ONEPAGE_NUM = 12;
    private LinearLayout mViewPagerParentLayout;
    private ViewPager mViewPager;
    private LinearLayout mPointGroup, mChoosedCupGroup, mPayLayout;
    private CoffeeViewPagerAdapter mPagerAdapter;
    private List<Coffee> mCoffees;
    private List<CoffeeFomat> mCoffeeFomats;
    private Button mPayNow, mCancleAllCup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = inflater.inflate(R.layout.fragment_buy, null);
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
        mChoosedCupGroup = (LinearLayout) content.findViewById(R.id.choosedCupGroup);
        mPayLayout = (LinearLayout) content.findViewById(R.id.payLayout);
        mPayNow = (Button) content.findViewById(R.id.payNow);
        mCancleAllCup = (Button) content.findViewById(R.id.cancleALL);
        mPayNow.setOnClickListener(this);
        mCancleAllCup.setOnClickListener(this);
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
        coffee.name = "wertyhj";
        coffee.price = "12.4";
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        mCoffees.add(coffee);
        Coffee coffee2 = new Coffee();
        coffee2.name = "wertyhj";
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

        mPagerAdapter = new CoffeeViewPagerAdapter(homeActivity, mCoffees, DEFAULT_ONEPAGE_NUM, this);
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

    private void addBottomCup() {
        final View view = LayoutInflater.from(homeActivity).inflate(R.layout.cup_choosed, null);
        ImageView cup = (ImageView) view.findViewById(R.id.choosedCup);
        ImageView cancleCup = (ImageView) view.findViewById(R.id.cancleCup);
        if (mChoosedCupGroup.getChildCount() != 0) {
            cup.setImageResource(R.mipmap.icon);
        }
        view.setTag(mCoffeeFomats.size());
        cancleCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCup(false, view);
            }
        });
        mChoosedCupGroup.addView(view, 0);
    }

    private void removeCup(boolean removeAll, View removeView) {
        if (removeAll) {
            mChoosedCupGroup.removeAllViews();
            mCoffeeFomats.clear();
            mPayLayout.setVisibility(View.GONE);
        } else {
            if (mChoosedCupGroup.getChildCount() == 1) {
                mPayLayout.setVisibility(View.GONE);
                mCoffeeFomats.clear();
            }
            if (removeView != null) {
                mChoosedCupGroup.removeView(removeView);
                mCoffeeFomats.remove(removeView.getTag());
            } else {
                mChoosedCupGroup.removeViewAt(0);
                if (mCoffeeFomats.size() > 0) {
                    mCoffeeFomats.remove(mCoffeeFomats.size() - 1);
                }
            }
        }
    }

    @Override
    public void onGridItemClick(AdapterView<?> parent, View view, int position, long id) {
        addBottomCup();
        ChooseCupDialog dialog = new ChooseCupDialog(homeActivity);
        dialog.setData(mCoffees.get(position), this);
        dialog.showDialog();
    }

    @Override
    public void cancle() {
        removeCup(false, null);
    }

    @Override
    public void getResult(CoffeeFomat coffeeFomat) {
        mPayLayout.setVisibility(View.VISIBLE);
        mCoffeeFomats.add(coffeeFomat);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payNow:
                break;
            case R.id.cancleALL:
                removeCup(true, null);
                break;
        }
    }
}
