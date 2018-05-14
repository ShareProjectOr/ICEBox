package example.jni.com.coffeeseller.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialList;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.listener.MessageReceviedListener;
import example.jni.com.coffeeseller.model.ChooseAndMking;
import example.jni.com.coffeeseller.model.GetFormula;
import example.jni.com.coffeeseller.model.listeners.ViewpagerPageChangedListener;
import example.jni.com.coffeeseller.model.new_adapter.ViewPagerAdapter;
import example.jni.com.coffeeseller.model.new_helper.ViewPagerLayout;
import example.jni.com.coffeeseller.model.new_listenner.CoffeeItemSelectedListener;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.views.activities.HomeActivity;

/**
 * Created by WH on 2018/3/30.
 */

public class NewBuyFragment extends BasicFragment implements CoffeeItemSelectedListener, View.OnClickListener, MessageReceviedListener {
    private static String TAG = "NewBuyFragment";
    private static long WAIT_TIME_TO_SCROLL = 30 * 1000;
    private View content;
    private HomeActivity homeActivity;

    private LinearLayout mLeftLayout;
    private LinearLayout mRightLayout;
    private LinearLayout mFloatLayout;
    private LinearLayout mContactsLayout;
    private RelativeLayout mViewpagerLayout;
    private android.support.v4.view.ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewpagerPageChangedListener;
    private LinearLayout mPointLayout, mHelpLayout;
    private TextView mMachineCode, mDeviceCode;

    private List<Coffee> mCoffees;
    private List<LinearLayout> layouts;
    private Handler handler;
    private ViewPagerLayout mViewPagerLayout;
    private ViewPagerAdapter mViewPagerAdapter;
    private ChooseAndMking mChooseAndMking;

    private boolean isViewPagerLooping = false;//是否再轮询中
    private boolean isCoffeeSelected = false;
    private boolean isAddPageChangeListenner = true;

    private Runnable autoScrollRunnable;
    private Timer autoScrollTimer;
    private TimerTask autoScrollTimerTask;

