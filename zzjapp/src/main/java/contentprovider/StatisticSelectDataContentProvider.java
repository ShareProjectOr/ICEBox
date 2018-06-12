package contentprovider;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.DiaLogUtil;
import adapter.MyAdapter;
import adapter.StatictisResultAdapter;
import entity.Item;
import entity.ItemExceptionData;
import entity.ItemTradeData;
import httputil.Constance;
import httputil.HttpRequest;
import httputil.JsonParseUtil;

/**
 * Created by WH on 2017/8/1.
 */

public class StatisticSelectDataContentProvider {
    private Activity mContext;
    private JsonParseUtil mJsonParseUtil;
    private List<? extends Item> mAgentList;
    private BaseAdapter mAdapter;
    private StatictisResultAdapter mResultAdapter;
    private TrendCallBack mCallBack;
    private Dialog mDialog;

    public StatisticSelectDataContentProvider(Activity context, BaseAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
        initData();
    }

    private void initData() {
        mJsonParseUtil = new JsonParseUtil();
        mAgentList = new ArrayList<>();
        mDialog = DiaLogUtil.createLoadingDialog(mContext, mContext.getString(R.string.shujujiazaizhong));
    }

    public void setResultAdapter(StatictisResultAdapter adapter) {
        mResultAdapter = adapter;
    }

    public void setTrendCallBack(TrendCallBack callBack) {
        mCallBack = callBack;
    }

    public void getAgents() {
//        mDialog.show();
        new DataStatisticsTask("getAgents", null, null, getAgentParams()).execute();
    }

    public void getMachineManagers(String agentId) {
        if (TextUtils.equals(agentId, "everything")) {
            agentId = "";
        }
        mDialog.show();
        new DataStatisticsTask("getManagers", agentId, null, getManagerParams()).execute();
    }

    public void getMachines(String agentId, String managerId) {
        if (TextUtils.equals(agentId, "everything")) {
            agentId = "";
        }
        if (TextUtils.equals(managerId, "everything")) {
            managerId = "";
        }
        mDialog.show();
        new DataStatisticsTask("getMachines", agentId, managerId, getMachineParams()).execute();
    }


//    public Map<String, String> getmap(int position) {
//
//        return mAgentList.get(position);
//    }

    public int getdatasetsize() {
        return mAgentList.size();
    }

    //    public List<Map<String, String>> getResult() {
//        return mAgentList;
//    }
    public List<? extends Item> getResult() {
        return mAgentList;
    }


    private Map<String, String> getAgentParams() {
        Map<String, String> body = new HashMap<>();
        body.put("P", "1");
        body.put("W[managerType]", "2");//"" + (Integer.parseInt(UserMessage.getManagerType()) + 1)
        body.put("tsy", UserMessage.getTsy());
        return body;
    }

    private Map<String, String> getManagerParams() {
        Map<String, String> body = new HashMap<>();
        body.put("W[managerType]", "3");
        body.put("tsy", UserMessage.getTsy());
        return body;
    }

    private Map<String, String> getMachineParams() {
        Map<String, String> body = new HashMap<>();
        Log.d("debug", "tsy==" + UserMessage.getTsy());
        body.put("tsy", UserMessage.getTsy());
        return body;
    }

