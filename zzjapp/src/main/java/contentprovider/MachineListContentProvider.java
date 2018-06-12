package contentprovider;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ViewUtils.DiaLogUtil;
import httputil.HttpRequest;
import otherutis.Tip;


/**
 * Created by Administrator on 2017/7/20.
 */

public class MachineListContentProvider {
    private List<Item> DataSet = new ArrayList<>();
    private Activity mActivity;
    private BaseAdapter mAdapter;
    private int total = 0;
    private int initAccountPage = 0;
    private ImageView tips;
    private boolean isallnull = false;

    public MachineListContentProvider(Activity activity, BaseAdapter mAdapter) {
        mActivity = activity;
        this.mAdapter = mAdapter;
    }

    public void sentview(ImageView view) {
        tips = view;
    }

    public void getData(final String url, final Map<String, String> body, final boolean refresh, final int page) {
        final Dialog dialog = DiaLogUtil.createLoadingDialog(mActivity, mActivity.getString(R.string.shujujiazaizhong));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            String error = "";

            @Override
            protected Boolean doInBackground(Void... params) {
                try {

                    JSONObject object = new JSONObject(HttpRequest.postString(
                            url, body));
                    Log.e("XXXXXX", object.toString());
                    if (refresh) {
                        DataSet.clear();
                    }

                    if (!object.getString("err").equals("")) {
                        error = object.getString("err");
                        return false;
                    }
                    JSONObject d = object.getJSONObject("d");
                    total = Integer.parseInt(d.getString("t"));
                    JSONArray L = d.getJSONArray("l");
                    System.out.println("返回结果" + L.toString());
                    for (int i = 0; i < L.length(); i++) {
                        JSONObject itemobject = (JSONObject) L.opt(i);
                        Item item = new Item();
                        item.setMachineCode(itemobject.getString("machineCode"));
                        item.setMachineName(itemobject.getString("machineName"));
                        item.setProductTime(itemobject.getString("productTime"));
                        item.setFactoryTime(itemobject.getString("factoryTime"));
                        item.setActivatedTime(itemobject.getString("activatedTime"));
                        item.setMachineAddr(itemobject.getString("machineAddress"));
                        item.setActivatedType(itemobject.getString("activatedType"));
                        item.setCfg_paperMachine(itemobject.getString("cfgPaperMachine"));
                        item.setCfg_coinMachine(itemobject.getString("cfgCoinMachine"));
                        item.setCfg_advertisingScreen(itemobject.getString("cfgAdvertisingScreen"));
                        item.setCfg_cardReader(itemobject.getString("cfgCardReader"));
                        item.setCfg_docoinMachine(itemobject.getString("cfgDocoinMachine"));
                        item.setCfg_MISPos(itemobject.getString("cfgMisPos"));
                        item.setCfg_driver(itemobject.getString("cfgDriver"));
                        item.setCfg_sellSoftware(itemobject.getString("cfgSellSoftware"));
                        item.setCfg_mediaSortware(itemobject.getString("cfgMediaSoftware"));
                        item.setState_exceptionType(itemobject.getString("stateExceptionType"));
                        item.setState_longLat();
                        item.setState_orangeNum(itemobject.getString("stateOrangeNum"));
                        item.setStateCupNum();
                        item.setStateCapNum(itemobject.getString("stateCapNum"));
                        item.setStateMachineType(itemobject.getString("stateMachineType"));
                        item.setStatePaperMachine(itemobject.getString("statePaperMachine"));
                        item.setStateBoundar(itemobject.getString("stateBoundar"));
                        item.setStateBTimeout(itemobject.getString("stateBtimeOut"));
                        item.setStateChangeDevice(itemobject.getString("stateChangeDevice"));
                        item.setStateAdvertisionScreen(itemobject.getString("stateAdvertisingScreen"));
                        item.setStateCarReader(itemobject.getString("stateCardReader"));
                        item.setStateMISPos(itemobject.getString("stateMisPos"));
                        item.setManagerID(itemobject.getString("managerID"));
                        item.setManagerName(itemobject.getString("managerName"));
                        item.setAgentID(itemobject.getString("agentID"));
                        item.setAgentName(itemobject.getString("agentName"));
                        item.setIsOnline(itemobject.getString("stateIsOnline"));
                        if (page == 3) {
                            if (itemobject.getString("stateIsOnline").equals("1")) {
                                DataSet.add(item);
                            }
                        } else {
                            DataSet.add(item);
                        }


                    }

                } catch (JSONException e) {
                    error =mActivity.getString(R.string.fuwuqikaixiaocai_chongshi);
                    return false;
                } catch (IOException e) {
                    error = mActivity.getString(R.string.wangluobugeili);
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                if (DataSet.size() == 0 && error.equals("")) {
                    if (page == 1) {
                        isallnull = true;
                    }
                    if (isallnull) {
                        tips.setVisibility(View.VISIBLE);
                        tips.setImageResource(R.mipmap.all_page_tips);
                    } else {
                        if (page == 2) {
                            tips.setVisibility(View.VISIBLE);
                            tips.setImageResource(R.mipmap.ex_page_tips);
                        } else if (page == 3) {
                            tips.setVisibility(View.VISIBLE);
                            tips.setImageResource(R.mipmap.normal_page_tips);
                        }
                    }
                } else {
                    tips.setVisibility(View.GONE);
                }
                if (!aBoolean) {
                    Toast.makeText(mActivity, error, Toast.LENGTH_LONG).show();
                    tips.setVisibility(View.GONE);
                    // new AlertView("提示", error, null, new String[]{"确定"}, null, mActivity, AlertView.Style.Alert, null).setCancelable(true).show();
                }
                mAdapter.notifyDataSetChanged();

            }
        }.execute();
    }

