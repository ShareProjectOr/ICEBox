package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import contentprovider.BindNameListContentProvider;
import contentprovider.MachineListContentProvider;

/**
 * Created by Administrator on 2017/8/3.
 */

public class ChooseMachineAdapter extends BaseAdapter {
    private ViewHolder mHolder;
    private Activity mActivity;
    private MachineListContentProvider mProvider;

    public ChooseMachineAdapter(Activity activity, MachineListContentProvider provider) {
        mActivity = activity;
        provider = new MachineListContentProvider(activity, this);
        mProvider = provider;
    }

    public MachineListContentProvider getmProvider() {
        return mProvider;
    }

    @Override
    public int getCount() {
        return mProvider.GetDataSetSize();
    }

    @Override
    public MachineListContentProvider.Item getItem(int position) {
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
        mHolder.name.setText(getItem(position).getMachineName());
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}
