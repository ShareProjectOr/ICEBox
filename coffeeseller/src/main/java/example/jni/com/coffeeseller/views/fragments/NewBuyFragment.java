package example.jni.com.coffeeseller.views.fragments;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.nfc.Tag;
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
import java.util.IllegalFormatCodePointException;
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
import example.jni.com.coffeeseller.model.new_adapter.MyViewPager;
import example.jni.com.coffeeseller.model.new_adapter.ViewPagerAdapter;
import example.jni.com.coffeeseller.model.new_helper.ViewPagerLayout;
import example.jni.com.coffeeseller.model.new_listenner.CoffeeItemSelectedListener;
import example.jni.com.coffeeseller.utils.DensityUtil;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.ObjectAnimatorUtil;
import example.jni.com.coffeeseller.utils.Waiter;
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
    private ViewPager mViewPager;
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
    private long lastClickLeftRight;
    private int curItemPosition = 0;
    private int lastItemPosition = 0;
    private long lastHuadongFinish;//上一次滑动完成

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
        mViewPager = (ViewPager) content.findViewById(R.id.viewPager);
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
        mChooseAndMking = new ChooseAndMking(homeActivity, this, handler);
        layouts = mViewPagerLayout.getLinearLayouts(mCoffees);
        mViewPagerAdapter = new ViewPagerAdapter(layouts);
        mViewPager.setAdapter(mViewPagerAdapter);
        initPagerChangeListenner();
        mViewPager.addOnPageChangeListener(mViewpagerPageChangedListener);
        // mViewPager.setOffscreenPageLimit(1);
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

                /*
                * 如果距离上一次滑动时间少于250ms不处理
                * */
                if (System.currentTimeMillis() - lastHuadongFinish < 250) {
                    return;
                }
            }

            @Override
            public void onPageSelected(int position) {

                lastItemPosition = curItemPosition;
//                curItemPosition = position % layouts.size();

                curItemPosition = position % mViewPagerLayout.getPageCount();

                changePoint(absPosition(curItemPosition));
                MyLog.d(TAG, position + "====onPageSelected = " + curItemPosition);

      /*          if (layouts == null || layouts.size() == 0) {
                    changePoint(0);
                } else {
                    int curPosition = position % layouts.size();
                    changePoint(absPosition(curPosition));
                    MyLog.d(TAG, position + "====onPageSelected = " + curPosition);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                lastHuadongFinish = System.currentTimeMillis();
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
        if (layouts.size() != 1) {
            mViewPager.setCurrentItem(layouts.size() * 100);
        }
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (isViewPagerLooping) {
                    int currentItem = mViewPager.getCurrentItem();
//                    mViewPager.setCurrentItem((currentItem + 1) % layouts.size(), true);
                    if (currentItem == Integer.MAX_VALUE) {

                        mViewPager.setCurrentItem((Integer.MAX_VALUE % layouts.size()) + layouts.size() * 100, true);

                    } else {
                        mViewPager.setCurrentItem((currentItem + 1), true);
                    }
//                    curItemPosition = (currentItem + 1) % layouts.size();
                    mViewPager.setCurrentItem((currentItem + 1), true);
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
        int size = mViewPagerLayout.getPageCount();

        MyLog.d(TAG, "--------------size = " + size);

        for (int i = 0; i < size; i++) {
            final int index = i;
            View pointView = LayoutInflater.from(homeActivity).inflate(R.layout.new_point, null);
            TextView point = (TextView) pointView.findViewById(R.id.point);
            if (i == 0) {
                point.setSelected(true);
                addPointSelectedAnim(point);
                curItemPosition = 0;
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

    private void addPointSelectedAnim(final TextView pointView) {

        pointView.clearAnimation();
        pointView.getLayoutParams().width = DensityUtil.dp2px(homeActivity, 16);
        pointView.requestLayout();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(DensityUtil.dp2px(homeActivity, 16), DensityUtil.dp2px(homeActivity, 48));
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                Integer value = (Integer) animation.getAnimatedValue();
                pointView.getLayoutParams().width = value;

                MyLog.d(TAG, "width =" + value);

                pointView.requestLayout();

            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                pointView.setSelected(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


                if (pointView.getLayoutParams().width != DensityUtil.dp2px(homeActivity, 48)) {

                    MyLog.d(TAG, "onAnimationEnd  = " + pointView.getLayoutParams().width);

                    pointView.getLayoutParams().width = DensityUtil.dp2px(homeActivity, 16);
                    pointView.requestLayout();
                    pointView.setSelected(false);
                } else {
                    pointView.setSelected(true);

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private void addPointUnSelectedAnim(final TextView pointView) {

        pointView.clearAnimation();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(DensityUtil.dp2px(homeActivity, 48), DensityUtil.dp2px(homeActivity, 16));
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                Integer value = (Integer) animation.getAnimatedValue();
                pointView.getLayoutParams().width = value;
                //   curVaule = value;

                MyLog.d(TAG, "width =" + value);

                pointView.requestLayout();

            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pointView.setSelected(false);
                pointView.getLayoutParams().width = DensityUtil.dp2px(homeActivity, 16);
                pointView.requestLayout();
                pointView.clearAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.start();

        if (pointView.getAnimation() == null) {
            pointView.getLayoutParams().width = DensityUtil.dp2px(homeActivity, 16);
            pointView.requestLayout();
            pointView.clearAnimation();
        }
    }

    public void removePoint() {
        mPointLayout.removeAllViews();
    }

    private void changePoint(int index) {

        int pointCount = mPointLayout.getChildCount();
        int position = index % pointCount;

        MyLog.d(TAG, "changePoint  = " + position);

        for (int i = 0; i < pointCount; i++) {
            LinearLayout layout = (LinearLayout) mPointLayout.getChildAt(i);
            TextView point = (TextView) layout.findViewById(R.id.point);

            if (i != position) {

                if (lastItemPosition == i) {
                    addPointUnSelectedAnim(point);
                } else {
                    point.setSelected(false);
                    point.getLayoutParams().width = DensityUtil.dp2px(homeActivity, 16);
                    point.requestLayout();
                }

            } else if (i == position) {

                point.setSelected(true);
                addPointSelectedAnim(point);
            }

        }
        //  curItemPosition = index;
    }

    public void updateUi(int waitTime) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                cancleAutoLoop();
                List<Coffee> coffees = SingleMaterialList.getInstance(homeActivity).getCoffeeList();
                mViewPager.removeAllViews();
                removePoint();
                if (coffees != null && coffees.size() > 0) {
                    layouts = mViewPagerLayout.getLinearLayouts(coffees);

                   /*

                   数据更新，待测试，如果可以，下面的可以注释

                   mViewPagerAdapter.updateLayouts(layouts);

                    addPoint();*/

                    if (layouts != null && layouts.size() > 0) {
                        mViewPagerAdapter = new ViewPagerAdapter(layouts);
                        mViewPager.setAdapter(mViewPagerAdapter);

                        addPoint();
                    }



                    /*if (layouts.size() != 1) {
                        mViewPager.setCurrentItem(layouts.size() * 100);
                    }
                    curItemPosition = 0;
                    lastItemPosition = 0;
                    initLayout();*/
                }
            }
        }, waitTime);
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

