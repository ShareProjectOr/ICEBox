package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.views.activities.BaseActivity;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountDetailFragment;

import java.util.Map;

/**
 * Created by WH on 2017/12/27.
 * 库存商品的分页
 */

public class StocksPageHelper implements View.OnClickListener {
    private View view;
    private Context context;
    private TextView homePage;
    private TextView lastPage;
    private TextView nextPage;
    private TextView totalPageCount;
    private TextView totalCount;
    private EditText page;
    private MachineItemAddViewHelper machineItemAddViewHelper;
    private TextView intent;
    private static int SHOW_PAGE = 5;//总共显示5页
    private String curPage = "1";
    private int totalPageText = 1, totalCountText = 0;

    public StocksPageHelper(Context context, MachineItemAddViewHelper machineItemAddViewHelper, View view) {
        this.context = context;
        this.view = view;
        this.machineItemAddViewHelper = machineItemAddViewHelper;
        bindViews();
    }

    private void bindViews() {
        homePage = (TextView) view.findViewById(R.id.homePage);
        lastPage = (TextView) view.findViewById(R.id.lastPage);
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

    public void bindDatas(int totalPageText, int totalCountText) {
        this.totalCountText = totalCountText;
        this.totalPageText = totalPageText;
        totalPageCount.setText(this.totalPageText + "");
        totalCount.setText( this.totalCountText + "");
        initTextColor();
    }

    public void initTextColor() {
        changeIfOverLimit();
        int curPage = Integer.parseInt(page.getText().toString());
        changeTextColor(curPage);
    }

    @Override
    public void onClick(View v) {
        if (totalPageText == 0 || totalCountText == 0) {
            return;
        }
        changeIfOverLimit();
        switch (v.getId()) {
            case R.id.homePage:
                if (page.getText().toString().equals("1")) {
                    return;
                }
                if (page.getText().toString().isEmpty()) {
                    page.setText("1");
                }
                lastPage.setTextColor(ContextCompat.getColor(context, R.color.gray_light_deep));
                initTextColor();
                page.setText("1");
                loadPageDatas();

                break;
            case R.id.lastPage:
                int curPage = Integer.valueOf(page.getText().toString());
                if (curPage == 1) {
                    return;
                }
                page.setText(String.valueOf(curPage - 1));
                changeTextColor(Integer.parseInt(page.getText().toString()));
                loadPageDatas();
                break;
            case R.id.nextPage:
                int curPage1 = Integer.valueOf(page.getText().toString());
                if (curPage1 == totalPageText) {
                    return;
                } else if (curPage1 > totalPageText) {

                }
                page.setText(String.valueOf(curPage1 + 1));
                changeTextColor(Integer.parseInt(page.getText().toString()));
                loadPageDatas();
                break;
            case R.id.intent:
                if (page.getText().toString().isEmpty()) {
                    page.setText("1");
                }
                if (Integer.parseInt(page.getText().toString()) > totalPageText) {
                    page.setText(totalPageText + "");
                }
                loadPageDatas();
                break;
        }
    }

    /*
    * 根据页数加载数据
    * */
    private void loadPageDatas() {
        curPage = page.getText().toString();
        Map<String, Object> params = RequestParamsContants.getInstance().getMachineStockProductParams();
        params.put("p", Integer.parseInt(curPage));
        machineItemAddViewHelper.getDatas(params);
    }

    private void changeTextColor(int page) {
        if (page <= 1) {
            lastPage.setTextColor(ContextCompat.getColor(context, R.color.gray_light_deep));
            homePage.setTextColor(ContextCompat.getColor(context, R.color.gray_light_deep));
        } else {
            lastPage.setTextColor(ContextCompat.getColor(context, R.color.blue));
            homePage.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }
        if (page < totalPageText) {
            if (page > 1) {
                lastPage.setTextColor(ContextCompat.getColor(context, R.color.blue));
            }
            nextPage.setTextColor(ContextCompat.getColor(context, R.color.blue));

        } else if (page == totalPageText) {
            nextPage.setTextColor(ContextCompat.getColor(context, R.color.gray_light_deep));
        }
    }

    private void changeIfOverLimit() {
        Map<String, Object> params = RequestParamsContants.getInstance().getTradeRecordsParams();
        if (page.getText().toString().isEmpty()) {
            page.setText("1");
            loadPageDatas();
        } else if (Integer.parseInt(page.getText().toString()) > totalPageText) {
            page.setText(String.valueOf(totalPageText));
            loadPageDatas();
        }
    }
}
