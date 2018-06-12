package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/8/24.
 */

public class UserListAdapter extends BaseAdapter {
    List<Map<String, Object>> items;
    Activity activity;

    public UserListAdapter(Activity ac, List<Map<String, Object>> itemsVaule) {
        items = itemsVaule;
        activity = ac;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.iconImg = (ImageView) convertView.findViewById(R.id.item_img);
            viewHolder.arrowImg = (ImageView) convertView.findViewById(R.id.item_arrow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iconImg.setImageResource((Integer) items.get(position).get("imgIcon"));
        viewHolder.text.setText((String) items.get(position).get("text"));
        return convertView;
    }

    class ViewHolder {
        ImageView iconImg;
        TextView text;
        ImageView arrowImg;
    }
}
