package fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adapter.MyAdapter;
import adapter.StatictisResultAdapter;
import contentprovider.UserMessage;
import customview.DoubleDatePickerDialog;
import customview.SlidingMenu;
import entity.ItemMachine;
import entity.ItemUser;
import otherutils.TimeUtil;

import static otherutils.TimeUtil.compareTime;
import static otherutils.TimeUtil.str2Date;

/**
 * Created by WH on 2017/7/14.
 */

public class FragmentFaultTotalData extends Fragment implements View.OnClickListener, StatictisResultAdapter. IsLoadingSuccess{
    private View cacheView;
    private Button mAppoint_agent;
    private Button mAppoint_machine_manager;
    private Button mAppoint_machine;
    private TextView mCreateTime, mLoadingText;
    private LinearLayout mLoadingLayout;
    private ImageView mLoadingImage;
    private LinearLayout mListLayout;
    private ListView mResultList;
    private ListPopupWindow mAgentWindow;
    private ListPopupWindow mManagerWindow;
    private ListPopupWindow mMachineWindow;
    private MyAdapter mAgentAdapter, mManagerAdapter, mMachineAdapter;
    private StatictisResultAdapter mTotalAdapter;
    private Map<String, String> mTotalParams;
    private SlidingMenu mMenu;
    private DoubleDatePickerDialog mDatePickerDialog;
    private Animation mAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null) {
            cacheView = inflater.inflate(R.layout.fragment_fault_data, null);
            bindViews();
            initData();
        }
        ViewGroup parent = (ViewGroup) cacheView.getParent();
        if (parent != null) {
            parent.removeView(cacheView);
        }
        return cacheView;
    }

    private void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.orange_rotate);
        mLoadingImage.startAnimation(mAnimation);

        mTotalParams = new HashMap<>();

        mAgentWindow = new ListPopupWindow(getActivity());
        mAgentAdapter = new MyAdapter(getActivity());
        mAgentWindow.setModal(true);
        mAgentWindow.setAdapter(mAgentAdapter);

        //初始化机器管理员的适配器
        mManagerWindow = new ListPopupWindow(getActivity());
        mManagerAdapter = new MyAdapter(getActivity());
        mManagerWindow.setModal(true);
        mManagerWindow.setAdapter(mManagerAdapter);

        mMachineWindow = new ListPopupWindow(getActivity());
        mMachineAdapter = new MyAdapter(getActivity());
        mMachineWindow.setModal(true);
        mMachineWindow.setAdapter(mMachineAdapter);

        mTotalAdapter = new StatictisResultAdapter(getActivity());
        mTotalAdapter.setLoadingSuccessCallback(this);
        mResultList.setAdapter(mTotalAdapter);
        initLoadingData();
        selectTime();
    }

    private void initLoadingData() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DATE));
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (day.length() < 2) {
            day = "0" + day;
        }
        String startTime = year + "-" + month + "-" + day + " 23:59:59";
        calendar.add(Calendar.DATE, -10);
        String endYear = String.valueOf(calendar.get(Calendar.YEAR));
        String endMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String endDay = String.valueOf(calendar.get(Calendar.DATE));
        if (endMonth.length() < 2) {
            endMonth = "0" + endMonth;
        }
        if (endDay.length() < 2) {
            endDay = "0" + endDay;
        }
        String endTime = endYear + "-" + endMonth + "-" + endDay + " 00:00:00";
        String body = "between|" + endTime + "|" + startTime;
        mCreateTime.setText(endYear + "-" + endMonth + "-" + endDay + "至" + year + "-" + month + "-" + day);
        mTotalParams.put("creatTime", body);
        mTotalAdapter.getExceptionTotal(mTotalParams);
        mTotalAdapter.setSlidingMenu(mMenu);
    }


    private void bindViews() {
        mAppoint_agent = (Button) cacheView.findViewById(R.id.appoint_agent);
        mAppoint_machine_manager = (Button) cacheView.findViewById(R.id.appoint_machine_manager);
        mAppoint_machine = (Button) cacheView.findViewById(R.id.appoint_machine);
        mCreateTime = (TextView) cacheView.findViewById(R.id.create_time);
        mResultList = (ListView) cacheView.findViewById(R.id.appoint_list);
        mLoadingText = (TextView) cacheView.findViewById(R.id.loading_text);
        mLoadingLayout = (LinearLayout) cacheView.findViewById(R.id.loading_layout);
        mLoadingImage = (ImageView) cacheView.findViewById(R.id.loading_img);
        mListLayout = (LinearLayout) cacheView.findViewById(R.id.list_layout);
        mMenu = (SlidingMenu) cacheView.findViewById(R.id.menu);
        Button mTogglemenu = (Button) cacheView.findViewById(R.id.togglemenu);

        mTogglemenu.setOnClickListener(this);
        mLoadingText.setOnClickListener(this);
        mAppoint_agent.setOnClickListener(this);
        mAppoint_machine_manager.setOnClickListener(this);
        mAppoint_machine.setOnClickListener(this);
        mCreateTime.setOnClickListener(this);

        hideView();
    }

    private void hideView() {

        switch (UserMessage.getManagerType()) {
            case "0":
                break;
            case "1":
                break;
            case "2":
                mAppoint_agent.setVisibility(View.GONE);
                break;
            case "3":
                mAppoint_agent.setVisibility(View.GONE);
                mAppoint_machine_manager.setVisibility(View.GONE);
                break;
        }
    }

    private void setClose() {
        if (mAgentWindow.isShowing()) {
            mAgentWindow.dismiss();
        }
        if (mManagerWindow.isShowing()) {
            mManagerWindow.dismiss();
        }
        if (mMachineWindow.isShowing()) {
            mMachineWindow.dismiss();
        }
        mMenu.closeMenu();
        mLoadingImage.startAnimation(mAnimation);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingText.setText(getString(R.string.shujujiazaizhong));
        mListLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appoint_agent:

                if (mAgentWindow.isShowing()) {
                    mAgentWindow.dismiss();
                    return;
                }
                mAgentAdapter.setTag("getAgents");
                mAgentAdapter.firstLoadData(null, null);
                mAgentWindow.setAnchorView(mAppoint_agent);
                mAgentWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ItemUser user = (ItemUser) mAgentAdapter.getItem(position);
                        String agentId = user.getManagerId();
                        if (agentId.equals("nothing")) {
                            return;
                        }
                        mAppoint_agent.setText(user.getManagerName());
                        mTotalParams.put("agentId", agentId);
                        //一旦代理商更改，管理员和机器初始化
                        mAppoint_machine_manager.setText(getContext().getResources().getString(R.string.appointMachineManager));
                        mAppoint_machine.setText(getContext().getResources().getString(R.string.appointMachine));
                        mAgentWindow.dismiss();
                        //将机器管理员数据初始化
                        mManagerAdapter.initResult();
                        mTotalParams.put("managerId", "");
                        setClose();
                        //更改统计结果数据
                        mTotalAdapter.getExceptionTotal(mTotalParams);
                    }
                });
                mAgentWindow.show();
                break;
            case R.id.appoint_machine_manager:
                if (mManagerWindow.isShowing()) {
                    mManagerWindow.dismiss();
                    return;
                }
                if (mManagerAdapter == null) {
                    mManagerAdapter = new MyAdapter(getActivity());
                    mManagerWindow.setAdapter(mManagerAdapter);
                }
                if (TextUtils.equals(UserMessage.getManagerType(), "2")) {
                    mTotalParams.put("agentId", UserMessage.getManagerId());
                } else if (TextUtils.equals(UserMessage.getManagerType(), "0") || TextUtils.equals(UserMessage.getManagerType(), "1")) {
                    if (TextUtils.isEmpty(mTotalParams.get("agentId"))) {
                        mTotalParams.put("agentId", "");
                    }

                }
                mManagerAdapter.setTag("getManagers");
                mManagerAdapter.firstLoadData(mTotalParams.get("agentId"), null);
                mManagerWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //如果没有获取到任何参数，里面默认添加“无”
                        if (mManagerAdapter.getCount() == 1) {
                            return;
                        }
                        ItemUser user = (ItemUser) mManagerAdapter.getItem(position);
                        String managerId = user.getManagerId();
                        String agentId = mTotalParams.get("agentId");
                        if (agentId.equals("nothing")) {
                            return;
                        }
                        mTotalParams.put("managerId", managerId);
                        mAppoint_machine_manager.setText(user.getManagerName());
                        mAppoint_machine.setText(getContext().getResources().getString(R.string.appointMachine));
                        mManagerWindow.dismiss();
                        //将机器数据初始化
                        mMachineAdapter.initResult();
                        mTotalParams.put("machineCode", "");
                        setClose();
                        //更改统计结果数据
                        mTotalAdapter.getExceptionTotal(mTotalParams);
                    }
                });
                mManagerWindow.setAnchorView(mAppoint_machine_manager);
                mManagerWindow.show();
                break;
            case R.id.appoint_machine:
                if (mMachineWindow.isShowing()) {
                    mMachineWindow.dismiss();
                    return;
                }
                if (mMachineAdapter == null) {
                    mMachineAdapter = new MyAdapter(getActivity());
                    mMachineWindow.setAdapter(mMachineAdapter);
                }
                if (TextUtils.isEmpty(mTotalParams.get("agentId"))) {
                    mTotalParams.put("agentId", "");
                }
                if (TextUtils.isEmpty(mTotalParams.get("managerId"))) {
                    mTotalParams.put("managerId", "");
                }
                switch (UserMessage.getManagerType()) {
                    case "0":
                        break;
                    case "1":
                        break;
                    case "2":
                        mTotalParams.put("agentId", UserMessage.getManagerId());
                        break;
                    case "3":
                        mTotalParams.put("managerId", UserMessage.getManagerId());
                        break;
                }
                String agentId = mTotalParams.get("agentId");
                final String managerId = mTotalParams.get("managerId");
                mMachineAdapter.setTag("getMachines");
                mMachineAdapter.firstLoadData(agentId, managerId);
                mMachineWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //如果没有获取到任何参数，里面默认添加“无”
                        if (mMachineAdapter.getCount() == 1) {
                            return;
                        }
                        ItemMachine machine = (ItemMachine) mMachineAdapter.getItem(position);
                        mAppoint_machine.setText(machine.getMachineName());
                        mTotalParams.put("machineCode", machine.getMachineCode());
                        mMachineWindow.dismiss();
                        setClose();
                        //更改统计结果数据
                        mTotalAdapter.getExceptionTotal(mTotalParams);
                    }
                });
                mMachineWindow.setAnchorView(mAppoint_machine);
                mMachineWindow.show();
                break;
            case R.id.create_time:
                String[] dateText = mCreateTime.getText().toString().trim().split("至");
                if (dateText.length == 2) {
                    Calendar c = Calendar.getInstance();
                    Date startDate = TimeUtil.str2Date("yyyy-MM-dd", dateText[0]);
                    c.setTime(startDate);
                    mDatePickerDialog.updateStartDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    Date endDate = TimeUtil.str2Date("yyyy-MM-dd", dateText[1]);
                    c.setTime(endDate);
                    mDatePickerDialog.updateEndDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                }
                mDatePickerDialog.show();
                break;
            case R.id.togglemenu:
                mMenu.toggle();
                break;
            case R.id.loading_text:
                setClose();
                mTotalAdapter.getExceptionTotal(mTotalParams);
                break;
        }

    }

    private void selectTime() {
        Calendar c = Calendar.getInstance();
        mDatePickerDialog = new DoubleDatePickerDialog(getActivity(), 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                String startMonth = String.valueOf(startMonthOfYear + 1);
                String endMonth = String.valueOf(endMonthOfYear + 1);
                String startDay = String.valueOf(startDayOfMonth);
                String endDay = String.valueOf(endDayOfMonth);
                if (String.valueOf(startMonth).length() < 2) {
                    startMonth = "0" + startMonth;
                }
                if (String.valueOf(endMonth).length() < 2) {
                    endMonth = "0" + endMonth;
                }
                if (String.valueOf(endDay).length() < 2) {
                    endDay = "0" + endDay;
                }
                if (String.valueOf(startDay).length() < 2) {
                    startDay = "0" + startDay;
                }
                String text = startYear + "-" + startMonth + "-" + startDayOfMonth + " 至 " + endYear + "-" + endMonth + "-" + endDayOfMonth;
                mCreateTime.setText(text);

                Date startDate = str2Date("yyyy-MM-dd HH:mm:ss", startYear + "-" + startMonth + "-" + startDay + " 00:00:00");
                Date endDate = str2Date("yyyy-MM-dd HH:mm:ss", endYear + "-" + endMonth + "-" + endDay + " 23:59:59");
                if (compareTime(endDate, startDate) <= 0) {
                    Toast.makeText(getContext(), R.string.shijianquyuwuxiao, Toast.LENGTH_SHORT).show();
                    return;
                }

                String body = "between|" + startYear + "-" + startMonth + "-" + startDay + " 00:00:00|" + endYear + "-" + endMonth + "-" + endDay + " 23:59:59";
                Log.d("debug", "time=" + body);
                mTotalParams.put("creatTime", body);
                setClose();
                //更改统计结果数据
                mTotalAdapter.getExceptionTotal(mTotalParams);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAgentWindow != null) {
            mAgentWindow.dismiss();
        }
        if (mManagerWindow != null) {
            mManagerWindow.dismiss();
        }
        if (mMachineWindow != null) {
            mMachineWindow.dismiss();
        }
    }

    private void loadingFail() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingImage.clearAnimation();
        mListLayout.setVisibility(View.GONE);
        mLoadingText.setText(R.string.meiyoushuju_chongshi);
        mLoadingText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void isLoadingSuccess(boolean isSuccess) {
        mMenu.closeMenu();
        if (mAgentWindow.isShowing()) {
            mAgentWindow.dismiss();
        }
        if (mManagerWindow.isShowing()) {
            mManagerWindow.dismiss();
        }
        if (mMachineWindow.isShowing()) {
            mMachineWindow.dismiss();
        }
        if (!isSuccess) {
            loadingFail();
        } else {
            mListLayout.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
            mLoadingText.setText(getString(R.string.shujujiazaizhong));
            mLoadingImage.clearAnimation();
        }
    }
}
