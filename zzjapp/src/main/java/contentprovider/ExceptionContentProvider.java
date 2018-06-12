package contentprovider;

import android.app.Activity;
import android.app.Dialog;
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

/**
 * Created by Administrator on 2017/8/1.
 */

public class ExceptionContentProvider {
    private Activity mActivity;
    private BaseAdapter mAdapter;
    private int total = 0;
    private int initAccountPage = 0;
    private List<Item> DataSet = new ArrayList<>();

    public ExceptionContentProvider(Activity mActivity, BaseAdapter mAdapter) {
        this.mActivity = mActivity;
        this.mAdapter = mAdapter;
    }

    public void getData(final String url, final Map<String, String> body) {

        final Dialog dialog = DiaLogUtil.createLoadingDialog(mActivity, mActivity.getString(R.string.shujujiazaizhong));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new AsyncTask<Void, Void, Boolean>() {
            String error;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    JSONObject object = new JSONObject(HttpRequest.postString(
                            url, body));
                    if (DataSet.size() != 0) {
                        DataSet.clear();
                    }
                    JSONObject d = object.getJSONObject("d");
                    Log.i("d", d.toString());
                    error = object.getString("err");
                    if (!error.equals("")) {
                        return false;
                    }
                    total = Integer.parseInt(d.getString("t"));
                    JSONArray array = d.getJSONArray("l");
                    Log.i("array", array.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject itemobject = (JSONObject) array.opt(i);
                        Item item = new Item();
                        item.setMachineCode(itemobject.getString("machineCode"));
                        item.setCreatTime(itemobject.getString("creatTime"));
                        item.setDealTime(itemobject.getString("dealTime"));
                        item.setExceptionId(itemobject.getString("exceptionID"));
                        item.setExceptionMsg(itemobject.getString("exceptionMsg"));
                        item.setIsDeal(itemobject.getString("isDeal"));
                        item.setSpendTime(itemobject.getString("spendTime"));
                        DataSet.add(item);
                    }
                } catch (IOException e1) {
                    error =  mActivity.getString(R.string.wangluocuowu) + e1;
                    return false;

                } catch (JSONException e1) {
                    error =mActivity.getString(R.string.fuwuqikaixiaocai_chongshi)+ e1;
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (DataSet.size() == 0 && error.equals("")) {
                    new AlertView(mActivity.getString(R.string.tishi), mActivity.getString(R.string.huangetiaojianshaxuan), null, new String[]{mActivity.getString(R.string.queding)}, null, mActivity, AlertView.Style.Alert, null).setCancelable(true).show();
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
            } else {
                initAccountPage = total % GetDataSetSize() > 0 ? total
                        / GetDataSetSize() + 1 : total / GetDataSetSize();
                return initAccountPage;
            }
        } else {
            return 1;
        }

    }

    public Item getItem(int position) {
        return DataSet.get(position);
    }

    public class Item {
        String exceptionMsg;
        String dealTime;
        String machineCode;
        String creatTime;
        String exceptionId;
        String isDeal;
        String spendTime;

        public String getExceptionMsg() {
            return exceptionMsg;
        }

        public void setExceptionMsg(String exceptionMsg) {
            this.exceptionMsg = exceptionMsg;
        }

        public String getDealTime() {
            return dealTime;
        }

        public void setDealTime(String dealTime) {
            this.dealTime = dealTime;
        }

        public String getMachineCode() {
            return machineCode;
        }

        public void setMachineCode(String machineCode) {
            this.machineCode = machineCode;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }

        public String getExceptionId() {
            return exceptionId;
        }

        public void setExceptionId(String exceptionId) {
            this.exceptionId = exceptionId;
        }

        public String getIsDeal() {
            return isDeal;
        }

        public void setIsDeal(String isDeal) {
            this.isDeal = isDeal;
        }

        public String getSpendTime() {
            return spendTime;
        }

        public void setSpendTime(String spendTime) {
            this.spendTime = spendTime;
        }
    }
}
