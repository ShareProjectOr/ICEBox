package customview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.ListPopupWindow;

/**
 * Created by Administrator on 2017/8/29.
 */

public class CustormPopWindow extends ListPopupWindow {
    public CustormPopWindow(@NonNull Context context) {
        super(context);
    }

    public CustormPopWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustormPopWindow(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustormPopWindow(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
