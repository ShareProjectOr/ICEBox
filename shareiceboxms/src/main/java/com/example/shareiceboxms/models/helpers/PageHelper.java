package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountDetailFragment;

import java.util.Map;

/**
 * Created by WH on 2017/12/27.
 */

public class PageHelper implements View.OnClickListener {
    private View view;
    private Context context;
    private TradeAccountDetailFragment fragment;
    private TextView homePage;
    private TextView lastPage;
    private LinearLayout pageItem;
    private TextView nextPage;
    private TextView totalPageCount;
    private TextView totalCount;
    private EditText page;
    private TextView intent;
    private static int SHOW_PAGE = 5;//总共显示5页
    private int totalPageText = 1, totalCountText = 0;

    public PageHelper(Context context, TradeAccountDetailFragment fragment, View view) {
        this.context = context;
        this.fragment = fragment;
        this.view = view;
        bindViews();
    }

    private void bindViews() {

        homePage = (TextView) view.findViewById(R.id.homePage);
        lastPage = (TextView) view.findViewById(R.id.lastPage);
//        pageItem = (LinearLayout) view.findViewById(R.id.pageItem);
        nextPage = (TextView) view.findViewById(R.id.nextPage);
        totalPageCount = (TextView) view.findViewById(R.id.totalPageCount);
        totalCount = (TextView) view.findViewById(R.id.totalCount);
        page = (EditText) view.findViewById(R.id.page);
        intent = (TextView) view.findViewById(R.id.intent);
        homePage.setOnClickListener(this);
        lastPage.setOnClickListener(this);
        nextPage.setOnClickListener(this);
        intent.setOnClickListener(this);
    }

    private void bindDatas() {
        totalPageCount.setText(totalPageText+"");
        totalCount.setText(totalCountText+"");
    }

    public void setTotalText(int totalPageText, int totalCountText) {
        this.totalPageText = totalPageText;
        this.totalCountText = totalCountText;
        bindDatas();
        initTextColor();
    }

    public void initTextColor() {
        int pageText = Integer.parseInt(page.getText().toString());
        if (totalPageText > 1) {
            nextPage.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }
    }

    @Override
    public void onClick(View v) {
        if (totalPageText == 0 || totalCountText == 0) {
            return;
        }
        Map<String, Object> params = RequestParamsContants.getInstance().getTradeRecordsParams();
        switch (v.getId()) {
            case R.id.homePage:
                if (page.getText().toString().equals("1")) {
                    return;
                }
                lastPage.setTextColor(ContextCompat.getColor(context, R.color.gray_light_deep));
                initTextColor();
                page.setText("1");
                fragment.getRecorDatas(params);
                break;
            case R.id.lastPage:
                int curPage = Integer.valueOf(page.getText().toString());
                if (curPage == 1) {
                    return;
                }
                if (curPage - 1 == 1) {
                    lastPage.setTextColor(ContextCompat.getColor(context, R.color.gray_light_deep));
                }
                if (curPage == totalPageText) {
                    nextPage.setTextColor(ContextCompat.getColor(context, R.color.blue));
                }
                page.setText(curPage - 1);
                params.put("p", Integer.parseInt(page.getText().toString()));
                fragment.getRecorDatas(params);
                break;
            case R.id.nextPage:
                int curPage1 = Integer.valueOf(page.getText().toString());
                if (curPage1 == totalPageText) {
                    return;
                }
                if (curPage1 + 1 == totalPageText) {
                    nextPage.setTextColor(ContextCompat.getColor(context, R.color.gray_light_deep));
                }
                if (curPage1 == 1) {
                    lastPage.setTextColor(ContextCompat.getColor(context, R.color.blue));
                }
                page.setText(curPage1 + 1);
                params.put("p", Integer.parseInt(page.getText().toString()));
                fragment.getRecorDatas(params);
                break;
            case R.id.intent:
                if (Integer.parseInt(page.getText().toString()) > totalPageText) {
                    page.setText(totalPageText + "");
                }
                if (page.getText().toString().equals("0")) {
                    page.setText("1");
                }
                params.put("p", Integer.parseInt(page.getText().toString()));
                fragment.getRecorDatas(params);
                break;
        }
    }
}
