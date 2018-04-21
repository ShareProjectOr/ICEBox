package example.jni.com.coffeeseller.views.fragments;

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

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.model.adapters.MaterialRecycleListAdapter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ConfigFragment extends BasicFragment {
    private View mView;
    private Button toDebug;
    private HomeActivity homeActivity;
    private TextView back;
    private RecyclerView materialList;
    private MaterialRecycleListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.config_fragment_layout));
        initView();
        return mView;
    }


    private void initView() {
        toDebug = (Button) mView.findViewById(R.id.debug_machine);
        back = (TextView) mView.findViewById(R.id.backToCheck);
        materialList = (RecyclerView) mView.findViewById(R.id.material_list);
        mAdapter = new MaterialRecycleListAdapter(getActivity());
        materialList.setAdapter(mAdapter);
        materialList.setLayoutManager(new LinearLayoutManager(getActivity()));
        materialList.setHasFixedSize(true);
        back.setOnClickListener(this);
        homeActivity = HomeActivity.getInstance();
        toDebug.setOnClickListener(this);
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
        }
    }
}

