package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cof.ac.inter.StateEnum;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialLsit;
import example.jni.com.coffeeseller.databases.DataBaseHelper;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.listener.MessageReceviedListener;
import example.jni.com.coffeeseller.model.adapters.CoffeeViewPagerAdapter;
import example.jni.com.coffeeseller.model.adapters.HomeViewPager;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;
import example.jni.com.coffeeseller.model.listeners.ViewpagerPageChangedListener;
import example.jni.com.coffeeseller.parse.PayResult;
import example.jni.com.coffeeseller.utils.GridViewTransformation;
import example.jni.com.coffeeseller.utils.Waiter;
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
    public ImageView mLogo, mIsOnLineImg;
    public TextView mMachineCode;
    public ImageView mHelp;
    private RelativeLayout mHelpLayout;
    private CoffeeViewPagerAdapter mPagerAdapter;
    private List<Coffee> mCoffees;
    private Handler handler;

    private BuyDialog buyDialog;


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

    private void initViews() {
        homeActivity = HomeActivity.getInstance();
        mLogo = (ImageView) content.findViewById(R.id.logo);
        mIsOnLineImg = (ImageView) content.findViewById(R.id.isOnLineImg);
        mHelp = (ImageView) content.findViewById(R.id.help);
        mHelpLayout = (RelativeLayout) content.findViewById(R.id.helpLayout);
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

    }

    private void initDatas() {

        buyDialog = new BuyDialog(homeActivity, R.style.dialog);
        mCoffees = new ArrayList<>();
        handler = new Handler();
        mCoffees = SingleMaterialLsit.getInstance(homeActivity).getCoffeeList();
        mPagerAdapter = new CoffeeViewPagerAdapter(homeActivity, mCoffees, this);
        mViewPager.addOnPageChangeListener(new ViewpagerPageChangedListener());
        mViewPager.setAdapter(mPagerAdapter);
        mMachineCode.setText(MachineConfig.getMachineCode() + "");

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
                                mIsOnLineImg.setImageResource(R.mipmap.on_line);
                            } else {
                                mIsOnLineImg.setImageResource(R.mipmap.ic_launcher_round);
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
        int formulaID = Integer.parseInt(msgType);


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