    public void getFaultTotal(Map<String, String> params, String tag) {
        StringBuilder buffer = new StringBuilder();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("tsy", UserMessage.getTsy());

        if (TextUtils.equals(params.get("agentId"), "everything") || TextUtils.isEmpty(params.get("agentId"))) {
            bodys.put("W[agentID]", "");
        } else {
            bodys.put("W[agentID]", params.get("agentId"));
        }
        Log.d("debug", "machineCode=" + params.get("machineCode"));
        if (TextUtils.equals(params.get("machineCode"), "everything") || TextUtils.isEmpty(params.get("machineCode"))) {
            bodys.put("W[machineCode]", "");
        } else {
            bodys.put("W[machineCode]", params.get("machineCode"));
        }
        if (TextUtils.equals(params.get("managerId"), "everything") || TextUtils.isEmpty(params.get("managerId"))) {
            bodys.put("W[managerID]", "");
        } else {
            bodys.put("W[managerID]", params.get("managerId"));
        }
        if (TextUtils.isEmpty(params.get("creatTime"))) {
            Toast.makeText(mContext, R.string.guzhangshijian, Toast.LENGTH_SHORT).show();
            return;
        } else {
            bodys.put("W[creatTime]", params.get("creatTime"));
        }
        buffer.append("total|spendTime|nDealTotal|dealTotal");
        bodys.put("K", buffer.toString());
        //  mDialog.show();
        if (TextUtils.equals(tag, "data")) {
            new DataFaultTask(Constance.GET_EXCEPTION_TOTAL, bodys, mResultAdapter, key2String()).execute();
        }
        if (TextUtils.equals(tag, "trend")) {
            new DataFaultTask(Constance.GET_EXCEPTION_TREND, bodys, mCallBack, key2String()).execute();
        }

    }

    public void getTotalResult(Map<String, String> params, String tag) {
        StringBuilder buffer = new StringBuilder();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("tsy", UserMessage.getTsy());

        if (TextUtils.equals(params.get("agentId"), "everything") || TextUtils.isEmpty(params.get("agentId"))) {
            bodys.put("W[agentID]", "");
        } else {
            bodys.put("W[agentID]", params.get("agentId"));
        }

        if (TextUtils.equals(params.get("machineCode"), "everything") || TextUtils.isEmpty(params.get("machineCode"))) {
            bodys.put("W[machineCode]", "");
        } else {
            bodys.put("W[machineCode]", params.get("machineCode"));
        }
        if (TextUtils.equals(params.get("managerId"), "everything") || TextUtils.isEmpty(params.get("managerId"))) {
            bodys.put("W[managerID]", "");
        } else {
            bodys.put("W[managerID]", params.get("managerId"));
        }
        if (TextUtils.isEmpty(params.get("endTime"))) {
            Toast.makeText(mContext, R.string.qingxuanzeshijian, Toast.LENGTH_SHORT).show();
            return;
        } else {
            bodys.put("W[endTime]", params.get("endTime"));
        }

        buffer.append("tradeMoney|total|failTotal|successTotal|cupNum|orangeNum");
        Log.d("debug", "time=" + params.get("paymentMode"));
        if (params.containsKey("paymentMode")) {
            if (TextUtils.equals(params.get("paymentMode"), "0")) {
                bodys.put("W[paymentMode]", "0");
                buffer.append("|cash");
            }
            if (TextUtils.equals(params.get("paymentMode"), "2")) {
                bodys.put("W[paymentMode]", "2");
                buffer.append("|alipay");
            }
            if (TextUtils.equals(params.get("paymentMode"), "1")) {
                bodys.put("W[paymentMode]", "1");
                buffer.append("|weChat");
            }
        } else {
            bodys.put("W[paymentMode]", "");
        }
        if (!TextUtils.isEmpty(params.get("isRefund"))) {
            bodys.put("W[isRefund]", params.get("isRefund"));
            buffer.append("|refundMoney");
        }
        bodys.put("K", buffer.toString());
        //       mDialog.show();
        if (TextUtils.equals(tag, "data")) {
            new DataTotalTask(Constance.GET_MACHINE_TRADE_TOTAL, bodys, mResultAdapter, key2String(params)).execute();
        }
        if (TextUtils.equals(tag, "trend")) {
            new DataTotalTask(Constance.GET_MACHINE_TRAND_TREND, bodys, mCallBack, key2String(params)).execute();
        }
    }

    //故障返回的key
    private Map<String, String> key2String() {
        Map<String, String> key2string = new HashMap<>();
        key2string.put("total", mContext.getResources().getString(R.string.faultTotal));
        key2string.put("spendTime", mContext.getResources().getString(R.string.spandTime));
        key2string.put("dealTotal", mContext.getResources().getString(R.string.dealTotal));
        key2string.put("nDealTotal", mContext.getResources().getString(R.string.nDealTotal));
        key2string.put("avgTime", mContext.getResources().getString(R.string.avgTime));
        return key2string;
    }

