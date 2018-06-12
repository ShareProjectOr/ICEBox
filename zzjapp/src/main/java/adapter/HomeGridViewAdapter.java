package adapter;

import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.Activity.HomeActivity;
import com.example.zhazhijiguanlixitong.R;

import contentprovider.UserMessage;


public class HomeGridViewAdapter extends BaseAdapter {
    private HomeActivity mHomeActivity;
    private String[] texts;
    private TypedArray imgs;

    public HomeGridViewAdapter(HomeActivity activity) {
        mHomeActivity = activity;
        texts = mHomeActivity.getResources().getStringArray(R.array.home_texts);

        imgs = mHomeActivity.getResources().obtainTypedArray(R.array.home_imgs);

    }


    @Override
    public int getCount() {
        if (UserMessage.getManagerType().equals("3")) {
            return imgs.length() - 1;
        }
        return imgs.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mHomeActivity.getLayoutInflater().inflate(R.layout.fragment_home_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageDrawable(imgs.getDrawable(position));
        viewHolder.textView.setText(texts[position]);
        return convertView;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