    public int GetDataSetSize() {
        return DataSet.size();
    }

    public int GetMaxPageAccount() {
        if (total != 0) {

            if (initAccountPage != 0 && GetDataSetSize() != 0) {
                return initAccountPage;
            } else if (GetDataSetSize() != 0) {
                initAccountPage = total % GetDataSetSize() > 0 ? total
                        / GetDataSetSize() + 1 : total / GetDataSetSize();
                return initAccountPage;
            } else {
                return 1;
            }
        } else {
            return 1;
        }

    }

    public Item getItem(int position) {
        return DataSet.get(position);
    }

    public void remove(int position) {
        DataSet.remove(position);
    }

    public class Item {
        String machineCode;//机器码
        String machineName;//机器名称
        String productTime; //生产时间
        String factoryTime; //出厂时间
        String activatedTime;//激活时间
        String machineAddr; //放置位置
        String activatedType;//激活状态（0:激活，1：未激活，2：报
        String cfg_paperMachine;//纸币机（0：有，1：没有）
        String cfg_coinMachine;//硬币机（0：有，1：没有）
        String cfg_advertisingScreen;//广告屏（0：有，1：没有）
        String cfg_cardReader;//IC卡读卡器（0：有，1：没有）
        String cfg_docoinMachine;//找零器（0：有，1：没有）
        String cfg_MISPos;//刷卡器（0：有，1：没有）
        String cfg_driver;//驱动版本
        String cfg_sellSoftware;//售卖软件版本
        String cfg_mediaSortware;//媒体软件版本
        String state_exceptionType;//异常标志（0表示数据或状态正常1表示数据或状态有异常）
        String IsOnline;

        public String getIsOnline() {
            return IsOnline;
        }

        void setIsOnline(String isOnline) {
            IsOnline = isOnline;
        }

        private String state_orangeNum;

        private String stateMachineType;

        private String statePaperMachine;

        private String stateBoundar;

        private String stateBTimeout;

        private String stateChangeDevice;

        private String stateAdvertisionScreen;

        private String stateCarReader;

        private String stateMISPos;

        private String managerID;

        private String managerName;

        private String agentID;

        private String agentName;

        public String getMachineCode() {
            return machineCode;
        }

