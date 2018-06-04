package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.MachineConfig.QRMsger;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.ReportBunker;
import example.jni.com.coffeeseller.bean.Step;
import example.jni.com.coffeeseller.contentprovider.Constance;
import example.jni.com.coffeeseller.contentprovider.ConstanceMethod;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.databases.DealOrderInfoManager;
import example.jni.com.coffeeseller.httputils.JsonUtil;
import example.jni.com.coffeeseller.httputils.OkHttpUtil;
import example.jni.com.coffeeseller.model.listeners.ChooseCupListenner;
import example.jni.com.coffeeseller.model.listeners.MkCoffeeListenner;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.views.fragments.NewBuyFragment;

/**
 * Created by WH on 2018/5/10.
 */

public class ChooseAndMking implements ChooseCupListenner, MkCoffeeListenner {
    private String TAG = "ChooseAndMking";
    private static int VIEW_SHOW_TIME = 2 * 60 * 1000;
    public static long TIME_BEFORE_MK_TO_CLEAR = 2 * 60 * 60 * 1000;
    public static int VIEW_CHOOSE_CUP = 0;
    public static int VIEW_ERR_TIP = 1;
    public static int MK_COFFEE = 2;
    private int curViewId = -1;
    private View view;
    private Context context;
    private TextView timeText;
    private LinearLayout layout;
    private CountDownTimer countDownTimer;
    private Handler handler;

    private Timer clearMachineTimer;
    private TimerTask clearMachineTimerTask;

    private Timer countTimeTimer;
    private TimerTask countTimeTimerTask;

    private MkCoffeeListenner mkcoffeeListenner;
    private NewBuyFragment newBuyFragment;
    private NewChooseCup newChooseCup;
    private NewErrorTip newErrorTip;
    private Coffee coffee;

    private boolean isShowing = false;
    private boolean isMaking = false;
    private boolean isPaying = false;
    private boolean isPayed = false;
    private boolean isClearing = false;
    private long lastMkTime = 0;
    private int curTimeCount = 0;
    private int downY;


    public ChooseAndMking(Context context, NewBuyFragment newBuyFragment, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.newBuyFragment = newBuyFragment;
        mkcoffeeListenner = this;
        initView();
    }

    public void init(Coffee coffee) {

        this.coffee = coffee;
        if (isMachineRight()) {//如果正常

        } else {//如果不正常

            //是否需要发送关门指令
            CheckCurMachineState.getInstance().sendCloseDoorComd();
        }
        view.setVisibility(View.VISIBLE);

        /*
        * 如果布局没有显示出来，就开始显示动画，开启倒计时，如果已经显示出来，重置倒计时时间
        * */
        if (!isShowing()) {
            viewEnterAnim();
            countDownTime();
        }
        curTimeCount = 0;
        timeText.setVisibility(View.VISIBLE);
        initViewTouchListenner();
        // countDownTime();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.new_taste_layout, null);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        timeText = (TextView) view.findViewById(R.id.tasteTimeLimit);

    }

