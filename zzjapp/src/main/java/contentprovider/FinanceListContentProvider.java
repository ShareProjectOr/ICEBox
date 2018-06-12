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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ViewUtils.DiaLogUtil;
import httputil.HttpRequest;
import otherutils.Tip;

public class FinanceListContentProvider {
    private List<Item> DataSet = new ArrayList<>();
    private Activity mActivity;
    private BaseAdapter mAdapter;
    private int total = 0;
    private int initAccountPage = 0;

    public FinanceListContentProvider(Activity mActivity, BaseAdapter mAdapter) {
        this.mActivity = mActivity;
        this.mAdapter = mAdapter;
    }

    public void getdata(final String url, final Map<String, String> body, final boolean refresh) {

//        final Dialog dialog = DiaLogUtil.createLoadingDialog(mActivity, "加载数据中...");
        final ProgressDialog dialog = ProgressDialog.show(mActivity, null, mActivity.getString(R.string.shujujiazaizhong), false, false);
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
                        item.setAgentId(itemobject.getString("agentID"));
                        item.setAgentName(itemobject.getString("agentName"));
                        item.setDivideID(itemobject.getString("divideID"));
                        item.setCompleteTime(itemobject.getString("completeTime"));
                        item.setType(itemobject.getString("type"));
                        item.setOperatorName(itemobject.getString("operatorName"));
                        item.setTradeTotal(itemobject.getString("tradeTotal"));
                        BigDecimal bd = new BigDecimal(itemobject.getString("divideMoney"));
                        item.setDivideMoney(String.valueOf(bd));
                        item.setCreatTime(itemobject.getString("creatTime"));
                        item.setStartTime(itemobject.getString("startTime"));
                        item.setEndTime(itemobject.getString("endTime"));
                        item.setOperatorId(itemobject.getString("operatorID"));
                        item.setReviewTime(itemobject.getString("reviewTime"));
                        DataSet.add(item);
                    }

                } catch (JSONException e) {
                    error = mActivity.getString(R.string.fuwuqikaixiaocai_chongshi);
                    return false;
                } catch (IOException e) {
                    error =mActivity.getString(R.string.wangluobugeili);
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                if (DataSet.size() == 0 && error.equals("")) {
                    new AlertView(mActivity.getString(R.string.tishi), mActivity.getString(R.string.zanwushuju), null, new String[]{mActivity.getString(R.string.queding)}, null, mActivity, AlertView.Style.Alert, null).setCancelable(true).show();
                }
                if (!aBoolean) {
                    new AlertView(mActivity.getString(R.string.tishi), error, null, new String[]{mActivity.getString(R.string.queding)}, null, mActivity, AlertView.Style.Alert, null).setCancelable(true).show();
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
        mAdapter.notifyDataSetChanged();
    }

    public class Item {
        String agentId;
        String divideID;
        String agentName;
        String completeTime;
        String type;
        String operatorName;
        String tradeTotal;
        String divideMoney;
        String creatTime;
        String startTime;
        String endTime;
        String operatorId;
        String reviewTime;

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getDivideID() {
            return divideID;
        }

        public void setDivideID(String divideID) {
            this.divideID = divideID;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getCompleteTime() {
            return completeTime;
        }

        public void setCompleteTime(String completeTime) {
            this.completeTime = completeTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getTradeTotal() {
            return tradeTotal;
        }

        public void setTradeTotal(String tradeTotal) {
            this.tradeTotal = tradeTotal;
        }

        public String getDivideMoney() {
            return divideMoney;
        }

        public void setDivideMoney(String divideMoney) {
            this.divideMoney = divideMoney;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
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

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getReviewTime() {
            return reviewTime;
        }

        public void setReviewTime(String reviewTime) {
            this.reviewTime = reviewTime;
        }
    }
}
