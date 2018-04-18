package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.ContainerType;
import cof.ac.inter.WaterType;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.model.listeners.IAddProcess;
import example.jni.com.coffeeseller.model.listeners.OnAddProcessCallBackListener;

/**
 * Created by Administrator on 2018/4/16.
 */

public class AddProcess implements IAddProcess, View.OnClickListener {
    private RadioGroup mChooseBunkersGroup;
    private RadioButton mBunkersBean;
    private RadioButton mBunkersHotWater;
    private RadioButton mBunkers1;
    private RadioButton mBunkers2;
    private RadioButton mBunkers3;
    private RadioButton mBunkers4;
    private RadioButton mBunkers5;
    private RadioButton mBunkers6;
    private EditText mWater_interval;
    private EditText mWater_capacity;
    private EditText mMaterial_time;
    private EditText mRotate_speed;
    private EditText mStir_speed;
    private RadioGroup mWaterTypeGroup;
    private RadioButton mHot;
    private RadioButton mCold;
    private Button ok, cancel;
    private ContainerConfig containerConfig;
    private OnAddProcessCallBackListener onAddProcessCallBackListener;
    private List<ContainerConfig> list;
    private Context mContext;
    private AlertDialog dialog;

    @Override
    public void AddProcess(Context mContext, List<ContainerConfig> list, OnAddProcessCallBackListener onAddProcessCallBackListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        this.mContext = mContext;
        this.onAddProcessCallBackListener = onAddProcessCallBackListener;
        this.list = list;
        View view = LayoutInflater.from(mContext).inflate(R.layout.addprocess_dialog_layout, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        mChooseBunkersGroup = (RadioGroup) view.findViewById(R.id.chooseBunkersGroup);
        mBunkersBean = (RadioButton) view.findViewById(R.id.bunkersBean);
        mBunkersHotWater = (RadioButton) view.findViewById(R.id.bunkersHotWater);
        mBunkers1 = (RadioButton) view.findViewById(R.id.bunkers1);
        mBunkers2 = (RadioButton) view.findViewById(R.id.bunkers2);
        mBunkers3 = (RadioButton) view.findViewById(R.id.bunkers3);
        mBunkers4 = (RadioButton) view.findViewById(R.id.bunkers4);
        mBunkers5 = (RadioButton) view.findViewById(R.id.bunkers5);
        mBunkers6 = (RadioButton) view.findViewById(R.id.bunkers6);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mWater_interval = (EditText) view.findViewById(R.id.water_interval);
        mWater_capacity = (EditText) view.findViewById(R.id.water_capacity);
        mMaterial_time = (EditText) view.findViewById(R.id.material_time);
        mRotate_speed = (EditText) view.findViewById(R.id.rotate_speed);
        mStir_speed = (EditText) view.findViewById(R.id.stir_speed);
        mWaterTypeGroup = (RadioGroup) view.findViewById(R.id.waterTypeGroup);
        mHot = (RadioButton) view.findViewById(R.id.hot);
        mCold = (RadioButton) view.findViewById(R.id.cold);
        containerConfig = new ContainerConfig();
        containerConfig.setContainer(ContainerType.BEAN_CONTAINER);//默认豆仓
        containerConfig.setWater_type(WaterType.HOT_WATER);//默认热水

        mChooseBunkersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.bunkersBean:
                        containerConfig.setContainer(ContainerType.BEAN_CONTAINER);
                        break;
                    case R.id.bunkersHotWater:
                        containerConfig.setContainer(ContainerType.HOTWATER_CONTAINER);
                        break;
                    case R.id.bunkers1:
                        containerConfig.setContainer(ContainerType.NO_ONE);
                        break;
                    case R.id.bunkers2:
                        containerConfig.setContainer(ContainerType.NO_TOW);
                        break;
                    case R.id.bunkers3:
                        containerConfig.setContainer(ContainerType.NO_THREE);
                        break;
                    case R.id.bunkers4:
                        containerConfig.setContainer(ContainerType.NO_FOUR);
                        break;
                    case R.id.bunkers5:
                        containerConfig.setContainer(ContainerType.NO_FIVE);
                        break;
                    case R.id.bunkers6:
                        containerConfig.setContainer(ContainerType.NO_SIX);
                        break;
                }
            }
        });
        mWaterTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.hot:
                        containerConfig.setWater_type(WaterType.HOT_WATER);
                        break;
                    case R.id.cold:
                        containerConfig.setWater_type(WaterType.COLD_WATER);
                        break;
                }
            }
        });


    }

    @Override
    public void EditProcess(final Context mContext, final List<ContainerConfig> list2, final int position, final OnAddProcessCallBackListener onAddProcessCallBackListener) {
        RadioGroup mChooseBunkersGroup;
        final EditText mWater_interval;
        final EditText mWater_capacity;
        final EditText mMaterial_time;
        final EditText mRotate_speed;
        final EditText mStir_speed;
        RadioGroup mWaterTypeGroup;
        Button ok, cancel;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        this.mContext = mContext;
        View view = LayoutInflater.from(mContext).inflate(R.layout.addprocess_dialog_layout, null);
        builder.setView(view);
       final AlertDialog dialog2 = builder.create();
        dialog2.show();
        final ContainerConfig containerConfig2 = new ContainerConfig();
        mChooseBunkersGroup = (RadioGroup) view.findViewById(R.id.chooseBunkersGroup);
        mBunkersBean = (RadioButton) view.findViewById(R.id.bunkersBean);
        mBunkersHotWater = (RadioButton) view.findViewById(R.id.bunkersHotWater);

        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mWater_interval = (EditText) view.findViewById(R.id.water_interval);
        mWater_capacity = (EditText) view.findViewById(R.id.water_capacity);
        mMaterial_time = (EditText) view.findViewById(R.id.material_time);
        mRotate_speed = (EditText) view.findViewById(R.id.rotate_speed);
        mStir_speed = (EditText) view.findViewById(R.id.stir_speed);
        mWaterTypeGroup = (RadioGroup) view.findViewById(R.id.waterTypeGroup);
        mWater_interval.setText(list2.get(position).getWater_interval() + "");
        mWater_capacity.setText(list2.get(position).getWater_capacity() + "");
        mMaterial_time.setText(list2.get(position).getMaterial_time() + "");
        mRotate_speed.setText(list2.get(position).getRotate_speed() + "");
        mStir_speed.setText(list2.get(position).getStir_speed() + "");
        containerConfig2.setContainer(ContainerType.BEAN_CONTAINER);//默认豆仓
        containerConfig2.setWater_type(WaterType.HOT_WATER);//默认热水
        mChooseBunkersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.bunkersBean:
                        containerConfig2.setContainer(ContainerType.BEAN_CONTAINER);
                        break;
                    case R.id.bunkersHotWater:
                        containerConfig2.setContainer(ContainerType.HOTWATER_CONTAINER);
                        break;
                    case R.id.bunkers1:
                        containerConfig2.setContainer(ContainerType.NO_ONE);
                        break;
                    case R.id.bunkers2:
                        containerConfig2.setContainer(ContainerType.NO_TOW);
                        break;
                    case R.id.bunkers3:
                        containerConfig2.setContainer(ContainerType.NO_THREE);
                        break;
                    case R.id.bunkers4:
                        containerConfig2.setContainer(ContainerType.NO_FOUR);
                        break;
                    case R.id.bunkers5:
                        containerConfig2.setContainer(ContainerType.NO_FIVE);
                        break;
                    case R.id.bunkers6:
                        containerConfig2.setContainer(ContainerType.NO_SIX);
                        break;
                }
            }
        });
        mWaterTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.hot:
                        containerConfig2.setWater_type(WaterType.HOT_WATER);
                        break;
                    case R.id.cold:
                        containerConfig2.setWater_type(WaterType.COLD_WATER);
                        break;
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWater_interval.getText().toString().isEmpty() || mMaterial_time.getText().toString().isEmpty()
                        || mWater_capacity.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "有选项为空", Toast.LENGTH_LONG).show();
                    return;
                }

                containerConfig2.setMaterial_time(Integer.parseInt(mMaterial_time.getText().toString()));
                if (mRotate_speed.getText().toString().isEmpty()) {
                    containerConfig2.setRotate_speed(127);
                } else {
                    containerConfig2.setRotate_speed(Integer.parseInt(mRotate_speed.getText().toString()));
                }
                if (mStir_speed.getText().toString().isEmpty()) {
                    containerConfig2.setStir_speed(127);
                } else {
                    containerConfig2.setStir_speed(Integer.parseInt(mStir_speed.getText().toString()));
                }
                containerConfig2.setWater_capacity(Integer.parseInt(mWater_capacity.getText().toString()));
                containerConfig2.setWater_interval(Integer.parseInt(mWater_interval.getText().toString()));
                onAddProcessCallBackListener.editSuccess(containerConfig2);
                dialog2.dismiss();


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }

    @Override
    public void RemoveProcess(Context mContext, List<ContainerConfig> list2, int position, OnAddProcessCallBackListener addProcessCallBackListener) {
        list2.remove(position);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (list.size() == 0) {
                    if (mWater_capacity.getText().toString().isEmpty() || mMaterial_time.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "有选项为空", Toast.LENGTH_LONG).show();
                        return;
                    }
                    containerConfig.setWater_interval(0);

                    containerConfig.setMaterial_time(Integer.parseInt(mMaterial_time.getText().toString()));
                    if (mRotate_speed.getText().toString().isEmpty()) {
                        containerConfig.setRotate_speed(127);
                    } else {
                        containerConfig.setRotate_speed(Integer.parseInt(mRotate_speed.getText().toString()));
                    }
                    if (mStir_speed.getText().toString().isEmpty()) {
                        containerConfig.setStir_speed(127);
                    } else {
                        containerConfig.setStir_speed(Integer.parseInt(mStir_speed.getText().toString()));
                    }
                    containerConfig.setWater_capacity(Integer.parseInt(mWater_capacity.getText().toString()));
                } else {
                    if (mWater_interval.getText().toString().isEmpty() || mMaterial_time.getText().toString().isEmpty()
                            || mWater_capacity.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "有选项为空", Toast.LENGTH_LONG).show();
                        return;
                    }

                    containerConfig.setMaterial_time(Integer.parseInt(mMaterial_time.getText().toString()));
                    if (mRotate_speed.getText().toString().isEmpty()) {
                        containerConfig.setRotate_speed(127);
                    } else {
                        containerConfig.setRotate_speed(Integer.parseInt(mRotate_speed.getText().toString()));
                    }
                    if (mStir_speed.getText().toString().isEmpty()) {
                        containerConfig.setStir_speed(127);
                    } else {
                        containerConfig.setStir_speed(Integer.parseInt(mStir_speed.getText().toString()));
                    }
                    containerConfig.setWater_capacity(Integer.parseInt(mWater_capacity.getText().toString()));
                    containerConfig.setWater_interval(Integer.parseInt(mWater_interval.getText().toString()));
                }


                if (list.add(containerConfig)) {
                    onAddProcessCallBackListener.AddSuccess();
                    dialog.dismiss();
                } else {
                    Toast.makeText(mContext, "添加失败,未知错误", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

                break;
            case R.id.cancel:
                dialog.dismiss();
                break;

        }
    }
}
