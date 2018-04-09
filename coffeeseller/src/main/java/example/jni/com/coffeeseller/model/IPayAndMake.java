package example.jni.com.coffeeseller.model;

import android.graphics.Bitmap;

/**
 * Created by WH on 2018/4/2.
 */

public interface IPayAndMake {

    /*
    * 上传数据，获取支付二维码
    * */
    Bitmap getQRBitmap();

    /*
    *主动方式判断用户是否支付
    * */
    boolean isPaied();

    /*
    *多杯咖啡制作中，调用isMakeSuccess();
    * */

    void making();

    /*
    * 制作结果,成功，失败。 成功：调用 takeCup(); 失败：调用refund();
    * */
    void isMakeSuccess(boolean isMakeSuccess);

    /*
    * 取杯
    * */
    void takeCup();

    /*
    * 退款
    * */
    void refund();
}
