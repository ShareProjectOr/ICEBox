package contentprovider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import httputil.HttpRequest;
import otherutils.Json2Map;
import otherutils.Tip;

/**
 * Created by Administrator on 2017/7/27.
 */

public class MachineContentProvider {
    private List<Item> DataSet = new ArrayList<>();
    private Activity mActivity;
    private BaseAdapter mAdapter;
    private TextView managerName, agentName;
    private Map<String, String> map;

    private DateListener mDateListener;

    public MachineContentProvider(Activity activity, BaseAdapter adapter) {
        mActivity = activity;
        mAdapter = adapter;

    }

    public interface DateListener {
        void getDate(List<Item> DataSet);
    }

    public void SetgetDateListener(DateListener dateListener) {
        if (mDateListener == null) {
            mDateListener = dateListener;
        }
    }

    public void initview(TextView managerName, TextView agentName) {
        this.managerName = managerName;
        this.agentName = agentName;
    }

    public void getData(final String url, final Map<String, String> body) {
        final ProgressDialog dialog = ProgressDialog.show(mActivity, null, mActivity.getString(R.string.shujujiazaizhong), false, false);
        new AsyncTask<Void, Void, Boolean>() {
            String error = "";

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    String response = HttpRequest.postString(
                            url, body);
                    Log.e("返回结果js", response);
                    JSONObject object = new JSONObject(response);

                    if (DataSet.size() != 0) {
                        DataSet.clear();
                    }
                    if (!object.getString("err").equals("")) {
                        error = object.getString("err");
                        return false;
                    }
                    JSONObject d = object.getJSONObject("d");
                    map = Json2Map.toHashMap(d);

                    List<String> nameArray = new ArrayList<>();
                    List<String> contentArray = new ArrayList<>();

                    nameArray.add("machineCode");
                    nameArray.add("machineName");
                    nameArray.add("machineAddress");
                    nameArray.add("stateMachineType");
                    nameArray.add("cashPrice");
                    nameArray.add("showPrice");
                    nameArray.add("alipayPrice");
                    nameArray.add("weChatPrice");
                    nameArray.add("stateCupNum");
                    nameArray.add("stateCapNum");
                    nameArray.add("stateOrangeNum");

                    //       nameArray.addAll(map.keySet());
                    contentArray.add(map.get("machineCode"));
                    contentArray.add(map.get("machineName"));
                    contentArray.add(map.get("machineAddress"));
                    contentArray.add(map.get("stateMachineType"));
                    contentArray.add(map.get("cashPrice"));
                    switch (map.get("showPrice")) {
                        case "0":
                            contentArray.add(mActivity.getString(R.string.xianjing));
                            break;
                        case "1":
                            contentArray.add(mActivity.getString(R.string.weixin));
                            break;
                        case "2":
                            contentArray.add(mActivity.getString(R.string.zhifubao));
                            break;
                    }
                    contentArray.add(map.get("alipayPrice"));
                    contentArray.add(map.get("weChatPrice"));
                    contentArray.add(map.get("stateCupNum"));
                    contentArray.add(map.get("stateCapNum"));
                    contentArray.add(map.get("stateOrangeNum"));


                    //  contentArray.addAll(map.values());
                    Log.e("转化后的数据", nameArray.toString() + "\n" + contentArray.toString());
                    for (int i = 0; i < nameArray.size(); i++) {
                        Item item = new Item();
                        item.setName(nameArray.get(i));
                        item.setContent(contentArray.get(i));
                        switch (nameArray.get(i)) {
                            case "machineCode":
                                item.setName(mActivity.getString(R.string.jiqibianma));
                                break;
                            case "price":
                                item.setName(mActivity.getString(R.string.danjie_yuan));
                                break;
                            case "alipayPrice":
                                item.setName(mActivity.getString(R.string.zhifubaodanjia_yuan));
                                break;
                            case "weChatPrice":
                                item.setName(mActivity.getString(R.string.weixindanjia_yuan));
                                break;
                            case "cfgMediaSoftware":
                                item.setName(mActivity.getString(R.string.ruanjianmeitibanben));
                                break;
                            case "cashPrice":
                                item.setName(mActivity.getString(R.string.xianjindanjia_yuan));
                                break;
                            case "stateMediaVersion":
                                item.setName(mActivity.getString(R.string.meitibanbenhao));
                                break;
                            case "stateClientVersion":
                                item.setName(mActivity.getString(R.string.kehuduanbanben));
                                break;
                            case "showPrice":
                                item.setName(mActivity.getString(R.string.zhanshidanjia));
                                break;
                            case "stateIsOnline":
                                item.setName(mActivity.getString(R.string.zaixianzhuangtai));
                                break;
                            case "stateUpdateTime":
                                item.setName(mActivity.getString(R.string.genxingshijian));
                                break;
                            case "machineName":
                                item.setName(mActivity.getString(R.string.jiqimingcheng));
                                break;
                            case "productTime":
                                item.setName(mActivity.getString(R.string.shengcanshijian));
                                break;
                            case "factoryTime":
                                item.setName(mActivity.getString(R.string.chuchangshijian));
                                break;
                            case "activatedTime":
                                item.setName(mActivity.getString(R.string.jihuoshijian));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.weijihuo));
                                        break;
                                    case "1":
                                        item.setContent(mActivity.getString(R.string.jihuo));
                                        break;
                                }
                                break;
                            case "machineAddress":
                                item.setName(mActivity.getString(R.string.fanzhiweizhi));
                                if (!contentArray.get(i).isEmpty() && !contentArray.get(i).equals("null")) {
                                    String[] managerAddress = contentArray.get(i).split("\\|");
                                    if (managerAddress.length == 2) {
                                        item.setContent(managerAddress[1]);
                                    } else if (managerAddress.length == 3) {
                                        item.setContent(managerAddress[1] + managerAddress[2]);
                                    } else {
                                        item.setContent("");
                                    }
                                } else {
                                    item.setContent("");
                                }

                                break;
                            case "activatedType":
                                item.setName(mActivity.getString(R.string.jihuozhuangtai));
                                break;
                            case "cfgPaperMachine":
                                item.setName(mActivity.getString(R.string.zhibiji));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.wu));
                                        break;
                                    case "1":
                                        item.setContent(mActivity.getString(R.string.you));
                                        break;

                                }
                                break;
                            case "cfgCoinMachine":
                                item.setName(mActivity.getString(R.string.yingbiji));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.wu));
                                        break;
                                    case "1":
                                        item.setContent(mActivity.getString(R.string.you));
                                        break;

                                }
                                break;
                            case "cfgAdvertisingScreen":
                                item.setName(mActivity.getString(R.string.guanggaoping));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.wu));
                                        break;
                                    case "1":
                                        item.setContent(mActivity.getString(R.string.you));
                                        break;

                                }
                                break;
                            case "cfgCardReader":
                                item.setName(mActivity.getString(R.string.ic_dukaqi));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.wu));
                                        break;
                                    case "1":
                                        item.setContent(mActivity.getString(R.string.you));
                                        break;

                                }
                                break;
                            case "cfgDocoinMachine":
                                item.setName(mActivity.getString(R.string.zhaolingqi));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.wu));
                                        break;
                                    case "1":
                                        item.setContent(mActivity.getString(R.string.you));
                                        break;

                                }
                                break;
                            case "cfgMisPos":
                                item.setName(mActivity.getString(R.string.shuakaqi));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.wu));
                                        break;
                                    case "1":
                                        item.setContent(mActivity.getString(R.string.you));
                                        break;

                                }
                                break;
                            case "cfgDriver":
                                item.setName(mActivity.getString(R.string.qudongbanben));
                                break;
                            case "cfgSellSoftware":
                                item.setName(mActivity.getString(R.string.shoumairuanjianbanben));
                                break;
                            case "cfgMediaSortware":
                                item.setName(mActivity.getString(R.string.meitiruanjianbanben));
                                break;
                            case "stateExceptionType":
                                item.setName(mActivity.getString(R.string.yichangbiaozhi));
                                break;
                            case "stateLongLat":
                                item.setName(mActivity.getString(R.string.jinweidu));
                                break;
                            case "stateOrangeNum":
                                item.setName(mActivity.getString(R.string.chengzishuliang));
                                break;
                            case "stateCupNum":
                                item.setName(mActivity.getString(R.string.beizishuliang));
                                break;
                            case "stateCapNum":
                                item.setName(mActivity.getString(R.string.gaizishuliang));
                                break;
                            case "stateMachineType":
                                item.setName(mActivity.getString(R.string.shebeizhuangtai));
                                switch (contentArray.get(i)) {
                                    case "00":
                                        item.setContent(mActivity.getString(R.string.zhengchang));
                                        break;
                                    default:
                                        item.setContent(mActivity.getString(R.string.yichang));
                                        break;
                                }
                                break;
                            case "statePaperMachine":
                                item.setName(mActivity.getString(R.string.qianxiangzhuangtai));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        break;
                                    case "1":
                                        break;
                                }
                                break;
                            case "stateBoundar":
                                item.setName(mActivity.getString(R.string.zhongduanjijiemianzhuangtai));
                                break;
                            case "stateBtimeOut":
                                item.setName(mActivity.getString(R.string.jiemianchaoshi));
                                switch (contentArray.get(i)) {
                                    case "00":
                                        item.setContent(mActivity.getString(R.string.zhengchang));
                                        break;
                                    case "01":
                                        item.setContent(mActivity.getString(R.string.changshijianbuqubei));
                                        break;
                                    case "02":
                                        item.setContent(mActivity.getString(R.string.changshijianbuquka));
                                        break;

                                }
                                break;
                            case "stateChangeDevice":
                                item.setName(mActivity.getString(R.string.zhaolingqizhuangtai));
                                switch (contentArray.get(i)) {
                                    case "00":
                                        item.setContent(mActivity.getString(R.string.zhengchang));
                                        break;
                                    default:
                                        item.setContent(contentArray.get(i));
                                        break;

                                }
                                break;
                            case "stateAdvertisingScreen":
                                item.setName(mActivity.getString(R.string.guanggaoping));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.yichang));
                                        break;
                                    default:
                                        item.setContent(mActivity.getString(R.string.zhengchang));
                                        break;

                                }
                                break;
                            case "stateCardReader":
                                item.setName(mActivity.getString(R.string.ic_dukaiqizhuangtai));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.yichang));
                                        break;
                                    default:
                                        item.setContent(mActivity.getString(R.string.zhengchang));
                                        break;

                                }
                                break;
                            case "stateMisPos":
                                item.setName(mActivity.getString(R.string.shuakaqi));
                                switch (contentArray.get(i)) {
                                    case "0":
                                        item.setContent(mActivity.getString(R.string.yichang));
                                        break;
                                    default:
                                        item.setContent(mActivity.getString(R.string.zhengchang));
                                        break;

                                }
                                break;
                            case "managerID":
                                item.setName(mActivity.getString(R.string.guanliyuanbianhao));
                                break;
                            case "managerName":
                                item.setName(mActivity.getString(R.string.guanliyuanxingming));
                                break;
                            case "agentID":
                                item.setName(mActivity.getString(R.string.dailishangbianhao));
                                break;
                            case "agentName":
                                item.setName(mActivity.getString(R.string.dailishangxingming));
                                break;
                        }


                        DataSet.add(item);

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
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (aBoolean) {
                    managerName.setText(mActivity.getString(R.string.guanliyuan) + map.get("managerName"));
                    agentName.setText(mActivity.getString(R.string.dailishang) + map.get("agentName"));
                }
                if (DataSet.size() == 0 && error.equals("")) {
                    Toast.makeText(mActivity, R.string.meiyourenhexinxi, Toast.LENGTH_LONG).show();
                }
                mDateListener.getDate(DataSet);
                if (!aBoolean) {
                    Toast.makeText(mActivity, error, Toast.LENGTH_LONG).show();
                }

                mAdapter.notifyDataSetChanged();

            }
        }.execute();
    }

    public Item getItem(int position) {
        return DataSet.get(position);
    }

    public int GetDataSize() {
        return DataSet.size();
    }

    public void remove(int position) {
        DataSet.remove(position);
    }

    public class Item {
        String name;
        String content;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}





