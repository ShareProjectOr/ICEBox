package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialLsit;
import example.jni.com.coffeeseller.databases.DataBaseHelper;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.model.adapters.CoffeeViewPagerAdapter;
import example.jni.com.coffeeseller.model.adapters.HomeViewPager;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;
import example.jni.com.coffeeseller.model.listeners.ViewpagerPageChangedListener;
import example.jni.com.coffeeseller.utils.GridViewTransformation;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.customviews.BuyDialog;

/**
 * Created by WH on 2018/3/30.
 */

public class BuyFragment extends BasicFragment implements GridViewItemListener, View.OnClickListener {
    private static String TAG = "BuyFragment";
    private View content;
    private HomeActivity homeActivity;
    public static int DEFAULT_ONEPAGE_NUM = 8;
    private HomeViewPager mViewPager;
    private LinearLayout mPointGroup;
    public ImageView mLogo;
    public TextView mMachineCode;
    public ImageView mHelp;
    private CoffeeViewPagerAdapter mPagerAdapter;
    private List<Coffee> mCoffees;
    private long lastClickTime;

    public static Coffee curSelectedCoffee;

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
        mLogo = (ImageView) content.findViewById(R.id.logo);
        mHelp = (ImageView) content.findViewById(R.id.help);
        mMachineCode = (TextView) content.findViewById(R.id.machineCode);
        mLogo.setOnClickListener(this);

        mViewPager = (HomeViewPager) content.findViewById(R.id.viewPager);
        mPointGroup = (LinearLayout) content.findViewById(R.id.pointGroup);
        mViewPager.setPageTransformer(true, new GridViewTransformation());
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

        mCoffees = new ArrayList<>();
        mCoffees = SingleMaterialLsit.getInstance(homeActivity).getCoffeeList();
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

    public void updateUi() {
        if (mCoffees != null) {
            mCoffees.addAll(SingleMaterialLsit.getInstance(homeActivity).getCoffeeList());
        }
        if (mPagerAdapter != null) {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGridItemClick(AdapterView<?> parent, View view, int position, long id) {


        Coffee coffee = mCoffees.get(position);
        curSelectedCoffee = coffee;
        BuyDialog.getInstance(homeActivity).setInitView();
        BuyDialog.getInstance(homeActivity).setFragment(this);
        BuyDialog.getInstance(homeActivity).showDialog();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuyDialog.getInstance(homeActivity).isShowing()) {
            BuyDialog.getInstance(homeActivity).disDialog();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logo:
                if (System.currentTimeMillis() - lastClickTime <= 2 * 1000) {
                    homeActivity.replaceFragment(FragmentEnum.ChooseCupNumFragment, FragmentEnum.LoginFragment);
                } else {
                    lastClickTime = System.currentTimeMillis();
                }
                break;
        }
    }
}
