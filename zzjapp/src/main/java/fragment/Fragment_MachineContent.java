package fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.Activity.MachineContentActivity;
import com.example.zhazhijiguanlixitong.Activity.TotalActivity;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.DiaLogUtil;
import contentprovider.BindNameListContentProvider;
import contentprovider.MachineContentProvider;
import contentprovider.UserMessage;
import httputil.Constance;


public class Fragment_MachineContent extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {
    private View view;
    private BindNameListContentProvider provider;
    private ListPopupWindow mPopWindow;
    private MachineContentProvider mprovider;
    private TextView ManagerName, AgentName, CashPrice, save_title;
    private ImageView machinetype;
    private Button reviseprice;

    private AlertDialog ReviseDialog;
    private Activity activity;
    private EditText wechatinput, alipayinput;
    private float FloatCashPrice;
    private RadioGroup showPriceType;
    private RadioButton cashButton, wechatButton, alipayButton;
    private String ShowPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment__machine_content, null, false);
            initview();
            initListener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }


    private void initListener() {
      /*  edit.setOnClickListener(this);
        bind.setOnClickListener(this);*/
        reviseprice.setOnClickListener(this);
        mPopWindow.setOnItemClickListener(this);
        machinetype.setOnClickListener(this);
        showPriceType.setOnCheckedChangeListener(this);
        mprovider.SetgetDateListener(new MachineContentProvider.DateListener() {
            @Override
            public void getDate(List<MachineContentProvider.Item> DataSet) {
                if (DataSet.size() < 7) {
                    reviseprice.setVisibility(View.GONE);
                } else {
                    reviseprice.setVisibility(View.VISIBLE);
                    FloatCashPrice = Float.parseFloat(DataSet.get(4).getContent());
                    CashPrice.setText(DataSet.get(4).getContent());
                    wechatinput.setText(DataSet.get(7).getContent());
                    alipayinput.setText(DataSet.get(6).getContent());
                    String content = DataSet.get(5).getContent();
                    if (TextUtils.equals(content, getString(R.string.weixin))) {
                        wechatButton.setChecked(true);
                        cashButton.setChecked(false);
                        alipayButton.setChecked(false);
                        ShowPrice = "1";
                    } else if (TextUtils.equals(content, getString(R.string.xianjing))) {
                        wechatButton.setChecked(false);
                        cashButton.setChecked(true);
                        alipayButton.setChecked(false);
                        ShowPrice = "0";
                    } else if (TextUtils.equals(content, getString(R.string.zhifubao))) {
                        wechatButton.setChecked(false);
                        cashButton.setChecked(false);
                        alipayButton.setChecked(true);
                        ShowPrice = "2";
                    }
                }

            }
        });
    }

    private void initview() {
        activity = getActivity();
        //  edit = (Button) view.findViewById(R.id.edit_machine);
        ListView content = (ListView) view.findViewById(R.id.machine_content_list);
        View revisePriceView = LayoutInflater.from(getActivity()).inflate(R.layout.revise_price_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.baocun, new SavePriceListener(true));
        builder.setNegativeButton(R.string.quexiao, new SavePriceListener(false));
        builder.setView(revisePriceView);
        ReviseDialog = builder.create();
        wechatinput = (EditText) revisePriceView.findViewById(R.id.wechat_input);
        alipayinput = (EditText) revisePriceView.findViewById(R.id.alipay_input);
        CashPrice = (TextView) revisePriceView.findViewById(R.id.cash_price);
        ImageView reducePrice = (ImageView) revisePriceView.findViewById(R.id.reduce);
        ImageView addPrice = (ImageView) revisePriceView.findViewById(R.id.add);
        reducePrice.setOnClickListener(this);
        addPrice.setOnClickListener(this);
        showPriceType = (RadioGroup) revisePriceView.findViewById(R.id.showPriceType);
        cashButton = (RadioButton) revisePriceView.findViewById(R.id.choose_cash);
        wechatButton = (RadioButton) revisePriceView.findViewById(R.id.choose_wechat);
        alipayButton = (RadioButton) revisePriceView.findViewById(R.id.choose_alipay);
        save_title = (TextView) revisePriceView.findViewById(R.id.save_title);
        //  bind = (Button) view.findViewById(R.id.bind);
        reviseprice = (Button) view.findViewById(R.id.revise_price);
        if (!UserMessage.getManagerType().equals("2")) {
            reviseprice.setVisibility(View.GONE);
        } else {
            reviseprice.setVisibility(View.VISIBLE);
        }
        mPopWindow = new ListPopupWindow(getActivity());
        ManagerName = (TextView) view.findViewById(R.id.managerName);
        AgentName = (TextView) view.findViewById(R.id.agentName);
        machinetype = (ImageView) view.findViewById(R.id.machinetype);
        switch (getActivity().getIntent().getStringExtra("machinetype")) {
            case "00":
                machinetype.setBackgroundResource(R.mipmap.machine_normal);
                break;
            default:
                machinetype.setBackgroundResource(R.mipmap.machine_ex);
                break;
        }
        //    mPopWindow.setAnchorView(bind);
        mPopWindow.setModal(true);
        mPopWindow.setAdapter(new BindListAdapter(getActivity()));
        MyAdapter myAdapter = new MyAdapter();
        content.setAdapter(myAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("machineCode", getActivity().getIntent().getStringExtra("machineCode"));
        params.addBodyParameter("userID", UserMessage.getManagerId());
        Log.e("xxxxxx", getActivity().getIntent().getStringExtra("machineCode") + "||" + provider.GetItem(position).getCode() + "||");
        mPopWindow.dismiss();
    }

    private void dobind(String url, RequestParams params) {
        Log.e("xxxxxx", url);
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, getString(R.string.data_is_submitted), true, false);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(10000);
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    if (object.getBoolean("d")) {
                        Toast.makeText(getActivity(), R.string.bind_success, Toast.LENGTH_LONG).show();
                        Map<String, String> map = new HashMap<>();
                        map.put("tsy", UserMessage.getTsy());
                        map.put("machineCode", getActivity().getIntent().getStringExtra("machineCode"));
                        mprovider.getData(Constance.GET_SINGLE_MACHINE_URL, map);
                    } else {
                        Toast.makeText(getActivity(), object.getString("err"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), getString(R.string.data_send_fail) + e, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(), getString(R.string.net_work_error) + e + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.choose_cash:
                ShowPrice = "0";
                break;
            case R.id.choose_wechat:
                ShowPrice = "1";
                break;
            case R.id.choose_alipay:
                ShowPrice = "2";
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private ViewHolder mHolder;

        MyAdapter() {
            mprovider = new MachineContentProvider(getActivity(), this);
            mprovider.initview(ManagerName, AgentName);
            Map<String, String> map = new HashMap<>();
            map.put("userID", UserMessage.getManagerId());
            map.put("machineCode", getActivity().getIntent().getStringExtra("machineCode"));
            mprovider.getData(Constance.GET_SINGLE_MACHINE_URL, map);
        }

        @Override
        public int getCount() {
            return mprovider.GetDataSize();
        }

        @Override
        public MachineContentProvider.Item getItem(int position) {
            return mprovider.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.machine_content_list_item_layout, parent, false);
                mHolder = new ViewHolder();
                mHolder.name = (TextView) convertView.findViewById(R.id.name);
                mHolder.content = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.name.setText(getItem(position).getName() + ":");
            mHolder.content.setText(getItem(position).getContent());
            if (getItem(position).getName().equals(getString(R.string.chengzishuliang)) || getItem(position).getName().equals(getString(R.string.beizishuliang))
                    || getItem(position).getName().equals(getString(R.string.gaizishuliang)) || getItem(position).getName().equals(getString(R.string.shengcanshijian))
                    || getItem(position).getName().equals(getString(R.string.chuchangshijian)) || getItem(position).getName().equals(getString(R.string.fangzhiweizhi))) {
                mHolder.name.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
            } else {
                mHolder.name.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
            if (getItem(position).getName().equals(getString(R.string.zhanshidanjia))) {
                mHolder.name.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                String content = getItem(position).getContent();
                if (TextUtils.equals(content, getString(R.string.xianjing))) {
                    mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.cash));
                } else if (TextUtils.equals(content, getString(R.string.weixin))) {
                    mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.wechat));
                } else if (TextUtils.equals(content, getString(R.string.zhifubao))) {
                    mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.alipay));
                }
            }
            if (getItem(position).getName().equals(getString(R.string.shebeizhuangtai))) {
                if (!getActivity().getIntent().getStringExtra("IsOnline").isEmpty()) {
                    if (getActivity().getIntent().getStringExtra("IsOnline").equals("1")) {
                        if (TextUtils.equals(getItem(position).getContent(), getString(R.string.zhengchang))) {
                            mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.sucessgreen));
                        } else {
                            mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                        }
                    } else {
                        mHolder.content.setText(R.string.lixian);
                        mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                    }
                } else {
                    if (TextUtils.equals(getItem(position).getContent(), getString(R.string.zhengchang))) {
                        mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.sucessgreen));
                    } else {
                        mHolder.content.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                    }
                }

            }

            return convertView;
        }

        class ViewHolder {
            TextView name, content;
        }
    }

    private class BindListAdapter extends BaseAdapter {
        private Holder holder;

        private BindListAdapter(Activity activity) {
            provider = new BindNameListContentProvider(activity, this);
        }

        @Override
        public int getCount() {
            return provider.GetDataSize();
        }

        @Override
        public BindNameListContentProvider.Item getItem(int position) {
            return provider.GetItem(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.bind_list_item_layout, null, false);
                holder = new Holder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.name.setText(getItem(position).getName());
            return convertView;
        }

        class Holder {
            TextView name;
        }
    }

    private class SavePriceListener implements DialogInterface.OnClickListener {
        private boolean IsSave;

        SavePriceListener(boolean IsSave) {
            this.IsSave = IsSave;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (IsSave) {
                switch (ShowPrice) {
                    case "0":
                        if (FloatCashPrice == 0) {
                            save_title.setText(R.string.zhanshidanjiebunengwei0);
                            DiaLogUtil.IsCommitDialog(dialog, false);
                            return;
                        }
                        break;
                    case "1":
                        if (Float.parseFloat(wechatinput.getText().toString()) == 0) {
                            save_title.setText(R.string.zhanshidanjiebunengwei0);
                            DiaLogUtil.IsCommitDialog(dialog, false);
                            return;
                        }
                        break;
                    default:
                        if (Float.parseFloat(alipayinput.getText().toString()) == 0) {
                            save_title.setText(R.string.zhanshidanjiebunengwei0);
                            DiaLogUtil.IsCommitDialog(dialog, false);
                            return;
                        }
                        break;
                }

                save_title.setText("");
                DiaLogUtil.IsCommitDialog(dialog, true);
                dialog.dismiss();
                CommitPrice(Constance.EDIT_MACHINE_CONTENT_URL);

            } else {
                DiaLogUtil.IsCommitDialog(dialog, true);
                dialog.dismiss();
            }

        }
    }

    private void CommitPrice(String url) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userID", UserMessage.getManagerId());
        params.addBodyParameter("machineCode", getActivity().getIntent().getStringExtra("machineCode"));
        params.addBodyParameter("params[machineName]", "");
        params.addBodyParameter("params[productTime]", "");
        params.addBodyParameter("params[weChatPrice]", wechatinput.getText().toString());
        params.addBodyParameter("params[factoryTime]", "");
        params.addBodyParameter("params[machineAddr]", "");
        params.addBodyParameter("params[alipayPrice]", alipayinput.getText().toString());
        params.addBodyParameter("params[cashPrice]", String.valueOf(FloatCashPrice));
        params.addBodyParameter("params[showPrice]", ShowPrice);

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, getString(R.string.tijiaozhong), true, false);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(10000);
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                //  Log.i("价格修改提交数据", responseInfo.result+"s");
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    if (object.getString("err").equals("")) {
                        Toast.makeText(getActivity(), getString(R.string.xiugaichenggong), Toast.LENGTH_LONG).show();
                        Map<String, String> map = new HashMap<>();
                        map.put("userID", UserMessage.getManagerId());
                        map.put("machineCode", getActivity().getIntent().getStringExtra("machineCode"));
                        mprovider.getData(Constance.GET_SINGLE_MACHINE_URL, map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), getString(R.string.data_send_fail) + e, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(), getString(R.string.net_work_error) + e + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_machine:
                MachineContentActivity parent = (MachineContentActivity) getActivity();
                parent.ReplaceToEditPage();
                break;
            case R.id.bind:
                if (provider.GetDataSize() == 0) {
                    provider.getdata();
                }
                mPopWindow.show();
                break;
            case R.id.machinetype:
                Intent intent = new Intent();
                intent.setClass(getActivity(), TotalActivity.class);
                intent.putExtra("machineCode", getActivity().getIntent().getStringExtra("machineCode"));
                startActivity(intent);
                break;
            case R.id.revise_price:
                if (ReviseDialog.isShowing()) {
                    ReviseDialog.dismiss();
                } else {
                    ReviseDialog.show();
                }
                break;
            case R.id.add:
                FloatCashPrice += 5;
                CashPrice.setText(String.valueOf(FloatCashPrice));
             /*   mprovider.getItem(4).setContent(String.valueOf(cashprice));
                myAdapter.notifyDataSetChanged();*/
                break;
            case R.id.reduce:
                if (FloatCashPrice - 5 >= 0) {

                    FloatCashPrice -= 5;

                } else {
                    FloatCashPrice = 0;
                }
                CashPrice.setText(String.valueOf(FloatCashPrice));
               /* mprovider.getItem(4).setContent(String.valueOf(cashprice1));
                myAdapter.notifyDataSetChanged();*/
                break;
        }
    }
}
