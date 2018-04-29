package example.jni.com.coffeeseller.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.CommitMaterialObject;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.httputils.UpdateAppManager;
import example.jni.com.coffeeseller.model.adapters.MaterialRecycleListAdapter;
import example.jni.com.coffeeseller.presenter.AddMaterialPresenter;
import example.jni.com.coffeeseller.presenter.CheckVersionPresenter;
import example.jni.com.coffeeseller.presenter.CommitMaterialPresenter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.viewinterface.IAddMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.ICheckVersionView;
import example.jni.com.coffeeseller.views.viewinterface.ICommitMaterialView;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ConfigFragment extends BasicFragment implements IAddMaterialView, ICommitMaterialView, ICheckVersionView {
    private View mView;
    private Button toDebug, materialCommit, checkVersion;
    private HomeActivity homeActivity;
    private TextView back;
    private RecyclerView materialList;
    private MaterialRecycleListAdapter mAdapter;
    private TextView cupStock, waterStock;
    private TextView addCupTime, addWaterTime;
    private TextView addCup, addWater;
    private MaterialSql sql;
    private int BunkersID;
    private CommitMaterialPresenter materialPresenter;
    private UpdateAppManager updateAppManager;
    private CheckVersionPresenter checkVersionPresenter;
    private TextView errCode, runState, netWorkState, CupNum, cupShelfState, boilerTemperature, boilerPressure, doorState, cupDoorState, driverVersion, updateTimeAndVersion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.config_fragment_layout, null);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        cupStock.setText(SharedPreferencesManager.getInstance(getActivity()).getCupNum() + "杯");
        waterStock.setText(SharedPreferencesManager.getInstance(getActivity()).getWaterCount() + "ml");
        addCupTime.setText(SharedPreferencesManager.getInstance(getActivity()).getAddCupTime());
        addWaterTime.setText(SharedPreferencesManager.getInstance(getActivity()).getAddWaterTime());

    }


    private void initView() {
        errCode = (TextView) mView.findViewById(R.id.errCode);
        runState = (TextView) mView.findViewById(R.id.runState);
        netWorkState = (TextView) mView.findViewById(R.id.netWorkState);
        CupNum = (TextView) mView.findViewById(R.id.CupNum);
        updateTimeAndVersion = (TextView) mView.findViewById(R.id.updateTimeAndVersion);
        cupShelfState = (TextView) mView.findViewById(R.id.cupShelfState);
        boilerTemperature = (TextView) mView.findViewById(R.id.boilerTemperature);
        boilerPressure = (TextView) mView.findViewById(R.id.boilerPressure);
        doorState = (TextView) mView.findViewById(R.id.doorState);
        cupDoorState = (TextView) mView.findViewById(R.id.cupDoorState);
        driverVersion = (TextView) mView.findViewById(R.id.driverVersion);
        toDebug = (Button) mView.findViewById(R.id.debug_machine);
        back = (TextView) mView.findViewById(R.id.backToCheck);
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
        checkVersion.setOnClickListener(this);
        homeActivity = HomeActivity.getInstance();
        sql = new MaterialSql(getActivity());
        toDebug.setOnClickListener(this);
        addCup.setOnClickListener(this);
        addWater.setOnClickListener(this);
        materialCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.debug_machine:
                homeActivity.replaceFragment(FragmentEnum.ConfigFragment, FragmentEnum.DebugFragment);
                break;
            case R.id.backToCheck:
                homeActivity.replaceFragment(FragmentEnum.ConfigFragment, FragmentEnum.MachineCheckFragment);
                break;
            case R.id.add_cup:
                BunkersID = 8;
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
}

