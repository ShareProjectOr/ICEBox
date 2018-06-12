package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import java.util.List;

import AddressCodeUtil.entity.ItemArea;
import AddressCodeUtil.entity.ItemBase;
import AddressCodeUtil.entity.ItemCity;


/**
 * Created by WH on 2017/7/22.
 */

public class AddressAdapter extends BaseAdapter {
    private List<ItemBase> addresses;
    private List<ItemCity> mCities;
    private List<ItemArea> mAreas;
    private LayoutInflater mInflater;
    private Activity mActivity;

    public AddressAdapter(Activity activity, List<ItemBase> addresses) {
        this.mActivity = activity;
        this.addresses = addresses;
        mInflater = LayoutInflater.from(this.mActivity);
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHodler = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_address, null);
            mViewHodler = new ViewHolder();
            mViewHodler.layout = (RelativeLayout) convertView.findViewById(R.id.item_layout);
            mViewHodler.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(mViewHodler);
        } else {
            mViewHodler = (ViewHolder) convertView.getTag();
        }
        mViewHodler.name.setText((addresses.get(position)).getName());
        return convertView;
    }

    public static class ViewHolder {
        RelativeLayout layout;
        TextView name;
    }
}
