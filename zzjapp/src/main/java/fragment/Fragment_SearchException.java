package fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.ObjectAnimatorUtil;
import adapter.PopWindowAdapter;
import contentprovider.BindNameListContentProvider;
import contentprovider.ExceptionContentProvider;
import contentprovider.UserMessage;
import customview.CustormPopWindow;
import customview.DoubleDatePickerDialog;
import customview.SlidingMenu;
import httputil.Constance;
import otherutils.FormatLongToTimeStr;


public class Fragment_SearchException extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener, View.OnKeyListener {
    private View view;
    private TextView mInverted_order;
    private EditText mSearch_text;
    private EditText mChooseagent;
    private EditText mChoosemanager;
    private EditText mIsDeal;
    private android.support.v4.widget.SwipeRefreshLayout mRefresh;
    private ListView mException_list;
    private Button mUppage;
    private Button mDownpage;
    private EditText mJumppage;
    private TextView mJump;
    private EditText mchoose_creatTime, mchoose_dealTime;
    private CustormPopWindow agentwindow, managerwindow, isdealwindow;
    private BindNameListContentProvider mProvider1, mProvider2;
    private List<String> IsDealArray = new ArrayList<>();
    private PopWindowAdapter mAdapter1, mAdapter2;
    private int currentpage = 1;
    private String default_sort = "";
    private String default_isDeal = "0";
    private String default_choose_creatTime = "";
    private String default_choose_dealTime = "";
    private ExceptionContentProvider Exprovider;
    private String default_agentId = "";
    private String default_managerId = "";
    private ListAdapter mAdapter;
    private SlidingMenu menu;
    private Button togglemenu;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getV(inflater);
    }

    private View getV(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_exception, null);
            initview();
            initvisible();
            initdata();
            initlistener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initdata() {
        switch (UserMessage.getManagerType()) {
            case "2":
                default_agentId = UserMessage.getManagerId();
                break;
            case "3":
                default_managerId = UserMessage.getManagerId();
                break;
        }

        agentwindow.setAdapter(mAdapter1);
        managerwindow.setAdapter(mAdapter2);
        mException_list.setAdapter(mAdapter);
        isdealwindow.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, IsDealArray));
        mRefresh.setColorSchemeResources(R.color.orange);
    }

    private void initvisible() {
        switch (UserMessage.getManagerType()) {
            case "3":
                mChooseagent.setVisibility(View.GONE);
                mChoosemanager.setVisibility(View.GONE);
                break;
            case "2":
                mChooseagent.setVisibility(View.GONE);
                mChoosemanager.setVisibility(View.VISIBLE);
                break;
            default:
                mChooseagent.setVisibility(View.VISIBLE);
                mChoosemanager.setVisibility(View.GONE);
                break;
        }
    }

    private void initlistener() {
        mChooseagent.setOnClickListener(this);
        mChoosemanager.setOnClickListener(this);
        mIsDeal.setOnClickListener(this);
        mUppage.setOnClickListener(this);
        mDownpage.setOnClickListener(this);
        mJump.setOnClickListener(this);
        mInverted_order.setOnClickListener(this);
        mIsDeal.setOnClickListener(this);
        mSearch_text.setOnKeyListener(this);
        mchoose_creatTime.setOnClickListener(this);
        mchoose_dealTime.setOnClickListener(this);
        managerwindow.setOnItemClickListener(this);
        mException_list.setOnItemClickListener(this);
        agentwindow.setOnItemClickListener(this);
        isdealwindow.setOnItemClickListener(this);
        if (!mRefresh.isRefreshing()) {
            mRefresh.setOnRefreshListener(this);
        }
        togglemenu.setOnClickListener(this);
    }

    private void initview() {
        mInverted_order = (TextView) view.findViewById(R.id.inverted_order);
        mSearch_text = (EditText) view.findViewById(R.id.search_text);
        mChooseagent = (EditText) view.findViewById(R.id.chooseagent);
        mChoosemanager = (EditText) view.findViewById(R.id.choosemanager);
        mIsDeal = (EditText) view.findViewById(R.id.isDeal);
        mchoose_creatTime = (EditText) view.findViewById(R.id.choose_creatTime);
        mchoose_dealTime = (EditText) view.findViewById(R.id.choose_dealTime);
        mRefresh = (android.support.v4.widget.SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mException_list = (ListView) view.findViewById(R.id.exception_list);
        togglemenu = (Button) view.findViewById(R.id.togglemenu);
        menu = (SlidingMenu) view.findViewById(R.id.menu);
        mUppage = (Button) view.findViewById(R.id.uppage);
        mDownpage = (Button) view.findViewById(R.id.downpage);
        mJumppage = (EditText) view.findViewById(R.id.jumppage);
        mJump = (TextView) view.findViewById(R.id.jump);
        IsDealArray.add(getString(R.string.weichuliguzhang));
        IsDealArray.add(getString(R.string.yichuliguzhang));
        agentwindow = new CustormPopWindow(getActivity());
        agentwindow.setAnimationStyle(R.style.popwin_anim_style);
        agentwindow.setAnchorView(mChooseagent);
        agentwindow.setModal(true);
        managerwindow = new CustormPopWindow(getActivity());
        managerwindow.setAnimationStyle(R.style.popwin_anim_style);
        managerwindow.setAnchorView(mChoosemanager);
        managerwindow.setModal(true);
        isdealwindow = new CustormPopWindow(getActivity());
        isdealwindow.setAnimationStyle(R.style.popwin_anim_style);
        isdealwindow.setAnchorView(mIsDeal);
        isdealwindow.setModal(true);
        mAdapter1 = new PopWindowAdapter(getActivity(), mProvider1);
        mProvider1 = mAdapter1.getmProvider();
        mAdapter2 = new PopWindowAdapter(getActivity(), mProvider2);
        mProvider2 = mAdapter2.getmProvider();
        mAdapter = new ListAdapter(getActivity());
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

    private void showbuttonAnima(View view, boolean b) {
        AnimatorSet animatorSet = new AnimatorSet();
        if (b) {
            ObjectAnimator AnimatorX = ObjectAnimatorUtil.ScaleAnimator(view, "X", 1f, 0.8f, 0.6f);
            animatorSet.play(AnimatorX);
        } else {
            ObjectAnimator AnimatorX = ObjectAnimatorUtil.ScaleAnimator(view, "X", 0.6f, 0.8f, 1f);
            animatorSet.play(AnimatorX);
        }
        animatorSet.setDuration(200);
        animatorSet.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseagent:
                if (mProvider1.GetDataSize() != 0) {
                    agentwindow.show();
                    return;
                }
                mProvider1.getdata();
                agentwindow.show();
                break;
            case R.id.togglemenu:
                menu.toggle();
                showbuttonAnima(togglemenu, menu.isOpen());
                break;
            case R.id.inverted_order:
                currentpage = 1;
                if (default_sort.equals("")) {
                    default_sort = "creatTime DESC";
                } else {
                    default_sort = "";
                }
                UpOrDownPage();
                break;
            case R.id.jump:
                if (mJumppage.getText().toString().isEmpty()) {
                    new AlertView(getString(R.string.tishi), getString(R.string.tiaozhuanyemianbunengweikong), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (Exprovider.GetMaxPageAccount() < Integer.parseInt(mJumppage.getText().toString())) {
                    new AlertView(getString(R.string.tishi),getString(R.string.zuiduonengtiaozhuandao) + Exprovider.GetMaxPageAccount() +getString(R.string.ye), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
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
            case R.id.choosemanager:
                if (mProvider2.GetDataSize() != 0) {
                    managerwindow.show();
                    return;
                }
                mProvider2.getdata();
                managerwindow.show();
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
                if (Exprovider.GetMaxPageAccount() <= currentpage) {
                    new AlertView(getString(R.string.tishi), getString(R.string.yijingshizuihouyiyele), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();

                    return;
                }
                currentpage++;
                UpOrDownPage();
                break;
            case R.id.isDeal:
                isdealwindow.show();
                break;
            case R.id.choose_creatTime:
                Calendar c = Calendar.getInstance();
                new DoubleDatePickerDialog(getActivity(), 0, new DoubleDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                        int startMonth = startMonthOfYear + 1;
                        int endMonth = endMonthOfYear + 1;
                        mchoose_creatTime.setText(startYear + "-" + startMonth + "-" + startDayOfMonth + getString(R.string.zhi) + endYear + "-" + endMonth + "-" + endDayOfMonth);
                        default_choose_creatTime = "between|" + startYear + "-" + startMonth + "-" + startDayOfMonth + " 00:00:00|" + endYear + "-" + endMonth + "-" + endDayOfMonth + " 00:00:00";
                        currentpage = 1;
                        UpOrDownPage();

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();

                break;
            case R.id.choose_dealTime:
                Calendar c2 = Calendar.getInstance();
                new DoubleDatePickerDialog(getActivity(), 0, new DoubleDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                        int startMonth = startMonthOfYear + 1;
                        int endMonth = endMonthOfYear + 1;
                        mchoose_dealTime.setText(startYear + "-" + startMonth + "-" + startDayOfMonth + "至" + endYear + "-" + endMonth + "-" + endDayOfMonth);
                        default_choose_dealTime = "between|" + startYear + "-" + startMonth + "-" + startDayOfMonth + " 00:00:00|" + endYear + "-" + endMonth + "-" + endDayOfMonth + " 00:00:00";
                        currentpage = 1;
                        UpOrDownPage();
                    }
                }, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DATE), true).show();
                break;
        }
    }

    private void UpOrDownPage() {
        Map<String, String> postbody = new HashMap<>();
        postbody.put("tsy", UserMessage.getTsy());
        postbody.put("P", String.valueOf(currentpage));
        postbody.put("W[agentID]", default_agentId);
        postbody.put("W[managerID]", default_managerId);
        postbody.put("W[isDeal]", default_isDeal);
        postbody.put("W[creatTime]", default_choose_creatTime);
        postbody.put("W[dealTime]", default_choose_dealTime);
        postbody.put("sort", default_sort);
        Exprovider.getData(Constance.GET_MACHINE_EXCEPTION_URL, postbody);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getAdapter() == mAdapter1) {
            menu.toggle();
            agentwindow.dismiss();
            if (position == 0) {
                mChooseagent.setText(getString(R.string.suoyou));
                default_agentId = "";
            } else {
                mChooseagent.setText(mProvider1.GetItem(position - 1).getName());
                default_agentId = mProvider1.GetItem(position - 1).getCode();
            }
            currentpage = 1;
            UpOrDownPage();
        } else if (parent.getAdapter() == mAdapter2) {
            menu.toggle();
            managerwindow.dismiss();
            if (position == 0) {
                mChoosemanager.setText(getString(R.string.suoyou));
                default_managerId = "";
            } else {
                mChoosemanager.setText(mProvider2.GetItem(position - 1).getName());
                default_managerId = mProvider2.GetItem(position - 1).getCode();
            }
            currentpage = 1;
            UpOrDownPage();
        } else if (parent.getAdapter() == mAdapter) {
            String dealmsg;
            switch (Exprovider.getItem(position).getDealTime()) {
                case "":
                    dealmsg = getString(R.string.gaiguzhangshangweichui);
                    break;
                default:
                    dealmsg = Exprovider.getItem(position).getDealTime();
                    break;
            }
            new AlertView(getString(R.string.xinxi), getString(R.string.guzhangchuxianshijian) + Exprovider.getItem(position).getCreatTime() + getString(R.string.guzhangchulishijian)
                    + dealmsg + getString(R.string.guzhangxiangxixinxi) + Exprovider.getItem(position).getExceptionMsg(), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
        } else {
            menu.toggle();
            isdealwindow.dismiss();
            mIsDeal.setText(IsDealArray.get(position));
            default_isDeal = String.valueOf(position);
            currentpage = 1;
            UpOrDownPage();
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
                        dosearchpost();
                    }
                    break;
            }


        }

        return false;
    }

    private void dosearchpost() {
        if (TextUtils.equals("", mSearch_text.getText().toString())) {
            new AlertView(getString(R.string.tishi), getString(R.string.qingshurushousuoneirong), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
            return;
        }
        initdefault();
        Map<String, String> postbody = new HashMap<>();
        postbody.put("tsy", UserMessage.getTsy());
        postbody.put("P", String.valueOf(currentpage));
        postbody.put("W[agentID]", default_agentId);
        postbody.put("W[managerID]", default_managerId);
        postbody.put("W[isDeal]", default_isDeal);
        postbody.put("W[creatTime]", default_choose_creatTime);
        postbody.put("W[dealTime]", default_choose_dealTime);
        postbody.put("sort", default_sort);
        postbody.put("keyword", mSearch_text.getText().toString());
        Exprovider.getData(Constance.GET_MACHINE_EXCEPTION_URL, postbody);
    }

    private void initdefault() {
        currentpage = 1;
        switch (UserMessage.getManagerType()) {
            case "2":
                default_managerId = "";
                break;
            case "3":
                break;
            default:
                default_agentId = "";
                break;
        }
        default_sort = "";
        default_choose_dealTime = "";
        default_choose_creatTime = "";
        default_isDeal = "0";
        mchoose_dealTime.setText("");
        mchoose_creatTime.setText("");
        mChoosemanager.setText("");
        mChooseagent.setText("");
        mIsDeal.setText(IsDealArray.get(0));
        mSearch_text.setText("");
    }


    private class ListAdapter extends BaseAdapter {
        private ViewHolder mHolder;

        ListAdapter(Activity activity) {
            Exprovider = new ExceptionContentProvider(activity, this);
            UpOrDownPage();
        }

        @Override
        public int getCount() {
            return Exprovider.GetDataSetSize();
        }

        @Override
        public ExceptionContentProvider.Item getItem(int position) {
            return Exprovider.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.user_list_item_layout,
                        parent, false);
                mHolder.code = (TextView) convertView.findViewById(R.id.user_manager_name);
                mHolder.exp = (TextView) convertView.findViewById(R.id.managerId);
                mHolder.sendtime = (TextView) convertView.findViewById(R.id.user_manager_phone);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.code.setText(getItem(position).getMachineCode());
            mHolder.exp.setText(getItem(position).getExceptionId());
            switch (getItem(position).getSpendTime()) {
                case "":
                    mHolder.sendtime.setText(R.string.gaiguzhangshangweichui);
                    break;
                default:
                    Long timelong = Long.parseLong(getItem(position).getSpendTime());
                    mHolder.sendtime.setText(FormatLongToTimeStr.formatLongToTimeStr(getActivity(),timelong));
                    break;
            }

            return convertView;
        }

        class ViewHolder {
            TextView code, exp, sendtime;
        }
    }

}
