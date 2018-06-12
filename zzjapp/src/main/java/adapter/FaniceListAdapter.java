package adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import contentprovider.FinanceListContentProvider;


public class FaniceListAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater inflater;
    private FinanceListContentProvider mProvider;

    public FaniceListAdapter(Activity activity) {
        mActivity = activity;
        inflater = mActivity.getLayoutInflater();
        mProvider = new FinanceListContentProvider(mActivity, this);
    }

    public FinanceListContentProvider getmProvider() {
        return mProvider;
    }

    @Override
    public int getCount() {
        return mProvider.GetDataSetSize();
    }

    @Override
    public FinanceListContentProvider.Item getItem(int position) {
        return mProvider.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.divide_list_item_layout, null, false);
            mHolder.creatTime = (TextView) convertView.findViewById(R.id.creatTime);
            mHolder.divide_type = (ImageView) convertView.findViewById(R.id.divide_type);
            mHolder.divide_money = (TextView) convertView.findViewById(R.id.divide_money);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.creatTime.setText(getItem(position).getCreatTime());
        switch (getItem(position).getType()) {
            case "0":
                mHolder.divide_type.setImageResource(R.mipmap.nocheck);
                break;
            case "1":
                mHolder.divide_type.setImageResource(R.mipmap.havechecked);
                break;
            case "2":
                mHolder.divide_type.setImageResource(R.mipmap.haveincome);
                break;
        }

        mHolder.divide_money.setText(getItem(position).getDivideMoney());
        return convertView;
    }

    class ViewHolder {
        TextView creatTime, divide_money;
        ImageView divide_type;
    }
}
