package widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import Interface.OnWheelScrollListener;
import Interface.onDateSetListener;
import adapter.NumericWheelAdapter;
import example.jni.com.datepickerbottompop.R;

import static example.jni.com.datepickerbottompop.R.id.sartmonth;

/**
 * Created by Administrator on 2018/2/8.
 */

public class BottomDoublePicker extends BottomSheetDialog implements View.OnClickListener, OnWheelScrollListener {
    private View mView;
    private Context mContext;
    private WheelView StartYear;
    private WheelView StartMonth;
    private WheelView StartDay;
    private WheelView EndYear;
    private WheelView EndMonth;
    private WheelView EndDay;
    private onDateSetListener mOnDateSetListener;
    private TextView chooseEndTime;
    private TextView chooseStartTime;
    private CustomHorizontalScrollView mScroller;
    private LinearLayout dateLayout;

    public BottomDoublePicker(@NonNull Context context) {

        super(context);
        Context themeContext = getContext();
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.double_datepicker_layout, null);
        mView = view;
        setContentView(mView);
        initDate();
    }

    public void setOnDateSetListener(onDateSetListener mOnDateSetListener) {
        if (mOnDateSetListener != null) {
            this.mOnDateSetListener = mOnDateSetListener;
        }
    }

    private void initDate() {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        dateLayout = (LinearLayout) mView.findViewById(R.id.startDatePick);
        chooseStartTime = (TextView) mView.findViewById(R.id.choose_startdate);
        chooseEndTime = (TextView) mView.findViewById(R.id.choose_enddate);
        chooseStartTime.setOnClickListener(this);
        chooseEndTime.setOnClickListener(this);
        mScroller = (CustomHorizontalScrollView) mView.findViewById(R.id.scroller);
        StartYear = (WheelView) mView.findViewById(R.id.startyear);
        initYear(StartYear);
        EndYear = (WheelView) mView.findViewById(R.id.endyear);
        initYear(EndYear);
        StartMonth = (WheelView) mView.findViewById(sartmonth);
        initMonth(StartMonth);
        EndMonth = (WheelView) mView.findViewById(R.id.endmonth);
        initMonth(EndMonth);
        StartDay = (WheelView) mView.findViewById(R.id.startday);
        initDay(curYear, curMonth, StartDay);
        EndDay = (WheelView) mView.findViewById(R.id.endday);
        initDay(curYear, curMonth, EndDay);

        StartYear.setCurrentItem(curYear - 1950);
        EndYear.setCurrentItem(curYear - 1950);
        StartMonth.setCurrentItem(curMonth - 1);
        EndMonth.setCurrentItem(curMonth - 1);
        StartDay.setCurrentItem(curDate - 1);
        EndDay.setCurrentItem(curDate - 1);
        StartYear.setVisibleItems(7);
        StartMonth.setVisibleItems(7);
        StartDay.setVisibleItems(7);
        EndYear.setVisibleItems(7);
        EndMonth.setVisibleItems(7);
        EndDay.setVisibleItems(7);
        StartYear.addScrollingListener(this);
        StartMonth.addScrollingListener(this);
        StartDay.addScrollingListener(this);
        EndYear.addScrollingListener(this);
        EndMonth.addScrollingListener(this);
        EndDay.addScrollingListener(this);
        // 设置监听
        Button ok = (Button) mView.findViewById(R.id.set);
        Button cancel = (Button) mView.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   mOnDateSetListener.onDateSet(year.getCurrentItem() + 1950, month.getCurrentItem() + 1, day.getCurrentItem() + 1);
                mOnDateSetListener.onDateSet(StartYear.getCurrentItem() + 1950, StartMonth.getCurrentItem() + 1, StartDay.getCurrentItem() + 1
                        , EndYear.getCurrentItem() + 1950, EndMonth.getCurrentItem() + 1, EndDay.getCurrentItem() + 1);
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /**
     * 初始化年
     */
    private void initYear(WheelView wheelView) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(mContext, 1950, 2050);
        numericWheelAdapter.setLabel(" 年");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        wheelView.setViewAdapter(numericWheelAdapter);
        wheelView.setCyclic(true);
    }

    /**
     * 初始化月
     */
    private void initMonth(WheelView wheelView) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(mContext, 1, 12, "%02d");
        numericWheelAdapter.setLabel(" 月");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        wheelView.setViewAdapter(numericWheelAdapter);
        wheelView.setCyclic(true);
    }

    /**
     * 初始化天
     */
    private void initDay(int arg1, int arg2, WheelView wheelView) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(mContext, 1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel(" 日");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        wheelView.setViewAdapter(numericWheelAdapter);
        wheelView.setCyclic(true);
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day;
        boolean flag;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    public BottomDoublePicker(@NonNull Context context, @StyleRes int theme) {
        super(context);
        Context themeContext = getContext();
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.double_datepicker_layout, null);
        mView = view;
        setContentView(mView);
        initDate();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.choose_enddate) {
            Log.d("witch", "宽度" + dateLayout.getWidth());
            mScroller.smoothScrollTo(dateLayout.getWidth(), 0);

        } else if (i == R.id.choose_startdate) {
            mScroller.smoothScrollTo(0, 0);

        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        int i = wheel.getId();
        if (i == R.id.startyear) {
            initDay(StartYear.getCurrentItem() + 1950, StartMonth.getCurrentItem() + 1, StartDay);

        } else if (i == R.id.sartmonth) {
            initDay(StartYear.getCurrentItem() + 1950, StartMonth.getCurrentItem() + 1, StartDay);

        } else if (i == R.id.startday) {
            initDay(StartYear.getCurrentItem() + 1950, StartMonth.getCurrentItem() + 1, StartDay);

        } else if (i == R.id.endyear) {
            initDay(EndYear.getCurrentItem() + 1950, StartMonth.getCurrentItem() + 1, StartDay);

        } else if (i == R.id.endmonth) {
            initDay(EndYear.getCurrentItem() + 1950, StartMonth.getCurrentItem() + 1, StartDay);

        } else if (i == R.id.endday) {
            initDay(EndYear.getCurrentItem() + 1950, StartMonth.getCurrentItem() + 1, StartDay);

        }
    }
}
