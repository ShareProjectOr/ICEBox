package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.MachineStockProductAdapter;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.contants.Constants;

import java.util.List;

/**
 * Created by WH on 2017/12/13.
 */

public class MachineItemAddView {
    private Context context;
    private View stateControlView;
    private View teleCOntrolView;
    private View stockProductView;
    private ListView stockProductList;
    private View listItemView;
    private StateControlHolder stateControlHolder;
    private TeleControlHolder teleControlHolder;
    private StockProductsHolder stockProductsHolder;

    public MachineItemAddView(Context context) {
        this.context = context;
    }

    public void addStateControlView(LinearLayout parentView) {
        if (stateControlView == null || stateControlHolder == null) {
            stateControlView = LayoutInflater.from(context).inflate(R.layout.state_control, null, false);
            stateControlHolder = new StateControlHolder(stateControlView);

        }
        //先添加View
        if (parentView.getChildAt(0) != stateControlView) {
            parentView.removeAllViews();
            parentView.addView(stateControlView);

        }
        //向View添加值
        stateControlHolder.netState.setText("80");
    }

    public void addTeleControlView(LinearLayout parentView) {
        if (teleCOntrolView == null || teleControlHolder == null) {
            teleCOntrolView = LayoutInflater.from(context).inflate(R.layout.tele_control, null, false);
            teleControlHolder = new TeleControlHolder(teleCOntrolView);
        }
        //先添加View
        if (parentView.getChildAt(0) != teleCOntrolView) {
            parentView.removeAllViews();
            parentView.addView(teleCOntrolView);
        }
        //向View添加值
        //   teleControlHolder.targetTemp.setText("80");
    }

    public void addStockProductView(LinearLayout parentView, List<ItemProduct> itemProducts) {
        if (stockProductView == null || teleControlHolder == null) {
            stockProductView = LayoutInflater.from(context).inflate(R.layout.machine_detail_prod_list, null, false);
            stockProductsHolder = new StockProductsHolder(stockProductView, itemProducts);
        }
        //先添加View
        if (parentView.getChildAt(0) != stockProductView) {
            parentView.removeAllViews();
            parentView.addView(stockProductView);
        }
        //向View添加值
    }

    public View getStateControlView() {
        return stateControlView;
    }

    public View getTeleCOntrolView() {
        return teleCOntrolView;
    }

    public View getStockProductView() {
        return stockProductView;
    }

    class StateControlHolder {
        public TextView runState, netState, doorState, lockState, lightState, fanState, refrigeratorState, tempState, appVersion, driverVersion, updateTime;

        public StateControlHolder(View itemView) {
            runState = (TextView) itemView.findViewById(R.id.runState);
            netState = (TextView) itemView.findViewById(R.id.netState);
            doorState = (TextView) itemView.findViewById(R.id.doorState);
            lockState = (TextView) itemView.findViewById(R.id.lockState);
            lightState = (TextView) itemView.findViewById(R.id.lightState);
            fanState = (TextView) itemView.findViewById(R.id.fanState);
            refrigeratorState = (TextView) itemView.findViewById(R.id.refrigeratorState);
            tempState = (TextView) itemView.findViewById(R.id.tempState);
            appVersion = (TextView) itemView.findViewById(R.id.AppVersion);
            driverVersion = (TextView) itemView.findViewById(R.id.driverVersion);
            updateTime = (TextView) itemView.findViewById(R.id.updateTime);

        }
    }

    class TeleControlHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        public TextView targetTemp, offsetTemp;
        public ImageView addTargetTemp, subTargetTemp, subOffsetTemp, addOffsetTemp;
        public SeekBar tempSeekbar, offSetTempSeekbar;
        public RelativeLayout restart, shutDown, check;
        public Switch lockSwitch;

