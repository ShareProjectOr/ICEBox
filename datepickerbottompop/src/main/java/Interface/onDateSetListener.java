package Interface;

import android.widget.DatePicker;

/**
 * Created by Administrator on 2018/2/8.
 */

public interface onDateSetListener {
    void onDateSet(int startYear, int startMonthOfYear, int startDayOfMonth,
                   int endYear, int endMonthOfYear, int endDayOfMonth);

    void onDateSet(int Year, int MonthOfYear, int startDayOfMonth);
}