//                    initContactLayout();
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                mViewPagerLayout.initViewHolder();
                isCoffeeSelected = false;
                isViewPagerLooping = false;
            }
        });

    }

    private int absPosition(int position) {

        if (position < 0) {
            return Math.abs(position % layouts.size());
        } else {
            return position % layouts.size();
        }

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
                    updateUi(0);
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

                if (mChooseAndMking.isMaking() || mChooseAndMking.isPaying()) {
                    return;
                }

                MyLog.d(TAG, "onClick deviceCode");
                homeActivity.replaceFragment(FragmentEnum.ChooseCupNumFragment, FragmentEnum.LoginFragment);

                break;
            case R.id.helpLayout:


                //做个延时操作

                //      mContactsLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.leftLayout:

                cancleAutoLoop();

                if (mChooseAndMking.isPaying() || mChooseAndMking.isMaking()) {
                    return;
                }

                if (System.currentTimeMillis() - lastClickLeftRight <= 250) {
                    return;
                }

                int viewPagerItemPosition = mViewPager.getCurrentItem() - 1;

                MyLog.d(TAG, " leftLayout position = " + viewPagerItemPosition);

                mViewPager.setCurrentItem(viewPagerItemPosition, true);
//                curItemPosition = absPosition(viewPagerItemPosition);

              /*  if (mViewPager.getCurrentItem() == 0) {
                    if (layouts.size() > 0) {
                        mViewPager.setCurrentItem(layouts.size() - 1, true);
                        curItemPosition = (layouts.size() - 1) % layouts.size();
                    }

                } else {
                    mViewPager.setCurrentItem((mViewPager.getCurrentItem() - 1) % layouts.size(), true);
                    curItemPosition = (mViewPager.getCurrentItem() - 1) % layouts.size();
                }*/
                lastClickLeftRight = System.currentTimeMillis();
                break;
            case R.id.rightLayout:

                cancleAutoLoop();

                if (mChooseAndMking.isPaying() || mChooseAndMking.isMaking()) {
                    return;
                }

                if (System.currentTimeMillis() - lastClickLeftRight <= 250) {
                    return;
                }

                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);

                MyLog.d(TAG, "----------------- " + mViewPager.getCurrentItem());

//                curItemPosition = (mViewPager.getCurrentItem() + 1) % layouts.size();

             /*   if (mViewPager.getCurrentItem() + 1 == layouts.size()) {
                    mViewPager.setCurrentItem(0, false);
                    curItemPosition = 0;
                } else {
                    mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % layouts.size(), true);

                    curItemPosition = (mViewPager.getCurrentItem() + 1) % layouts.size();
                }*/

                lastClickLeftRight = System.currentTimeMillis();

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