    private void initViewTouchListenner() {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        downY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (!isShowing) {
                            return true;
                        }

                        MyLog.d(TAG, "ACTION_MOVE = " + (event.getY() - downY));

                        if (event.getY() - downY > 30) {
                            if (curViewId == VIEW_CHOOSE_CUP) {

                                if (newChooseCup != null && !isPayed) {//如果支付完成不能滑动
                                    isShowing = false;
                                    newChooseCup.cancle();
                                    return true;
                                }

                            } else if (curViewId == VIEW_ERR_TIP) {
                                isShowing = false;
                                viewOutAnim(0);
                                return true;
                            } else if (curViewId == MK_COFFEE) {
                                return true;
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return true;
            }
        });
    }

    private boolean isMachineRight() {
        layout.removeAllViews();
        if (CheckCurMachineState.getInstance().isCanMaking() && CheckCurMachineState.getInstance().isCupEnghou(context)) {
            MyLog.d(TAG, "machine is ok ");
            curViewId = VIEW_CHOOSE_CUP;
            VIEW_SHOW_TIME = 2 * 60 * 1000;

            //每次请求前，清除上一次的计时器
            if (newChooseCup != null) {
                newChooseCup.stopTaskCheckPay();
                newChooseCup = null;
            }
            newChooseCup = new NewChooseCup(context, coffee, this, handler, this);
            layout.addView(newChooseCup.getView());
            return true;
        } else {
            MyLog.d(TAG, "machine is has error ");
            curViewId = VIEW_ERR_TIP;
            VIEW_SHOW_TIME = 10 * 1000;

            newErrorTip = new NewErrorTip(context, this);
            newErrorTip.setErrTip(CheckCurMachineState.getInstance().getStateTip());
            layout.addView(newErrorTip.getView());

            //  viewOutAnim(10000);
            return false;
        }

    }

    public long getLastMkTime() {
        return lastMkTime;
    }

    public View getView() {
        return view;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public boolean isMaking() {
        return isMaking;
    }

    public void setPaying(boolean paying) {
        isPaying = paying;
    }

    public void setPayed(boolean payed) {
        isPayed = payed;
    }

    public boolean isPaying() {
        return isPaying;
    }

    public void setCurTimeCount(int curTimeCount) {
        this.curTimeCount = curTimeCount;
    }

    private void countDownTime() {

        stopCountTimer();

        if (countTimeTimer == null) {
            countTimeTimer = new Timer();
        }
        if (countTimeTimerTask == null) {
            countTimeTimerTask = new TimerTask() {
                @Override
                public void run() {

                    if (VIEW_SHOW_TIME / 1000 - curTimeCount <= 0) {

                        if (curViewId == VIEW_CHOOSE_CUP) {

                            newChooseCup.cancle();

                        } else if (curViewId == VIEW_ERR_TIP) { //10s倒计时结束后就消失
                            //   viewOutAnim(0);
                        }
                        //       cancle(null);
                        viewOutAnim(0);
                        curTimeCount = 0;
                        stopCountTimer();
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                timeNotFinish();
                            }
                        });
                        curTimeCount++;
                    }
                }
            };
        }

        countTimeTimer.schedule(countTimeTimerTask, 0, 1000);
    }

    private void timeNotFinish() {
        timeText.setText((VIEW_SHOW_TIME / 1000 - curTimeCount) + " s");

        if (curViewId == VIEW_CHOOSE_CUP) {
            if (newChooseCup.judgeHeapHot((VIEW_SHOW_TIME / 1000 - curTimeCount))) return;

        }
    }

    public void stopCountTimer() {

        if (countTimeTimer != null) {
            countTimeTimer.cancel();
            countTimeTimer = null;

        }
        countTimeTimerTask = null;
        curTimeCount = 0;
    }

    private void cancleOrder(String order) {
        Map<String, Object> params = ConstanceMethod.getParams();
        params.put("tradeCode", order);

        MyLog.d(TAG, "cancleOrder RQ_URL = " + Constance.TRADE_CLOSE);
        MyLog.d(TAG, "cancleOrder params = " + JsonUtil.mapToJson(params));

        String RESPONSE_TEXT = null;
        try {
            RESPONSE_TEXT = OkHttpUtil.post(Constance.TRADE_CLOSE, JsonUtil.mapToJson(params));
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyLog.W(TAG, "cancleOrder data" + RESPONSE_TEXT);

        if (RESPONSE_TEXT != null) {


        } else {

        }
    }

    //更新数据库原料表
    private String updateMaterial(DealRecorder dealRecorder) {


        List<ReportBunker> bunkers = new ArrayList<>();
        if (coffee == null || coffee.getStepList() == null || coffee.getStepList().size() <= 0) {
            return "";
        }
        if (dealRecorder == null || dealRecorder.getContainerConfigs() == null || dealRecorder.getContainerConfigs().size() <= 0) {
            return "";
        }
        MaterialSql materialSql = new MaterialSql(context);

        int waterUseTotal = 0;
        String waterMaterialId = materialSql.getMaterialIDByContainerID("7");
        ;
        for (int i = 0; i < coffee.getStepList().size(); i++) {
            Step step = coffee.getStepList().get(i);

            if (step != null)//&& step.getContainerConfig().getWater_capacity() == 0

                MyLog.d(TAG, "step getName = " + step.getMaterial().getName());
            if (step.getContainerConfig() == null) { //如果没有步骤

                continue;
            }


            MyLog.d(TAG, "step type = " + step.getMaterial().getName());

            if (step.getMaterial() != null) {


                if (step.getContainerConfig().getWater_capacity() != 0 || TextUtils.equals(waterMaterialId, step.getMaterial().getMaterialID() + "")) {

                    //更新用水量
                    waterUseTotal += step.getContainerConfig().getWater_capacity();

                }


                MyLog.d(TAG, "materialID is " + step.getMaterial().getMaterialID());

                MyLog.d(TAG, "getMaterial_time =  " + step.getContainerConfig().getMaterial_time());

                String sqlRestMaterial = materialSql.getStorkByMaterialID(step.getMaterial().getMaterialID() + "");

                float sqlRestMaterialInt = Float.parseFloat(sqlRestMaterial);

                if (step.getContainerConfig().getMaterial_time() == 0) {


                    continue;
                }

                float mkUseMaterialInt = ((float) dealRecorder.getContainerConfigs().get(i).getMaterial_time()) / step.getContainerConfig().getMaterial_time() * step.getAmount();

                MyLog.d(TAG, "mkUseMaterialInt =  " + mkUseMaterialInt);
                if (mkUseMaterialInt == 0) {


                    continue;
                }

                Log.e(TAG, " materialID is  " + step.getMaterial().getMaterialID() + " stock is " + sqlRestMaterial + ",used= " + mkUseMaterialInt);

                Long stock = Long.parseLong((int) (sqlRestMaterialInt - mkUseMaterialInt) + "");

                boolean isUpdateSuccess = materialSql.updateMaterialStockByMaterialId(step.getMaterial().getMaterialID() + "", stock + "");

                MyLog.W(TAG, "update material is " + isUpdateSuccess + ", materialId=" + step.getMaterial().getMaterialID()
                        + ", usedMaterial = " + mkUseMaterialInt + " sqlRestMaterial= " + sqlRestMaterialInt + ", stock=" + (sqlRestMaterialInt - mkUseMaterialInt));

                ReportBunker reportBunker = new ReportBunker();
                int bunkerId = Integer.parseInt(materialSql.getBunkerIDByMaterialD(step.getMaterial().getMaterialID() + ""));

                MyLog.d(TAG, "getMaterialID " + step.getMaterial().getMaterialID() + " , bunkerID =" + bunkerId);
                reportBunker.setBunkerID(bunkerId);
                reportBunker.setUnit(Math.round(mkUseMaterialInt) + "");
                reportBunker.setMaterialStock(Math.round((sqlRestMaterialInt - mkUseMaterialInt)) + "");

                bunkers.add(reportBunker);
            }

        }
/*
* 更新用水量
* */
        if (waterUseTotal != 0 && waterMaterialId != null && !TextUtils.isEmpty(waterMaterialId)) {

            String sqlRestWaterMaterial = materialSql.getStorkByMaterialID(waterMaterialId + "");

            float sqlRestWaterMaterialInt = Float.parseFloat(sqlRestWaterMaterial);


            int waterStock = (int) (sqlRestWaterMaterialInt - waterUseTotal / 10);

            boolean isUpdateSuccess = materialSql.updateMaterialStockByMaterialId(waterMaterialId + "", waterStock + "");

            MyLog.d(TAG, "用水量更新：materialID is" + waterMaterialId + " , rest= "
                    + sqlRestWaterMaterialInt + ", used= " + waterUseTotal / 10
                    + " , stock= " + waterStock + ", isUpdateSuccess= " + isUpdateSuccess);


            int bunkerId = Integer.parseInt(materialSql.getBunkerIDByMaterialD(waterMaterialId + ""));
            ReportBunker reportBunker = new ReportBunker();
            reportBunker.setBunkerID(bunkerId);
            reportBunker.setUnit(Math.round(waterUseTotal) + "");
            reportBunker.setMaterialStock(Math.round(waterStock) + "");

            bunkers.add(reportBunker);
        }

        //更新杯子
        String cupBunkerId = materialSql.getBunkerIDByContainerID("8");
        String cupNum = materialSql.getStorkByBunkersID(cupBunkerId);
        if (!TextUtils.isEmpty(cupNum)) {
            int cupNumInt = Integer.parseInt(cupNum);
            if (cupNumInt > 0) {
                materialSql.updateContact(cupBunkerId, "", "", "", "", "", (cupNumInt - 1) + "", "", "", "", "");

                ReportBunker reportBunkerCup = new ReportBunker();
                reportBunkerCup.setBunkerID(Integer.parseInt(cupBunkerId));
                reportBunkerCup.setUnit(1 + "");
                reportBunkerCup.setMaterialStock((cupNumInt - 1) + "");
                bunkers.add(reportBunkerCup);

            }
        }


        /*
        * 更新数据库本地交易数据库bunker
        * */
        JSONArray jsonArray = (JSONArray) com.alibaba.fastjson.JSONObject.toJSON(bunkers);
        String array = jsonArray.toString();
        dealRecorder.setBunkers(array);

        DealOrderInfoManager.getInstance(context).update(dealRecorder);

        MyLog.W(TAG, "updateMaterial is over");
        return array;
    }

    /*
    * 清洗机器
    * */
    public void clearMachine(final List<ContainerConfig> containerConfigs) {

        stopTaskClearMachine();

        MyLog.d(TAG, "clearMachine");
        if (clearMachineTimer == null) {
            clearMachineTimer = new Timer();
        }
        if (clearMachineTimerTask == null) {
            clearMachineTimerTask = new TimerTask() {
                @Override
                public void run() {

                    if (CheckCurMachineState.getInstance().isCupShelfRightPlaceClearMachineTest()
                            && CheckCurMachineState.getInstance().isDoorCloseMachineTest()) {
                        boolean isResultSuccess = ClearMachine.clearMachineByHotWater(100, 0);

                        if (isResultSuccess) {
                            MyLog.d(TAG, "clearMachine success!");
                            stopTaskClearMachine();
                        } else {
                            MyLog.d(TAG, "clearMachine failed!");
                        }
                        isClearing = false;
                    }
                }
            };
        }
        clearMachineTimer.schedule(clearMachineTimerTask, 0, 2000);
    }

    public void stopTaskClearMachine() {

        if (clearMachineTimer != null) {

            clearMachineTimer.cancel();
        }
        clearMachineTimer = null;
        clearMachineTimerTask = null;
    }

    /*
    * 消失动画
    * */
    public void viewOutAnim(int waitTime) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.view_out);

                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (view != null) {
                            view.clearAnimation();
                            initState();
                            view.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                if (view != null) {
                    view.startAnimation(anim);
                }
            }
        }, waitTime);


    }

    /*
      * 进入动画
      * */

    public void viewEnterAnim() {

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.view_enter);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (view != null) {
                    view.clearAnimation();
                    isShowing = true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (view != null) {
            view.startAnimation(anim);
        }
    }

    public void initState() {
        isPaying = false;
        isMaking = false;
        isShowing = false;
        curTimeCount = 0;
        newBuyFragment.initLayout();
    }


    @Override
    public void cancle(final String order) {
        //通知服务器取消订单
        if (TextUtils.isEmpty(order) || order == null) {
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cancleOrder(order);
                }
            }).start();
        }
        MyLog.d(TAG, "choosecup is cancle");

        stopCountTimer();

        viewOutAnim(0);

    }

    @Override
    public void paying(boolean b) {
        this.isPaying = b;
    }

    @Override
    public void hasPay(final CoffeeFomat coffeeFomat, final DealRecorder dealRecorder) {


        if (curViewId != VIEW_CHOOSE_CUP) {
            return;
        }

        //本地保存交易记录
        DealOrderInfoManager.getInstance(context).addToTable(dealRecorder);

        final CoffeeFomat fomat = coffeeFomat;

        handler.post(new Runnable() {
            @Override
            public void run() {
                curTimeCount = 0;
                NewMkCoffee mkCoffee = new NewMkCoffee(context, coffeeFomat, dealRecorder, mkcoffeeListenner, handler);
                layout.removeAllViews();
                layout.addView(mkCoffee.getView());
                curViewId = MK_COFFEE;
                //countDownTime();
                timeText.setVisibility(View.GONE);
                isMaking = true;

            }
        });
    }

    @Override
    public void getMkResult(DealRecorder dealRecorder, final boolean success, final boolean isCalculateMaterial) {
        lastMkTime = System.currentTimeMillis();

        MyLog.d(TAG, "getMkResult been called!");
        final DealRecorder recorder = dealRecorder;

        new Thread(new Runnable() {
            @Override
            public void run() {

                String bunkers = "";
                if (isCalculateMaterial) {

                    MyLog.d(TAG, "isCalculateMaterial= " + isCalculateMaterial);

                    //更新数据库原料表
                    bunkers = updateMaterial(recorder);

                }

                //上报交易结果给服务器

                QRMsger qrMsger = new QRMsger();
                DealRecorder newDealRecorder = qrMsger.reportTradeToServer(recorder, bunkers);


                //更新本地交易记录
                DealOrderInfoManager.getInstance(context).update(newDealRecorder);

                if (!success && !isCalculateMaterial) {

                } else {

                    //清洗机器

                    if (!isClearing) {
                        clearMachine(null);
                        isClearing = true;
                    }
                }

            }
        }).start();

        //更新BuyFragment ui
        if (newBuyFragment != null) {
            newBuyFragment.updateUi(0);
        }

        viewOutAnim(10000);

    }
}
