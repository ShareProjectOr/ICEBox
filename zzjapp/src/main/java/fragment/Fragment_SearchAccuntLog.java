package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.alexswitch.ISwitch;
import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ChooseMachineManagerAdapter;
import bean.AccountLogItem;
import contentprovider.AccountLogListContentProvider;
import contentprovider.UserListContentProvider;
import contentprovider.UserMessage;
import customview.DoubleDatePickerDialog;
import httputil.Constance;


public class Fragment_SearchAccuntLog extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private TextView mInverted_orderbyaccountTime;
    private EditText mChoosemanager;
    private EditText mAccountMode;
    private ISwitch mBackWay;
    private EditText mAccountReason;
    private ISwitch mAccountType;
    private EditText mChoose_startTime;
    private android.support.v4.widget.SwipeRefreshLayout mRefresh;
    private ListView mAccountLog_list;
    private Button mUppage;
    private Button mDownpage;
    private EditText mJumppage;
    private TextView mJump;
    private AccountLogListContentProvider mProvider;
    private AccountLogAdapter mAdapter;
    private String default_managerNum = "";
    private int currentpage = 1;
    private String default_sort = "";
    private String default_backWay = "1";
    private String default_accountTime = "";
    private String default_accountMode = "";
    private String default_accountReason = "";
    private String default_accountType = "1";
    private ChooseMachineManagerAdapter mAdapter1;
    private UserListContentProvider mProvider1;
    private ListPopupWindow managernumwindow, AccountReasonwindow, AccountModewindow;
    private List<String> AccountReasonArray = new ArrayList<>();
    private List<String> AccountModeArray = new ArrayList<>();
    private ArrayAdapter<String> mAdapter2;
    private ArrayAdapter<String> mAdapter3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_accuntlog, null, false);
            bindViews();
            initdata();
            initlistener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initlistener() {
        mBackWay.setOnISwitchOnClickListener(new MyISwitchOnClickListener(mBackWay));
        mAccountType.setOnISwitchOnClickListener(new MyISwitchOnClickListener(mAccountType));
        mChoose_startTime.setOnClickListener(this);
        mChoosemanager.setOnClickListener(this);
        managernumwindow.setOnItemClickListener(this);
        if (!mRefresh.isRefreshing()) {
            mRefresh.setOnRefreshListener(this);
        }
        AccountReasonwindow.setOnItemClickListener(this);
        AccountModewindow.setOnItemClickListener(this);
        mInverted_orderbyaccountTime.setOnClickListener(this);
        mUppage.setOnClickListener(this);
        mDownpage.setOnClickListener(this);
        mJump.setOnClickListener(this);
        mAccountReason.setOnClickListener(this);
        mAccountMode.setOnClickListener(this);
        mAccountLog_list.setOnItemClickListener(this);
    }

    private void initdata() {
        mAccountLog_list.setAdapter(mAdapter);
        managernumwindow.setAdapter(mAdapter1);
        managernumwindow.setAnimationStyle(R.style.popwin_anim_style);
        AccountModewindow.setAdapter(mAdapter3);
        AccountModewindow.setAnimationStyle(R.style.popwin_anim_style);
        AccountReasonwindow.setAdapter(mAdapter2);
        AccountReasonwindow.setAnimationStyle(R.style.popwin_anim_style);
    }

    private void bindViews() {
        mInverted_orderbyaccountTime = (TextView) view.findViewById(R.id.inverted_orderbyaccountTime);
        mChoosemanager = (EditText) view.findViewById(R.id.choosemanager);
        mAccountMode = (EditText) view.findViewById(R.id.accountMode);
        mBackWay = (ISwitch) view.findViewById(R.id.backWay);
        mAccountReason = (EditText) view.findViewById(R.id.accountReason);
        mAccountType = (ISwitch) view.findViewById(R.id.accountType);
        mChoose_startTime = (EditText) view.findViewById(R.id.choose_startTime);
        mRefresh = (android.support.v4.widget.SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mAccountLog_list = (ListView) view.findViewById(R.id.AccountLog_list);
        mUppage = (Button) view.findViewById(R.id.uppage);
        mDownpage = (Button) view.findViewById(R.id.downpage);
        mJumppage = (EditText) view.findViewById(R.id.jumppage);
        mJump = (TextView) view.findViewById(R.id.jump);
        mAdapter = new AccountLogAdapter(getActivity());
        mAdapter1 = new ChooseMachineManagerAdapter(getActivity(), mProvider1);
        mProvider1 = mAdapter1.getmProvider();
        managernumwindow = new ListPopupWindow(getActivity());
        managernumwindow.setAnchorView(mChoosemanager);
        managernumwindow.setModal(true);
        mRefresh.setColorSchemeResources(R.color.orange);
        AccountReasonArray.add(getString(R.string.jiaoyi));
        AccountReasonArray.add(getString(R.string.zhazhishibaituikuan));
        //  AccountReasonArray.add("超时退款");
        AccountModeArray.add(getString(R.string.xianjing));
        AccountModeArray.add(getString(R.string.weixin));
        AccountModeArray.add(getString(R.string.zhifubao));
        mAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, AccountReasonArray);
        mAdapter3 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, AccountModeArray);
        AccountReasonwindow = new ListPopupWindow(getActivity());
        AccountReasonwindow.setAnchorView(mAccountReason);
        AccountReasonwindow.setModal(true);
        AccountModewindow = new ListPopupWindow(getActivity());
        AccountModewindow.setAnchorView(mAccountMode);
        AccountModewindow.setModal(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_startTime:
                Calendar c = Calendar.getInstance();
                new DoubleDatePickerDialog(getActivity(), 0, new DoubleDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                        int startMonth = startMonthOfYear + 1;
                        int endMonth = endMonthOfYear + 1;
                        mChoose_startTime.setText(startYear + "-" + startMonth + "-" + startDayOfMonth + getString(R.string.zhi) + endYear + "-" + endMonth + "-" + endDayOfMonth);
                        default_accountTime = "between|" + startYear + "-" + startMonth + "-" + startDayOfMonth + " 00:00:00|" + endYear + "-" + endMonth + "-" + endDayOfMonth + " 00:00:00";
                        currentpage = 1;
                        UpOrDownPage();

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
                break;
            case R.id.choosemanager:
                if (managernumwindow.isShowing()) {
                    managernumwindow.dismiss();
                    return;
                }
                if (mProvider1.GetDataSetSize() != 0) {
                    managernumwindow.show();
                    return;
                }
                Map<String, String> body = new HashMap<>();
                body.put("P", "1");
                body.put("N", "1000");
                if (UserMessage.getManagerType().equals("2")) {
                    body.put("W[agentID]", UserMessage.getManagerId());
                }
                body.put("W[activatedType]", "1");
                body.put("W[managerType]", "3");
                body.put("tsy", UserMessage.getTsy());
                mProvider1.getData(Constance.GET_USER_LIST_URL, body, false);
                managernumwindow.show();
                break;
            case R.id.accountReason:
                if (AccountReasonwindow.isShowing()) {
                    AccountReasonwindow.dismiss();
                } else {
                    AccountReasonwindow.show();
                }
                break;
            case R.id.accountMode:
                if (AccountModewindow.isShowing()) {
                    AccountModewindow.dismiss();
                } else {
                    AccountModewindow.show();
                }
                break;
            case R.id.inverted_orderbyaccountTime:
                if (default_sort.equals("")) {
                    default_sort = "accountTime DESC";
                } else {
                    default_sort = "";
                }
                currentpage = 1;
                UpOrDownPage();
                break;
            case R.id.uppage:
                if (currentpage == 1) {
                    new AlertView(getString(R.string.tishi), getString(R.string.yijingshidiyiyele), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                currentpage--;
                UpOrDownPage();
                break;
            case R.id.downpage:
                if (mProvider.GetMaxPageAccount() <= currentpage) {
                    new AlertView(getString(R.string.tishi), getString(R.string.yijingshizuihouyiyele), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                currentpage++;
                UpOrDownPage();
                break;
            case R.id.jump:
                if (mJumppage.getText().toString().isEmpty()) {
                    new AlertView(getString(R.string.tishi), getString(R.string.tiaozhuanyemianbunengweikong), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (mProvider.GetMaxPageAccount() < Integer.parseInt(mJumppage.getText().toString())) {
                    new AlertView(getString(R.string.tishi), getString(R.string.zuiduonengtiaozhuandao) + mProvider.GetMaxPageAccount() + getString(R.string.ye), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (mJumppage.getText().toString().equals("0")) {
                    new AlertView(getString(R.string.tishi), getString(R.string.tiaozhuanyemianbunengwei0), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (mJumppage.getText().toString().equals("")) {
                    new AlertView(getString(R.string.tishi), getString(R.string.tianxietiaozhuanyemian), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                currentpage = Integer.parseInt(mJumppage.getText().toString());
                UpOrDownPage();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter() == mAdapter) {
            new AlertView(getString(R.string.xinxi), getString(R.string.jieqibianhao) + mProvider.getItem(position).getMachineCode()
                    + getString(R.string.chuzhangyuanying) + mProvider.getItem(position).getAccountReason()
                    + getString(R.string.jiaoyidanhao) + mProvider.getItem(position).getTradeCode()
                    + getString(R.string.ruzhangfangshi) + mProvider.getItem(position).getAccountMode(), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
        } else if (parent.getAdapter() == mAdapter1) {
            managernumwindow.dismiss();
            default_managerNum = mProvider1.getItem(position).getManagerNum();
            mChoosemanager.setText(mProvider1.getItem(position).getManagerName());
            currentpage = 1;
            UpOrDownPage();

        } else if (parent.getAdapter() == mAdapter2) {
            AccountReasonwindow.dismiss();
            default_accountReason = String.valueOf(position);
            mAccountReason.setText(AccountReasonArray.get(position));
            currentpage = 1;
            UpOrDownPage();
        } else {
            AccountModewindow.dismiss();
            default_accountMode = String.valueOf(position);
            mAccountMode.setText(AccountModeArray.get(position));
            currentpage = 1;
            UpOrDownPage();
        }
    }

    @Override
    public void onRefresh() {
        initdefault();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UpOrDownPage();
                mRefresh.setRefreshing(false);
            }
        }, 2000);
    }

    private void initdefault() {
        mChoosemanager.setText("");
        mAccountReason.setText("");
        mAccountMode.setText("");
        mChoose_startTime.setText("");
        mAccountType.setIsOpen(false);
        mBackWay.setIsOpen(false);
        default_managerNum = "";
        default_accountReason = "";
        default_accountMode = "";
        currentpage = 1;
        default_accountTime = "";
        default_accountType = "1";
        default_backWay = "1";
    }

    private class MyISwitchOnClickListener implements ISwitch.ISwitchOnClickListeners {
        private View view;

        MyISwitchOnClickListener(View view) {
            this.view = view;
        }

        @Override
        public void open() {
            switch (view.getId()) {
                case R.id.backWay:
                    default_backWay = "0";
                    currentpage = 1;
                    UpOrDownPage();
                    break;
                case R.id.accountType:
                    default_accountType = "0";
                    currentpage = 1;
                    UpOrDownPage();
                    break;
            }
        }

        @Override
        public void close() {
            switch (view.getId()) {
                case R.id.backWay:
                    default_backWay = "1";
                    currentpage = 1;
                    UpOrDownPage();
                    break;
                case R.id.accountType:
                    default_accountType = "1";
                    currentpage = 1;
                    UpOrDownPage();
                    break;
            }
        }
    }

    private void UpOrDownPage() {
        Map<String, String> postbody = new HashMap<>();
        postbody.put("tsy", UserMessage.getTsy());
        postbody.put("P", String.valueOf(currentpage));
        postbody.put("W[managerNum]", default_managerNum);
        postbody.put("W[backWay]", default_backWay);
        postbody.put("W[accountTime]", default_accountTime);
        postbody.put("W[accountMode]", default_accountMode);
        postbody.put("W[accountReason]", default_accountReason);
        postbody.put("W[accountType]", default_accountType);
        postbody.put("sort", default_sort);
        mProvider.getData(Constance.GET_ACCOUNT_LOG_LIST_URL, postbody);
    }

    private class AccountLogAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ViewHolder mHolder;

        AccountLogAdapter(Activity mActivity) {
            inflater = mActivity.getLayoutInflater();
            mProvider = new AccountLogListContentProvider(mActivity, this);
            UpOrDownPage();
        }

        @Override
        public int getCount() {
            return mProvider.GetDataSetSize();
        }

        @Override
        public AccountLogItem getItem(int position) {
            return mProvider.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.accountlog_list_item_layout, null, false);
                mHolder.accountId = (TextView) convertView.findViewById(R.id.accountId);
                mHolder.accountType = (TextView) convertView.findViewById(R.id.accountType);
                mHolder.accountTime = (TextView) convertView.findViewById(R.id.accountTime);
                mHolder.accountMoney = (TextView) convertView.findViewById(R.id.accountMoney);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.accountId.setText(getItem(position).getAccountId());
            mHolder.accountMoney.setText(getItem(position).getAccountMoney());
            mHolder.accountTime.setText(getItem(position).getAccountTime());
            mHolder.accountType.setText(getItem(position).getAccountType());
            return convertView;
        }

        private class ViewHolder {
            TextView accountId, accountType, accountTime, accountMoney;
        }
    }
}
