package example.jni.com.coffeeseller.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cof.ac.inter.DebugAction;
import cof.ac.inter.Result;

import java.util.List;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.MachineState;
import cof.ac.inter.StateListener;
import cof.ac.util.DataSwitcher;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.CommitMaterialObject;
import example.jni.com.coffeeseller.bean.MachineConfig;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.httputils.UpdateAppManager;
import example.jni.com.coffeeseller.model.adapters.MaterialRecycleListAdapter;
import example.jni.com.coffeeseller.presenter.AddCupPresenter;
import example.jni.com.coffeeseller.presenter.CheckVersionPresenter;
import example.jni.com.coffeeseller.presenter.CommitMaterialPresenter;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.customviews.SetFloatButton;
import example.jni.com.coffeeseller.views.viewinterface.IAddCupView;
import example.jni.com.coffeeseller.views.viewinterface.IAddMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.ICheckVersionView;
import example.jni.com.coffeeseller.views.viewinterface.ICommitMaterialView;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ConfigFragment extends BasicFragment implements IAddMaterialView, ICommitMaterialView, ICheckVersionView, StateListener, IAddCupView, CompoundButton.OnCheckedChangeListener {
    private View mView;
    private Button toDebug, materialCommit, checkVersion, saveConfig, changePassword, searchTrade, toSystem;
    private HomeActivity homeActivity;
    private TextView back;
    private RecyclerView materialList;
    private MaterialRecycleListAdapter mAdapter;
    private TextView cupStock, waterStock;
    private TextView addCupTime, addWaterTime;
    private TextView addCup, addWater, mMachineName;
    private MaterialSql sql;
    private int BunkersID;
    private CommitMaterialPresenter materialPresenter;
    private UpdateAppManager updateAppManager;
    private CheckVersionPresenter checkVersionPresenter;
    private TextView errCode, runState, netWorkState, CupNum, cupShelfState, boilerTemperature, boilerPressure, doorState, cupDoorState, driverVersion, updateTimeAndVersion;
    private CoffMsger msger;
    private Handler mHandler = new Handler();
    private AddCupPresenter addCupPresenter;
    private EditText mMachineCode, mTime, mWaterPercent, passWord;
    private CheckBox isOutCupAutoClear, onClear, usuallyWaterType, hotAndColdWaterType;
    private int lastState;
    private String TAG = "configFrament";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.config_fragment_layout, null);
        initView();
        initData();
        return mView;
    }

    private void initData() {

        addCupTime.setText(SharedPreferencesManager.getInstance(getActivity()).getAddCupTime());
        addWaterTime.setText(SharedPreferencesManager.getInstance(getActivity()).getAddWaterTime());
        mMachineCode.setText(SharedPreferencesManager.getInstance(getActivity()).getMachineCode());
        passWord.setText(SharedPreferencesManager.getInstance(getActivity()).getLoginPassword());
        boolean isOutCupClear = SharedPreferencesManager.getInstance(getActivity()).getOutCupAutoClear();
        boolean isOnClear = SharedPreferencesManager.getInstance(getActivity()).getIsOnClear();
        if (isOutCupClear) {
            isOutCupAutoClear.setChecked(true);
        } else {
            isOutCupAutoClear.setChecked(false);
        }

        if (isOnClear) {
            onClear.setChecked(true);
        } else {
            onClear.setChecked(false);
        }
        if (msger != null) {

            driverVersion.setText("驱动版本:" + msger.getCurState().getVersion());
            if (msger.getLastMachineState().isWasteContainerFull()) {
                waterStock.setText("充足");
            } else {
                waterStock.setText("不足");
            }
        } else {

        }

        updateTimeAndVersion.setText("最后更新于 " + SharedPreferencesManager.getInstance(getActivity()).getLastAppUpdateTime() + " 当前版本: " + getVersion());


    }


    private void initView() {

        msger = CoffMsger.getInstance();
        if (msger != null) {
            msger.setStateListener(this);
        }
        addCupPresenter = new AddCupPresenter(this);
        errCode = (TextView) mView.findViewById(R.id.errCode);
        runState = (TextView) mView.findViewById(R.id.runState);
        netWorkState = (TextView) mView.findViewById(R.id.netWorkState);
        CupNum = (TextView) mView.findViewById(R.id.CupNum);
        mMachineName = (TextView) mView.findViewById(R.id.machineName);
        mMachineName.setOnClickListener(this);
        toSystem = (Button) mView.findViewById(R.id.toSystem);
        toSystem.setOnClickListener(this);
        passWord = (EditText) mView.findViewById(R.id.passWord);
        saveConfig = (Button) mView.findViewById(R.id.saveConfig);
        searchTrade = (Button) mView.findViewById(R.id.search_trade);
        updateTimeAndVersion = (TextView) mView.findViewById(R.id.updateTimeAndVersion);
        cupShelfState = (TextView) mView.findViewById(R.id.cupShelfState);
        boilerTemperature = (TextView) mView.findViewById(R.id.boilerTemperature);
        boilerPressure = (TextView) mView.findViewById(R.id.boilerPressure);
        doorState = (TextView) mView.findViewById(R.id.doorState);
        changePassword = (Button) mView.findViewById(R.id.changePassword);
        cupDoorState = (TextView) mView.findViewById(R.id.cupDoorState);
        driverVersion = (TextView) mView.findViewById(R.id.driverVersion);
        toDebug = (Button) mView.findViewById(R.id.debug_machine);
        back = (TextView) mView.findViewById(R.id.backToCheck);
        mMachineCode = (EditText) mView.findViewById(R.id.machineCode);
        mTime = (EditText) mView.findViewById(R.id.time);
        mWaterPercent = (EditText) mView.findViewById(R.id.waterPercent);
        isOutCupAutoClear = (CheckBox) mView.findViewById(R.id.isOutCupAutoClear);
        isOutCupAutoClear.setOnCheckedChangeListener(this);
        onClear = (CheckBox) mView.findViewById(R.id.onClear);
        onClear.setOnCheckedChangeListener(this);
        usuallyWaterType = (CheckBox) mView.findViewById(R.id.usuallyWaterType);
        hotAndColdWaterType = (CheckBox) mView.findViewById(R.id.hotAndColdWaterType);
        materialList = (RecyclerView) mView.findViewById(R.id.material_list);
        mAdapter = new MaterialRecycleListAdapter(getActivity());
        checkVersionPresenter = new CheckVersionPresenter(this);
        checkVersion = (Button) mView.findViewById(R.id.check_update);
        updateAppManager = new UpdateAppManager(getActivity());
        materialCommit = (Button) mView.findViewById(R.id.material_commit);
        cupStock = (TextView) mView.findViewById(R.id.cup_num);
        waterStock = (TextView) mView.findViewById(R.id.water_count);
        materialPresenter = new CommitMaterialPresenter(this);
        addCupTime = (TextView) mView.findViewById(R.id.add_cup_time);
        addWaterTime = (TextView) mView.findViewById(R.id.add_water_time);
        addCup = (TextView) mView.findViewById(R.id.add_cup);
        addWater = (TextView) mView.findViewById(R.id.add_water);
        materialList.setAdapter(mAdapter);
        materialList.setLayoutManager(new LinearLayoutManager(getActivity()));
        materialList.setHasFixedSize(true);
        back.setOnClickListener(this);
        saveConfig.setOnClickListener(this);
        checkVersion.setOnClickListener(this);
        homeActivity = HomeActivity.getInstance();
        searchTrade.setOnClickListener(this);
        sql = new MaterialSql(getActivity());
        toDebug.setOnClickListener(this);
        addCup.setOnClickListener(this);
        addWater.setOnClickListener(this);
        materialCommit.setOnClickListener(this);
        changePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.debug_machine:


                Result result = msger.Debug(DebugAction.RESET, 0, 0);//复位机器
                if (result.getCode() == Result.SUCCESS) {
                    msger.startCheckState();
                    Toast.makeText(getActivity(), "机器复位成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "机器复位失败" + result.getCode(), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.backToCheck:
                materialPresenter.CommitMaterial2();
                break;
            case R.id.add_cup:
                addCupPresenter.AddCup();
                break;
            case R.id.add_water:
                BunkersID = 7;
                break;
            case R.id.material_commit:
                materialPresenter.CommitMaterial();
                break;
            case R.id.check_update:
                checkVersionPresenter.CheckVersion();
                break;
            case R.id.saveConfig:
                if (passWord.getText().toString().isEmpty() || passWord.getText().length() < 6) {
                    Toast.makeText(getActivity(), "密码不能为空或不能短于6位", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mMachineCode.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "机器号未填写", Toast.LENGTH_LONG).show();
                    return;
                }
                //检测到机器号改变了 则删除所有行
                if (!SharedPreferencesManager.getInstance(getActivity()).getMachineCode().equals(mMachineCode.getText().toString())) {
                    MaterialSql sql = new MaterialSql(getActivity());
                    sql.deleteAllContent();
                }
                SharedPreferencesManager.getInstance(getActivity()).setMachineCode(mMachineCode.getText().toString());
                SharedPreferencesManager.getInstance(getActivity()).setLoginPassword(passWord.getText().toString());
                Toast.makeText(getActivity(), "配置信息已保存", Toast.LENGTH_LONG).show();
                homeActivity.replaceFragment(FragmentEnum.ConfigFragment, FragmentEnum.MachineCheckFragment);
                break;
            case R.id.changePassword:
                if (passWord.getText().toString().isEmpty() || passWord.getText().length() < 6) {
                    Toast.makeText(getActivity(), "密码不能为空或不能短于6位", Toast.LENGTH_LONG).show();
                    return;
                }
                SharedPreferencesManager.getInstance(getActivity()).setLoginPassword(passWord.getText().toString());
                Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_LONG).show();
                //
                break;

            case R.id.search_trade:
                homeActivity.replaceFragment(FragmentEnum.ConfigFragment, FragmentEnum.TradeFragment);
                break;
            case R.id.machineName:
              /*  SetFloatButton.getInstance(getActivity()).showFlowButton();
                goSystemSetting();*/
                break;
            case R.id.toSystem:
                SetFloatButton.getInstance(getActivity()).showFlowButton();
                goSystemSetting();
                break;
        }
    }

    /***
     * 进入系统设置界面
     */
    private void goSystemSetting() {

        try {
            getActivity().sendBroadcast(new Intent("com.allwinner.show_nav_bar"));//显示系统导航栏
            Intent it1 = new Intent(Settings.ACTION_SETTINGS);
            startActivity(it1);
        } catch (Exception e) {

            MyLog.d("configFragment", "error occured opening system setting");
        }
    }

    @Override
    public MaterialSql getSql() {
        return sql;
    }

    @Override
    public Context getcontext() {
        return getActivity();
    }

    @Override
    public String getBunkersID() {
        return String.valueOf(BunkersID);
    }

    @Override
    public void notifySetDataChange(MaterialSql sql) {

        cupStock.setText(SharedPreferencesManager.getInstance(getActivity()).getCupNum() + "杯");
        waterStock.setText(SharedPreferencesManager.getInstance(getActivity()).getWaterCount() + "ml");
        addCupTime.setText(SharedPreferencesManager.getInstance(getActivity()).getAddCupTime());
        addWaterTime.setText(SharedPreferencesManager.getInstance(getActivity()).getAddWaterTime());
    }

    @Override
    public List<CommitMaterialObject> getList() {

        return sql.getCommitMaterialObjectList();
    }

    @Override
    public void ShowResult(String result) {
        Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
    }

    ProgressDialog dialog;

    @Override
    public void ShowLoading() {
        dialog = ProgressDialog.show(getActivity(), null, "正在提交原料中,请稍等", false, false);
        dialog.show();
    }

    @Override
    public void HideLoading() {
        dialog.dismiss();
    }

    @Override
    public void ChangePage() {
        homeActivity.replaceFragment(FragmentEnum.ConfigFragment, FragmentEnum.MachineCheckFragment);
    }

    @Override
    public void showLoad(String downLoadUrl) {//开始下载软件
        updateAppManager.cheakUpdateInfo(downLoadUrl);
    }

    @Override
    public void showErroResult(String err) {//检测更新出错时显示错误信息
        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getLocationVersion() {
        return getVersion();
    }


    public String getVersion() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void stateArrived(final MachineState machineState) {
        if (machineState != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    switch (machineState.getMajorState().getStateCode()) {

                        case 0:
                            errCode.setText("异常状态:无异常");
                            break;
                        case 0x0a:
                            errCode.setText("异常状态:" + DataSwitcher.byte2Hex(machineState.getMajorState().getLowErr_byte()));
                            break;
                        default:
                            errCode.setText("异常状态:" + DataSwitcher.byte2Hex(machineState.getMajorState().getLowErr_byte()));
                            break;
                    }

                    runState.setText("运行状态:" + DataSwitcher.byte2Hex(machineState.getMajorState().getState_byte()));
                    netWorkState.setText("网络状态:" + MachineConfig.getNetworkType());
                    if (machineState.isCupShelfRightPlace()) {
                        cupShelfState.setText("杯架状态:正常");
                    } else {
                        cupShelfState.setText("杯架状态:杯架未初始化");
                    }

                    if (machineState.hasCup()) {
                        CupNum.setText("仓杯余量:有杯");
                    } else {
                        CupNum.setText("仓杯余量:无杯");
                    }
                    //   machineState.is
                    boilerTemperature.setText("锅炉温度:" + machineState.getPotTemp());
                    boilerPressure.setText("锅炉压力:" + machineState.getPotPressure());
                    if (machineState.isFrontDoorOpen()) {
                        doorState.setText("大门状态:开启");
                    } else {
                        doorState.setText("大门状态:关闭");
                    }

                    if (machineState.isLittleDoorOpen()) {
                        doorState.setText("取杯门状态:开启");
                    } else {
                        doorState.setText("取杯门状态:关闭");
                    }
                }
            });
        }
    }

    @Override
    public Context getThis() {
        return getActivity();
    }

    @Override
    public void showResultAndUpdateView() {
        Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_LONG).show();
        CupNum.setText(SharedPreferencesManager.getInstance(getActivity()).getCupNum() + "");
        cupStock.setText(SharedPreferencesManager.getInstance(getActivity()).getCupNum() + "个");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.isOutCupAutoClear:
                if (isChecked) {
                    SharedPreferencesManager.getInstance(getActivity()).setOutCupAutoClear(true);
                } else {
                    SharedPreferencesManager.getInstance(getActivity()).setOutCupAutoClear(false);
                }
                break;
            case R.id.onClear:
                if (isChecked) {
                    SharedPreferencesManager.getInstance(getActivity()).setIsOnClear(true);
                } else {
                    SharedPreferencesManager.getInstance(getActivity()).setIsOnClear(false);
                }
                break;
        }
    }
}

