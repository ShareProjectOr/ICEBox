package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import contentprovider.StatisticSelectDataContentProvider;
import entity.ItemMachine;
import entity.ItemUser;
import httputil.Constance;
import httputil.HttpRequest;

/**
 * Created by WH on 2017/8/1.
 */

public class MyAdapter extends BaseAdapter {
    public static final String GET_AGENTS="getAgents";
    public static final String GET_MANAGERS="getManagers";
    public static final String GET_MACHINES="getMachines";
    private Activity mActivity;
    private StatisticSelectDataContentProvider mProvider;
    private String mTag;

    public MyAdapter(Activity acitivity) {
        mActivity = acitivity;
        mProvider = new StatisticSelectDataContentProvider(acitivity, this);
    }

    @Override
    public int getCount() {
        return mProvider.getResult().size();
    }

    @Override
    public Object getItem(int position) {
        return mProvider.getResult().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHodler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.popup_list_item, parent, false);
            mViewHodler = new ViewHolder();
            mViewHodler.name = (TextView) convertView.findViewById(R.id.name_text);
            convertView.setTag(mViewHodler);
        } else {
            mViewHodler = (ViewHolder) convertView.getTag();
        }
        if (mTag.equals(GET_AGENTS) || mTag.equals(GET_MANAGERS)) {
            //mProvider.getmap(position)
            mViewHodler.name.setText(((ItemUser) (mProvider.getResult().get(position))).getManagerName());
        }
        if (mTag.equals(GET_MACHINES)) {
            mViewHodler.name.setText(((ItemMachine) (mProvider.getResult().get(position))).getMachineName());
        }
        return convertView;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public void firstLoadData(String agentId, String managerId) {
        if (getCount() == 0 || getCount() == 1) {
        if (mTag.equals(GET_AGENTS)) {
            mProvider.getAgents();
        }
        if (mTag.equals(GET_MANAGERS)) {
            mProvider.getMachineManagers(agentId);
        }
        if (mTag.equals(GET_MACHINES)) {
            mProvider.getMachines(agentId, managerId);
        }
    }
}

    public void initResult() {
        mProvider.getResult().clear();
    }

    public static class ViewHolder {
        TextView name;
    }
}
