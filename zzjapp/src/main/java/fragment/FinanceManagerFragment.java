package fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zhazhijiguanlixitong.Activity.DetailsDividerActivity;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.ActionItem;
import ViewUtils.TitlePopup;
import adapter.FaniceListAdapter;
import contentprovider.FinanceListContentProvider;
import contentprovider.UserMessage;
import httputil.Constance;
import httputil.HttpRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceManagerFragment extends Fragment implements View.OnClickListener, TitlePopup.OnItemOnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private View view;
    private android.support.v4.widget.SwipeRefreshLayout mRefresh;
    private Button mUppage;
    private Button mDownpage;
    private TextView mJump;
    private String default_type = "";
    private FinanceListContentProvider mProvider;
    private String default_agentID = "";
    private int currentpage = 1;
    private ListView divider_list;
    private TitlePopup titlePopup;
    private boolean isfirst = true;//只初始化一次
    private List<String> DataSet = new ArrayList<>();
    private int SelectPosition;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private Button backtop;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_finance_manager, container, false);
            bindViews();
            initListener();
            getData(false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }


    private void getData(boolean refresh) {
        Map<String, String> postbody = new HashMap<>();
        postbody.put("userID", UserMessage.getManagerId());
        postbody.put("P", String.valueOf(currentpage));
        postbody.put("W[agentID]", default_agentID);
        postbody.put("W[type]", default_type);
        postbody.put("sort", "creatTime");
        mProvider.getdata(Constance.GET_DIVIDER_LIST_URL, postbody, refresh);
    }

    private void initListener() {
        mUppage.setOnClickListener(this);
        mDownpage.setOnClickListener(this);
        backtop.setOnClickListener(this);
        mJump.setOnClickListener(this);
        titlePopup.setItemOnClickListener(this);
        if (!mRefresh.isRefreshing()) {
            mRefresh.setOnRefreshListener(this);
        }

        divider_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        Log.i("最后一项", String.valueOf(divider_list.getLastVisiblePosition()));
                        Log.i("总项数", String.valueOf(divider_list.getCount()));
                        if (divider_list.getLastVisiblePosition() == (divider_list
                                .getCount() - 1)) {
                            Log.i("滚动到底部", "XXXX");
                            if (currentpage < mProvider.GetMaxPageAccount()) {
                                currentpage++;
                                getData(false);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.shujujiazaiwanbi), Toast.LENGTH_SHORT).show();
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

            }
        });
    }

    public int getScrollY() {

        View c = divider_list.getChildAt(0);

        if (c == null) {

            return 0;
        }
        int firstVisiblePosition = divider_list.getFirstVisiblePosition();

        int top = c.getTop();

        return -top + firstVisiblePosition * c.getHeight();
    }

    private void bindViews() {
        switch (UserMessage.getManagerType()) {
            case "2":
                default_agentID = UserMessage.getManagerId();
                break;
            default:
                default_agentID = "";
                break;
        }
        mRefresh = (android.support.v4.widget.SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefresh.setColorSchemeResources(R.color.orange);
        divider_list = (ListView) view.findViewById(R.id.divider_list);
        divider_list.setOnItemClickListener(this);
        backtop = (Button) view.findViewById(R.id.top_btn);
        mUppage = (Button) view.findViewById(R.id.uppage);
        mDownpage = (Button) view.findViewById(R.id.downpage);
        mJump = (TextView) view.findViewById(R.id.jump);
        titlePopup = new TitlePopup(getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.addAction(new ActionItem(getActivity(),getString(R.string.suoyou)), 0);
        FaniceListAdapter mAdapter = new FaniceListAdapter(getActivity());
        mProvider = mAdapter.getmProvider();
        divider_list.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_btn:
                divider_list.smoothScrollToPosition(0);
                backtop.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onItemClick(ActionItem item, int position) {
        switch (position) {
            case 0:
                default_agentID = "";
                break;
            default:
                default_agentID = DataSet.get(position - 1);
                break;

        }
        getData(false);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentpage = 1;
                getData(true);
                mRefresh.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mRefresh.isRefreshing()) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), DetailsDividerActivity.class);
        intent.putExtra("divideID", mProvider.getItem(position).getDivideID());
        startActivity(intent);
    }


}
