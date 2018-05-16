package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.WaterType;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.bean.Step;
import example.jni.com.coffeeseller.bean.Taste;
import example.jni.com.coffeeseller.communicate.TaskService;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.model.listeners.MsgTransListener;
import example.jni.com.coffeeseller.parse.ParseRQMsg;
import example.jni.com.coffeeseller.parse.PayResult;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.QRMaker;
import example.jni.com.coffeeseller.utils.TextUtil;
import example.jni.com.coffeeseller.views.customviews.BuyDialog;

/**
 * Created by WH on 2018/4/25.
 */

public class NewErrorTip implements View.OnClickListener {
    private static String TAG = "NewErrorTip";
    private Context mContext;
    private View mView;
    private TextView mErrTip, mClose;
    private ChooseAndMking mChooseAndMking;


    public NewErrorTip(Context context, ChooseAndMking chooseAndMking) {
        mContext = context;
        mChooseAndMking = chooseAndMking;
        initView();
    }

    public View getView() {
        return mView;
    }

    private void initView() {

        mView = LayoutInflater.from(mContext).inflate(R.layout.new_error_tip, null);
        mErrTip = (TextView) mView.findViewById(R.id.errTip);
        mClose = (TextView) mView.findViewById(R.id.close);
        mClose.setOnClickListener(this);
    }

    public void setErrTip(String errTip) {
        mErrTip.setText(errTip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:

                if (mChooseAndMking != null) {
                    mChooseAndMking.viewOutAnim(0);
                }
                break;
        }
    }
}
