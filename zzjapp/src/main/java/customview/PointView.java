package customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

/**
 * Created by WH on 2017/7/28.
 */

public class PointView extends android.support.v7.widget.AppCompatTextView {
    private Context mContext;

    public PointView(Context context) {
        super(context);
        mContext = context;
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) dp2px(30), (int) dp2px(30));
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);
    }

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            setBackground(mContext.getResources().getDrawable(R.drawable.icon_right_selected));
        } else {
            setBackground(mContext.getResources().getDrawable(R.drawable.icon_right));
        }
    }

    private float dp2px(int dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    private float px2dp(int pxValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }
}
