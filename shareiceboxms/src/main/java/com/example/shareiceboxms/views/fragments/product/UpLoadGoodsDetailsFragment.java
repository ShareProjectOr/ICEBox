package com.example.shareiceboxms.views.fragments.product;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.UpLoadDetailsGoodListAdapter;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.widget.ListViewForScrollView;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpLoadGoodsDetailsFragment extends BaseFragment implements View.OnTouchListener {
    private View contentView;
    private String upLoadGoodsId;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ImageView mDrawerIcon;
    private ImageView mSaoma;
    private ListViewForScrollView mUpLoadDetailsGoodsList;
    private UpLoadDetailsGoodListAdapter mAdapter;
    private HomeActivity homeActivity;
   // private NestedScrollView contentScroll;
  //  private TextView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_up_load_goods));
            upLoadGoodsId = FragmentFactory.getInstance().getSavedBundle().getString("upLoadGoodsId");
            initview();
            initdata();
            iniListener();
        }
        return contentView;
    }

    private void iniListener() {
        mDrawerIcon.setOnClickListener(this);
        mSaoma.setOnClickListener(this);
      //  contentScroll.setOnTouchListener(this);
    }

    private void initdata() {
    }

    private void initview() {
        mAppBarLayout = (AppBarLayout) contentView.findViewById(R.id.appBarLayout);
        mToolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        mDrawerIcon = (ImageView) contentView.findViewById(R.id.drawerIcon);
        mSaoma = (ImageView) contentView.findViewById(R.id.saoma);
        mAdapter = new UpLoadDetailsGoodListAdapter(getActivity());
      //  contentScroll = (NestedScrollView) contentView.findViewById(R.id.contentScroll);
        homeActivity = (HomeActivity) getActivity();
        mUpLoadDetailsGoodsList = (ListViewForScrollView) contentView.findViewById(R.id.upLoadDetailsGoodsList);
        mUpLoadDetailsGoodsList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saoma:
                homeActivity.openSaoma();
                break;
            case R.id.drawerIcon:
                homeActivity.clickIconToOpenDrawer();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
      /*  if (contentScroll.getScrollY() == 0) {
            //顶部
        }

        // 判断scrollview 滑动到底部
        // scrollY 的值和子view的高度一样，这认为滑动到了底部
        //getMeasuredHeight()实际高度
        if (contentScroll.getChildAt(0).getMeasuredHeight() - contentScroll.getHeight()
                == contentScroll.getScrollY()) {
            //底部
        }*/
        return false;
    }
}
