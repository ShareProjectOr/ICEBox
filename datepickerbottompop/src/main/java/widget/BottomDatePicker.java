package widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import Interface.OnWheelScrollListener;
import Interface.onDateSetListener;
import adapter.NumericWheelAdapter;
import example.jni.com.datepickerbottompop.R;

/**
 * Created by Administrator on 2018/2/7.
 */

public class BottomDatePicker extends BottomSheetDialog {
    private View mView;
    private Context mContext;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private onDateSetListener mOnDateSetListener;

    public BottomDatePicker(@NonNull Context context) {
        super(context);
        Context themeContext = getContext();
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.datepicker_layout, null);
        mView = view;
        setContentView(mView);
        initDate();


    }

    public void setOnDateSetListener(onDateSetListener mOnDateSetListener) {
        if (mOnDateSetListener != null) {
            this.mOnDateSetListener = mOnDateSetListener;
        }
    }


    public BottomDatePicker(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        Context themeContext = getContext();
        mContext = context;
        // setButton(BUTTON_POSITIVE,
        // themeContext.getText(android.R.string.date_time_done), this);
        //  setIcon(0);

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.datepicker_layout, null);
        mView = view;
        setContentView(mView);
        initDate();

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

    private void initDate() {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        year = (WheelView) mView.findViewById(R.id.year);
        initYear(year);
        month = (WheelView) mView.findViewById(R.id.month);
        initMonth(month);
        day = (WheelView) mView.findViewById(R.id.day);
        initDay(curYear, curMonth, day);


        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        year.setVisibleItems(7);
        month.setVisibleItems(7);
        day.setVisibleItems(7);
        year.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                initDay(year.getCurrentItem() + 1950, month.getCurrentItem() + 1, day);

            }
        });
        month.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                initDay(year.getCurrentItem() + 1950, month.getCurrentItem() + 1, day);
            }
        });
        // 设置监听
        Button ok = (Button) mView.findViewById(R.id.set);
        Button cancel = (Button) mView.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDateSetListener.onDateSet(year.getCurrentItem() + 1950, month.getCurrentItem() + 1, day.getCurrentItem() + 1);
                String str = String.format(Locale.CHINA, "%4d年%2d月%2d日", year.getCurrentItem() + 1950, month.getCurrentItem() + 1, day.getCurrentItem() + 1);
                Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        LinearLayout cancelLayout = (LinearLayout) mView.findViewById(R.id.view_none);
        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dismiss();
                return false;
            }
        });

    }

}
