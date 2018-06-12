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
import bean.AccountLogItem;
import httputil.HttpRequest;
import otherutils.Tip;


public class AccountLogListContentProvider {
    private Activity mActivity;
    private BaseAdapter mAdapter;
    private List<AccountLogItem> DataSet = new ArrayList<>();
    private int total = 0;
    private int initAccountPage = 0;

    public AccountLogListContentProvider(Activity mActivity, BaseAdapter mAdapter) {
        this.mActivity = mActivity;
        this.mAdapter = mAdapter;
    }

    public void getData(final String url, final Map<String, String> body) {
        final Dialog dialog = DiaLogUtil.createLoadingDialog(mActivity, null);
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
                        AccountLogItem item = new AccountLogItem();
                        item.setAccountId(itemobject.getString("accountID"));
                        item.setManagerNum(itemobject.getString("managerNum"));
                        item.setMachineCode(itemobject.getString("machineCode"));
                        switch (itemobject.getString("accountReason")) {
                            case "0":
                                item.setAccountReason(mActivity.getString(R.string.jiaoyi));
                                break;
                            case "1":
                                item.setAccountReason(mActivity.getString(R.string.zhazhishibaituikuan));
                                break;
                            case "2":
                                item.setAccountReason(mActivity.getString(R.string.caoshituikuan));
                                break;
                        }
                        switch (itemobject.getString("accountType")) {
                            case "0":
                                item.setAccountType(mActivity.getString(R.string.chuzhang));
                                break;
                            case "1":
                                item.setAccountType(mActivity.getString(R.string.ruzhang));
                                break;
                        }
                        item.setTradeCode(itemobject.getString("tradeCode"));
                        item.setAccountTime(itemobject.getString("accountTime"));
                        switch (itemobject.getString("accountMode")) {
                            case "0":
                                item.setAccountMode(mActivity.getString(R.string.xianjing));
                                break;
                            case "1":
                                item.setAccountMode(mActivity.getString(R.string.weixin));
                                break;
                            case "2":
                                item.setAccountMode(mActivity.getString(R.string.zhifubao));
                                break;
                        }
                        item.setAccountMoney(itemobject.getString("accountMoney"));
                        DataSet.add(item);
                    }
                } catch (IOException e1) {
                    error = mActivity.getString(R.string.wangluocuowu);
                    return false;

                } catch (JSONException e1) {
                    error = mActivity.getString(R.string.fuwuqikaixiaocai_chongshi);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
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

    public AccountLogItem getItem(int position) {
        return DataSet.get(position);
    }

}
