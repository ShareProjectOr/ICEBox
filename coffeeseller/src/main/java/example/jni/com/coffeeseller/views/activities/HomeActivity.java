package example.jni.com.coffeeseller.views.activities;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.MachineState;
import cof.ac.inter.StateListener;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.model.listeners.TaskServiceListener;
import example.jni.com.coffeeseller.parse.PayResult;
import example.jni.com.coffeeseller.presenter.AddFragmentPresenter;
import example.jni.com.coffeeseller.views.fragments.BasicFragment;
import example.jni.com.coffeeseller.views.viewinterface.IAddFragmentView;

import static example.jni.com.coffeeseller.factory.FragmentEnum.ChooseCupNumFragment;
import static example.jni.com.coffeeseller.factory.FragmentEnum.MachineCheckFragment;

/**
 * Created by Administrator on 2018/3/20.
 */

public class HomeActivity extends AppCompatActivity implements IAddFragmentView, TaskServiceListener, StateListener {
    private static HomeActivity mInstance;
    private AddFragmentPresenter mAddFragmentPresenter;
    private String TAG = "HomeActivity";
    public static String Version;
    private int lastState = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //动态注册，此广播只能动态注册才能接收到
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//网络的连接（包括wifi和移动网络）
        checkNetWorkType();
    }

    private boolean checkNetWorkType() {
        ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            MachineConfig.setNetworkType(0);
            return false;
        }
        if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
            Log.d(TAG, "wifi is access");
            MachineConfig.setNetworkType(2);
            return info.isConnected();
        } else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {   //MOBILE
            Log.d(TAG, "MOBILE is access");
            MachineConfig.setNetworkType(1);
            return info.isConnected();
        } else {
            Log.d(TAG, "netWorkType is unaccess");
            MachineConfig.setNetworkType(0);
            return false;
        }
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static synchronized HomeActivity getInstance() {
        return mInstance;
    }

    private void init() {
        initViews();
        initDatas();

    }

    private void initDatas() {
        Version = getVersion();
    }

    private void initViews() {
        mInstance = this;
        FragmentFactory.curPage = MachineCheckFragment;
        CoffMsger.getInstance().setStateListener(this);
//          FragmentFactory.curPage = ChooseCupNumFragment;
        mAddFragmentPresenter = new AddFragmentPresenter(this);
        mAddFragmentPresenter.AddFragment();

    }


    public void replaceFragment(FragmentEnum currentPage, FragmentEnum toPage) {
        FragmentFactory.lastPage = currentPage;
        FragmentFactory.curPage = toPage;
        mAddFragmentPresenter.AddFragment();
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public BasicFragment getFragment() {
        return FragmentFactory.getInstance().getFragment(FragmentFactory.curPage);
    }


    @Override
    public int getLayoutId() {
        return R.id.frag_container;
    }

    @Override
    public void payListenner(PayResult payResult) {

    }

    @Override
    public void formulaUpdate(String formulaId) {

    }

    @Override
    public void stateArrived(final MachineState machineState) {
        Log.e(TAG, "stateArrived");
        if (machineState != null) {
            Log.e(TAG, "lastState is " + lastState + "current state is " + machineState.getMajorState().getStateCode() + "and crrent lowErro_byte is " + machineState.getMajorState().getHighErr_byte());
            if (lastState != machineState.getMajorState().getStateCode()) { //上一次不等于当前的状态则提交
                Log.e(TAG, "lastState is " + lastState + "current state is " + machineState.getMajorState().getStateCode());
                TaskService.getInstance().sendStateMsg();
                lastState = machineState.getMajorState().getStateCode();
            }
        }

    }
}
