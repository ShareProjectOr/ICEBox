package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import contentprovider.BindNameListContentProvider;
import fragment.Fragment_SearchException;

/**
 * Created by Administrator on 2017/8/2.
 */

public class PopWindowAdapter extends BaseAdapter {
    private ViewHolder mHolder;
    private Activity mActivity;
    private BindNameListContentProvider mProvider;

    public PopWindowAdapter(Activity activity, BindNameListContentProvider provider) {
        mActivity = activity;
        provider = new BindNameListContentProvider(activity, this);
        mProvider = provider;
    }

    public BindNameListContentProvider getmProvider() {
        return mProvider;
    }

    @Override
    public int getCount() {
        return mProvider.GetDataSize();
    }

    @Override
    public BindNameListContentProvider.Item getItem(int position) {
        return mProvider.GetItem(position);
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
        if (position == 0) {
            mHolder.name.setText(R.string.suoyou);
        } else {
            mHolder.name.setText(getItem(position - 1).getName());
        }

        return convertView;

    }

    class ViewHolder {
        TextView name;
    }
}
