package contentprovider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import httputil.Constance;
import httputil.HttpRequest;
import otherutils.Tip;

/**
 * Created by Administrator on 2017/7/27.
 */

public class BindNameListContentProvider {
    private List<Item> DataSet = new ArrayList<>();
    private Activity mActivity;
    private BaseAdapter mAdapter;

    public BindNameListContentProvider(Activity activity, BaseAdapter adapter) {
        mActivity = activity;
        mAdapter = adapter;
    }

    public void getdata() {
        final Map<String, String> post = new HashMap<>();
        post.put("P", "1");
        post.put("activatedType", "1");
        post.put("N", "1");
        post.put("tsy", UserMessage.getTsy());
        switch (UserMessage.getManagerType()) {
            case "2":
                post.put("W[managerType]", "3");
                post.put("W[agentID]", UserMessage.getManagerId());
                break;
            default:
                post.put("W[managerType]", "2");
                break;
        }
        final ProgressDialog dialog = ProgressDialog.show(mActivity, null, mActivity.getString(R.string.zhengzaijiazairenyuanlibiao), false, false);
        if (DataSet.size() != 0) {
            DataSet.clear();
        }
        new AsyncTask<Void, Void, Boolean>() {
            String error;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    JSONObject object = new JSONObject(HttpRequest.postString(
                            Constance.GET_USER_LIST_URL, post));
                    error = object.getString("err");
                    if (!error.equals("")) {
                        return false;
                    }

                    JSONObject d = object.getJSONObject("d");
                    String N = d.getString("t");
                    Map<String, String> post2 = new HashMap<>();
                    post2.put("P", "1");
                    post2.put("activatedType", "1");
                    post2.put("N", N);
                    post2.put("tsy", UserMessage.getTsy());
                    switch (UserMessage.getManagerType()) {
                        case "2":
                            post2.put("W[managerType]", "3");
                            post2.put("W[agentID]", UserMessage.getManagerId());
                            break;
                        default:
                            post2.put("W[managerType]", "2");
                            break;
                    }
                    JSONObject all = new JSONObject(HttpRequest.postString(Constance.GET_USER_LIST_URL, post2));
                    error = all.getString("err");
                    if (!error.equals("")) {
                        return false;
                    }
                    JSONArray L = all.getJSONObject("d").getJSONArray("l");
                    for (int i = 0; i < L.length(); i++) {
                        JSONObject itemobject = (JSONObject) L.opt(i);
                        Item item = new Item();
                        item.setName(itemobject.getString("managerName"));
                        item.setCode(itemobject.getString("managerID"));
                        DataSet.add(item);
                    }
                } catch (JSONException e) {
                    error =  mActivity.getString(R.string.fuwuqikaixiaocai_chongshi) + e;
                    return false;
                } catch (IOException e) {
                    error = mActivity.getString(R.string.wangluobugeili2) + e;
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
                    new AlertView(mActivity.getString(R.string.tishi), mActivity.getString(R.string.meiyouchuangjiandailishang), null, new String[]{mActivity.getString(R.string.queding)}, null, mActivity, AlertView.Style.Alert, null).setCancelable(true).show();
                }
                if (!aBoolean) {
                    new AlertView(mActivity.getString(R.string.tishi), error, null, new String[]{mActivity.getString(R.string.queding)}, null, mActivity, AlertView.Style.Alert, null).setCancelable(true).show();
                }
                mAdapter.notifyDataSetChanged();

            }
        }.execute();
    }

    public int GetDataSize() {
        return DataSet.size();
    }

    public Item GetItem(int position) {
        return DataSet.get(position);
    }

    public class Item {
        String name;
        String code;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
