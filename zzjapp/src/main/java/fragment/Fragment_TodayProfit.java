package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.StatisticsViewUtil;
import contentprovider.UserMessage;
import customview.DoubleDatePickerDialog;
import entity.ItemTotal;
import httputil.Constance;
import httputil.ConstanceMethod;
import httputil.HttpRequest;
import httputil.JsonParseUtil;
import otherutis.TimeUtil;

import static otherutis.TimeUtil.compareTime;
import static otherutis.TimeUtil.str2Date;


public class Fragment_TodayProfit extends Fragment implements View.OnClickListener {
    private View cacheView;
    private Context context;
    private PieChart mSuccess_PieChat, mFail_PieChat;
    private TextView mTime, mTotal_Money, mRefund_Money, mAlipay_Money, mWechat_Money, mCash_Money,
            mAlipay_Refund_Money, mWechat_Refund_Money, mCash_Refund_Money, mTotal_Orange, mAvg_Orange, mTitle;
    private TextView mSuccessDesc, mFailDesc;
    private LinearLayout mLegendLayout, mFailLegendLayout;
    private HorizontalScrollView mScrollLayout;
    private StatisticsViewUtil mViewUtil;
    private List<String> names;
    private List<Integer> successDatas, failDatas;
    private List<Integer> colors;
    private JsonParseUtil mJsonParseUtil;
    private DoubleDatePickerDialog mDatePickerDialog;
    private SharedPreferences mPreferences;
    private String mTimeParams;
    private String mMachineCode;
    private TextView machineCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null) {
            cacheView = inflater.inflate(R.layout.fragment_today_profit, null);
            bindViews();
            initData();
        }
        ViewGroup parent = (ViewGroup) cacheView.getParent();
        if (parent != null) {
            parent.removeView(cacheView);
        }
        return cacheView;
    }

    private void bindViews() {
        mSuccess_PieChat = (PieChart) cacheView.findViewById(R.id.success_piechat);
        mFail_PieChat = (PieChart) cacheView.findViewById(R.id.fail_piechat);
        mTime = (TextView) cacheView.findViewById(R.id.time);
        mTotal_Money = (TextView) cacheView.findViewById(R.id.total_money);
        mRefund_Money = (TextView) cacheView.findViewById(R.id.refund_money);
        mAlipay_Money = (TextView) cacheView.findViewById(R.id.alipay_money);
        machineCount = (TextView) cacheView.findViewById(R.id.machineCount);
        mWechat_Money = (TextView) cacheView.findViewById(R.id.wechat_money);
        mCash_Money = (TextView) cacheView.findViewById(R.id.cash_money);
        mAlipay_Refund_Money = (TextView) cacheView.findViewById(R.id.alipay_refund_money);
        mWechat_Refund_Money = (TextView) cacheView.findViewById(R.id.wechat_refund_money);
        mCash_Refund_Money = (TextView) cacheView.findViewById(R.id.cash_refund_money);
        mTotal_Orange = (TextView) cacheView.findViewById(R.id.total_orange);
        mAvg_Orange = (TextView) cacheView.findViewById(R.id.avg_orange);
        ImageView mTime_Select = (ImageView) cacheView.findViewById(R.id.time_select);
        ImageView mRefresh = (ImageView) cacheView.findViewById(R.id.refresh);
        mSuccessDesc = (TextView) cacheView.findViewById(R.id.success_desc);
        mFailDesc = (TextView) cacheView.findViewById(R.id.fail_desc);
        mLegendLayout = (LinearLayout) cacheView.findViewById(R.id.legend_layout);
        mFailLegendLayout = (LinearLayout) cacheView.findViewById(R.id.fail_legend_layout);
        mScrollLayout = (HorizontalScrollView) cacheView.findViewById(R.id.scrollLayout);
        mTitle = (TextView) cacheView.findViewById(R.id.title);
        mTime_Select.setOnClickListener(this);
        mRefresh.setOnClickListener(this);
    }

    private void initData() {
        context=getContext();
        if (!TextUtils.isEmpty(getActivity().getIntent().getStringExtra("machineCode"))) {
            mMachineCode = getActivity().getIntent().getStringExtra("machineCode");
            machineCount.setText(getString(R.string.shebei) + mMachineCode);
        } else {
            machineCount.setText(getString(R.string.suoyoushebei));
        }
        mViewUtil = new StatisticsViewUtil(getContext());
        mJsonParseUtil = new JsonParseUtil();
        names = new ArrayList<>();
        successDatas = new ArrayList<>();
        failDatas = new ArrayList<>();
        colors = new ArrayList<>();
        names.add(getString(R.string.zanwushuju));
        colors.add(ContextCompat.getColor(getActivity(), R.color.black_overlay));
        mSuccessDesc.setText(R.string.chenggong_bei);
        mFailDesc.setText(R.string.shibai_bei);
        mViewUtil.drawNPieChat(mSuccess_PieChat, getString(R.string.zanwushuju), ContextCompat.getColor(getActivity(), R.color.black_overlay));
        mViewUtil.customizeLegend(names, null, colors, getContext(), mLegendLayout);
        mViewUtil.drawNPieChat(mFail_PieChat, getString(R.string.zanwushuju), ContextCompat.getColor(getActivity(), R.color.black_overlay));
        mViewUtil.customizeLegend(names, null, colors, getContext(), mFailLegendLayout);
        initTime();
        initDatePicker();
        new DataTotalTask(Constance.GET_MACHINE_TRADE_TOTAL, setParams()).execute();
    }

    private void initTime() {
        mPreferences = ConstanceMethod.getSharedPreferences(getContext(), "time");
        mPreferences.edit().putString("lastSelectedTime", "");
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String date = String.valueOf(calendar.get(Calendar.DATE));
        mTime.setText(mTime.getText().toString() + year + "-" + addZero(month) + "-" + addZero(date));
        mTimeParams = "between|" + year + "-" + addZero(month) + "-" + addZero(date) + " 00:00:00|" + year + "-" + addZero(month) + "-" + addZero(date) + " 23:59:59";
    }

    private void initDatePicker() {
        Calendar c = Calendar.getInstance();
        mDatePickerDialog = new DoubleDatePickerDialog(getActivity(), 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                String startMonth = String.valueOf(startMonthOfYear + 1);
                String endMonth = String.valueOf(endMonthOfYear + 1);
                String startDay = String.valueOf(startDayOfMonth);
                String endDay = String.valueOf(endDayOfMonth);
                startMonth = addZero(startMonth);
                endMonth = addZero(endMonth);
                endDay = addZero(endDay);
                startDay = addZero(startDay);
                String text = startYear + "-" + startMonth + "-" + startDay + " 至 " + endYear + "-" + endMonth + "-" + endDay;
                mPreferences.edit().putString("lastSelectedTime", text).apply();

                Date startDate = str2Date("yyyy-MM-dd HH:mm:ss", startYear + "-" + startMonth + "-" + startDay + " 00:00:00");
                Date endDate = str2Date("yyyy-MM-dd HH:mm:ss", endYear + "-" + endMonth + "-" + endDay + " 23:59:59");
                if (compareTime(endDate, startDate) <= 0) {
                    Toast.makeText(getContext(), R.string.shijianxuanzebuhefa, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (startYear != Calendar.getInstance().get(Calendar.YEAR) || endYear != Calendar.getInstance().get(Calendar.YEAR)) {
                    Toast.makeText(getContext(), R.string.chaxunbenniandu, Toast.LENGTH_SHORT).show();
                    return;
                }
                mTime.setText(getString(R.string.tongjishijianduan) + text);
                String month = addZero(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
                String day = addZero(String.valueOf(Calendar.getInstance().get(Calendar.DATE)));
                Log.d("----------------------", month);
                if (TextUtils.equals(startMonth, month) && TextUtils.equals(startDay, day) && (TextUtils.equals(endMonth, month)) && TextUtils.equals(endDay, day)) {
                    mTitle.setText(R.string.jinrishouyi);

                } else {
                    mTitle.setText(R.string.tongjishouyi);
                }

                mTimeParams = "between|" + startYear + "-" + startMonth + "-" + startDay + " 00:00:00|" + endYear + "-" + endMonth + "-" + endDay + " 23:59:59";
                //更改统计结果数据
                new DataTotalTask(Constance.GET_MACHINE_TRADE_TOTAL, setParams()).execute();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
    }

    private String addZero(String time) {
        if (time.length() < 2) {
            time = "0" + time;
        }
        return time;
    }

    private void drawPieChat(PieChart pieChart, List<String> names, List<Integer> datas, List<Integer> colors, String
            description, boolean isShowText) {
        PieData pieData = mViewUtil.getPieData(names, datas, colors, isShowText);
        pieChart = mViewUtil.initPieChart(pieChart, description, pieData);
        mViewUtil.setOnPieChatClickListener(pieChart, getContext(), names);
        pieChart.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_select:
                String lastSelectedTime = mPreferences.getString("lastSelectedTime", "");
                Log.d("debug", "lastSelectedTime===" + lastSelectedTime);
                if (TextUtils.isEmpty(lastSelectedTime)) {
                    Calendar c = Calendar.getInstance();
                    mDatePickerDialog.updateStartDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    mDatePickerDialog.updateEndDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                } else {
                    String[] dateText = lastSelectedTime.trim().split("至");
                    if (dateText.length == 2) {
                        Calendar c = Calendar.getInstance();
                        Date startDate = TimeUtil.str2Date("yyyy-MM-dd", dateText[0]);
                        c.setTime(startDate);
                        mDatePickerDialog.updateStartDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                        Date endDate = TimeUtil.str2Date("yyyy-MM-dd", dateText[1]);
                        c.setTime(endDate);
                    }
                }
                mDatePickerDialog.show();
                break;
            case R.id.refresh:
                new DataTotalTask(Constance.GET_MACHINE_TRADE_TOTAL, setParams()).execute();
                break;
        }

    }

    private Map<String, String> setParams() {
        Map<String, String> bodys = new HashMap<>();
        bodys.put("userID", UserMessage.getManagerId());
        if (TextUtils.isEmpty(mMachineCode)) {
            if (TextUtils.equals(UserMessage.getManagerType(), "2")) {
                bodys.put("W[agentID]", UserMessage.getManagerId());
            } else {
                bodys.put("W[agentID]", "");
            }
            if (TextUtils.equals(UserMessage.getManagerType(), "3")) {
                bodys.put("W[managerID]", UserMessage.getManagerId());
            } else {
                bodys.put("W[managerID]", "");
            }
        } else {
            bodys.put("W[machineCode]", mMachineCode);
        }
        bodys.put("W[payTime]", mTimeParams);
        Log.e("传递参数", bodys.toString());
        bodys.put("K", "total|cupNum|orangeNum|tradeMoney|refundMoney|alipay|cash|weChat|failTotal|successTotal");
        return bodys;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //拉取总计结果数据
    private class DataTotalTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err;
        private Map<String, String> bodys;
        private ItemTotal results;
        private String url;

        DataTotalTask(String url, Map<String, String> bodys) {
            this.bodys = bodys;
            this.url = url;
            results = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.d("debug", "result==" + bodys.toString());
                response = HttpRequest.postString(url, bodys);
                Log.d("debug", "response===" + response);
                JSONObject jsonResponse = new JSONObject(response);
                err = jsonResponse.getString("err");
                if (TextUtils.isEmpty(err)) {
                    JSONObject result = jsonResponse.getJSONObject("d");
                    results = mJsonParseUtil.getTotalResult(result);
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                err =context.getString(R.string.wangluobugeili);
                return false;
            } catch (JSONException e1) {
                err = context.getString(R.string.fuwuqikaixiaocai_chongshi);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (!success) {
                Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
            } else {
                setDatas(results);
                Log.d("debug", mScrollLayout.getX() + "=====" + mScrollLayout.getWidth());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScrollLayout.smoothScrollTo((int) mScrollLayout.getX() + mScrollLayout.getWidth() / 2 - 150, (int) mScrollLayout.getY());
                    }
                }, 0);
            }
        }
    }

    private void setDatas(ItemTotal results) {
        names.clear();
        colors.clear();
        names.add(getString(R.string.zhifubao));
        names.add(getString(R.string.weixin));
        names.add(getString(R.string.xianjing));
        colors.add(ContextCompat.getColor(getActivity(), R.color.alipay));
        colors.add(ContextCompat.getColor(getActivity(), R.color.wechat));
        colors.add(ContextCompat.getColor(getActivity(), R.color.cash));
        if (results == null) {
            return;
        }
        Log.d("debug", "results===" + results);
        successDatas.clear();
        failDatas.clear();

        mTotal_Money.setText(results.getIncomeTotal() + "");
        mRefund_Money.setText(results.getRefoundTotal() + "");
        mAlipay_Money.setText(results.getAlipayIncomeTotal() + "");
        mWechat_Money.setText(results.getWechatIncomeTotal() + "");
        mCash_Money.setText(results.getCashIncomeTotal() + "");
        mAlipay_Refund_Money.setText(results.getAlipayRefoundTotal() + "");
        mWechat_Refund_Money.setText(results.getWechatRefoundTotal() + "");
        mCash_Refund_Money.setText(results.getCashRefoundTotal() + "");
        mTotal_Orange.setText(results.getOrangeNum() + "");
        int cups = results.getCupNum();
        int oranges = results.getOrangeNum();
        if (cups != 0) {
            Float avgOrange = ((float) oranges) / cups;
            mAvg_Orange.setText(results.string2BigDecimal(String.valueOf(avgOrange)) + "");
        } else {
            mAvg_Orange.setText(0 + "");
        }
        mFailDesc.setText(getString(R.string.shibai) + results.getFailCup() + getString(R.string.bei));
        mSuccessDesc.setText(getString(R.string.chenggong) + results.getSuccessCup() + getString(R.string.bei));

        int alipaySuccessCup = results.getAlipaySuccessCup();
        int weChatSuccessCup = results.getWeChatSuccessCup();
        int cashSuccessCup = results.getCashSuccessCup();

        int alipayFailCup = results.getAlipayFailCup();
        int wechatFailCup = results.getWechatFailCup();
        int cashFailCup = results.getCashFailCup();

        successDatas.clear();
        failDatas.clear();
        mLegendLayout.removeAllViews();
        mFailLegendLayout.removeAllViews();
        successDatas = getResultDatas(alipaySuccessCup, weChatSuccessCup, cashSuccessCup);
        failDatas = getResultDatas(alipayFailCup, wechatFailCup, cashFailCup);
        if (successDatas.size() == 0) {
            mViewUtil.drawNPieChat(mSuccess_PieChat, getString(R.string.bei_0), ContextCompat.getColor(getActivity(), R.color.ed));
            mViewUtil.customizeLegend(names, getTextDatas(alipaySuccessCup, weChatSuccessCup, cashSuccessCup), colors, getContext(), mLegendLayout);
        } else {
            drawPieChat(mSuccess_PieChat, getResultNames(alipaySuccessCup, weChatSuccessCup, cashSuccessCup), successDatas, getResultColocs(alipaySuccessCup, weChatSuccessCup, cashSuccessCup), "", true);
            mViewUtil.customizeLegend(names, getTextDatas(alipaySuccessCup, weChatSuccessCup, cashSuccessCup), colors, getContext(), mLegendLayout);
        }

        if (failDatas.size() == 0) {
            mViewUtil.drawNPieChat(mFail_PieChat, getString(R.string.bei_0), ContextCompat.getColor(getActivity(), R.color.ed));
            mViewUtil.customizeLegend(names, getTextDatas(alipayFailCup, wechatFailCup, cashFailCup), colors, getContext(), mFailLegendLayout);
        } else {
            drawPieChat(mFail_PieChat, getResultNames(alipayFailCup, wechatFailCup, cashFailCup), failDatas, getResultColocs(alipayFailCup, wechatFailCup, cashFailCup), "", true);
            mViewUtil.customizeLegend(names, getTextDatas(alipayFailCup, wechatFailCup, cashFailCup), colors, getContext(), mFailLegendLayout);
        }
        mScrollLayout.smoothScrollTo((int) mTotal_Money.getX(), (int) mTotal_Money.getY());
    }

    //傍边文字描述使用的数据
    private List<Integer> getTextDatas(int alipay, int wechat, int cash) {
        List<Integer> datas = new ArrayList<>();
        datas.add(alipay);
        datas.add(wechat);
        datas.add(cash);
        return datas;
    }

    //饼图显示的数据
    private List<Integer> getResultDatas(int alipay, int wechat, int cash) {
        List<Integer> datas = new ArrayList<>();
        if (alipay != 0) {
            datas.add(alipay);
        }
        if (wechat != 0) {
            datas.add(wechat);
        }
        if (cash != 0) {
            datas.add(cash);
        }
        return datas;
    }

    //饼图显示的标签
    private List<String> getResultNames(int alipay, int wechat, int cash) {
        List<String> datas = new ArrayList<>();
        if (alipay != 0) {
            datas.add(names.get(0));
        }
        if (wechat != 0) {
            datas.add(names.get(1));
        }
        if (cash != 0) {
            datas.add(names.get(2));
        }
        return datas;
    }

    //饼图显示的颜色
    private List<Integer> getResultColocs(int alipay, int wechat, int cash) {
        List<Integer> datas = new ArrayList<>();
        if (alipay != 0) {
            datas.add(colors.get(0));
        }
        if (wechat != 0) {
            datas.add(colors.get(1));
        }
        if (cash != 0) {
            datas.add(colors.get(2));
        }
        return datas;
    }
}
