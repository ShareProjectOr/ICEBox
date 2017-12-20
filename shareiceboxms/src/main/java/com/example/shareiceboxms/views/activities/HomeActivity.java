package com.example.shareiceboxms.views.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.ConstanceMethod;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.Sql;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.helpers.NotifySnackbar;
import com.example.shareiceboxms.views.fragments.AboutFragment;
import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.ChangePasswordFragment;
import com.example.shareiceboxms.views.fragments.PerSonFragment;
import com.example.shareiceboxms.views.fragments.exception.ExceptionFragment;
import com.example.shareiceboxms.views.fragments.machine.MachineFragment;
import com.example.shareiceboxms.views.fragments.product.ProductFragment;
import com.example.shareiceboxms.views.fragments.OpeningDoorFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeFragment;

import org.zackratos.ultimatebar.UltimateBar;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    private TabLayout tabLayout;
    private TextView notifyLayout;
    public BaseFragment curFragment = null;
    String curFragmentTag;
    private OnBackPressListener mOnBackPressListener;
    private int currentHomePageNum = 0;
    private boolean showHomepage = true;
    private final int SCANNIN_GREQUEST_CODE = 1;
    private static final int CAMERA_OK = 517;
    private long lastBackClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        setImmersiveStateBar();
        SaveUserMessager();
        initViews();
        initData();
        initListener();
    }

    private void initListener() {
        tabLayout.addOnTabSelectedListener(this);
    }

    private void initData() {

        for (int i = 0; i < Constants.TabTitles.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setIcon(Constants.TabIcons[i]);
            tab.setText(Constants.TabTitles[i]);
            tabLayout.addTab(tab);
        }
        curFragment = new TradeFragment();
        switchFragment();
        showNotify();


    }

    private void initViews() {
        notifyLayout = (TextView) findViewById(R.id.notify);
        setNotifySnackbar();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.drawable.selector_text_color));
        navigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.drawable.selector_text_color));
        navigationView.setCheckedItem(R.id.icon_home);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);//修改toolbar默认图标，必须添加
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    public DrawerLayout getDrawer() {
        return drawer;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (showHomepage) {
                mOnBackPressListener.OnBackDown();
            } else {
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
             /*   curFragment = new TradeFragment();
                tabLayout.setVisibility(View.GONE);
                showHomepage = false;*/
                break;
            case R.id.icon_logout:
                new MyDialog().getLogoutDialog(HomeActivity.this).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getCurFragment() {
        switch (currentHomePageNum) {
            case 0:
                curFragment = new TradeFragment();
                break;
            case 1:
                curFragment = new MachineFragment();
                break;
            case 2:
                curFragment = new ExceptionFragment();
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
    * 设置全透明状态栏
    * /*/
    private void setImmersiveStateBar() {
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
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
        ft.commit();
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
        ConstanceMethod.isFirstLogin(this, false);
        DoSql();
    }

    public void finishActivity() {
        Log.d("---finishActivity---", "----");
        if (System.currentTimeMillis() - lastBackClicked < 2000) {
            super.onBackPressed();
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
                    String result = data.getStringExtra("QR_CODE");
                    // TODO 获取结果，做逻辑操作
                    Toast.makeText(getApplication(), result, Toast.LENGTH_LONG).show();
                    //  mMachineCode.setText(result);
                    //    tvResult.setText(result);
                    if (!(curFragment instanceof OpeningDoorFragment)) {
                        FragmentFactory.getInstance().getSavedBundle().putString("eQcode", result);
                        curFragment = new OpeningDoorFragment();
                        showHomepage = false;
                        switchFragment();
                    }
                } else {
                    Toast.makeText(getApplication(), "无法获取扫描结果", Toast.LENGTH_LONG).show();
                    //  new AlertView("提示", "无法获取扫描结果", null, new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, null).show();
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    Intent intent = new Intent();
                    intent.setClass(getApplication(), CaptureActivity.class);
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
                intent.setClass(getApplication(), CaptureActivity.class);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        } else {
//这个说明系统版本在6.0之下，不需要动态获取权限。
            Intent intent = new Intent();
            intent.setClass(getApplication(), CaptureActivity.class);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

        }
    }

/*    public void jumpActivity(Class<?> activitycalss, Bundle intentData) {
        Log.e("HomeActivity", "扫码");
        Intent intent = new Intent();
        if (activitycalss != null) {
            intent.setClass(getApplication(), activitycalss);
            if (intentData != null) {
                intent.putExtra("intentdata", intentData);
            }
            startActivity(intent);
        }

    }*/

    @Override
    public void jumpActivity(Class<?> activitycalss, Bundle intentData) {
        Intent intent = new Intent();
        if (activitycalss != null) {
            intent.setClass(getApplication(), activitycalss);
            if (intentData != null) {
                intent.putExtra("intentdata", intentData);
            }
            startActivity(intent);
        }

    }

    /*
        * 添加通知
        * */
    private void setNotifySnackbar() {
        NotifySnackbar.addNotifySnackbar(this, notifyLayout);
    }

    public void selectedException() {
        tabLayout.getTabAt(2).select();
    }

    public void showNotify() {
        NotifySnackbar.showNotify();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentHomePageNum = tab.getPosition();
        switch (tab.getPosition()) {
            case 0:
                if (!(curFragment instanceof TradeFragment)) {
                    curFragment = new TradeFragment();

                }

                break;
            case 1:
                if (!(curFragment instanceof MachineFragment)) {
                    curFragment = new MachineFragment();
                }
                break;
            case 2:
                if (!(curFragment instanceof ExceptionFragment)) {
                    curFragment = new ExceptionFragment();
                }
                break;
            case 3:
                if (!(curFragment instanceof ProductFragment)) {
                    curFragment = new ProductFragment();
                }
                break;
            default:
                break;
        }
        switchFragment();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public interface OnBackPressListener {
        void OnBackDown();
    }


}
