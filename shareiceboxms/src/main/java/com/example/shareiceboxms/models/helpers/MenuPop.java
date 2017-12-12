package com.example.shareiceboxms.models.helpers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListPopupWindow;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.MenuPopAdapter;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MenuPop {

    public static ListPopupWindow CreateMenuPop(Context context, View view, String[] titleArray) {
        ListPopupWindow MenuPop = new ListPopupWindow(context);
        MenuPop.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_menu_pop));
        MenuPop.setModal(true);
        MenuPop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        MenuPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        MenuPop.setAnchorView(view);
        MenuPop.setAdapter(new MenuPopAdapter(context, titleArray));
        return MenuPop;

    }
}
