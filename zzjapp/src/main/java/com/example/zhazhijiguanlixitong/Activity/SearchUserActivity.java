package com.example.zhazhijiguanlixitong.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

import contentprovider.UserListContentProvider;
import contentprovider.UserMessage;
import httputil.Constance;


public class SearchUserActivity extends Activity implements
        OnItemClickListener, OnClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnKeyListener, AdapterView.OnItemSelectedListener {
    private ListView user_list;
    private UserListContentProvider mProvider;
    private int currentpage = 1;
    private SwipeRefreshLayout refreshLayout;
    private BroadcastReceiver receiver;
    private String default_activatedType = "1";
    private ListAdapter mAdapter;
    private String default_agentid = UserMessage.getManagerId();
    private boolean scrollFlag = false;// 标记是否滑动
    private Button backtop;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initview();
        initListener();
        getdata(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            finish();
        } else {
            finish();
        }

    }

    private void getdata(boolean refresh) {
        Map<String, String> body = new HashMap<>();
        body.put("P", String.valueOf(currentpage));
        body.put("W[activatedType]", default_activatedType);
        String default_managerType = "3";
        body.put("W[managerType]", default_managerType);
        body.put("W[agentID]", default_agentid);
        body.put("userID", UserMessage.getManagerId());
        Log.e("代理商编号", default_agentid);
        mProvider.getData(Constance.GET_USER_LIST_URL, body, refresh);
    }


    private void initListener() {
        user_list.setOnItemClickListener(this);
        backtop.setOnClickListener(this);
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setOnRefreshListener(this);
        }
        user_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        Log.i("最后一项", String.valueOf(user_list.getLastVisiblePosition()));
                        Log.i("总项数", String.valueOf(user_list.getCount()));
                        if (user_list.getLastVisiblePosition() == (user_list
                                .getCount() - 1)) {
                            Log.i("滚动到底部", "XXXX");
                            if (currentpage < mProvider.GetMaxPageAccount()) {
                                currentpage++;
                                getdata(false);
                            } else {
                                Toast.makeText(getApplication(),getString(R.string.shujujiazaiwanbi), Toast.LENGTH_SHORT).show();
                            }
                            //  toTopBtn.setVisibility(View.VISIBLE);

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

            }
        });
    }

    public int getScrollY() {

        View c = user_list.getChildAt(0);

        if (c == null) {

            return 0;
        }
        int firstVisiblePosition = user_list.getFirstVisiblePosition();

        int top = c.getTop();

        return -top + firstVisiblePosition * c.getHeight();
    }

    private void initview() {
        user_list = (ListView) findViewById(R.id.user_list);
        backtop = (Button) findViewById(R.id.top_btn);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onRefresh();
            }
        };
        IntentFilter filter = new IntentFilter(ManagerContentActivity.action);
        registerReceiver(receiver, filter);
        mAdapter = new ListAdapter(this);
        user_list.setAdapter(mAdapter);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(R.color.orange);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentpage = 1;
                getdata(true);
                refreshLayout.setRefreshing(false);
            }
        }, 2000);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentpage = 1;
        switch (position) {
            case 0:
                default_activatedType = "1";

                break;
            case 1:
                default_activatedType = "0";
                break;
            case 2:
                default_activatedType = "";
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class ListAdapter extends BaseAdapter {
        private ViewHolder mViewHolder;
        private LayoutInflater mInflater;

        private ListAdapter(Activity mActivity) {
            mInflater = mActivity.getLayoutInflater();
            mProvider = new UserListContentProvider(mActivity, this);
        }

        @Override
        public int getCount() {
            return mProvider.GetDataSetSize();
        }

        @Override
        public UserListContentProvider.Item getItem(int position) {
            return mProvider.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.user_list_item_layout,
                        parent, false);
                mViewHolder.UserManagerName = (TextView) convertView
                        .findViewById(R.id.user_manager_name);
                mViewHolder.UserManagerPhone = (TextView) convertView
                        .findViewById(R.id.user_manager_phone);
                mViewHolder.UserManageremail = (TextView) convertView.findViewById(R.id.email);
                mViewHolder.UserManagerId = (TextView) convertView.findViewById(R.id.managerId);
                mViewHolder.UserAddress = (TextView) convertView.findViewById(R.id.address);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.UserManagerName.setText(mProvider.getItem(position)
                    .getManagerName());

            mViewHolder.UserManagerPhone.setText(mProvider.getItem(position)
                    .getManagerTelephone());
            mViewHolder.UserManagerId.setText(mProvider.getItem(position).getManagerId());
            mViewHolder.UserManageremail.setText(mProvider.getItem(position).getManagerEmail());
            if (mProvider.getItem(position).getManagerAddress().contains("|")) {
                String[] managerAddress = mProvider.getItem(position).getManagerAddress().split("\\|");
                if (managerAddress.length == 2) {
                    mViewHolder.UserAddress.setText(managerAddress[1]);
                } else if (managerAddress.length == 3) {
                    mViewHolder.UserAddress.setText(managerAddress[1] + managerAddress[2]);
                }
            } else {
                mViewHolder.UserAddress.setText(mProvider.getItem(position).getManagerAddress());
            }

            return convertView;
        }

        class ViewHolder {
            TextView UserManagerName;
            TextView UserManagerId;
            TextView UserManagerPhone;
            TextView UserManageremail;
            TextView UserAddress;
        }
    }

    public void ChangeUserActivatedType(String url, RequestParams params, final int position) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(10000);
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    if (object.getString("err").equals("")) {
                        Toast.makeText(getApplication(), R.string.xiugaichenggong, Toast.LENGTH_SHORT).show();
                        mProvider.remove(position);
                    } else {
                        Toast.makeText(getApplication(), object.getString("err"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), responseInfo.result + e, Toast.LENGTH_LONG).show();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplication(), getString(R.string.wangluobugeili)+ e + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Intent intent = new Intent();
        intent.setClass(this, SearchMachineActivity.class);
        intent.putExtra("managerId", mProvider.getItem(position).getManagerId());
        intent.putExtra("managerName", mProvider.getItem(position).getManagerName());
        startActivity(intent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    @Override
    public void onClick(View v) {
        if (refreshLayout.isRefreshing()) {
            return;
        }
        switch (v.getId()) {
            case R.id.top_btn:
                user_list.smoothScrollToPosition(0);
                backtop.setVisibility(View.GONE);
                break;
        }

    }
}
