package contentprovider;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.bigkoo.alertview.AlertView;
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
import otherutils.Tip;


public class TradeListContentProvider {
    private BaseAdapter mAdapter;
    private Activity mActivty;
    private List<Item> DataSet = new ArrayList<>();
    private int total = 0;
    private int initAccountPage = 0;

    public TradeListContentProvider(Activity mActivty, BaseAdapter mAdapter) {
        this.mActivty = mActivty;
        this.mAdapter = mAdapter;
    }

    public void getData(final String url, final Map<String, String> body, final boolean refresh) {
        final Dialog dialog = DiaLogUtil.createLoadingDialog(mActivty, mActivty.getString(R.string.shujujiazaizhong));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            String error;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    JSONObject object = new JSONObject(HttpRequest.postString(
                            url, body));
                    if (refresh) {
                        DataSet.clear();
                    }
                    JSONObject d = object.getJSONObject("d");
                    error = object.getString("err");
                    if (!error.equals("")) {
                        return false;
                    }
                    total = Integer.parseInt(d.getString("t"));
                    JSONArray array = d.getJSONArray("l");
                    Log.e("订单列表",d.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject itemobject = (JSONObject) array.opt(i);
                        Item item = new Item();
                        item.setTradeCode(itemobject.getString("tradeCode"));
                        item.setMachineCode(itemobject.getString("machineCode"));
                        item.setCupNum(itemobject.getString("cupNum"));
                        item.setOrangeNum(itemobject.getString("orangeNum"));
                        item.setStartTime(itemobject.getString("payTime"));
                        item.setTradeMoney(itemobject.getString("tradeMoney"));
                        item.setMachineName(itemobject.getString("machineName"));
                        switch (itemobject.getString("orderStatus")) {
                            case "1":
                                item.setOrderStatus(mActivty.getString(R.string.wancheng));
                                break;
                            case "0":
                                item.setOrderStatus(mActivty.getString(R.string.weiwancheng));
                                break;
                            case "2":
                                item.setOrderStatus(mActivty.getString(R.string.dingdanshibai));
                                break;
                        }
                        switch (itemobject.getString("paymentMode")) {
                            case "1":
                                item.setPaymentMode(mActivty.getString(R.string.weixin));
                                break;
                            case "0":
                                item.setPaymentMode(mActivty.getString(R.string.xianjing));
                                break;
                            case "2":
                                item.setPaymentMode(mActivty.getString(R.string.zhifubao));
                                break;
                        }

                       // item.setIsRefund(itemobject.getString("isRefund"));
                        item.setRefundMoney(itemobject.getString("refundMoney"));
                     //   item.setRemark(itemobject.getString("remark"));
                        item.setSuccessCup(itemobject.getString("successCup"));
                        item.setMachineAddress(itemobject.getString("machineAddress"));
                        DataSet.add(item);
                    }
                } catch (IOException e1) {
                    error = mActivty.getString(R.string.wangluocuowu);
                    return false;

                } catch (JSONException e1) {
                    error = mActivty.getString(R.string.fuwuqikaixiaocai_chongshi);
                    Log.e("sss", e1 + "");
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                if (DataSet.size() == 0 && error.equals("")) {
                    new AlertView(mActivty.getString(R.string.tishi), mActivty.getString(R.string.zanwushuju), null, new String[]{mActivty.getString(R.string.queding)}, null, mActivty, AlertView.Style.Alert, null).setCancelable(true).show();
                }
                if (!aBoolean) {
                    new AlertView(mActivty.getString(R.string.tishi), error, null, new String[]{mActivty.getString(R.string.queding)}, null, mActivty, AlertView.Style.Alert, null).setCancelable(true).show();
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

    public class Item {
        String tradeCode;
        String machineCode;
        String cupNum;
        String orangeNum;
        String startTime;
        String endTime;
        String tradeMoney;
        String orderStatus;
        String paymentMode;
        String isRefund;
        String refundMoney;
        String remark;
        String successCup;
        String machineName;
        String machineAddress;

        public String getMachineAddress() {
            return machineAddress;
        }

        public void setMachineAddress(String machineAddress) {
            this.machineAddress = machineAddress;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public String getSuccessCup() {
            return successCup;
        }

        void setSuccessCup(String successCup) {
            this.successCup = successCup;
        }

        public String getTradeCode() {
            return tradeCode;
        }

        void setTradeCode(String tradeCode) {
            this.tradeCode = tradeCode;
        }

        public String getMachineCode() {
            return machineCode;
        }

        public void setMachineCode(String machineCode) {
            this.machineCode = machineCode;
        }

        public String getCupNum() {
            return cupNum;
        }

        public void setCupNum(String cupNum) {
            this.cupNum = cupNum;
        }

        public String getOrangeNum() {
            return orangeNum;
        }

        public void setOrangeNum(String orangeNum) {
            this.orangeNum = orangeNum;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getTradeMoney() {
            return tradeMoney;
        }

        public void setTradeMoney(String tradeMoney) {
            this.tradeMoney = tradeMoney;
        }

        void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getIsRefund() {
            return isRefund;
        }

        public void setIsRefund(String isRefund) {
            this.isRefund = isRefund;
        }

        public String getRefundMoney() {
            return refundMoney;
        }

        public void setRefundMoney(String refundMoney) {
            this.refundMoney = refundMoney;
        }

        void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