    private long lastHelpContactsShowTime;
    private long lastDownTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        content = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.new_fragment_buy));
        init();
        return content;
    }

    private void init() {
        initViews();
        initDatas();
    }

    private void initViews() {
        homeActivity = HomeActivity.getInstance();

        mLeftLayout = (LinearLayout) content.findViewById(R.id.leftLayout);
        mRightLayout = (LinearLayout) content.findViewById(R.id.rightLayout);
        mFloatLayout = (LinearLayout) content.findViewById(R.id.floatLayout);
        mContactsLayout = (LinearLayout) content.findViewById(R.id.contactsLayout);
        mViewpagerLayout = (RelativeLayout) content.findViewById(R.id.viewpagerLayout);
        mHelpLayout = (LinearLayout) content.findViewById(R.id.helpLayout);
        mViewPager = (android.support.v4.view.ViewPager) content.findViewById(R.id.viewPager);
        mPointLayout = (LinearLayout) content.findViewById(R.id.pointLayout);
        mMachineCode = (TextView) content.findViewById(R.id.machineCode);
        mDeviceCode = (TextView) content.findViewById(R.id.deviceCode);

        mDeviceCode.setOnClickListener(this);
        mHelpLayout.setOnClickListener(this);
        mLeftLayout.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);

        handler = new Handler();
        mViewPagerLayout = new ViewPagerLayout(homeActivity, this);
        mCoffees = new ArrayList<>();
        mCoffees = SingleMaterialList.getInstance(homeActivity).getCoffeeList();
        layouts = mViewPagerLayout.getLinearLayouts(mCoffees);
        mChooseAndMking = new ChooseAndMking(homeActivity, this, handler);

        mViewPagerAdapter = new ViewPagerAdapter(layouts);
        mViewPager.setAdapter(mViewPagerAdapter);
        initPagerChangeListenner();
        mViewPager.addOnPageChangeListener(mViewpagerPageChangedListener);


        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mChooseAndMking.isMaking() || mChooseAndMking.isPaying()) {

                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        cancleAutoLoop();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        cancleAutoLoop();


                    case MotionEvent.ACTION_UP:
                        //手松开的时间记录
                        lastDownTime = System.currentTimeMillis();
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return false;

            }
        });
    }

    private void initPagerChangeListenner() {
        mViewpagerPageChangedListener = new ViewPager.OnPageChangeListener() {
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
        };
    }

    private void startAutoLoop() {
        isViewPagerLooping = true;
        handler.postDelayed(autoScrollRunnable, 4000);
    }

    private void cancleAutoLoop() {
        isViewPagerLooping = false;
        handler.removeCallbacks(autoScrollRunnable);
    }

    private void initDatas() {

        mCoffees = new ArrayList<>();
        addPoint();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (isViewPagerLooping) {
                    int currentItem = mViewPager.getCurrentItem();
                    mViewPager.setCurrentItem((currentItem + 1) % layouts.size(), true);
                    handler.postDelayed(autoScrollRunnable, 4000);
                }
            }
        };
        mMachineCode.setText(MachineConfig.getMachineCode() + "");

        TaskService.getInstance().setOnMessageReceviedListener(this);

        updateOnLine();

        autoScrollTimeCheck();

    }

    private void addPoint() {
        for (int i = 0; i < layouts.size(); i++) {
            final int index = i;
            View pointView = LayoutInflater.from(homeActivity).inflate(R.layout.new_point, null);
            TextView point = (TextView) pointView.findViewById(R.id.point);
            if (i == 0) {
                point.setSelected(true);
            }
            point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePoint(index);
                }
            });
            mPointLayout.addView(pointView);
        }
    }

    public void removePoint() {
        mPointLayout.removeAllViews();
    }

    private void changePoint(int index) {

        for (int i = 0; i < mPointLayout.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) mPointLayout.getChildAt(i);
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<Coffee> coffees = SingleMaterialList.getInstance(homeActivity).getCoffeeList();
                mViewPager.removeAllViews();
                removePoint();
                if (coffees != null && coffees.size() > 0) {
                    layouts = mViewPagerLayout.getLinearLayouts(coffees);
                    mViewPagerAdapter = new ViewPagerAdapter(layouts);
                    mViewPager.setAdapter(mViewPagerAdapter);
                    addPoint();
                }
            }
        });
    }


    public void autoScrollTimeCheck() {

        stopAutoScrollTimer();

        if (autoScrollTimer == null) {
            autoScrollTimer = new Timer();
        }
        if (autoScrollTimerTask == null) {
            autoScrollTimerTask = new TimerTask() {
                @Override
                public void run() {

                    if (System.currentTimeMillis() - lastDownTime > WAIT_TIME_TO_SCROLL) {
                        if (!isViewPagerLooping && !isCoffeeSelected && !mChooseAndMking.isMaking() && !mChooseAndMking.isPaying()) {
                            startAutoLoop();
                        }
                    } else {
                        cancleAutoLoop();
                    }

                    initContactLayout();
                }
            };
        }
        autoScrollTimer.schedule(autoScrollTimerTask, 0, 2000);
    }

    private void initContactLayout() {
    /*
    * 客服联系方式一分钟后消失
    * */
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (mContactsLayout.getVisibility() == View.VISIBLE) {
                    if (lastHelpContactsShowTime >= 60 * 1000) {
                        mContactsLayout.setVisibility(View.GONE);
                    } else {
                        lastHelpContactsShowTime += 2000;
                    }
                }
            }
        });
    }

    public void stopAutoScrollTimer() {

        if (autoScrollTimer != null) {
            autoScrollTimer.cancel();
        }
        autoScrollTimer = null;
        autoScrollTimerTask = null;
    }


    public void initLayout() {
        mViewPagerLayout.initViewHolder();
        isCoffeeSelected = false;
        isViewPagerLooping = true;
    }

    /*
    * 更新在线情况
    * */
    private void updateOnLine() {

      /*  Timer timer = null;
        TimerTask timerTask = null;
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (MachineConfig.getNetworkType() == 0) {
                                mIsOnLineImg.setImageResource(R.mipmap.ic_launcher_round);
                            } else {
                                mIsOnLineImg.setImageResource(R.mipmap.on_line);
                            }
                        }
                    });
                }
            };
        }
        timer.schedule(timerTask, 0, 10 * 1000);*/
    }

    @Override
    public void getMsgType(String msgType) {
        MyLog.d(TAG, "getMsgType come!");

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                GetFormula getFormula = new GetFormula();
                Boolean isUpdate = getFormula.getFormula(homeActivity);
                return isUpdate;
            }

            @Override
            protected void onPostExecute(Boolean isUpdate) {
                super.onPostExecute(isUpdate);
                if (isUpdate) {
                    updateUi();
                }

            }
        }.execute();

    }

    @Override
    public void stopSomeOneCoffeeSell(int formulaID) {

    }

    @Override
    public void notifyDataSetChange() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAutoScrollTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deviceCode:

                MyLog.d(TAG, "onClick deviceCode");
                homeActivity.replaceFragment(FragmentEnum.ChooseCupNumFragment, FragmentEnum.LoginFragment);

                break;
            case R.id.helpLayout:


                //做个延时操作

                mContactsLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.leftLayout:

                if (mViewPager.getCurrentItem() == 0) {
                    if (layouts.size() > 0) {
                        mViewPager.setCurrentItem(layouts.size() - 1, true);
                    }

                } else {
                    mViewPager.setCurrentItem((mViewPager.getCurrentItem() - 1) % layouts.size(), true);
                }

                break;
            case R.id.rightLayout:

                if (mViewPager.getCurrentItem() + 1 == layouts.size()) {
                    mViewPager.setCurrentItem(0, false);
                } else {
                    mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % layouts.size(), true);
                }

                break;
        }
    }

    @Override
    public void getView(Coffee coffee, int page, int position, ViewPagerLayout.ViewHolder viewHolder) {

        MyLog.d(TAG, "getView");

        if (mChooseAndMking.isMaking() || viewHolder.isOver || mChooseAndMking.isPaying()) {//制作中时点击无效
            return;
        }
        if (!viewHolder.isSelected) {
            isCoffeeSelected = true;
            mFloatLayout.removeAllViews();
            mChooseAndMking.init(coffee);
            cancleAutoLoop();
            mFloatLayout.addView(mChooseAndMking.getView());
        } else {
            isCoffeeSelected = false;
            mFloatLayout.removeAllViews();
        }

        mViewPagerLayout.updateViewHolder(viewHolder);
        return;
    }
}
