package com.example.shareiceboxms.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.helpers.NotifySnackbar;
import com.example.shareiceboxms.views.fragments.AboutFragment;
import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.ChangePasswordFragment;
import com.example.shareiceboxms.views.fragments.PerSonFragment;
import com.example.shareiceboxms.views.fragments.exception.ExceptionFragment;
import com.example.shareiceboxms.views.fragments.machine.MachineFragment;
import com.example.shareiceboxms.views.fragments.product.ProductFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeFragment;

import org.zackratos.ultimatebar.UltimateBar;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    private TabLayout tabLayout;
    private TextView notifyLayout;
    BaseFragment curFragment = null;
    String curFragmentTag;
    private OnBackPressListener mOnBackPressListener;
    private int currentHomePageNum = 0;
    private boolean showHomepage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        setImmersiveStateBar();
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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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

        drawer.setDrawerListener(toggle);
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
                getCurFragment();
                switchFragment();
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.icon_home:
                if (!showHomepage) {
                    getCurFragment();
                    showHomepage = true;
                    tabLayout.setVisibility(View.VISIBLE);
                    switchFragment();
                }

                break;
            case R.id.icon_person:
                if (!(curFragment instanceof PerSonFragment)) {
                    curFragment = new PerSonFragment();
                    showHomepage = false;
                    tabLayout.setVisibility(View.GONE);
                    switchFragment();
                }

                break;
            case R.id.icon_change_pass:
                if (!(curFragment instanceof ChangePasswordFragment)) {
                    curFragment = new ChangePasswordFragment();
                    showHomepage = false;
                    tabLayout.setVisibility(View.GONE);
                    switchFragment();
                }
                break;
            case R.id.icon_about:
                if (!(curFragment instanceof AboutFragment)) {
                    curFragment = new AboutFragment();
                    showHomepage = false;
                    tabLayout.setVisibility(View.GONE);
                    switchFragment();
                }
                break;
            case R.id.icon_update:
             /*   curFragment = new TradeFragment();
                tabLayout.setVisibility(View.GONE);
                showHomepage = false;*/
                break;
            case R.id.icon_logout:
              /*  curFragment = new TradeFragment();
                tabLayout.setVisibility(View.GONE);
                showHomepage = false;*/
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getCurFragment() {
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
    private void switchFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.home_tab_frame, curFragment);
        ft.commit();
    }

    public void finishActivity() {
        Log.d("---finishActivity---", "----");
        super.onBackPressed();
    }

    public void setOnBackPressListener(OnBackPressListener onBackPressListener) {
        if (onBackPressListener != null) {
            mOnBackPressListener = onBackPressListener;
        }
    }

    public void clickIconToOpenDrawer() {
        drawer.openDrawer(Gravity.START);
    }

    public void jumpActivity(Class<?> activitycalss, Bundle intentData) {
        Log.e("HomeActivity", "扫码");
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
