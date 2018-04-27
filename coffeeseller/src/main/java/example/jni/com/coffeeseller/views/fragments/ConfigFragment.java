package example.jni.com.coffeeseller.views.fragments;

import android.content.Context;
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
import example.jni.com.coffeeseller.model.adapters.MaterialRecycleListAdapter;
import example.jni.com.coffeeseller.presenter.AddMaterialPresenter;
import example.jni.com.coffeeseller.presenter.CommitMaterialPresenter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.viewinterface.IAddMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.ICommitMaterialView;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ConfigFragment extends BasicFragment implements IAddMaterialView, ICommitMaterialView {
    private View mView;
    private Button toDebug, materialCommit;
    private HomeActivity homeActivity;
    private TextView back;
    private RecyclerView materialList;
    private MaterialRecycleListAdapter mAdapter;
    private TextView cupStock, waterStock;
    private TextView addCupTime, addWaterTime;
    private TextView addCup, addWater;
    private MaterialSql sql;
    private int BunkersID;
    private AddMaterialPresenter mPresenter;
    private CommitMaterialPresenter materialPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.config_fragment_layout));
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
        toDebug = (Button) mView.findViewById(R.id.debug_machine);
        back = (TextView) mView.findViewById(R.id.backToCheck);
        materialList = (RecyclerView) mView.findViewById(R.id.material_list);
        mAdapter = new MaterialRecycleListAdapter(getActivity());
        materialCommit = (Button) mView.findViewById(R.id.material_commit);
        cupStock = (TextView) mView.findViewById(R.id.cup_num);
        waterStock = (TextView) mView.findViewById(R.id.water_count);
        materialPresenter = new CommitMaterialPresenter(this);
        addCupTime = (TextView) mView.findViewById(R.id.add_cup_time);
        addWaterTime = (TextView) mView.findViewById(R.id.add_water_time);

        addCup = (TextView) mView.findViewById(R.id.add_cup);
        addWater = (TextView) mView.findViewById(R.id.add_water);
        materialList.setAdapter(mAdapter);
        mPresenter = new AddMaterialPresenter(this);
        materialList.setLayoutManager(new LinearLayoutManager(getActivity()));
        materialList.setHasFixedSize(true);
        back.setOnClickListener(this);
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
}

