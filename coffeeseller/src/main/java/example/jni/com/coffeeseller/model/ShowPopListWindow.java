package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import java.util.List;

import example.jni.com.coffeeseller.model.listeners.IShowPopListWindow;
import example.jni.com.coffeeseller.model.listeners.OnPopListItemClickListener;

/**
 * Created by Administrator on 2018/4/16.
 */

public class ShowPopListWindow implements IShowPopListWindow, AdapterView.OnItemClickListener {
    private OnPopListItemClickListener mOnPopListItemClickListener;
    private View anchor;

    @Override
    public void ShowPopListWindow(List<String> list, Context mContext, View anchor, OnPopListItemClickListener mOnPopListItemClickListener) {
        this.mOnPopListItemClickListener = mOnPopListItemClickListener;
        this.anchor = anchor;
        ListPopupWindow window = new ListPopupWindow(mContext);
        window.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_expandable_list_item_1, list));
        window.setAnchorView(anchor);
        window.setModal(true);
        if (!window.isShowing()) {
            window.show();
        }
        window.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnPopListItemClickListener != null) {
            mOnPopListItemClickListener.ItemClickListener(position, anchor);
        }
    }
}
