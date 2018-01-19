package com.example.shareiceboxms.views.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.widget.PathTextView;
import com.example.shareiceboxms.views.activities.HomeActivity;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Lyu on 2017/12/19.
 * 正在开门中
 */

public class OpeningDoorFragment extends BaseFragment {
    private PathTextView mShowtext;
    private GifImageView mShowgif;
    private View content;
    private String QRCode;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_opening_door));
            bindViews();
        }
        return content;
    }

    private void bindViews() {
        mShowtext = (PathTextView) content.findViewById(R.id.showtext);
        mShowgif = (GifImageView) content.findViewById(R.id.showgif);
        homeActivity = (HomeActivity) getActivity();
        QRCode = FragmentFactory.getInstance().getSavedBundle().getString("QRCode");
        //当前页面最多停留30S，如果30S后还收不到服务器的回馈，则认为其开门失败.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeActivity.curFragment = new OpenDoorFailFragment();
                homeActivity.switchFragment();
            }
        }, 30000);
      /*  //  Glide.with(this).load(R.drawable.opening).asGif().fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mShowgif);
        if (Build.VERSION.SDK_INT >= 21) {
            //5.0以上
            mShowtext.setTextColor(Color.BLUE);
            mShowtext.setTextSize(2);
            mShowtext.init("Opening", -1);
        }*/
    }
}
