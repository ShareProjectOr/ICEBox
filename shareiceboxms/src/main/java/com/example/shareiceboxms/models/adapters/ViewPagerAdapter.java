package com.example.shareiceboxms.models.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import com.example.shareiceboxms.views.fragments.BaseFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeFragment;
import com.example.shareiceboxms.models.contants.Constants;

/**
 * Created by WH on 2017/11/28.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    FragmentManager fm;
    List<BaseFragment> fragments;
    String[] TabTitles;


    public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments, String[] TabTitles) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
        this.TabTitles = TabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return (fragments == null) ? null : fragments.get(position);
    }

    @Override
    public int getCount() {
        return (fragments == null) ? 0 : fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return (TabTitles.length < position) ? TabTitles[0] : TabTitles[position];
    }

}
