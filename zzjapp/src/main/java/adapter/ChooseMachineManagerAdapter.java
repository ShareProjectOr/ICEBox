package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import contentprovider.UserListContentProvider;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ChooseMachineManagerAdapter extends BaseAdapter {
    private ViewHolder mHolder;
    private Activity mActivity;
    private UserListContentProvider mProvider;

    public ChooseMachineManagerAdapter(Activity activity, UserListContentProvider provider) {
        mActivity = activity;
        provider = new UserListContentProvider(activity, this);
        mProvider = provider;
    }

    public UserListContentProvider getmProvider() {
        return mProvider;
    }

    @Override
    public int getCount() {
        return mProvider.GetDataSetSize();
    }

    @Override
    public UserListContentProvider.Item getItem(int position) {
        return mProvider.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(android.R.layout.simple_spinner_item, null, false);
            mHolder.name = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.name.setText(getItem(position).getManagerName());
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}
