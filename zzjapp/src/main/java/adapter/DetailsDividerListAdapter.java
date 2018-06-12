package adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import java.util.List;

import bean.DetailsDividerItem;


public class DetailsDividerListAdapter extends BaseAdapter {
    private List<DetailsDividerItem> DataSet;
    private Activity mActivity;
    private LayoutInflater inflater;

    public DetailsDividerListAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        inflater = mActivity.getLayoutInflater();
    }

    public void SetList(List<DetailsDividerItem> DataSet) {
        this.DataSet = DataSet;
    }

    @Override
    public int getCount() {
        return DataSet.size();
    }

    @Override
    public DetailsDividerItem getItem(int position) {
        return DataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.details_divide_list_item_layout, parent, false);

        }
        TextView title = ViewHolder.get(convertView, R.id.title);
        TextView content = ViewHolder.get(convertView, R.id.content);
        title.setText(getItem(position).getTitle());
        title.setTextColor(ContextCompat.getColor(mActivity, R.color.black));

        String titleContent = getItem(position).getTitle();
        if (TextUtils.equals(titleContent,mActivity.getString(R.string.shenjiliuchengbiaozhi))){
            switch (getItem(position).getContent()) {
                case "0":
                    content.setText(R.string.weishenhe);
                    content.setTextColor(ContextCompat.getColor(mActivity, R.color.red));
                    break;
                case "1":
                    content.setText(R.string.yishenhe);
                    content.setTextColor(ContextCompat.getColor(mActivity, R.color.orange));
                    break;
                case "2":
                    content.setText(R.string.yidakuan);
                    content.setTextColor(ContextCompat.getColor(mActivity, R.color.deepgreen));
                    break;
            }
        }else{
            content.setText(getItem(position).getContent());
            content.setTextColor(ContextCompat.getColor(mActivity, R.color.black));
        }
        return convertView;
    }


}