    //取得筛选的条件
    private Map<String, String> key2String(Map<String, String> params) {

        Map<String, String> key2string = new HashMap<>();
        key2string.put("orangeNum", mContext.getResources().getString(R.string.orangeNum));
        key2string.put("cupNum", mContext.getResources().getString(R.string.cupNum));
        key2string.put("failTotal", mContext.getResources().getString(R.string.failTotal));
        key2string.put("successTotal", mContext.getResources().getString(R.string.successTotal));
        key2string.put("total", mContext.getResources().getString(R.string.total));
        if (TextUtils.equals(params.get("isRefund"), "0")) {
            key2String().remove("total");
            key2string.remove("successTotal");
            key2string.put("refundMoney", mContext.getResources().getString(R.string.notRefundMoney));
        }
        if (TextUtils.equals(params.get("isRefund"), "1")) {
            key2String().remove("total");
            key2string.remove("successTotal");
            key2string.put("refundMoney", mContext.getResources().getString(R.string.hasRefundMoney));
        }

        key2string.put("tradeMoney", mContext.getResources().getString(R.string.tradeMoney));
        key2string.put("time", mContext.getResources().getString(R.string.time));
        return key2string;
    }

    //回掉接口
    public interface TrendCallBack {
        List<ItemTradeData> trendResult(List<ItemTradeData> tendResults, Map<String, String> keys);

        List<ItemExceptionData> faultTrendResult(List<ItemExceptionData> tendResults, Map<String, String> keys);
    }

    //拉取总计结果数据
    private class DataTotalTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err;
        private Map<String, String> bodys, keys;
        private StatictisResultAdapter adapter;
        private Map<String, String> results;
        private List<ItemTradeData> resultTrends;
        private TrendCallBack callBack;
        private String url;

        DataTotalTask(String url, Map<String, String> bodys, StatictisResultAdapter adapter, Map<String, String> keys) {
            this.bodys = bodys;
            this.keys = keys;
            this.url = url;
            this.adapter = adapter;
            results = new HashMap<>();
        }

