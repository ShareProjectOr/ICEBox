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

public class ShowPopListWindow implements IShowPopListWindow {
    private OnPopListItemClickListener mOnPopListItemClickListener;
    private View anchor;

    @Override
    public void ShowPopListWindow(List<String> list, Context mContext, final View anchor, final OnPopListItemClickListener mOnPopListItemClickListener) {
        this.mOnPopListItemClickListener = mOnPopListItemClickListener;
        this.anchor = anchor;
        final ListPopupWindow window = new ListPopupWindow(mContext);
        window.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_expandable_list_item_1, list));
        window.setAnchorView(anchor);
        window.setModal(true);

        window.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnPopListItemClickListener != null) {
                    mOnPopListItemClickListener.ItemClickListener(position, anchor);
                    window.dismiss();
                }
            }
        });
        window.show();
    }


}
