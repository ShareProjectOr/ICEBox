package com.example.shareiceboxms.views.fragments.product;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.UpLoadDetailsGoodListAdapter;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.MyDialog;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.models.widget.ListViewForScrollView;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.fragments.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpLoadGoodsDetailsFragment extends BaseFragment {
    private View contentView;
    private String recordID;
    private ImageView mDrawerIcon;
    private ImageView mSaoma;
    private ListView mUpLoadDetailsGoodsList;
    private UpLoadDetailsGoodListAdapter mAdapter;
    private HomeActivity homeActivity;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_up_load_goods));
            initview();
            initdata();
            iniListener();
        }
        return contentView;
    }

    private void iniListener() {
        mDrawerIcon.setOnClickListener(this);
        mSaoma.setOnClickListener(this);
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setOnRefreshListener(this);
        }
    }

    private void initdata() {
        final Dialog load = MyDialog.loadDialog(getActivity());
        load.show();
        new AsyncTask<Void, Void, Boolean>() {
            String respone;
            String err;
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    respone = OkHttpUtil.post(HttpRequstUrl.UPLOAD_RECORD_DETAILS_URL, JsonUtil.mapToJson(RequestParamsContants.getInstance().getUploadDetailsParams()));
                    JSONObject jsonObject = new JSONObject(respone);
                    err = jsonObject.getString("err");
                    if (err.equals("") || err.equals("null")) {
                        Log.e("uploadDetails",respone.toString());
                        mAdapter.setJsonData(jsonObject.getJSONObject("d"));
                        return true;
                    }
                } catch (IOException e) {
                    err = RequstTips.getErrorMsg(e.getMessage());
                } catch (JSONException e) {
                    err = RequstTips.JSONException_Tip;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                load.dismiss();
                if (aBoolean) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void initview() {
        mDrawerIcon = (ImageView) contentView.findViewById(R.id.drawerIcon);
        mSaoma = (ImageView) contentView.findViewById(R.id.saoma);
        mAdapter = new UpLoadDetailsGoodListAdapter(getActivity());
        homeActivity = (HomeActivity) getActivity();
        refreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.refreshLayout);
        mUpLoadDetailsGoodsList = (ListView) contentView.findViewById(R.id.upLoadDetailsGoodsList);
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
    public void onRefresh() {
        if (refreshLayout.isRefreshing()) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.isHeadDataUpdate = false;

                initdata();

                refreshLayout.setRefreshing(false);


            }
        }, Constants.REFREASH_DELAYED_TIME);
    }
}
