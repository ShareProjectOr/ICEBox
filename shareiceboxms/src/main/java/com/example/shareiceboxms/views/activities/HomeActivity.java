package com.example.shareiceboxms.views.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemPerson;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.ConstanceMethod;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.contants.Sql;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.GetUserDetailInfo;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.models.http.mqtt.GetService;
import com.example.shareiceboxms.models.http.mqtt.MqttService;
import com.example.shareiceboxms.views.fragments.AboutFragment;
import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.ChangePasswordFragment;
import com.example.shareiceboxms.views.fragments.CloseDoorFragment;
import com.example.shareiceboxms.views.fragments.OpenDoorSuccessFragment;
import com.example.shareiceboxms.views.fragments.OpeningDoorFragment;
import com.example.shareiceboxms.views.fragments.PerSonFragment;
import com.example.shareiceboxms.views.fragments.exception.ExceptionFragment;
import com.example.shareiceboxms.views.fragments.machine.MachineFragment;
import com.example.shareiceboxms.views.fragments.product.ProductFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeFragment;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener, GetUserDetailInfo.GetUserDetailInfoCallback {
    /*
    * 01:开关门 doorState: 0 关门 1 开门
    * 02：异常
    * 03：推送上下货信息
    *
    * */
    public static int DEFAULT = 0;
    public static int OPENING_DOOR = 1;
    public static int OPEN_LOCK_SUCCESS = 2;
    public static int OPEN_LOCK_FAILED = 3;
    DrawerLayout drawer;
    NavigationView navigationView;
    private TabLayout tabLayout;
    public BaseFragment curFragment = null;
    private TextView MnanagerNameAndTimePart;
    private OnBackPressListener mOnBackPressListener;
    private int currentHomePageNum = 0;
    private boolean showHomepage = true;
    private final int SCANNIN_GREQUEST_CODE = 1;
    private static final int CAMERA_OK = 0X01;
    private long lastBackClicked;
    public static final String BROADCAST_ACTION_NOTIFIY = "com.example.shareiceboxms.notify";
    private NotificationBroadcastReceiver mNotificationBroadcastReceiver;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private static HomeActivity mInstance;
    private static Handler handler;
    private int LastDoorState = DEFAULT;//0初始状态,1开门状态,2上货完成关门,3开门失败状态
    private boolean isRequestOpen = false;
    public String qrResult;//二维码扫描结果
    public boolean isRecogniteMachineCode = false;//是否是识别机器码
    private Intent mIntent;
    private GetUserDetailInfo.GetUserDetailInfoCallback callback;

    public static HomeActivity getInstance() {

        if (mInstance == null) {
            mInstance = new HomeActivity();
        }
        return mInstance;
    }

    public HomeActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
     /*   SaveUserMessager();
        initViews();
        startMqttService();
        initData();
        initListener();
        initHandler();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        SaveUserMessager();
        initViews();
        startMqttService();
        initData();
        initListener();
        initHandler();

    }


    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj == null) {
                    Log.d("----handleMessage---", "掉线");
                    GetUserDetailInfo getUserDetailInfo = new GetUserDetailInfo(HomeActivity.this, callback);
                    getUserDetailInfo.getData();
                } else {
                    Log.d("----handleMessage---", "收到消息");
                    JSONObject object = (JSONObject) msg.obj;
                    try {
//                    Log.e("push", object.toString() + "------" + object.getString("createTime") + "----time:" + SecondToDate.getDateToString(Long.parseLong(object.getString("createTime"))));
                        long recevermsgTime = Long.parseLong(object.getString("createTime"));
                        long savemsgTime = ConstanceMethod.getSharedPreferences(HomeActivity.this, "Msg").getLong("msgTag", 0);
                        Log.e("time", "savemsgTime:" + savemsgTime + "----recevermsgTime:" + recevermsgTime + "----" + (recevermsgTime <= savemsgTime));
                        if (recevermsgTime <= savemsgTime) {
                            //如果消息标记和本地储存的消息标记一致，则任务是已经处理了的消息，不再处理


                        /*
                        * 开门页面时，让app后台运行，门关闭后，点开app，开锁中页面一直显示
                        * */
//                        if (LastDoorState != DEFAULT) {
//                            onBackPressed();
//                        }
                            return;
                        }

                        String tymsgType = object.getString("msgType");
                        ConstanceMethod.addHasDealMsg(HomeActivity.this, Long.parseLong(object.getString("createTime")));
                        switch (tymsgType) {

                            case "01"://门开关通知

                                if (!isRequestOpen) {
                                    return;
                                }
                                Log.e("push", object.toString() + "time:" + SecondToDate.getDateToString(Long.parseLong(object.getString("createTime"))));
                                if (object.getInt("doorState") == 1) {//已开门
                                    Log.e("doorState", "1");
                                    if (curFragment instanceof OpeningDoorFragment || LastDoorState == OPENING_DOOR) {
                                        curFragment = new OpenDoorSuccessFragment();
                                        showHomepage = false;
                                        switchFragment();
                                    /*
                                    * 将dialog 消失掉
                                    * */

                                        LastDoorState = OPEN_LOCK_SUCCESS;
                                    } else if (curFragment instanceof OpenDoorSuccessFragment || LastDoorState == OPEN_LOCK_SUCCESS) {
                                        return;
                                    }
                                } else if (object.getInt("doorState") == 0) {//关门,上货成功

                                /*
                                * 其实可以不添加的，之后看要不要删掉把
                                * */
                                /*if (curFragment instanceof OpeningDoorFragment || LastDoorState == OPENING_DOOR) {
                                    Log.d("222222222222", "----------");
                                    //收到的门状态为关门,且上一状态为初始状态则认为是失败
                                    curFragment = new OpenDoorFailFragment();
                                    showHomepage = false;
                                    isRequestOpen = false;
                                    switchFragment();
                                    LastDoorState = OPEN_LOCK_FAILED;
                                    qrResult = "";
                                    return;
                                }*/
                                /*else{
                                    onBackPressed();
                                }*/
                                }
                                break;
                            case "02"://机器故障通知
                                //  ConstanceMethod.addHasDealMsg(HomeActivity.this, Long.parseLong(object.getString("createTime")));
                                mNotificationManager.notify(1, mBuilder.build());
                                break;
                            case "03":
                                if (object.getInt("doorState") != 0) {
                                    return;
                                }
                                if (curFragment instanceof OpenDoorSuccessFragment || LastDoorState == OPEN_LOCK_SUCCESS) {
                                    LastDoorState = DEFAULT;
                                    if (object.has("goodsList")) {
                                        FragmentFactory.getInstance().getSavedBundle().putString("callbackMsg", object.toString());
                                        curFragment = new CloseDoorFragment();
                                        showHomepage = false;
                                        isRequestOpen = false;
                                        switchFragment();
                                        Toast.makeText(getApplication(), "关门成功", Toast.LENGTH_LONG).show();
                                        return;
                                    } else {
                                        //上次的门状态必须为开门，此次收到关门才认为是关锁成功，
                                        // 但并不一定会收到上下货数据，最.
//                                    onBackPressed();
                                        getCurFragment();
                                        showHomepage = true;
                                        isRequestOpen = false;
                                        qrResult = "";
                                        switchFragment();
                                        LastDoorState = DEFAULT;
                                        Toast.makeText(getApplication(), "关门成功，没有上下货", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                super.handleMessage(msg);
            }
        };
    }


    private void initListener() {
        tabLayout.addOnTabSelectedListener(this);
    }

    private void initData() {

        if (PerSonMessage.userType == Constants.MACHINE_MANAGER && !PerSonMessage.role.contains("1")) {
            addPartTab();
        } else {
            addAllTab();
        }
        switchFragment();
        setNotifySnackbar();
        MnanagerNameAndTimePart.setText(PerSonMessage.name + ", " + SecondToDate.getTimePart());
    }

    private void addPartTab() {
        tabLayout.removeAllTabs();
        for (int i = 0; i < Constants.TabTitlesForManager.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setIcon(Constants.TabIconsForManager[i]);
            tab.setText(Constants.TabTitlesForManager[i]);
            tabLayout.addTab(tab);
        }
        curFragment = new MachineFragment();
    }

    private void addAllTab() {
        tabLayout.removeAllTabs();
        for (int i = 0; i < Constants.TabTitles.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setIcon(Constants.TabIcons[i]);
            tab.setText(Constants.TabTitles[i]);
            tabLayout.addTab(tab);
        }
        curFragment = new TradeFragment();
    }


    private void initViews() {
        callback = this;
        mNotificationBroadcastReceiver = new NotificationBroadcastReceiver();
        IntentFilter intentFilterNotify = new IntentFilter(BROADCAST_ACTION_NOTIFIY);
        registerReceiver(mNotificationBroadcastReceiver, intentFilterNotify);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(HomeActivity.this);
        mIntent = new Intent(this, MqttService.class);
        mBuilder.setContentTitle("通知")
                //设置通知栏标题
                .setContentText("有机器发生了故障!!!") //设置通知栏显示内容
                .setContentIntent(getDefalutIntent(1)) //设置通知栏点击意图
//  .setNumber(number) //设置通知集合的数量
                //  .setTicker("故障通知来啦!") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.push_icon)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.push_icon));//设置通知小ICON
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        MnanagerNameAndTimePart = (TextView) headerView.findViewById(R.id.managerNameAndTimepart);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_color));
        navigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.selector_text_color));
        navigationView.setCheckedItem(R.id.icon_home);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);//修改toolbar默认图标，必须添加
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void startMqttService() {

        startService(mIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /*
            * 强制开门中时不能按返回按钮
            * */
          /*  if (curFragment instanceof OpeningDoorFragment) {
                 return;
            }*/
            if (showHomepage) {
                mOnBackPressListener.OnBackDown();
            } else {
                /*
                * 当用户返回开锁成功返回首页后，不改变门状态，让它同样能跳转到上下货页面
                * */
                if (curFragment instanceof OpenDoorSuccessFragment || LastDoorState == OPEN_LOCK_SUCCESS) {

                } else if (curFragment instanceof OpeningDoorFragment || LastDoorState == OPENING_DOOR) {
                    //如果开门中返回，也不做什么处理
                } else {
                    LastDoorState = DEFAULT;
                }
                showHomepage = true;
                getCurFragment();
                switchFragment();
                navigationView.setCheckedItem(R.id.icon_home);
                tabLayout.setVisibility(View.VISIBLE);

            }

//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_home:
                if (!showHomepage) {
                    getCurFragment();
                    showHomepage = true;
                    switchFragment();
                }

                break;
            case R.id.icon_rMachineCode:
                isRecogniteMachineCode = true;
                openSaoma();
                break;
            case R.id.icon_person:
                if (!(curFragment instanceof PerSonFragment)) {
                    curFragment = new PerSonFragment();
                    showHomepage = false;
                    switchFragment();
                }

                break;
            case R.id.icon_change_pass:
                if (!(curFragment instanceof ChangePasswordFragment)) {
                    curFragment = new ChangePasswordFragment();
                    showHomepage = false;
                    switchFragment();
                }
                break;
            case R.id.icon_about:
                if (!(curFragment instanceof AboutFragment)) {
                    curFragment = new AboutFragment();
                    showHomepage = false;
                    switchFragment();
                }
                break;
            case R.id.icon_update:
                checktheupdata();
             /*   curFragment = new TradeFragment();
                tabLayout.setVisibility(View.GONE);
                showHomepage = false;*/
                break;
            case R.id.icon_logout:
                MyDialog myDialog = new MyDialog(this);
                myDialog.showDialog(myDialog.getLogoutDialog(HomeActivity.this));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void checktheupdata() {
        final Map<String, Object> body = new HashMap<>();
        body.put("version", getVersion());
        new AsyncTask<Void, Void, Boolean>() {
            String response;
            String version;
            String fileUrl;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    response = OkHttpUtil.post(HttpRequstUrl.GET_APP_VERSION_URL, JsonUtil.mapToJson(body));
                    JSONObject object = new JSONObject(response);
                    if (!object.getJSONObject("d").getString("version").equals("null")) {
                        version = object.getJSONObject("d").getString("version");
                        fileUrl = object.getJSONObject("d").getString("fileUrl");
                        return true;
                    }
                } catch (IOException | JSONException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    if (!getVersion().equals(version)) {
                        new AlertDialog.Builder(HomeActivity.this).setTitle("版本更新").setMessage("检测到有新版本,建议您更新")
                                .setNegativeButton("暂不更新", null).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(fileUrl);
                                intent.setData(content_url);
                                startActivity(intent);
                            }
                        }).create().show();
                    } else {
                        Toast.makeText(getApplication(), "当前已是最新版本...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplication(), response, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();


    }


    /*
    * 需要注意的是，只有在Android 4.4及以上系统才支持沉浸式模式
    *
    * 最标准的沉浸式模式
    * */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
    }

    public void getCurFragment() {
        switch (currentHomePageNum) {
            case 0:
                if (PerSonMessage.userType == Constants.MACHINE_MANAGER && !PerSonMessage.role.contains("1")) {
                    curFragment = new MachineFragment();
                } else {
                    curFragment = new TradeFragment();
                }
                break;
            case 1:
                if (PerSonMessage.userType == Constants.MACHINE_MANAGER && !PerSonMessage.role.contains("1")) {
                    curFragment = new ExceptionFragment();
                } else {
                    curFragment = new MachineFragment();
                }
                break;
            case 2:

                if (PerSonMessage.userType == Constants.MACHINE_MANAGER && !PerSonMessage.role.contains("1")) {
                    curFragment = new ProductFragment();
                } else {
                    curFragment = new ExceptionFragment();
                }
                break;
            case 3:
                curFragment = new ProductFragment();
                break;

        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    /*
    * 切换fragment
    * */
    public void switchFragment() {
        if (showHomepage) {
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.GONE);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.home_tab_frame, curFragment);
        ft.commitAllowingStateLoss();
        BaseFragment.curFragment = curFragment;
    }

    public void DoSql() {
        Sql sql = new Sql(this);
        boolean isupdated = false;
        for (String saves : sql.getAllUserID()) {
            if (String.valueOf(PerSonMessage.userId).equals(saves)) {
                Log.d("debug", "userId" + PerSonMessage.userId);
                sql.updateContact(String.valueOf(PerSonMessage.userId), PerSonMessage.loginAccount, PerSonMessage.loginPassword);
                isupdated = true;
                break;
            }
        }
        if (!isupdated) {
            sql.insertContact(String.valueOf(PerSonMessage.userId), PerSonMessage.loginAccount, PerSonMessage.loginPassword);
        }

    }

    //记录登录状态
    private void SaveUserMessager() {

        if (ConstanceMethod.getSharedPreferences(this, "ShowWelcome").getBoolean("isRemember", false)) {//登录界面选择记录密码则执行保存信息等操作
            if (ConstanceMethod.getSharedPreferences(this, "ShowWelcome").getBoolean("isFirst", true)) {
                ConstanceMethod.isFirstLogin(this, false);
            }
            DoSql();
        } else {//登录界面为勾选记住密码则执行清空保存信息等操作
            Sql sql = new Sql(this);
            if (sql.getAllCotacts().size() != 0) {
                sql.deleteAllContact(sql.getAllCotacts().size());
            }
            ConstanceMethod.isFirstLogin(this, true);
        }

    }

    public void finishActivity() {
        Log.d("---finishActivity---", "----");
        if (System.currentTimeMillis() - lastBackClicked < 2000) {
            super.onBackPressed();
            GetService.getInstance().breakClient();
//            System.exit(0);
        } else {
            lastBackClicked = System.currentTimeMillis();
            Toast.makeText(this, "在按一次退出程序", Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnBackPressListener(OnBackPressListener onBackPressListener) {
        if (onBackPressListener != null) {
            mOnBackPressListener = onBackPressListener;
        }
    }

    public void clickIconToOpenDrawer() {
        drawer.openDrawer(Gravity.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    qrResult = data.getStringExtra("QR_CODE");
                    //Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
                    requestOpenDoor(qrResult);
                } else {
                    Toast.makeText(getApplication(), "无法获取扫描结果", Toast.LENGTH_LONG).show();
                    clearRecogniteMsg();
                }
                break;

        }
    }

    /*
    * 如果是识别机器码状态，清除
    * */
    private void clearRecogniteMsg() {
        isRecogniteMachineCode = false;
        qrResult = "";
        navigationView.setCheckedItem(R.id.icon_home);
    }

    public void requestOpenDoor(final String QRCode) {
        if ("".equals(qrResult)) {
            Toast.makeText(this, "解析失败，请重新扫码", Toast.LENGTH_SHORT).show();
            clearRecogniteMsg();
            return;
        }
        String qrString[] = QRCode.split("machineCode=");

        Log.d("---------------------", qrString.toString());
        if (qrString.length <= 1) {
            Toast.makeText(this, "解析失败，请重新扫码", Toast.LENGTH_SHORT).show();
            clearRecogniteMsg();
            return;
        }
        String machineCode = qrString[1];
        if (isRecogniteMachineCode) {
            new MyDialog(this).showDialog(MyDialog.normalDialog(this, machineCode));
            clearRecogniteMsg();
            return;
        }
        Log.e("machineCode", machineCode);
        final Map<String, Object> body = new HashMap<>();
        body.put("machineCode", machineCode);
        body.put("userID", PerSonMessage.userId);
        body.put("QRCode", QRCode);
        body.put("password", PerSonMessage.loginPassword);
        new AsyncTask<Void, Void, Boolean>() {
            String err;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    String response = OkHttpUtil.post(HttpRequstUrl.OPEN_MACHINE_DOOR_URL, JsonUtil.mapToJson(body));
                    Log.e("openResponse", response + "##");
                    JSONObject object = new JSONObject(response);
                    err = object.getString("err");
                    return (err.equals("") || err.equals("null")) && object.getBoolean("d");

                } catch (IOException e) {
                    err = RequstTips.getErrorMsg(e.getMessage());
                } catch (JSONException e) {
                    Log.e("openResponse", e + "##");
                    err = RequstTips.JSONException_Tip;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {

                if (aBoolean) {
                    isRequestOpen = true;
                    //我觉得这个地方不要判断
//                    if (!(curFragment instanceof OpeningDoorFragment)) {
                    FragmentFactory.getInstance().getSavedBundle().putString("QRCode", QRCode);
                    curFragment = new OpeningDoorFragment();
                    LastDoorState = OPENING_DOOR;
                    showHomepage = false;
                    switchFragment();
                } else {
                    Toast.makeText(getApplication(), err, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    Intent intent = new Intent();
                    intent.setClass(getApplication(), QrCodeActivity.class);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    Toast.makeText(getApplication(), "您拒绝了系统调用摄像头权限,请手动打开相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void openSaoma() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA}, CAMERA_OK);
                //当fragment申请权限是fragment本身申请权限,不需要ActivityCompat.requestPermissions.不然无法执行授权回调
                //当Activity申请权限是Activity本身申请权限,需要ActivityCompat.requestPermissions.来执行授权回调
            } else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
                Intent intent = new Intent();
                intent.setClass(getApplication(), QrCodeActivity.class);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        } else {
//这个说明系统版本在6.0之下，不需要动态获取权限。
            Intent intent = new Intent();
            intent.setClass(getApplication(), QrCodeActivity.class);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

        }
    }


    @Override
    public void jumpActivity(Class<?> activitycalss, Bundle intentData) {
        Intent intent = new Intent();
        if (activitycalss != null) {
            intent.setClass(getApplication(), activitycalss);
            if (intentData != null) {
                intent.putExtras(intentData);
            }
            startActivity(intent);
        }

    }

    /*
        * 添加通知
        * */
    public void setNotifySnackbar() {
        //  NotifySnackbar.addNotifySnackbar(this, notifyLayout);
    }

    @SuppressWarnings("ConstantConditions")
    public void selectedException() {
        tabLayout.getTabAt(2).select();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentHomePageNum = tab.getPosition();
        switch (tab.getPosition()) {
            case 0:
                if (PerSonMessage.userType == Constants.MACHINE_MANAGER && !PerSonMessage.role.contains("1")) {
                    addCurMachineFragment();
                } else {
                    addCurTradFragment();
                }
                break;
            case 1:

                if (PerSonMessage.userType == Constants.MACHINE_MANAGER && !PerSonMessage.role.contains("1")) {
                    addCurExecptionFragment();
                } else {
                    addCurMachineFragment();
                }
                break;
            case 2:
                if (PerSonMessage.userType == Constants.MACHINE_MANAGER && !PerSonMessage.role.contains("1")) {
                    addCurProductFragment();
                } else {
                    addCurExecptionFragment();
                }
                break;
            case 3:
                addCurProductFragment();
                break;
            default:
                break;
        }
        switchFragment();

    }

    private void addCurExecptionFragment() {
        if (!(curFragment instanceof ExceptionFragment)) {
            curFragment = new ExceptionFragment();
        }
    }

    private void addCurProductFragment() {
        if (!(curFragment instanceof ProductFragment)) {
            curFragment = new ProductFragment();
        }
    }

    private void addCurMachineFragment() {
        if (!(curFragment instanceof MachineFragment)) {
            curFragment = new MachineFragment();
        }
    }

    private void addCurTradFragment() {
        if (!(curFragment instanceof TradeFragment)) {
            curFragment = new TradeFragment();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void getUserDetail(ItemPerson itemPerson) {
        if (itemPerson != null) {
            try {
                long newLoginTime = SecondToDate.getLongOfDate(SecondToDate.getDateOfString(itemPerson.lastLoginTime));
                long localLoginTime = SecondToDate.getLongOfDate(SecondToDate.getDateOfString(PerSonMessage.lastLoginTime));
                if (newLoginTime > localLoginTime || !TextUtils.equals(String.valueOf(newLoginTime), String.valueOf(localLoginTime))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("掉线通知");
                    builder.setMessage("您已服务器的通讯已断开,请重新登录" + "\n可能原因:\n1.您的账号已被他人登录");
                    /*
                    * 2.网络环境差
                    3.服务器重启或异常
                    * */

                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GetService.getInstance().breakClient();
                            PerSonMessage.isexcit = true;
                            jumpActivity(LoginActivity.class, null);
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    startMqttService();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            startMqttService();
        }
    }

    public interface OnBackPressListener {
        void OnBackDown();
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Log.e("掉线", "掉线");


        handler.sendEmptyMessage(0);

    }

    @Override
    public void messageArrived(final String s, final MqttMessage mqttMessage) throws Exception {
        //  Log.e("push", "recevied_push" + new String(mqttMessage.getPayload()));
        JSONObject object = new JSONObject(new String(mqttMessage.getPayload()));
        Message msg = new Message();
        msg.obj = object;
        handler.sendMessage(msg);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntent);
        unregisterReceiver(mNotificationBroadcastReceiver);//销毁广播
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public PendingIntent getDefalutIntent(int flags) {
        Intent intent = new Intent(BROADCAST_ACTION_NOTIFIY);
        return PendingIntent.getBroadcast(this, 0, intent, flags);
    }


    class NotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            //接收notification点击事件
            addCurExecptionFragment();
            selectedException();
            showHomepage = true;
            switchFragment();
        }
    }
}