        DataTotalTask(String url, Map<String, String> bodys, TrendCallBack callBack, Map<String, String> keys) {
            this.bodys = bodys;
            this.keys = keys;
            this.url = url;
            this.callBack = callBack;
            resultTrends = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.d("debug", "result==" + bodys.toString());
                response = HttpRequest.postString(url, bodys);
                JSONObject jsonResponse = new JSONObject(response);
                err = jsonResponse.getString("err");
                if (TextUtils.isEmpty(err)) {
                    if (adapter != null) {
                        JSONObject result = jsonResponse.getJSONObject("d");
                        results = mJsonParseUtil.getTotalResult(result, keys);
                    }
                    if (callBack != null) {
                        JSONArray result = jsonResponse.getJSONArray("d");
                        resultTrends = mJsonParseUtil.getTotalTrendResult(result, keys);
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                err = mContext.getString(R.string.wangluobukeyong);
                return false;
            } catch (JSONException e1) {
                err = mContext.getString(R.string.fuwuqikaixiaocai);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (!success) {
                Toast.makeText(mContext, err, Toast.LENGTH_LONG).show();
            }
            if (adapter != null) {
                adapter.setDatas(results, keys);
            }
            if (callBack != null) {
                callBack.trendResult(resultTrends, keys);
                Log.d("debug", "resultTrends=====" + resultTrends.toString());
            }

        }
    }

    //拉取代理商或机器管理员数据/机器
    private class DataStatisticsTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err;
        private String Tag;
        private String agentId;
        private String managerId;
        private Map<String, String> bodys;

        DataStatisticsTask(String Tag, String agentId, String managerId, Map<String, String> bodys) {
            this.Tag = Tag;
            this.agentId = agentId;
            this.managerId = managerId;
            this.bodys = bodys;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                bodys.put("N", "1000");
                if (Tag.equals(MyAdapter.GET_AGENTS)) {
                    response = HttpRequest.postString(Constance.GET_USER_LIST_URL, bodys);
                }
                if (Tag.equals(MyAdapter.GET_MANAGERS)) {
                    bodys.put("W[agentID]", agentId);
                    response = HttpRequest.postString(Constance.GET_USER_LIST_URL, bodys);
                    Log.d("debug", agentId + "  response==" + response);
                }

                if (TextUtils.equals(Tag, MyAdapter.GET_MACHINES)) {
                    bodys.put("W[agentID]", agentId);
                    bodys.put("W[managerID]", managerId);
                    Log.d("debug", "body=" + bodys.toString());
                    response = HttpRequest.postString(Constance.GET_MACHINE_LIST_URL, bodys);
                }
                JSONObject object = new JSONObject(response);
                err = object.getString("err");
                if (TextUtils.isEmpty(err)) {
                    mDialog.dismiss();
                    JSONObject message = object.getJSONObject("d");
                    mAgentList.clear();
                    if (Tag.equals(MyAdapter.GET_AGENTS) || Tag.equals(MyAdapter.GET_MANAGERS)) {
                        mAgentList = mJsonParseUtil.getUser(message);
                    }
                    if (Tag.equals(MyAdapter.GET_MACHINES)) {
                        mAgentList = mJsonParseUtil.getMachine(mContext,message);
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                err = mContext.getString(R.string.wangluobukeyong);
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                return false;
            } catch (JSONException e1) {
                err = mContext.getString(R.string.fuwuqikaixiaocai);
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                //     Toast.makeText(mContext, "success", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(mContext, err, Toast.LENGTH_LONG).show();
            }
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    //拉取故障结果数据
    private class DataFaultTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err;
        private Map<String, String> bodys, keys;
        private StatictisResultAdapter adapter;
        private Map<String, String> results;
        private List<ItemExceptionData> resultTrends;
        private TrendCallBack callBack;
        private String url;


        public DataFaultTask(String url, Map<String, String> bodys, StatictisResultAdapter adapter, Map<String, String> keys) {
            this.bodys = bodys;
            this.keys = keys;
            this.url = url;
            this.adapter = adapter;
            results = new HashMap<>();
            Log.d("debug", "params==" + bodys.toString());
        }

        public DataFaultTask(String url, Map<String, String> bodys, TrendCallBack callBack, Map<String, String> keys) {
            this.bodys = bodys;
            this.keys = keys;
            this.url = url;
            this.callBack = callBack;
            results = new HashMap<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.d("debug", "result==" + bodys.toString());
                response = HttpRequest.postString(url, bodys);
                Log.d("debug", "response======" + response.toString());
                JSONObject jsonResponse = new JSONObject(response);
                err = jsonResponse.getString("err");
                if (TextUtils.isEmpty(err)) {
//                    mDialog.dismiss();
                    if (adapter != null) {
                        JSONObject result = jsonResponse.getJSONObject("d");
                        results = mJsonParseUtil.getFaultResult(result);
                    }
                    if (callBack != null) {
                        JSONArray result = jsonResponse.getJSONArray("d");
                        resultTrends = mJsonParseUtil.getFaultTrendResult(result);
                        Log.d("debug", "++++++++" + resultTrends.toString());
                    }
                    return true;
                } else {
                    return false;
                }

            } catch (IOException e) {
                err = mContext.getString(R.string.wangluobukeyong);;
                return false;
            } catch (JSONException e1) {
                err = mContext.getString(R.string.fuwuqikaixiaocai);;
                return false;
            }
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (!success) {
                Toast.makeText(mContext, err, Toast.LENGTH_LONG).show();
            }
            if (adapter != null) {
                adapter.setDatas(results, keys);
            }
            if (callBack != null) {
                callBack.faultTrendResult(resultTrends, keys);
                Log.d("debug", "resultTrends=====" + resultTrends.toString());
            }

        }


    }
}
