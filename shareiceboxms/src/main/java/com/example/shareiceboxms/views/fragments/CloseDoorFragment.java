package com.example.shareiceboxms.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.widget.ListViewForScrollView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseDoorFragment extends BaseFragment {
    private View containerView;
    private ListViewForScrollView productUpdateList;
    private Button addMore, sure;
    private String callbackMsg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_close_door, container, false);
        return containerView;
    }

    private void bindViews() {
        productUpdateList = (ListViewForScrollView) containerView.findViewById(R.id.productUpdateList);
        addMore = (Button) containerView.findViewById(R.id.addMore);
        sure = (Button) containerView.findViewById(R.id.sure);
    }

    private void init() {
        //adapter 中listview使用的子布局为：close_door_list_item.xml
    }

    private void bindListenner() {
        addMore.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addMore:
                break;
            case R.id.sure:
                break;
        }
    }
}
