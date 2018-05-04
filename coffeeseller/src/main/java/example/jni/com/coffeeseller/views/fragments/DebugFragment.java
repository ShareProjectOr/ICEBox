package example.jni.com.coffeeseller.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.factory.FragmentFactory;
import example.jni.com.coffeeseller.model.AddProcess;
import example.jni.com.coffeeseller.model.adapters.ProcessListViewAdapter;
import example.jni.com.coffeeseller.model.listeners.IAddProcess;
import example.jni.com.coffeeseller.model.listeners.OnAddProcessCallBackListener;
import example.jni.com.coffeeseller.presenter.DropMaterialPresenter;
import example.jni.com.coffeeseller.presenter.SaveCoffeePresenter;
import example.jni.com.coffeeseller.presenter.ShowPopListWindowPresenter;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.viewinterface.IDebugDropMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.ISaveCoffeeView;
import example.jni.com.coffeeseller.views.viewinterface.IShowPopListWindowView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class DebugFragment extends BasicFragment implements IDebugDropMaterialView, IShowPopListWindowView, ISaveCoffeeView, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private View mView;
    private EditText mChooseBunkers;
    private EditText dropAccunt;
    private Button mStart_drop;
    private Button mAdd_process;
    private Button Save, Make;
    private Button getDropSpeed;
    private ListView mProcessList;
    private View anchorView;
    private List<String> popList = new ArrayList<>();
    private int Position = 0;
    private ShowPopListWindowPresenter mShowPopListWindowPresenter;
    private DropMaterialPresenter mDropMaterialPresenter;
    private ProcessListViewAdapter mAdapter;
    private IAddProcess iAddProcess;
    public List<ContainerConfig> processList = new ArrayList<>();
    private SaveCoffeePresenter presenter;
    private boolean iscanDrop = true;
    private TextView back;
    private HomeActivity homeActivity;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.debug_fragment_layout,null,false);
        initview();
        initdata();

        return mView;
    }

    private void initdata() {

    }

    private void initview() {
        mChooseBunkers = (EditText) mView.findViewById(R.id.ChooseBunkers);
        dropAccunt = (EditText) mView.findViewById(R.id.dropAcount);
        mStart_drop = (Button) mView.findViewById(R.id.start_drop);
        mAdd_process = (Button) mView.findViewById(R.id.add_process);
        getDropSpeed = (Button) mView.findViewById(R.id.getDropSpeed);
        back = (TextView) mView.findViewById(R.id.backToConfig);
        homeActivity = HomeActivity.getInstance();
        back.setOnClickListener(this);
        getDropSpeed.setOnClickListener(this);
        Make = (Button) mView.findViewById(R.id.make);
        mProcessList = (ListView) mView.findViewById(R.id.processList);
        Save = (Button) mView.findViewById(R.id.save);
        presenter = new SaveCoffeePresenter(this);
        Save.setOnClickListener(this);
        mAdapter = new ProcessListViewAdapter(getActivity(), this);
        iAddProcess = new AddProcess();
        mProcessList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mShowPopListWindowPresenter = new ShowPopListWindowPresenter(this);
        mDropMaterialPresenter = new DropMaterialPresenter(this);
        mChooseBunkers.setOnClickListener(this);
        Make.setOnClickListener(this);
        mStart_drop.setOnClickListener(this);
        mAdd_process.setOnClickListener(this);
        mProcessList.setOnItemClickListener(this);
        mProcessList.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ChooseBunkers:
                anchorView = mChooseBunkers;
                mShowPopListWindowPresenter.ShowWindow();
                break;
            case R.id.start_drop:
                if (iscanDrop) {
                    iscanDrop = false;
                   // mDropMaterialPresenter.startDrop();

                } else {
                    Toast.makeText(getActivity(), "当前正在执行落粉中,别着急", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.add_process:
                iAddProcess.AddProcess(getActivity(), processList, new OnAddProcessCallBackListener() {
                    @Override
                    public void AddSuccess() {
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void editSuccess(ContainerConfig config) {

                    }


                });
                break;
            case R.id.save:
                presenter.Save();
                break;
            case R.id.make:
                presenter.make();
                break;
            case R.id.getDropSpeed:
                if (dropAccunt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "你不输入真实落料量,你要我咋给你计算???", Toast.LENGTH_LONG).show();
                    return;
                }
                int realdropAcount = Integer.parseInt(dropAccunt.getText().toString());
                int Speed = realdropAcount / 5;
                new AlertDialog.Builder(getActivity()).setMessage("单位落料量:" + Speed + "\t(0.1g/s)").setPositiveButton("我记住了", null).create().show();
                break;
            case R.id.backToConfig:
                homeActivity.replaceFragment(FragmentEnum.DebugFragment, FragmentEnum.ConfigFragment);
                break;
        }
    }

/*    @Override
    public int getContainerID() {
        Log.d("Debug","点击位置:"+Position);
        return Constance.bunkersID[Position];
    }

    @Override
    public void ShowDropResult(String result) {
        Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
    }

    @Override
    public int getTime() {
        return 0;
    }

    @Override
    public void setButtonSate(boolean b) {
        iscanDrop = true;
    }*/


    @Override
    public List<String> getPopList() {
        if (popList.size() != 0) {
            popList.clear();
        }
        Collections.addAll(popList, Constance.bunkersName);
        return popList;
    }

    @Override
    public View getAnchorView() {
        return anchorView;
    }

    @Override
    public void setText(int positon, View view) {
        Position = positon;
        EditText edt = (EditText) view;
        edt.setText(popList.get(positon));
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public List<ContainerConfig> getList() {
        return processList;
    }

    @Override
    public Context getcontext() {
        return getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        iAddProcess.EditProcess(getActivity(), processList, position, new OnAddProcessCallBackListener() {
            @Override
            public void AddSuccess() {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void editSuccess(ContainerConfig config) {
                processList.remove(position);
                processList.add(position, config);
                mAdapter.notifyDataSetChanged();
            }


        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(getActivity()).setTitle("您确定要删除此步吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                processList.remove(position);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
        return true;
    }

    @Override
    public void ShowDropResult(String result, String ContainerID) {

    }

    @Override
    public void ShowEditDialog(String ContainerID) {

    }
}
