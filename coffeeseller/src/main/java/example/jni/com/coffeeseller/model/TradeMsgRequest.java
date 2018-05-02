package example.jni.com.coffeeseller.model;

import android.os.CountDownTimer;

import java.util.Timer;
import java.util.TimerTask;

import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.MachineConfig.QRMsger;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.model.listeners.MsgTransListener;
import example.jni.com.coffeeseller.utils.MyLog;

/**
 * Created by WH on 2018/5/1.
 */

public class TradeMsgRequest {
    private static String TAG = "TradeMsgRequest";
    public int mCurRequest;
    public static int REQUEST_QR_CODE = 0;
    public static int REQUEST_CHECK_PAY = 1;
    private TimerTask mCheckPayTask = null;
    private Timer mTimer = null;

    /*
* 请求二维码
* */
    public void requestQRCode(MsgTransListener msgTransListener, DealRecorder mDealRecorder, Coffee coffee) {

        MyLog.d(TAG, "requestQRCode");
        mCurRequest = REQUEST_QR_CODE;
        QRMsger qrMsger = new QRMsger(mDealRecorder);
        qrMsger.reqQR(msgTransListener, null);
    }

    /*
* 开始检查订单是否支付
* */
    public void beginTaskCheckPay(final MsgTransListener msgTransListener, final DealRecorder mDealRecorder) {

        mCurRequest = REQUEST_CHECK_PAY;

        if (mTimer == null) {

            mTimer = new Timer(true);
        }
        if (mCheckPayTask == null) {

            mCheckPayTask = new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    if (!mDealRecorder.isPayed() && mDealRecorder.isVlide()) {
                        QRMsger qrMsger = new QRMsger();
                        qrMsger.checkPay(msgTransListener, mDealRecorder.getOrder());
                    } else {
                        stopTaskCheckPay();
                    }
                }
            };

            mTimer.schedule(mCheckPayTask, 20000, 9000);//交易之后20秒进行查询交易状态,每隔5秒查询一次
        }

    }

    public void stopTaskCheckPay() {

        if (mTimer != null) {

            mTimer.cancel();
        }
        mTimer = null;
        mCheckPayTask = null;
    }
}
