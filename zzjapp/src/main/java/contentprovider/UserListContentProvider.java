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

public class UserListContentProvider {
    private Activity mActivity;
    private BaseAdapter mAdapter;
    private int total = 0;
    private int initAccountPage = 0;
    private List<Item> DataSet = new ArrayList<>();

    public UserListContentProvider(Activity mActivity, BaseAdapter mAdapter) {
        this.mActivity = mActivity;
        this.mAdapter = mAdapter;
    }

    public void getData(final String url, final Map<String, String> body, final boolean refresh) {
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
                    if (refresh) {
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
                        item.setManagerId(itemobject.getString("managerID"));
                        item.setManagerNum(itemobject.getString("managerNum"));
                        item.setActivatedType(itemobject
                                .getString("activatedType"));
                        item.setManagerType(itemobject.getString("managerType"));

                        item.setManagerName(itemobject.getString("managerName"));

                        item.setManagerTelephone(itemobject
                                .getString("managerTelephone"));

                        item.setManagerEmail(itemobject
                                .getString("managerEmail"));
                        item.setManagerAddress(itemobject
                                .getString("managerAddress"));
                        item.setManagerCard(itemobject
                                .getString("managerCard"));
                        item.setManagerCompany(itemobject
                                .getString("managerCompany"));
                        item.setAgentID(itemobject.getString("agentID"));
                        item.setDivideProportion(itemobject.getString("divideProportion"));
                        if (itemobject.has("agentName")) {
                            item.setAgentName(itemobject.getString("agentName"));
                        }
                        if (itemobject.has("commpanyAddress")) {
                            item.setCompanyAddress(itemobject
                                    .getString("commpanyAddress"));
                        }
                        if (itemobject.has("commpanyNum")) {
                            item.setCompanyNum(itemobject
                                    .getString("commpanyNum"));
                        }
                        if (itemobject.has("managerBankAccount")) {
                            item.setManagerBankAccount(itemobject.getString("managerBankAccount"));
                        }
                        DataSet.add(item);
                      /*  if (Integer.parseInt(UserMessage.getManagerType()) <= Integer.parseInt(itemobject.getString("managerType"))) {
                            DataSet.add(item);
                        }*/

                    }
                } catch (IOException e1) {
                    error = mActivity.getString(R.string.wangluocuowu);
                    return false;

                } catch (JSONException e1) {
                    error = mActivity.getString(R.string.fuwuqikaixiaocai_chongshi);;
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

    public Item getItem(int position) {
        return DataSet.get(position);
    }

    public void remove(int position) {
        DataSet.remove(position);
    }

    public class Item {
        String managerId;
        String managerNum;
        String activatedType;
        String managerType;
        String managerName;
        String managerTelephone;
        String managerEmail;
        String managerAddress;
        String managerCard;
        String managerCompany;
        String agentID;
        String agentName;
        String managerBankAccount = "";
        String companyAddress;
        String companyNum;
        String divideProportion;

        public String getDivideProportion() {
            return divideProportion;
        }

        public void setDivideProportion(String divideProportion) {
            this.divideProportion = divideProportion;
        }

        public String getManagerId() {
            return managerId;
        }

        public void setManagerId(String managerId) {
            this.managerId = managerId;
        }

        public String getManagerNum() {
            return managerNum;
        }

        public void setManagerNum(String managerNum) {
            this.managerNum = managerNum;
        }

        public String getActivatedType() {
            return activatedType;
        }

        public void setActivatedType(String activatedType) {
            this.activatedType = activatedType;
        }

        public String getManagerType() {
            return managerType;
        }

        public void setManagerType(String managerType) {
            this.managerType = managerType;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }

        public String getManagerTelephone() {
            return managerTelephone;
        }

        public void setManagerTelephone(String managerTelephone) {
            this.managerTelephone = managerTelephone;
        }

        public String getManagerEmail() {
            return managerEmail;
        }

        public void setManagerEmail(String managerEmail) {
            this.managerEmail = managerEmail;
        }

        public String getManagerAddress() {
            return managerAddress;
        }

        public void setManagerAddress(String managerAddress) {
            this.managerAddress = managerAddress;
        }

        public String getManagerCard() {
            return managerCard;
        }

        public void setManagerCard(String managerCard) {
            this.managerCard = managerCard;
        }

        public String getManagerCompany() {
            return managerCompany;
        }

        public void setManagerCompany(String managerCompany) {
            this.managerCompany = managerCompany;
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

        public String getManagerBankAccount() {
            return managerBankAccount;
        }

        void setManagerBankAccount(String managerBankAccount) {
            this.managerBankAccount = managerBankAccount;
        }

        public String getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }

        public String getCompanyNum() {
            return companyNum;
        }

        public void setCompanyNum(String companyNum) {
            this.companyNum = companyNum;
        }

    }
}
