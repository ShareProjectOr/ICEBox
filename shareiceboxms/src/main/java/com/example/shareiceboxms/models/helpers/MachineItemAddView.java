package com.example.shareiceboxms.models.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.MachineStockProductAdapter;
import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/12/13.
 */

public class MachineItemAddView {
    private Context context;
    private View stateControlView;
    private View teleCOntrolView;
    private View stockProductView;
    private StateControlHolder stateControlHolder;
    private TeleControlHolder teleControlHolder;
    private StockProductsHolder stockProductsHolder;
    private MachineItemAddViewHelper machineItemAddViewHelper;

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
    }

    /*
    *更新状态控制的值
    * */
    public void updateStateControlUi(ItemMachine itemMachine) {
        stateControlHolder.runState.setText(Constants.MachineRunState[itemMachine.faultState]);
        stateControlHolder.netState.setText(Constants.MachineOnLineState[itemMachine.networkState]);
        stateControlHolder.doorState.setText(Constants.MachineDoorSate[itemMachine.doorState]);
        stateControlHolder.lockState.setText(Constants.MachineLockState[itemMachine.lockState]);
        stateControlHolder.lightState.setText(Constants.MachineLightState[itemMachine.lightState]);
        stateControlHolder.fanState.setText(Constants.MachineFanState[itemMachine.blowerState]);
        stateControlHolder.refrigeratorState.setText(Constants.MachineRefrigeratorState[itemMachine.refrigeratorState]);
        stateControlHolder.tempState.setText(itemMachine.machineTemperature);
        stateControlHolder.appVersion.setText(itemMachine.clientVersion);
        stateControlHolder.driverVersion.setText(itemMachine.driverVersion);
        stateControlHolder.updateTime.setText(itemMachine.updateTime);
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

    /*
    *更新状态远程控制的值
    * */
    public void updateTeleControlUi(ItemMachine itemMachine) {
        teleControlHolder.targetTemp.setText(itemMachine.targetTemperature);
        teleControlHolder.offsetTemp.setText(itemMachine.deviationTemperature);
        itemMachine.targetTemperature = "10℃";
        itemMachine.deviationTemperature = "12℃";
        int tartgetTemp = Integer.valueOf(itemMachine.targetTemperature.replace("℃", "").trim());
        int offsetTemp = Integer.valueOf(itemMachine.deviationTemperature.replace("℃", "").trim());
        Log.d("--updateTeleControlUi--", "tartgetTemp===" + tartgetTemp);
        teleControlHolder.tempSeekbar.setProgress(tartgetTemp);
        teleControlHolder.offSetTempSeekbar.setProgress(offsetTemp);
    }


    public void addStockProductView(LinearLayout parentView, ItemMachine itemMachine, ScrollView scrollView) {
        if (stockProductView == null || teleControlHolder == null) {
            stockProductView = LayoutInflater.from(context).inflate(R.layout.machine_detail_prod_list, null, false);
            stockProductsHolder = new StockProductsHolder(stockProductView, itemMachine, scrollView);
            machineItemAddViewHelper.getDatas(RequestParamsContants.getInstance().getMachineStockProductParams());
        }
        //先添加View
        if (parentView.getChildAt(0) != stockProductView) {
            parentView.removeAllViews();
            parentView.addView(stockProductView);
        }
        //向View添加值
    }

    public void refreshStockProduct(boolean isRefresh) {
        if (isRefresh) {
            if (stockProductsHolder != null && stockProductsHolder.itemProducts != null) {
                stockProductsHolder.itemProducts.clear();
                stockProductsHolder.isLoading = false;
                machineItemAddViewHelper.getDatas(RequestParamsContants.getInstance().getMachineStockProductParams());
            }
        }

    }

    /*
      *更新库存商品的值
      * */
    public void updateStockProductUi() {

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
            restart.setOnClickListener(this);
            shutDown.setOnClickListener(this);
            check.setOnClickListener(this);
            offSetTempSeekbar.setOnSeekBarChangeListener(this);
            tempSeekbar.setOnSeekBarChangeListener(this);
            tempSeekbar.setMax(Constants.MAX_TARGET_TEMP);
            offSetTempSeekbar.setMax(Constants.MAX_OFFSET_TEMP);
            lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Map<String, Object> params = RequestParamsContants.getInstance().getMachineLightControlParams();
                    params.put("isChecked",isChecked);
                    TeleControlHelper.getInstance().setContext(context);
                    TeleControlHelper.getInstance().getDatas(HttpRequstUrl.MACHINE_LightControl_URL, params);
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
                case R.id.restart:
                    MyDialog restartDialog = new MyDialog(context);
                    restartDialog.showDialog(restartDialog.getMachineTeleControlDialog("确定要重启机器吗？"
                            , HttpRequstUrl.MACHINE_Restart_URL, RequestParamsContants.getInstance().getMachineRestartParams()));
                    break;
                case R.id.shutDown:
                    MyDialog shutDownDialog = new MyDialog(context);
                    shutDownDialog.showDialog(shutDownDialog.getMachineTeleControlDialog("确定要将机器关机吗？"
                            , HttpRequstUrl.MACHINE_Shutdown_URL, RequestParamsContants.getInstance().getMachineShutdownParams()));
                    break;
                case R.id.check:
                    MyDialog checkDialog = new MyDialog(context);
                    checkDialog.showDialog(checkDialog.getMachineTeleControlDialog("确定要将机器关机吗？"
                            , HttpRequstUrl.MACHINE_Check_URL, RequestParamsContants.getInstance().getMachineCheckParams()));
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
        public ScrollView scrollView;
        public ListView stockProductList;
        public MachineStockProductAdapter adapter;
        public List<ItemProduct> itemProducts;
        public boolean isLoading = false;

        public StockProductsHolder(View itemView, final ItemMachine itemMachine, final ScrollView scrollView) {
            itemProducts = new ArrayList<ItemProduct>();
            this.scrollView = scrollView;
            stockProductList = (ListView) itemView.findViewById(R.id.productList);
            adapter = new MachineStockProductAdapter(context, this.itemProducts);
            machineItemAddViewHelper = new MachineItemAddViewHelper(this.itemProducts, adapter, itemMachine);
            machineItemAddViewHelper.setView(stockProductList, scrollView);
            machineItemAddViewHelper.setContext(context);
            stockProductList.setAdapter(adapter);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (scrollView.getChildAt(0).getHeight() == scrollView.getHeight() +
                            scrollView.getScrollY() - scrollView.getPaddingTop() - scrollView.getPaddingBottom()) {
                        if (!isLoading) {
                            isLoading = true;
                        } else {
                            return false;
                        }
                        if (machineItemAddViewHelper.getCurPage() < machineItemAddViewHelper.getTotalPage()) {
                            Map<String, Object> params = RequestParamsContants.getInstance().getMachineStockProductParams();
                            params.put("p", machineItemAddViewHelper.getCurPage() + 1);
                            machineItemAddViewHelper.getDatas(params);
                            adapter.notifyDataSetChanged();
                            ListViewForScrollView.setListViewHeightBasedOnChildren(stockProductList, scrollView);
                        } else {
                            Toast.makeText(context, "偷偷告诉你,数据已经全部加载...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
                }
            });
        }
    }
}