        public void setMachineCode(String machineCode) {
            this.machineCode = machineCode;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        void setProductTime(String productTime) {
            this.productTime = productTime;
        }

        public void setFactoryTime(String factoryTime) {
            this.factoryTime = factoryTime;
        }

        void setActivatedTime(String activatedTime) {
            this.activatedTime = activatedTime;
        }

        void setMachineAddr(String machineAddr) {
            this.machineAddr = machineAddr;
        }

        public String getActivatedType() {
            return activatedType;
        }

        public void setActivatedType(String activatedType) {
            this.activatedType = activatedType;
        }

        void setCfg_paperMachine(String cfg_paperMachine) {
            this.cfg_paperMachine = cfg_paperMachine;
        }

        void setCfg_coinMachine(String cfg_coinMachine) {
            this.cfg_coinMachine = cfg_coinMachine;
        }

        void setCfg_advertisingScreen(String cfg_advertisingScreen) {
            this.cfg_advertisingScreen = cfg_advertisingScreen;
        }

        void setCfg_cardReader(String cfg_cardReader) {
            this.cfg_cardReader = cfg_cardReader;
        }

        void setCfg_docoinMachine(String cfg_docoinMachine) {
            this.cfg_docoinMachine = cfg_docoinMachine;
        }

        void setCfg_MISPos(String cfg_MISPos) {
            this.cfg_MISPos = cfg_MISPos;
        }

        void setCfg_driver(String cfg_driver) {
            this.cfg_driver = cfg_driver;
        }

        void setCfg_sellSoftware(String cfg_sellSoftware) {
            this.cfg_sellSoftware = cfg_sellSoftware;
        }

        void setCfg_mediaSortware(String cfg_mediaSortware) {
            this.cfg_mediaSortware = cfg_mediaSortware;
        }

        void setState_exceptionType(String state_exceptionType) {
            this.state_exceptionType = state_exceptionType;
        }

        void setState_longLat() {
        }

        public String getState_orangeNum() {
            return state_orangeNum;
        }

        void setState_orangeNum(String state_orangeNum) {
            this.state_orangeNum = state_orangeNum;
        }

        void setStateCupNum() {
        }

        void setStateCapNum(String stateCapNum) {
        }

        public String getStateMachineType() {
            return stateMachineType;
        }

        public void setStateMachineType(String stateMachineType) {
            this.stateMachineType = stateMachineType;
        }

        public String getStatePaperMachine() {
            return statePaperMachine;
        }

        public void setStatePaperMachine(String statePaperMachine) {
            this.statePaperMachine = statePaperMachine;
        }

        public String getStateBoundar() {
            return stateBoundar;
        }

        public void setStateBoundar(String stateBoundar) {
            this.stateBoundar = stateBoundar;
        }

        public String getStateBTimeout() {
            return stateBTimeout;
        }

        public void setStateBTimeout(String stateBTimeout) {
            this.stateBTimeout = stateBTimeout;
        }

        public String getStateChangeDevice() {
            return stateChangeDevice;
        }

        public void setStateChangeDevice(String stateChangeDevice) {
            this.stateChangeDevice = stateChangeDevice;
        }

        public String getStateAdvertisionScreen() {
            return stateAdvertisionScreen;
        }

        public void setStateAdvertisionScreen(String stateAdvertisionScreen) {
            this.stateAdvertisionScreen = stateAdvertisionScreen;
        }

        public String getStateCarReader() {
            return stateCarReader;
        }

        public void setStateCarReader(String stateCarReader) {
            this.stateCarReader = stateCarReader;
        }

        public String getStateMISPos() {
            return stateMISPos;
        }

        public void setStateMISPos(String stateMISPos) {
            this.stateMISPos = stateMISPos;
        }

        public String getManagerID() {
            return managerID;
        }

        public void setManagerID(String managerID) {
            this.managerID = managerID;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }

        public String getAgentID() {
            return agentID;
        }

        public void setAgentID(String agentID) {
            this.agentID = agentID;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }
    }
}
