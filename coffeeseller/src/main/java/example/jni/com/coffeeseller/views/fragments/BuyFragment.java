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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialList;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.listener.MessageReceviedListener;
import example.jni.com.coffeeseller.model.CheckCurMachineState;
import example.jni.com.coffeeseller.model.ClearMachine;
import example.jni.com.coffeeseller.model.GetFormula;
import example.jni.com.coffeeseller.model.adapters.CoffeeViewPagerAdapter;
import example.jni.com.coffeeseller.model.adapters.HomeViewPager;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;
import example.jni.com.coffeeseller.model.listeners.ViewpagerPageChangedListener;
import example.jni.com.coffeeseller.utils.GridViewTransformation;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.customviews.BuyDialog;

/**
 * Created by WH on 2018/3/30.
 */

public class BuyFragment extends BasicFragment implements GridViewItemListener, View.OnClickListener, MessageReceviedListener {
    private static String TAG = "BuyFragment";
    private View content;
    private HomeActivity homeActivity;
    public static int DEFAULT_ONEPAGE_NUM = 8;
    private HomeViewPager mViewPager;
    private LinearLayout mPointGroup;
    private RelativeLayout mViewPagerParentLayout;
    public ImageView mLogo, mIsOnLineImg;
    public TextView mMachineCode;
    public ImageView mHelp;
    private RelativeLayout mHelpLayout;
    private CoffeeViewPagerAdapter mPagerAdapter;
    private GridViewItemListener gridViewItemListener;
    private List<Coffee> mCoffees;
    private Handler handler;
    private Timer clearMachineTimer;
    private TimerTask clearMachineTimerTask;
    private BuyDialog buyDialog;

    private long lastClearMachineTime = 0;


    public Coffee curSelectedCoffee;

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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initViews() {
        homeActivity = HomeActivity.getInstance();
        mLogo = (ImageView) content.findViewById(R.id.logo);
        mIsOnLineImg = (ImageView) content.findViewById(R.id.isOnLineImg);
        mHelp = (ImageView) content.findViewById(R.id.help);
        mHelpLayout = (RelativeLayout) content.findViewById(R.id.helpLayout);
        mViewPagerParentLayout = (RelativeLayout) content.findViewById(R.id.viewPagerParentLayout);
        mMachineCode = (TextView) content.findViewById(R.id.machineCode);
        mLogo.setOnClickListener(this);


        mViewPager = (HomeViewPager) content.findViewById(R.id.viewPager);
        mPointGroup = (LinearLayout) content.findViewById(R.id.pointGroup);
        mHelpLayout.setOnClickListener(this);
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

        mViewPagerParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mViewPager.dispatchTouchEvent(event);
                return false;
            }
        });
    }

    private void initDatas() {

        buyDialog = new BuyDialog(homeActivity, R.style.dialog);
        mCoffees = new ArrayList<>();
        handler = new Handler();

        gridViewItemListener = this;

        mCoffees = SingleMaterialList.getInstance(homeActivity).getCoffeeList();
        mPagerAdapter = new CoffeeViewPagerAdapter(homeActivity, mCoffees, this);
        mViewPager.addOnPageChangeListener(new ViewpagerPageChangedListener());
        mViewPager.setAdapter(new CoffeeViewPagerAdapter(homeActivity, mCoffees, this));
        mMachineCode.setText(MachineConfig.getMachineCode() + "");

        TaskService.getInstance().setOnMessageReceviedListener(this);

        updateOnLine();

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

    public void removePoint() {
        mPointGroup.removeAllViews();
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<Coffee> coffees = SingleMaterialList.getInstance(homeActivity).getCoffeeList();
                mViewPager.removeAllViews();
                removePoint();
                if (coffees != null && coffees.size() > 0) {

                    mPagerAdapter = new CoffeeViewPagerAdapter(homeActivity, coffees, gridViewItemListener);
                    mViewPager.addOnPageChangeListener(new ViewpagerPageChangedListener());
                    mViewPager.setAdapter(new CoffeeViewPagerAdapter(homeActivity, mCoffees, gridViewItemListener));
                }
            }
        });
    }

    /*
   * 清洗机器
   * */
 /*   public void clearMachine(final List<ContainerConfig> containerConfigs) {

        MyLog.d(TAG, "clearMachine");
        lastClearMachineTime = System.currentTimeMillis();
        if (clearMachineTimer == null) {
            clearMachineTimer = new Timer();
        }
        if (clearMachineTimerTask == null) {
            clearMachineTimerTask = new TimerTask() {
                @Override
                public void run() {

                    if (CheckCurMachineState.getInstance().isCupShelfRightPlaceClearMachineTest()) {
//                        boolean isResultSuccess = ClearMachine.clearMachineByHotWater(100, 0);
                        boolean isOver=ClearMachine.clearMachineAllModule(containerConfigs);
                        if (System.currentTimeMillis() - lastClearMachineTime > 50 * 1000) {

                            MyLog.d(TAG, "clearMachine over 50s");
                            stopTaskClearMachine();
                            return;
                        }
                        if (isOver) {
                            MyLog.d(TAG, "clearMachine success!");
                            stopTaskClearMachine();
                        } else {
                            MyLog.d(TAG, "clearMachine failed!");
                        }
                    }
                }
            };
        }
        clearMachineTimer.schedule(clearMachineTimerTask, 0, 2000);
    }

    public void stopTaskClearMachine() {

        if (clearMachineTimer != null) {

            clearMachineTimer.cancel();
        }
        clearMachineTimer = null;
        clearMachineTimerTask = null;
    }*/

    /*
    * 更新在线情况
    * */
    private void updateOnLine() {

        Timer timer = null;
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
        timer.schedule(timerTask, 0, 10 * 1000);
    }

    @Override
    public void onGridItemClick(AdapterView<?> parent, View view, int position, long id) {


        Coffee coffee = mCoffees.get(position);
        curSelectedCoffee = coffee;

        if (buyDialog == null) {
            buyDialog = new BuyDialog(homeActivity, R.style.dialog);
        }
        buyDialog.setInitData(this, coffee);
        buyDialog.showDialog();

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
                    List<Coffee> coffees = SingleMaterialList.getInstance(homeActivity).getCoffeeList();
                    mViewPager.removeAllViews();
                    removePoint();
                    if (coffees != null && coffees.size() > 0) {

                        mPagerAdapter = new CoffeeViewPagerAdapter(homeActivity, coffees, gridViewItemListener);
                        mViewPager.addOnPageChangeListener(new ViewpagerPageChangedListener());
                        mViewPager.setAdapter(new CoffeeViewPagerAdapter(homeActivity, mCoffees, gridViewItemListener));
                    }
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
        if (buyDialog != null && buyDialog.isShowing()) {
            buyDialog.disDialog();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logo:
                homeActivity.replaceFragment(FragmentEnum.ChooseCupNumFragment, FragmentEnum.LoginFragment);
               /* if (System.currentTimeMillis() - lastClickTime <= 2 * 1000) {
                    homeActivity.replaceFragment(FragmentEnum.ChooseCupNumFragment, FragmentEnum.LoginFragment);
                } else {
                    lastClickTime = System.currentTimeMillis();
                    homeActivity.replaceFragment(FragmentEnum.ChooseCupNumFragment, FragmentEnum.TradeFragment);
                }*/
                break;
            case R.id.helpLayout:
                if (buyDialog == null) {
                    buyDialog = new BuyDialog(homeActivity, R.style.dialog);
                }
                buyDialog.setInitView(BuyDialog.VIEW_HELP);
                buyDialog.showDialog();
                break;
        }
    }

}
