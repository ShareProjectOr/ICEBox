package example.jni.com.coffeeseller.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cof.ac.inter.CoffMsger;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.bean.ReportBunker;
import example.jni.com.coffeeseller.bean.Step;
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.contentprovider.ConstanceMethod;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialLsit;
import example.jni.com.coffeeseller.databases.DealOrderInfoManager;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.ChooseCup;
import example.jni.com.coffeeseller.model.MkCoffee;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.model.listeners.MkCoffeeListenner;
import example.jni.com.coffeeseller.parse.PayResult;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.utils.ScreenUtil;
import example.jni.com.coffeeseller.views.fragments.BuyFragment;

import static cof.ac.inter.ContainerType.BEAN_CONTAINER;

/**
 * Created by WH on 2018/3/22.
 */

public class BuyDialog extends Dialog implements ChooseCupListenner, MkCoffeeListenner {
    private static String TAG = "BuyDialog";
    private static BuyDialog mInstance;
    private Context context;
    private Coffee coffee;
    private Handler handler;
    private ChooseCupListenner chooseCupListenner;
    private MkCoffeeListenner mkCoffeeListenner;
    private BuyFragment fragment;

    public static BuyDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BuyDialog(context, R.style.dialog);
        }
        return mInstance;
    }

    public BuyDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BuyDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    public void init() {

        initView();
        initData();
    }

    private void initView() {

        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setTitle(null);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wl.width = ScreenUtil.getScreenWidth(context) / 2;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.alpha = 0.9f;
        window.setAttributes(wl);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
    }

    private void initData() {
        handler = new Handler();
        coffee = BuyFragment.curSelectedCoffee;
        chooseCupListenner = this;
        mkCoffeeListenner = this;
    }

    public void setInitView() {
        ChooseCup chooseCup = new ChooseCup(context, coffee, chooseCupListenner, handler);
        setContentView(chooseCup.getView());
    }

    public void setFragment(BuyFragment fragment) {
        this.fragment = fragment;
    }


    public void showDialog() {
        show();
    }

    public void disDialog() {
        if (isShowing()) {
            dismiss();
        }
    }

    //上报交易给服务器

    private void reportTradeToServer(DealRecorder dealRecorder, List<ReportBunker> bunkers) {
        /*
        {

            tradeCode:"20163258865" // string 交易单号
            machineCode:"20201700285513"//string 机器号
            makeState: 0 //Number 0 代表制作失败 ， 1 代表制作成功

            bunkers : [{
            bunkerID:100,
                    materialStock:256,
        },{
            bunkerID	:100,
                    materialStock:256,
        }]

        }*/


        Map<String, Object> params = ConstanceMethod.getParams();
        params.put("tradeCode", dealRecorder.getOrder());
        params.put("makeState", (dealRecorder.isMakeSuccess() ? 1 : 0));
        params.put("bunkers", bunkers);


        MyLog.d(TAG, "reportTradeToServer RQ_URL = " + Constance.TRADE_UPLOAD);
        MyLog.d(TAG, "reportTradeToServer params = " + JsonUtil.mapToJson(params));

        String RESPONSE_TEXT = null;
        try {
            RESPONSE_TEXT = OkHttpUtil.post(Constance.TRADE_UPLOAD, JsonUtil.mapToJson(params));
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyLog.W(TAG, "reportTradeToServer data" + RESPONSE_TEXT);

        if (RESPONSE_TEXT != null) {

            try {
                JSONObject response = new JSONObject(RESPONSE_TEXT);
                dealRecorder.setReportSuccess(response.getBoolean("uploadState"));
                dealRecorder.setReportMsg(response.getString("err"));
            } catch (JSONException e) {
                dealRecorder.setReportSuccess(false);
                dealRecorder.setReportMsg("JSONException");
                e.printStackTrace();
            }

        } else {
            dealRecorder.setReportSuccess(false);
            dealRecorder.setReportMsg("请求返回为null");
        }

    }

    //更新数据库原料表
    protected List<ReportBunker> updateMaterial(DealRecorder dealRecorder) {
        List<ReportBunker> bunkers = new ArrayList<>();
        if (coffee == null || coffee.getStepList() == null || coffee.getStepList().size() <= 0) {
            return bunkers;
        }
        if (dealRecorder == null || dealRecorder.getContainerConfigs() == null || dealRecorder.getContainerConfigs().size() <= 0) {
            return bunkers;
        }
        MaterialSql materialSql = new MaterialSql(context);


        for (int i = 0; i < coffee.getStepList().size(); i++) {
            Step step = coffee.getStepList().get(i);
            if (step != null && step.getContainerConfig().getWater_capacity() == 0)

                if (step.getMaterial() != null) {

                    String sqlRestMaterial = materialSql.getStorkByMaterialID(step.getMaterial().getMaterialID() + "");
                    int sqlRestMaterialInt = Integer.parseInt(sqlRestMaterial);

                    int mkUseMaterialInt = dealRecorder.getContainerConfigs().get(i).getMaterial_time() * step.getMaterial().getOutput();


                    boolean isUpdateSuccess = materialSql.updateMaterialStockByMaterialId(step.getMaterial().getMaterialID() + "", (sqlRestMaterialInt - mkUseMaterialInt) + "");

                    MyLog.W(TAG, "update material is " + isUpdateSuccess + ", materialId=" + step.getMaterial().getMaterialID()
                            + ", stock=" + (sqlRestMaterialInt - mkUseMaterialInt));

                    ReportBunker reportBunker = new ReportBunker();
                    reportBunker.setBunkerID();
                    reportBunker.setMaterialStock((sqlRestMaterialInt - mkUseMaterialInt));

                    bunkers.add(reportBunker);

                    SharedPreferencesManager.getInstance(context).setCupNum(SharedPreferencesManager.getInstance(context).getCupNum());

                }
        }
    }

    @Override
    public void cancle(String order) {
        //通知服务器取消订单

        MyLog.d(TAG, "choosecup is cancle");

        disDialog();
    }

    @Override
    public void hasPay(final CoffeeFomat coffeeFomat, final DealRecorder dealRecorder) {

        //本地保存交易记录
        DealOrderInfoManager.getInstance(context).addToTable(dealRecorder);

        final CoffeeFomat fomat = coffeeFomat;
        handler.post(new Runnable() {
            @Override
            public void run() {
                MkCoffee mkCoffee = new MkCoffee(context, fomat, dealRecorder, mkCoffeeListenner);
                setContentView(mkCoffee.getView());
            }
        });

    }

    public void updateUi(List<Coffee> coffees) {
        coffees.clear();
        coffees.addAll(SingleMaterialLsit.getInstance(context).getCoffeeList());

    }

    @Override
    public void getMkResult(DealRecorder dealRecorder, boolean makeSuccess) {


        //更新本地交易记录
        DealOrderInfoManager.getInstance(context).update(dealRecorder);

        //更新数据库原料表

        List<ReportBunker> bunkers = updateMaterial(dealRecorder);

        //上报交易结果给服务器


        //更新BuyFragment ui
        if (fragment != null) {
            fragment.updateUi();
        }


        disDialog();

    }

}
