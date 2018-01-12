package com.example.shareiceboxms.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemCloseDoorGoods;
import com.example.shareiceboxms.models.adapters.UpdateGoodsCompleteAdapter;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.widget.ListViewForScrollView;
import com.example.shareiceboxms.views.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseDoorFragment extends BaseFragment {
    private View containerView;
    private RelativeLayout closeDoorLayout;
    private ListView productUpdateList;
    private Button addMore, sure;
    private UpdateGoodsCompleteAdapter adapter;
    private List<ItemCloseDoorGoods> datas;
    private HomeActivity homeActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = super.onCreateView(inflater, container, FragmentFactory.getInstance().putLayoutId(R.layout.fragment_close_door));
            bindViews();
            init();
            bindListenner();
        }
        return containerView;
    }

    private void bindViews() {
        closeDoorLayout = (RelativeLayout) containerView.findViewById(R.id.closeDoorLayout);
        productUpdateList = (ListView) containerView.findViewById(R.id.productUpdateList);
        addMore = (Button) containerView.findViewById(R.id.addMore);
        sure = (Button) containerView.findViewById(R.id.sure);
    }

    private void init() {
        homeActivity = (HomeActivity) getActivity();
        datas = new ArrayList<>();
        String value = FragmentFactory.getInstance().getSavedBundle().getString("callbackMsg");
        Log.i("读取的value", "value=" + value);
        try {
            JSONObject object = new JSONObject(value);
            if (object.has("goodsList")) {
                JSONArray array = object.getJSONArray("goodsList");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = (JSONObject) array.opt(i);
                    ItemCloseDoorGoods item = new ItemCloseDoorGoods();
                    item.bindData(jsonObject);
                    datas.add(item);

                }
            } else {
                Toast.makeText(getActivity(), "未检测到任何标签变化,请确认您是否上下货", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new UpdateGoodsCompleteAdapter(getContext(), datas);
        productUpdateList.setAdapter(adapter);
        ListViewForScrollView.setListViewHeightBasedOnChildren(productUpdateList, closeDoorLayout);
    }

    private void bindListenner() {
        addMore.setOnClickListener(this);
        sure.setOnClickListener(this);
        homeActivity.setOnBackPressListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addMore:
                homeActivity.openSaoma();
                break;
            case R.id.sure:
                homeActivity.onBackPressed();
                break;
        }
    }
}
