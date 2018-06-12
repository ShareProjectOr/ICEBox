package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.Activity.PersonalInfoActivity;
import com.example.zhazhijiguanlixitong.R;

import java.util.List;

/**
 * Created by WH on 2017/8/8.
 */

public class GridViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mNames;

    public GridViewAdapter(Context context, List<String> names) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mNames = names;
    }

    @Override
    public int getCount() {
        return mNames.size();
    }

    @Override
    public Object getItem(int position) {
        return mNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHodler = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.popup_list_item, null);
            mViewHodler = new ViewHolder();
            mViewHodler.name = (TextView) convertView.findViewById(R.id.name_text);
            convertView.setTag(mViewHodler);
        } else {
            mViewHodler = (ViewHolder) convertView.getTag();
        }
        mViewHodler.name.setText(mNames.get(position));
        return convertView;
    }

    public static class ViewHolder {
        TextView name;
    }
}
