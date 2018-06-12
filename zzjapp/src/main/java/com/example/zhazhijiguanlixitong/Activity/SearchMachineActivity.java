package com.example.zhazhijiguanlixitong.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.DiaLogUtil;
import ViewUtils.ScreenUtil;
import contentprovider.MachineListContentProvider;
import contentprovider.UserMessage;
import httputil.Constance;
import otherutis.MachineTypeEnum;

public class SearchMachineActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, View.OnKeyListener, AdapterView.OnItemLongClickListener, OnItemClickListener, RadioGroup.OnCheckedChangeListener {
    private android.support.v4.widget.SwipeRefreshLayout mRefresh;
    private ListView mMachine_list;
    private int currentpage = 1;
    private MachineListContentProvider mProvider;
    private String default_machine_activatedType = "1";
    private String default_exceptionType = "";
    private BroadcastReceiver receiver;
    private String default_sort = "activatedTime";
    private EditText time;
    private ViewGroup extView;
    private AlertView extview;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private Button backtop;
    private TextView title;
    private RadioGroup stateMachineType;
    private int initpage = 1;
    private ImageView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_machine);
        initview();
        initListener();
        getData(false, currentpage);
    }

    private void getData(boolean refresh, int currentpage) {
        Map<String, String> postbody = new HashMap<>();
        postbody.put("userID", UserMessage.getManagerId());
        postbody.put("P", String.valueOf(currentpage));
        postbody.put("W[activatedType]", default_machine_activatedType);
        postbody.put("W[exceptionType]", default_exceptionType);
        postbody.put("sort", default_sort);
        if (getIntent().getStringExtra("managerId") != null) {
            title.setText(getIntent().getStringExtra("managerName") + getString(R.string.deshebei));
            postbody.put("W[managerID]", getIntent().getStringExtra("managerId"));
        } else {
            switch (UserMessage.getManagerType()) {
                case "2":
                    postbody.put("W[agentID]", UserMessage.getManagerId());
                    break;
                case "3":
                    postbody.put("W[managerID]", UserMessage.getManagerId());
                    break;
            }
        }

        Log.e("传递参数", postbody.toString());
        mProvider.getData(Constance.GET_MACHINE_LIST_URL, postbody, refresh, currentpage);
    }


    private void initListener() {
        stateMachineType.setOnCheckedChangeListener(this);
        mMachine_list.setOnItemClickListener(this);
        //   mMachine_list.setOnItemLongClickListener(this);
        backtop.setOnClickListener(this);
        mMachine_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        Log.i("最后一项", String.valueOf(mMachine_list.getLastVisiblePosition()));
                        Log.i("总项数", String.valueOf(mMachine_list.getCount()));
                        if (mMachine_list.getLastVisiblePosition() == (mMachine_list
                                .getCount() - 1)) {

                            if (currentpage < mProvider.GetMaxPageAccount()) {
                                currentpage++;
                                getData(false, currentpage);
                            } else {
                                Toast.makeText(getApplication(), R.string.shujujiazaiwanbi, Toast.LENGTH_SHORT).show();
                            }

                        }
                        // 判断滚动到顶部
                        if (view.getFirstVisiblePosition() == 0) {
                            backtop.setVisibility(View.GONE);

                        }
                        if (getScrollY() >= 1200) {
                            backtop.setVisibility(View.VISIBLE);
                        } else if (getScrollY() <= 1000) {
                            backtop.setVisibility(View.GONE);
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = true;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
// 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag
                        && ScreenUtil.getScreenViewBottomHeight(mMachine_list) >= ScreenUtil
                        .getScreenHeight(SearchMachineActivity.this)) {
                    if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                        backtop.setVisibility(View.VISIBLE);
                    } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                        backtop.setVisibility(View.GONE);
                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }
        });
        if (!mRefresh.isRefreshing()) {
            mRefresh.setOnRefreshListener(this);
        }
    }

    public int getScrollY() {

        View c = mMachine_list.getChildAt(0);

        if (c == null) {

            return 0;
        }
        int firstVisiblePosition = mMachine_list.getFirstVisiblePosition();

        int top = c.getTop();

        return -top + firstVisiblePosition * c.getHeight();
    }

    private void initview() {

        extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.machine_shutdown_input, null, false);
        extview = new AlertView(getString(R.string.tishi), getString(R.string.inputguanjishijian), null, new String[]{getString(R.string.queding)}, null, this, AlertView.Style.Alert, this).setCancelable(true);
        time = (EditText) extView.findViewById(R.id.time);
        title = (TextView) findViewById(R.id.title);
        stateMachineType = (RadioGroup) findViewById(R.id.stateMachineType);
        backtop = (Button) findViewById(R.id.top_btn);
        tips = (ImageView) findViewById(R.id.tips);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onRefresh();
            }
        };
        IntentFilter filter = new IntentFilter(MachineContentActivity.action);
        registerReceiver(receiver, filter);
        List<String> PopWindowArray = new ArrayList<>();
        PopWindowArray.add(getString(R.string.shengcanshijian));
        PopWindowArray.add(getString(R.string.chuchangshijian));
        PopWindowArray.add(getString(R.string.jihuoshijian));
        mRefresh = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.refresh);
        mMachine_list = (ListView) findViewById(R.id.machine_list);
        MachineListAdapter mAdapter = new MachineListAdapter(this);
        mMachine_list.setAdapter(mAdapter);
        //mJump = (TextView) findViewById(R.id.jump);
        mRefresh.setColorSchemeResources(R.color.orange);
    }

    @Override
    public void onClick(View v) {
        if (mRefresh.isRefreshing()) {
            return;
        }
        switch (v.getId()) {
            case R.id.uppage:
                if (currentpage == 1) {
                    Toast.makeText(getApplication(), R.string.yijingshidiyiyele, Toast.LENGTH_LONG).show();
                    return;
                }
                currentpage--;

                break;

            case R.id.downpage:
                if (mProvider.GetMaxPageAccount() <= currentpage) {
                    Toast.makeText(getApplication(), R.string.yijingshizuihouyiyele, Toast.LENGTH_LONG).show();
                    return;
                }
                currentpage++;

                break;
            case R.id.inverted_order:
                currentpage = 1;
                if (default_sort.equals("")) {
                    default_sort = "productTime DESC";
                } else {
                    default_sort = "";
                }

                break;

            case R.id.top_btn:
                mMachine_list.smoothScrollToPosition(0);
                backtop.setVisibility(View.GONE);
                break;
        }

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                    }
                    break;
            }


        }

        return false;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                boolean isOpen = imm.isActive();
                extview.setMarginBottom(isOpen && hasFocus ? 120 : 0);
                System.out.println(isOpen);
            }
        });
        extview.addExtView(extView);
        extview.show();
        return true;
    }

    @Override
    public void onItemClick(Object o, int position) {
        extview.dismiss();
        String timetext = time.getText().toString();
        if (timetext.isEmpty()) {
            Toast.makeText(this, R.string.noting, Toast.LENGTH_SHORT).show();
        } else {
            RequestParams params = new RequestParams();
            params.addBodyParameter("machineCode", mProvider.getItem(position).getMachineCode());
            params.addBodyParameter("wakeUpTime", timetext);
            dosavecommit(Constance.MACHINE_SHUNTDOWN_URL, params);
        }
    }

    private void dosavecommit(String url, RequestParams params) {
        final Dialog dialog = DiaLogUtil.createLoadingDialog(this, getString(R.string.tijiaozhong));
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(10000);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    if (object.getBoolean("d")) {
                        new AlertView(getString(R.string.tishi), getString(R.string.caozuochenggong), null, new String[]{getString(R.string.queding)}, null, SearchMachineActivity.this, AlertView.Style.Alert, null).setCancelable(true).show();
                    } else {
                        new AlertView(getString(R.string.cuowu), object.getString("err"), null, new String[]{getString(R.string.queding)}, null, SearchMachineActivity.this, AlertView.Style.Alert, null).setCancelable(true).show();
                    }
                } catch (JSONException e) {
                    new AlertView(getString(R.string.cuowu), getString(R.string.canshucuowu) + e, null, new String[]{getString(R.string.queding)}, null, SearchMachineActivity.this, AlertView.Style.Alert, null).setCancelable(true).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                new AlertView(getString(R.string.cuowu), getString(R.string.wangluobugeili) + e + s, null, new String[]{getString(R.string.queding)}, null, SearchMachineActivity.this, AlertView.Style.Alert, null).setCancelable(true).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getApplication(), MachineContentActivity.class);
        intent.putExtra("machinetype", mProvider.getItem(position).getStateMachineType());
        intent.putExtra("machineCode", mProvider.getItem(position).getMachineCode());
        intent.putExtra("IsOnline", mProvider.getItem(position).getIsOnline());
        startActivity(intent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentpage = 1;
                //  default_sort = "";

                getData(true, currentpage);
                mRefresh.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> postbody = new HashMap<>();
        postbody.put("tsy", UserMessage.getTsy());
        postbody.put("P", String.valueOf(currentpage));
        postbody.put("W[exceptionType]", default_exceptionType);
        String finalCode = "";
        postbody.put("W[machineAddress]", finalCode);
        postbody.put("sort", default_sort);
        switch (position) {
            case 0:
                default_machine_activatedType = "";
                postbody.put("W[activatedType]", default_machine_activatedType);
                break;
            case 1:
                default_machine_activatedType = "1";
                postbody.put("W[activatedType]", default_machine_activatedType);
                break;
            case 2:
                default_machine_activatedType = "0";
                postbody.put("W[activatedType]", default_machine_activatedType);
                break;
        }

        switch (UserMessage.getManagerType()) {
            case "2":
                postbody.put("W[agentID]", UserMessage.getManagerId());
                break;
            case "3":
                postbody.put("W[managerID]", UserMessage.getManagerId());
                break;
        }
        Log.e("传递参数", postbody.toString());
        mProvider.getData(Constance.GET_MACHINE_LIST_URL, postbody, false, initpage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.all:
                default_exceptionType = "";
                title.setText(R.string.suoyoushebei);
                initpage = 1;
                break;
            case R.id.normal:
                default_exceptionType = "1";
                title.setText(R.string.zhengchangshebei);
                initpage = 3;
                break;
            case R.id.exception:
                default_exceptionType = "0";
                title.setText(R.string.yichangshebei);
                initpage = 2;
                break;
        }
        currentpage = 1;
        getData(true, initpage);
    }

    private class MachineListAdapter extends BaseAdapter {
        private ViewHolder holder;
        private LayoutInflater inflater;

        private MachineListAdapter(Activity activity) {
            inflater = activity.getLayoutInflater();
            mProvider = new MachineListContentProvider(activity, this);
            mProvider.sentview(tips);
        }

        @Override
        public int getCount() {
            return mProvider.GetDataSetSize();
        }

        @Override
        public MachineListContentProvider.Item getItem(int position) {
            return mProvider.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.machine_list_item_layout1, parent, false);
                holder = new ViewHolder();
                holder.MachineName = (TextView) convertView.findViewById(R.id.machineName);
                holder.machineType = (ImageView) convertView.findViewById(R.id.machineType);
                holder.orangeNum = (TextView) convertView.findViewById(R.id.orangeNum);
                holder.MachineIcon = (ImageView) convertView.findViewById(R.id.machineIcon);
                holder.IsOnline = (ImageView) convertView.findViewById(R.id.IsOnline);
                holder.machine_erro = (TextView) convertView.findViewById(R.id.machine_erro);
                holder.machineCode = (TextView) convertView.findViewById(R.id.machineCode);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.machineCode.setText(getString(R.string.shebeihao) + getItem(position).getMachineCode());
            holder.MachineName.setText(getString(R.string.mingcheng) + getItem(position).getMachineName());
            switch (getItem(position).getStateMachineType()) {
                case "00":
                    holder.machineType.setImageResource(R.mipmap.state_normal);
                    holder.MachineIcon.setBackgroundResource(R.mipmap.machine_normal);
                    holder.machine_erro.setVisibility(View.GONE);
                    // holder.machineType.setTextColor(ContextCompat.getColor(SearchMachineActivity.this, R.color.green));
                    break;
                default:
                    holder.machineType.setImageResource(R.mipmap.state_exception);
                    holder.machine_erro.setVisibility(View.VISIBLE);
                    TypedArray errCodes = getResources().obtainTypedArray(R.array.err_codes);
                    String errDetail = getItem(position).getStateMachineType();
                    Log.d("--------11-------",errDetail);
                    for (int i = 0; i < errCodes.length(); i++) {
                        String[] err = errCodes.getString(i).split("\\|");
                        if(err.length==2){
                            if (TextUtils.equals(err[0], errDetail)) {
                                holder.machine_erro.setText(err[1]);
                            }
                        }
                    }

                    holder.MachineIcon.setBackgroundResource(R.mipmap.machine_ex);
                    //  holder.machineType.setTextColor(ContextCompat.getColor(SearchMachineActivity.this, R.color.red));
                    break;
            }
            switch (getItem(position).getIsOnline()) {
                case "1":
                    holder.IsOnline.setImageResource(R.mipmap.online);
                    break;
                default:
                    holder.IsOnline.setImageResource(R.mipmap.breakline);
                    break;
            }
            switch (getItem(position).getState_orangeNum()) {
                case "":
                    holder.orangeNum.setText(R.string.wucheng);
                    break;
                default:
                    holder.orangeNum.setText(getItem(position).getState_orangeNum());
                    if (Integer.parseInt(getItem(position).getState_orangeNum()) > 50) {
                        holder.orangeNum.setTextColor(ContextCompat.getColor(SearchMachineActivity.this, R.color.sucessgreen));
                    } else {
                        holder.orangeNum.setTextColor(ContextCompat.getColor(SearchMachineActivity.this, R.color.red));
                    }

                    break;
            }


            return convertView;
        }

        class ViewHolder {
            TextView MachineName, orangeNum, machine_erro, machineCode;
            ImageView MachineIcon, IsOnline, machineType;
        }
    }


}
