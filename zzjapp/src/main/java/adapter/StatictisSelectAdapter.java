package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/7/22.
 */

public class StatictisSelectAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String, String>> mDatas;

    public StatictisSelectAdapter(Context context, List<Map<String, String>> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
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
            mViewHodler.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(mViewHodler);
        } else {
            mViewHodler = (ViewHolder) convertView.getTag();
        }
        mViewHodler.name.setText((mDatas.get(position).get("managerName")));
        return convertView;
    }

    public static class ViewHolder {
        TextView name;
    }
}
