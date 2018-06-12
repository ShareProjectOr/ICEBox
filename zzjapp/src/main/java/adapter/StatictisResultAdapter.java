package adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import contentprovider.StatisticSelectDataContentProvider;
import customview.SlidingMenu;
import httputil.Constance;

/**
 * Created by WH on 2017/7/22.
 */

public class StatictisResultAdapter extends BaseAdapter {
    private StatisticSelectDataContentProvider mProvider;
    private LayoutInflater mInflater;
    private Map<String, String> mDatas;
    private Map<String, String> key2string;
    private SlidingMenu mMenu;
    private boolean isFirstSliding = true;
    private IsLoadingSuccess mIsLoadingSuccess;

    public StatictisResultAdapter(Activity activity) {
        mInflater = LayoutInflater.from(activity);
        mDatas = new HashMap<>();
        key2string = new HashMap<>();
        mProvider = new StatisticSelectDataContentProvider(activity, null);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHodler = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.appoint_list_item, null);
            mViewHodler = new ViewHolder();
            mViewHodler.name = (TextView) convertView.findViewById(R.id.name_text);
            mViewHodler.num = (TextView) convertView.findViewById(R.id.num_text);
            convertView.setTag(mViewHodler);
        } else {
            mViewHodler = (ViewHolder) convertView.getTag();
        }
        String key = mDatas.keySet().toArray()[position].toString();
        mViewHodler.name.setText(key2string.get(key));
        mViewHodler.num.setText(mDatas.get(key));
        return convertView;
    }

    public void setSlidingMenu(SlidingMenu menu) {
        mMenu = menu;
    }

    public void setDatas(Map<String, String> datas, Map<String, String> key2Strs) {
        this.mDatas = datas;
        this.key2string = key2Strs;
        this.notifyDataSetChanged();
        if (datas.size() > 0) {
            mIsLoadingSuccess.isLoadingSuccess(true);
        } else {
            mIsLoadingSuccess.isLoadingSuccess(false);
        }
    }

    public void getResultTotal(Map<String, String> params) {
        mProvider.setResultAdapter(this);
        mProvider.getTotalResult(params, "data");
    }

    //故障结果
    public void getExceptionTotal(Map<String, String> params) {
        mProvider.setResultAdapter(this);
        mProvider.getFaultTotal(params, "data");
    }

    private static class ViewHolder {
        TextView name;
        TextView num;
    }

    public void setLoadingSuccessCallback(IsLoadingSuccess isLoadingSuccess) {
        mIsLoadingSuccess = isLoadingSuccess;
    }

    public interface IsLoadingSuccess {
        void isLoadingSuccess(boolean isSuccess);
    }
}
