package example.jni.com.coffeeseller.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.databases.DataBaseHelper;

/**
 * Created by Administrator on 2018/3/20.
 */

public class HomeFragment extends BasicFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DataBaseHelper dd = DataBaseHelper.getInstance(getActivity());

        return inflater.inflate(R.layout.progressbar_layout, null);

    }

}
