package fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.Activity.TradeItemActivity;
import com.example.zhazhijiguanlixitong.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ChooseMachineAdapter;
import adapter.PopWindowAdapter;
import contentprovider.BindNameListContentProvider;
import contentprovider.MachineListContentProvider;
import contentprovider.TradeListContentProvider;
import contentprovider.UserMessage;
import customview.DoubleDatePickerDialog;
import httputil.Constance;

public class Fragment_SearchTrade extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private android.support.v4.widget.SwipeRefreshLayout mRefresh;
    private ListView mTrade_list;
    private Button mUppage;
    private Button mDownpage;
    private EditText mJumppage;
    private TextView mJump;
    private TradeListContentProvider mProvider;
    private int currentpage = 1;
    private String default_agentId = "";
    private String default_managerId = "";
    private String default_machineCode = "";
    private String default_isRefund = "";
    private String default_sort = "payTime DESC";
    private String default_choose_startTime = "";
    private String default_choose_endTime = "";
    private TradeListAdapter mTradeListAdapter;
    private ListPopupWindow agentwindow, managerwindow, machinewindow, paymentModewindow, isRefundwindow;
    private BindNameListContentProvider mProvider1, mProvider2;
    private PopWindowAdapter mAdapter1, mAdapter2;
    private List<String> paymentModeArray = new ArrayList<>();
    private List<String> isRefundArray = new ArrayList<>();
    private ArrayAdapter<String> paymentModeAdapter;
    private String default_paymentMode = "";
    private MachineListContentProvider mProvider3;
    private ChooseMachineAdapter mAdapter3;
    private boolean scrollFlag = false;// 标记是否滑动
    private Button backtop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_trade, null, false);
            bindViews();
            initListener();
            initData();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initData() {
        mTrade_list.setAdapter(mTradeListAdapter);
        paymentModeArray.add(getString(R.string.xianjing));
        paymentModeArray.add(getString(R.string.weixin));
        paymentModeArray.add(getString(R.string.zhifubao));
        paymentModeArray.add(getString(R.string.suoyou));
        isRefundArray.add(getString(R.string.weituikuan));
        isRefundArray.add(getString(R.string.yituikuan));
        isRefundArray.add(getString(R.string.suoyou));
        agentwindow.setAdapter(mAdapter1);
        managerwindow.setAdapter(mAdapter2);
        machinewindow.setAdapter(mAdapter3);
        paymentModeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, paymentModeArray);
        paymentModewindow.setAdapter(paymentModeAdapter);
        isRefundwindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, isRefundArray));
        mRefresh.setColorSchemeResources(R.color.orange);
    }


    private void initListener() {
        if (!mRefresh.isRefreshing()) {
            mRefresh.setOnRefreshListener(this);
        }
        mUppage.setOnClickListener(this);
        mDownpage.setOnClickListener(this);
        mJump.setOnClickListener(this);
        backtop.setOnClickListener(this);
        mTrade_list.setOnItemClickListener(this);
        agentwindow.setOnItemClickListener(this);
        managerwindow.setOnItemClickListener(this);
        machinewindow.setOnItemClickListener(this);
        paymentModewindow.setOnItemClickListener(this);
        isRefundwindow.setOnItemClickListener(this);
        mTrade_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        Log.i("最后一项", String.valueOf(mTrade_list.getLastVisiblePosition()));
                        Log.i("总项数", String.valueOf(mTrade_list.getCount()));
                        if (mTrade_list.getLastVisiblePosition() == (mTrade_list
                                .getCount() - 1)) {
                            Log.i("滚动到底部", "XXXX");
                            if (currentpage < mProvider.GetMaxPageAccount()) {
                                currentpage++;
                                UpOrDownPage(false);
                            } else {
                                Toast.makeText(getActivity(), R.string.zheshisuoyoudingdanle, Toast.LENGTH_SHORT).show();
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
// 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
/*                if (scrollFlag
                        && ScreenUtil.getScreenViewBottomHeight(mTrade_list) >= ScreenUtil
                        .getScreenHeight(getActivity())) {
                    if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                        backtop.setVisibility(View.VISIBLE);
                    } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                        backtop.setVisibility(View.GONE);
                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }*/
            }
        });
    }

    public int getScrollY() {

        View c = mTrade_list.getChildAt(0);

        if (c == null) {

            return 0;
        }
        int firstVisiblePosition = mTrade_list.getFirstVisiblePosition();

        int top = c.getTop();

        return -top + firstVisiblePosition * c.getHeight();
    }

    private void bindViews() {
       /* switch (UserMessage.getManagerType()) {
            case "2":
                default_agentId = UserMessage.getManagerId();
                break;
            case "3":
                default_managerId = UserMessage.getManagerId();
                break;
        }*/
        default_agentId = UserMessage.getManagerId();
        mRefresh = (android.support.v4.widget.SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mTrade_list = (ListView) view.findViewById(R.id.trade_list);
        mUppage = (Button) view.findViewById(R.id.uppage);
        mDownpage = (Button) view.findViewById(R.id.downpage);
        mJumppage = (EditText) view.findViewById(R.id.jumppage);
        backtop = (Button) view.findViewById(R.id.top_btn);

        mJump = (TextView) view.findViewById(R.id.jump);
        mTradeListAdapter = new TradeListAdapter(getActivity());
        agentwindow = new ListPopupWindow(getActivity());
        //   agentwindow.setAnchorView(mChooseagent);
        agentwindow.setAnimationStyle(R.style.popwin_anim_style);
        agentwindow.setModal(true);
        managerwindow = new ListPopupWindow(getActivity());
        //   managerwindow.setAnchorView(mChoosemanager);
        managerwindow.setAnimationStyle(R.style.popwin_anim_style);
        managerwindow.setModal(true);
        machinewindow = new ListPopupWindow(getActivity());
        //   machinewindow.setAnchorView(mChoosemachine);
        machinewindow.setAnimationStyle(R.style.popwin_anim_style);
        machinewindow.setModal(true);
        paymentModewindow = new ListPopupWindow(getActivity());
        //  paymentModewindow.setAnchorView(mChoosepaytype);
        paymentModewindow.setAnimationStyle(R.style.popwin_anim_style);
        paymentModewindow.setModal(true);
        isRefundwindow = new ListPopupWindow(getActivity());
        isRefundwindow.setAnimationStyle(R.style.popwin_anim_style);
        //  isRefundwindow.setAnchorView(mChooseisRefund);
        isRefundwindow.setModal(true);
        mAdapter1 = new PopWindowAdapter(getActivity(), mProvider1);
        mProvider1 = mAdapter1.getmProvider();
        mAdapter2 = new PopWindowAdapter(getActivity(), mProvider2);
        mProvider2 = mAdapter2.getmProvider();
        mAdapter3 = new ChooseMachineAdapter(getActivity(), mProvider3);
        mProvider3 = mAdapter3.getmProvider();
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
                        //   mChoose_startTime.setText(startYear + "-" + startMonth + "-" + startDayOfMonth + "至" + endYear + "-" + endMonth + "-" + endDayOfMonth);
                        default_choose_startTime = "between|" + startYear + "-" + startMonth + "-" + startDayOfMonth + " 00:00:00|" + endYear + "-" + endMonth + "-" + endDayOfMonth + " 00:00:00";
                        currentpage = 1;
                        //   UpOrDownPage();
                        //     menu.toggle();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
                break;
            case R.id.choose_endTime:
                Calendar c2 = Calendar.getInstance();
                new DoubleDatePickerDialog(getActivity(), 0, new DoubleDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                        int startMonth = startMonthOfYear + 1;
                        int endMonth = endMonthOfYear + 1;
                        //     mChoose_endTime.setText(startYear + "-" + startMonth + "-" + startDayOfMonth + "至" + endYear + "-" + endMonth + "-" + endDayOfMonth);
                        default_choose_endTime = "between|" + startYear + "-" + startMonth + "-" + startDayOfMonth + " 00:00:00|" + endYear + "-" + endMonth + "-" + endDayOfMonth + " 00:00:00";
                        currentpage = 1;
                        //  UpOrDownPage();
                        //  menu.toggle();
                    }
                }, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DATE), true).show();
                break;
            case R.id.uppage:
                if (currentpage == 1) {
                    new AlertView(getString(R.string.tishi), getString(R.string.yijingshidiyiyele), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                currentpage--;
                //   UpOrDownPage();
                break;
            case R.id.top_btn:
                mTrade_list.smoothScrollToPosition(0);
                backtop.setVisibility(View.GONE);
                break;
            case R.id.togglemenu:
                //  menu.toggle();
                break;
            case R.id.downpage:
                if (mProvider.GetMaxPageAccount() <= currentpage) {
                    new AlertView(getString(R.string.tishi), getString(R.string.yijingshizuihouyiyele), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                currentpage++;
                // UpOrDownPage();
                break;
            case R.id.jump:
                if (mJumppage.getText().toString().isEmpty()) {
                    new AlertView(getString(R.string.tishi),getString(R.string.tiaozhuanyemianbunengweikong), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
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
                //    UpOrDownPage();
                break;
            case R.id.chooseagent:
                if (agentwindow.isShowing()) {
                    agentwindow.dismiss();
                    return;
                }
                if (mProvider1.GetDataSize() != 0) {
                    agentwindow.show();
                    return;
                }
                mProvider1.getdata();
                agentwindow.show();
                break;
            case R.id.choosemanager:
                if (managerwindow.isShowing()) {
                    managerwindow.dismiss();
                    return;
                }
                if (mProvider2.GetDataSize() != 0) {
                    managerwindow.show();
                    return;
                }
                mProvider2.getdata();
                managerwindow.show();
                break;
            case R.id.choosepaytype:
                if (paymentModewindow.isShowing()) {
                    paymentModewindow.dismiss();
                } else {
                    paymentModewindow.show();
                }
                break;
            case R.id.chooseisRefund:
                if (isRefundwindow.isShowing()) {
                    isRefundwindow.dismiss();
                } else {
                    isRefundwindow.show();
                }

                break;
            case R.id.inverted_order_bytradeMoney:
                currentpage = 1;
                if (default_sort.equals("")) {
                    default_sort = "tradeMoney DESC";
                } else {
                    default_sort = "";
                }
                //   menu.toggle();
                //   UpOrDownPage();
                break;
            case R.id.inverted_orderbyendTime:
                currentpage = 1;
                if (default_sort.equals("")) {
                    default_sort = "endTime DESC";
                } else {
                    default_sort = "";
                }
                //   menu.toggle();
                //  UpOrDownPage();
                break;
            case R.id.inverted_orderbystartTime:
                currentpage = 1;
                if (default_sort.equals("")) {
                    default_sort = "startTime DESC";
                } else {
                    default_sort = "";
                }
                //   menu.toggle();
                //  UpOrDownPage();
                break;
            case R.id.choosemachine:
                if (machinewindow.isShowing()) {
                    machinewindow.dismiss();
                    return;
                }
                if (mProvider3.GetDataSetSize() != 0) {
                    machinewindow.show();
                    return;
                }
                Map<String, String> body = new HashMap<>();
                body.put("tsy", UserMessage.getTsy());
                body.put("P", "1");
                body.put("N", String.valueOf(1000));
                mProvider3.getData(Constance.GET_MACHINE_LIST_URL, body, false, 1);
                machinewindow.show();
                break;
        }
    }

    private void UpOrDownPage(boolean refresh) {
        Map<String, String> postbody = new HashMap<>();
        postbody.put("userID", UserMessage.getManagerId());
        postbody.put("P", String.valueOf(currentpage));
        postbody.put("W[agentID]", default_agentId);
        postbody.put("W[managerID]", default_managerId);
        postbody.put("W[isRefund]", default_isRefund);
        postbody.put("W[startTime]", default_choose_startTime);
        postbody.put("W[endTime]", default_choose_endTime);
        postbody.put("W[machineCode]", default_machineCode);
        postbody.put("W[paymentMode]", default_paymentMode);
        postbody.put("sort", default_sort);
        Log.e("传递参数", postbody.toString());
        mProvider.getData(Constance.GET_TRADE_LIST_URL, postbody, refresh);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getAdapter() == mTradeListAdapter) {
            Intent intent = new Intent();
            intent.putExtra("tradeCode", mProvider.getItem(position).getTradeCode());
            intent.setClass(getActivity(), TradeItemActivity.class);
            startActivity(intent);
        } else if (parent.getAdapter() == mAdapter1) {
            //   menu.toggle();
            agentwindow.dismiss();
            if (position == 0) {
                default_agentId = "";
                //   mChooseagent.setText("所有");
            } else {
                //   mChooseagent.setText(mProvider1.GetItem(position - 1).getName());
                default_agentId = mProvider1.GetItem(position - 1).getCode();
            }
            currentpage = 1;
            //    UpOrDownPage();
        } else if (parent.getAdapter() == mAdapter2) {
            //  menu.toggle();
            managerwindow.dismiss();

            if (position == 0) {
                default_managerId = "";
                //    mChoosemanager.setText("所有");
            } else {
                //    mChoosemanager.setText(mProvider2.GetItem(position - 1).getName());
                default_managerId = mProvider2.GetItem(position - 1).getCode();
            }

            currentpage = 1;
            //   UpOrDownPage();
        } else if (parent.getAdapter() == paymentModeAdapter) {
            //   menu.toggle();
            paymentModewindow.dismiss();
            //  mChoosepaytype.setText(paymentModeArray.get(position));
            currentpage = 1;
            if (position == 3) {
                default_paymentMode = "";
            } else {
                default_paymentMode = String.valueOf(position);
            }
            //  UpOrDownPage();
        } else if (parent.getAdapter() == mAdapter3) {
            //  menu.toggle();
            machinewindow.dismiss();
            //   mChoosemachine.setText(mProvider3.getItem(position).getMachineName());
            currentpage = 1;
            default_machineCode = mProvider3.getItem(position).getMachineCode();
            //   UpOrDownPage();
        } else {
            //   menu.toggle();
            isRefundwindow.dismiss();
            //   mChooseisRefund.setText(isRefundArray.get(position));
            currentpage = 1;
            if (position == 2) {
                default_isRefund = "";
            } else {
                default_isRefund = String.valueOf(position);
            }

            //   UpOrDownPage();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentpage = 1;
                UpOrDownPage(true);
                mRefresh.setRefreshing(false);
            }
        }, 2000);
    }


    private class TradeListAdapter extends BaseAdapter {
        private ViewHolder mHolder;

        TradeListAdapter(Activity mActivity) {
            mProvider = new TradeListContentProvider(mActivity, this);
            UpOrDownPage(false);
        }

        @Override
        public int getCount() {
            return mProvider.GetDataSetSize();
        }

        @Override
        public TradeListContentProvider.Item getItem(int position) {
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.trade_list_item_layout, parent, false);
                mHolder.machineName = (TextView) convertView.findViewById(R.id.machineName);
                mHolder.TradeTime = (TextView) convertView.findViewById(R.id.TradeTime);
                mHolder.cupNum = (TextView) convertView.findViewById(R.id.cupNum);
                mHolder.tradeMoney = (TextView) convertView.findViewById(R.id.tradeMoney);
                mHolder.successCup = (TextView) convertView.findViewById(R.id.successCup);
                mHolder.isRefund = (TextView) convertView.findViewById(R.id.isRefund);
                mHolder.realIncome = (TextView) convertView.findViewById(R.id.real_income);
                mHolder.machineAddress = (TextView) convertView.findViewById(R.id.machineAddress);
                mHolder.machineCode = (TextView) convertView.findViewById(R.id.machineCode);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.machineName.setText(getString(R.string.mingcheng)+ getItem(position).getMachineName());
            mHolder.TradeTime.setText(getItem(position).getStartTime());
            mHolder.cupNum.setText(getItem(position).getCupNum());
            mHolder.successCup.setText(getItem(position).getSuccessCup());
            mHolder.tradeMoney.setText(getItem(position).getTradeMoney());
            mHolder.machineCode.setText(getString(R.string.shebeihao) + getItem(position).getMachineCode());
            String[] managerAddress = getItem(position).getMachineAddress().split("\\|");
            if (managerAddress.length == 2) {
                mHolder.machineAddress.setText(managerAddress[1]);
            } else if (managerAddress.length == 3) {
                mHolder.machineAddress.setText(managerAddress[1] + managerAddress[2]);
            } else {
                mHolder.machineAddress.setText("");
            }

            Float income;
            if (!getItem(position).getRefundMoney().isEmpty()) {
                income = Float.parseFloat(getItem(position).getTradeMoney()) - Float.parseFloat(getItem(position).getRefundMoney());
            } else {
                income = Float.parseFloat(getItem(position).getTradeMoney());
            }


            mHolder.realIncome.setText(String.valueOf(income));
            switch (getItem(position).getRefundMoney()) {
                case "":
                    mHolder.isRefund.setVisibility(View.GONE);
                    break;
                case "0":
                    mHolder.isRefund.setVisibility(View.GONE);
                    break;
                default:
                    mHolder.isRefund.setVisibility(View.VISIBLE);
                    mHolder.isRefund.setText(getString(R.string.tuikuan) + getItem(position).getRefundMoney().replace("-", "") + "元");
                    break;
            }
            return convertView;
        }

        class ViewHolder {
            TextView machineName, TradeTime, cupNum, successCup, tradeMoney, isRefund, realIncome, machineAddress, machineCode;
        }
    }
}
