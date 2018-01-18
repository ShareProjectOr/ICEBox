package com.example.shareiceboxms.models.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.ChoosePopupWindowAdapter;
import com.example.shareiceboxms.models.beans.ItemPerson;
import com.example.shareiceboxms.views.activities.HomeActivity;

import java.util.List;

/**
 * Created by WH on 2018/1/17.
 */

public class ChoosePopupWindow extends PopupWindow {
    private static ChoosePopupWindowAdapter adapter;
    private Context context;
    private View view;
    private ListView listView;
    private static WindowManager.LayoutParams params;

    public ChoosePopupWindow(Context context) {
        super(context);
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.choose_popup_window, null);
        listView = (ListView) view.findViewById(R.id.choosePersonList);
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.containner).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.choose_popup_anim);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public static void showPopFormBottom(View view, final HomeActivity activity) {
        ChoosePopupWindow choosePopupWindow = new ChoosePopupWindow(activity);
//        设置Popupwindow显示位置（从底部弹出）
        choosePopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = activity.getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        activity.getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        choosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
    }

    public static void setAdapter(List<ItemPerson> childs, Context context) {

        adapter = new ChoosePopupWindowAdapter(childs, context);
    }

    public static ChoosePopupWindowAdapter getAdapter() {
        return adapter;
    }

}