        public TeleControlHolder(View itemView) {
            targetTemp = (TextView) itemView.findViewById(R.id.targetTemp);
            addTargetTemp = (ImageView) itemView.findViewById(R.id.addTargetTemp);
            subTargetTemp = (ImageView) itemView.findViewById(R.id.subTargetTemp);
            tempSeekbar = (SeekBar) itemView.findViewById(R.id.tempSeekbar);
            tempSeekbar.setProgress(Integer.parseInt(targetTemp.getText().toString()));//绑定进度

            offsetTemp = (TextView) itemView.findViewById(R.id.offsetTemp);
            subOffsetTemp = (ImageView) itemView.findViewById(R.id.subOffsetTemp);
            addOffsetTemp = (ImageView) itemView.findViewById(R.id.addOffsetTemp);
            offSetTempSeekbar = (SeekBar) itemView.findViewById(R.id.subTempSeekbar);
            offSetTempSeekbar.setProgress(Integer.parseInt(offsetTemp.getText().toString()));

            restart = (RelativeLayout) itemView.findViewById(R.id.restart);
            shutDown = (RelativeLayout) itemView.findViewById(R.id.shutDown);
            check = (RelativeLayout) itemView.findViewById(R.id.check);
            lockSwitch = (Switch) itemView.findViewById(R.id.lockSwitch);
            addTargetTemp.setOnClickListener(this);
            subTargetTemp.setOnClickListener(this);
            addOffsetTemp.setOnClickListener(this);
            subOffsetTemp.setOnClickListener(this);
            offSetTempSeekbar.setOnSeekBarChangeListener(this);
            tempSeekbar.setOnSeekBarChangeListener(this);
            lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                    } else {

                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            int TargetTemp = Integer.parseInt(targetTemp.getText().toString());
            int OffSetTemp = Integer.parseInt(offsetTemp.getText().toString());
            switch (v.getId()) {
                case R.id.addTargetTemp:


                    if (TargetTemp >= Constants.MAX_TARGET_TEMP) {
                        Toast.makeText(context, "当前已达到最大目标温度,无法再增加", Toast.LENGTH_SHORT).show();
                    } else {
                        TargetTemp++;
                        targetTemp.setText(String.valueOf(TargetTemp));
                    }

                    break;
                case R.id.subTargetTemp:
                    if (TargetTemp <= Constants.MIN_TARGET_TEMP) {
                        Toast.makeText(context, "当前已达到最小目标温度,无法再减少", Toast.LENGTH_SHORT).show();
                    } else {
                        TargetTemp--;
                        targetTemp.setText(String.valueOf(TargetTemp));
                    }

                    break;
                case R.id.addOffsetTemp:
                    if (OffSetTemp >= Constants.MAX_OFFSET_TEMP) {
                        Toast.makeText(context, "当前已达到最大偏差温度,无法再增加", Toast.LENGTH_SHORT).show();
                    } else {
                        OffSetTemp++;
                        offsetTemp.setText(String.valueOf(OffSetTemp));
                    }

                    break;
                case R.id.subOffsetTemp:
                    if (OffSetTemp <= Constants.MIN_OFFSET_TEMP) {
                        Toast.makeText(context, "当前已达到最小偏差温度,无法再减少", Toast.LENGTH_SHORT).show();
                    } else {
                        OffSetTemp--;
                        offsetTemp.setText(String.valueOf(OffSetTemp));
                    }

                    break;
            }
            tempSeekbar.setProgress(Integer.parseInt(targetTemp.getText().toString()));
            offSetTempSeekbar.setProgress(Integer.parseInt(offsetTemp.getText().toString()));
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar == tempSeekbar) {
                targetTemp.setText(String.valueOf(progress));
            } else {
                offsetTemp.setText(String.valueOf(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            if (seekBar == tempSeekbar) {
                //向左滑动的进度小于最低目标温度时的处理
                if (seekBar.getProgress() <= Constants.MIN_TARGET_TEMP) {
                    seekBar.setProgress(Constants.MIN_TARGET_TEMP);
                    targetTemp.setText(String.valueOf(seekBar.getProgress()));
                }
                //向右滑动的进度大于最大目标温度时的处理
                if (seekBar.getProgress() >= Constants.MAX_TARGET_TEMP) {
                    seekBar.setProgress(Constants.MAX_TARGET_TEMP);
                    targetTemp.setText(String.valueOf(seekBar.getProgress()));
                }
            } else {


                //向左滑动的进度小于最低偏差温度时的处理
                if (seekBar.getProgress() <= Constants.MIN_OFFSET_TEMP) {
                    seekBar.setProgress(Constants.MIN_OFFSET_TEMP);
                    offsetTemp.setText(String.valueOf(seekBar.getProgress()));
                }
                //向右滑动的进度大于最大偏差温度时的处理
                if (seekBar.getProgress() >= Constants.MAX_OFFSET_TEMP) {
                    seekBar.setProgress(Constants.MAX_OFFSET_TEMP);
                    offsetTemp.setText(String.valueOf(seekBar.getProgress()));
                }
            }
        }
    }

    class StockProductsHolder {
        public ListView stockProductList;
        public MachineStockProductAdapter adapter;

        public StockProductsHolder(View itemView, List<ItemProduct> itemProducts) {
            stockProductList = (ListView) itemView.findViewById(R.id.productList);
            adapter = new MachineStockProductAdapter(context, itemProducts);
            stockProductList.setAdapter(adapter);
        }
    }
}
