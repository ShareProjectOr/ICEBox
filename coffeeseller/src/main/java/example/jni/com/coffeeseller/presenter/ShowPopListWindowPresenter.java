package example.jni.com.coffeeseller.presenter;

import android.content.Context;
import android.view.View;

import java.util.List;

import example.jni.com.coffeeseller.model.ShowPopListWindow;
import example.jni.com.coffeeseller.model.listeners.IShowPopListWindow;
import example.jni.com.coffeeseller.model.listeners.OnPopListItemClickListener;
import example.jni.com.coffeeseller.views.viewinterface.IDebugDropMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.IShowPopListWindowView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class ShowPopListWindowPresenter {
    private IShowPopListWindow mIShowPopListWindow;
    private IShowPopListWindowView mIShowPopListWindowView;


    public ShowPopListWindowPresenter(IShowPopListWindowView mIShowPopListWindowView) {
        mIShowPopListWindow = new ShowPopListWindow();
        this.mIShowPopListWindowView = mIShowPopListWindowView;
    }

    public void ShowWindow() {
        mIShowPopListWindow.ShowPopListWindow(mIShowPopListWindowView.getPopList(), mIShowPopListWindowView.getContext(), mIShowPopListWindowView.getAnchorView(), new OnPopListItemClickListener() {

            @Override
            public void ItemClickListener(int position, View view) {
                mIShowPopListWindowView.setText(position, view);
            }
        });
    }
}
