package com.example.shareiceboxms.views.fragments.exception;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;

import com.example.shareiceboxms.models.adapters.ExceptionListAdapter;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contentprovider.ExceptionListData;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.factories.MyViewFactory;
import com.example.shareiceboxms.models.helpers.ActionItem;
import com.example.shareiceboxms.models.helpers.DoubleDatePickerDialog;
import com.example.shareiceboxms.models.helpers.LoadMoreHelper;
import com.example.shareiceboxms.models.helpers.MenuPop;
import com.example.shareiceboxms.models.helpers.TitlePopup;
import com.example.shareiceboxms.models.helpers.Util;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WH on 2017/11/27.
 * Edit by LYU on 2017/12/11.
 */

public class ExceptionFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    private View containerView;
    private HomeActivity homeActivity;
    private ImageView drawerIcon, showPop, saoma;
    private ExceptionListAdapter mRecycleAdapter;
    private RecyclerView exceptionList;
    private Context mContext;
    private ExceptionListData contentprovider;
    private TextView exceptionType;
    private TextView mTimeSelect;
    private ListPopupWindow mTilePopup;
    private Button chooseIsDetails;
    private LinearLayout exceptionTypeLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout selectTimeLayout;
    private DoubleDatePickerDialog datePickerDialog;
    private Integer exceptionLeve = null;
    private Map<String, Object> initPostBody = new HashMap<>();
    private int currentPage = 1;
    private int pageNum = 5;
    private int isDetail = 0;
    private String[] happenTime = null;
    private boolean isOpen = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_exception));
            initViews();
            initListener();
            initDatas();
        }
        return containerView;
    }

    private void initListener() {
        drawerIcon.setOnClickListener(this);
        showPop.setOnClickListener(this);
        chooseIsDetails.setOnClickListener(this);
        selectTimeLayout.setOnClickListener(this);
        exceptionTypeLayout.setOnClickListener(this);
        if (!mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setOnRefreshListener(this);
        }
        saoma.setOnClickListener(this);
    }


    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    private void initDatas() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0J及以上
          /*  Switch test = new Switch(getActivity());
            test.setThumbTintList(ContextCompat.getColorStateList(getActivity(), R.color.oncheck_switch_color2));//滑动块
            test.setTrackTintList(ContextCompat.getColorStateList(getActivity(), R.color.oncheck_switch_color));*/
            /*chooseIsDetails.setThumbResource(R.mipmap.swich_thumb_30px);
            chooseIsDetails.setTrackResource(R.color.gray_light_deep);*/
        } else {
           /* chooseIsDetails.setTextOn(" ");
            chooseIsDetails.setTextOff(" ");*/
        }
        homeActivity = (HomeActivity) mContext;
        homeActivity.setOnBackPressListener(this);
        new MyViewFactory(mContext).BuildRecyclerViewRule(exceptionList, new LinearLayoutManager(mContext), new DefaultItemAnimator(), true).setAdapter(mRecycleAdapter);
        contentprovider.getData(HttpRequstUrl.EXCEPTION_LIST_URL, initPostBody, true);
    }

    private void initViews() {
        mContext = getActivity();
        Calendar c = Calendar.getInstance();
        initPostBody = RequestParamsContants.getInstance().getExceptionListParams();
        drawerIcon = (ImageView) containerView.findViewById(R.id.drawerIcon);
        mTimeSelect = (TextView) containerView.findViewById(R.id.timeSelector);
        exceptionList = (RecyclerView) containerView.findViewById(R.id.exception_list);
        saoma = (ImageView) containerView.findViewById(R.id.saoma);
        selectTimeLayout = (RelativeLayout) containerView.findViewById(R.id.selectTime);
        exceptionTypeLayout = (LinearLayout) containerView.findViewById(R.id.exception_type_layout);
        datePickerDialog = new DoubleDatePickerDialog(mContext, 0, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
        showPop = (ImageView) containerView.findViewById(R.id.showpup);
        chooseIsDetails = (Button) containerView.findViewById(R.id.Is_details);
        mRecycleAdapter = new ExceptionListAdapter(getActivity(), this);
        LoadMoreHelper loadMoreHelper = new LoadMoreHelper().setContext(getContext()).setAdapter(mRecycleAdapter)
                .setLoadMoreListenner(this)
                .bindScrollListener(exceptionList)
                .setVisibleThreshold(0);
        contentprovider = mRecycleAdapter.getContentProvider();
        contentprovider.setCanLoad(loadMoreHelper, currentPage);
        exceptionType = (TextView) containerView.findViewById(R.id.exception_type);
        mTilePopup = MenuPop.CreateMenuPop(mContext, exceptionTypeLayout, Constants.EXCEPTION_LV_TITLE);
        mTilePopup.setOnItemClickListener(this);
        mRefreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.blue));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
            case R.id.showpup:

                break;
            case R.id.selectTime:
                datePickerDialog.show();
                break;
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
            case R.id.exception_type_layout:
                mTilePopup.show();
                break;
            case R.id.Is_details:
                if (isOpen) {
                    isOpen = false;
                    chooseIsDetails.setBackgroundResource(R.mipmap.switcher_off);
                    isDetail = 0;
                } else {
                    isDetail = 1;
                    isOpen = true;
                    chooseIsDetails.setBackgroundResource(R.mipmap.switcher_on);
                }
                mRecycleAdapter.isdetails = isDetail;
                currentPage = 1;
                getDatas(true);
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 1;
                getDatas(true);
                mRefreshLayout.setRefreshing(false);
            }
        }, Constants.REFREASH_DELAYED_TIME);
    }

    private void getDatas(boolean b) {
        Map<String, Object> map = new HashMap<>();
        map.put("n", pageNum);
        map.put("p", currentPage);
        map.put("appUserID", PerSonMessage.userId);
        map.put("isDeal", isDetail);
        switch (PerSonMessage.userType) {
            case 3:
                map.put("managerID", PerSonMessage.userId);
                break;
            case 2:
                map.put("agentID", PerSonMessage.userId);
                break;
            default:
                break;

        }
        if (exceptionLeve != null && exceptionLeve != 2) {
            map.put("exceptionLevel", exceptionLeve);
        } else {
            map.put("exceptionLevel", null);
        }
        map.put("happenTime", RequestParamsContants.getInstance().getSelectTime(happenTime));
        contentprovider.getData(HttpRequstUrl.EXCEPTION_LIST_URL, map, b);
    }

    @Override
    public void OnBackDown() {
        Log.e("ExceptionFragment", "Onback");
        homeActivity.finishActivity();


    }

    @Override
    public String[] onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
        mTimeSelect.setText(startYear + "-" + String.valueOf(startMonthOfYear + 1) + "-" + startDayOfMonth + "至" + endYear + "-" + String.valueOf(endMonthOfYear + 1) + "-" + endDayOfMonth);
        happenTime = super.onDateSet(startDatePicker, startYear, startMonthOfYear, startDayOfMonth, endDatePicker, endYear, endMonthOfYear, endDayOfMonth);
        currentPage = 1;
        getDatas(true);
        return null;
    }

    @Override
    public void clearDates() {
        happenTime = null;
        mTimeSelect.setText("");
        currentPage = 1;
        getDatas(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
             /*   chooseIsDetails.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_switch_open));
                chooseIsDetails.setTrackDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_switch_open));*/
            } else {
           /*     chooseIsDetails.setTextOn(" ");
                chooseIsDetails.setTextOff(" ");*/
            }

            isDetail = 1;
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
             /*   chooseIsDetails.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_switch));
                chooseIsDetails.setTrackDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_switch));*/
            } else {
               /* chooseIsDetails.setTextOn(" ");
                chooseIsDetails.setTextOff(" ");*/
            }

            isDetail = 0;

        }
        mRecycleAdapter.isdetails = isDetail;
        currentPage = 1;
        getDatas(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mTilePopup.dismiss();
        exceptionType.setText(Constants.EXCEPTION_LV_TITLE[position]);
        exceptionLeve = position;
        currentPage = 1;
        getDatas(true);
    }

    @Override
    public void loadMore(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView) {
        Log.e("loadmore", "doing");
        if (currentPage < contentprovider.GetMaxPageAccount()) {
            Map<String, Object> postbody = new HashMap<>();
            postbody.put("n", pageNum);
            postbody.put("p", currentPage + 1);
            postbody.put("appUserID", PerSonMessage.userId);
            postbody.put("isDeal", isDetail);
            switch (PerSonMessage.role) {
                case "3":
                    postbody.put("managerID", PerSonMessage.userId);
                    break;
                case "2":
                    postbody.put("agentID", PerSonMessage.userId);
                    break;
                default:
                    break;

            }
            if (exceptionLeve != null && exceptionLeve != 2) {
                postbody.put("exceptionLevel", exceptionLeve);
            } else {
                postbody.put("exceptionLevel", null);
            }

            postbody.put("happenTime", RequestParamsContants.getInstance().getSelectTime(happenTime));
            contentprovider.getData(HttpRequstUrl.EXCEPTION_LIST_URL, postbody, false);
        } else {
            Toast.makeText(getActivity(), "偷偷告诉你,数据已全部加载完毕...", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
