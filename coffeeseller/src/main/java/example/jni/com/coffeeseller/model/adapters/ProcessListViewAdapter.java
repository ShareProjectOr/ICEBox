package example.jni.com.coffeeseller.model.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.views.fragments.DebugFragment;

/**
 * Created by Administrator on 2018/4/16.
 */

public class ProcessListViewAdapter extends BaseAdapter {
    private Context mContext;
    private DebugFragment fragment;

    public ProcessListViewAdapter(Context mContext, DebugFragment fragment) {
        this.fragment = fragment;
        this.mContext = mContext;
    }

    @Override

    public int getCount() {
        return fragment.processList.size();
    }

    @Override
    public Object getItem(int position) {
        return fragment.processList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.process_list_item_layout, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();

        }
        Log.d("processList", fragment.processList.get(position) + "");
        switch (fragment.processList.get(position).getContainer()) {
            case BEAN_CONTAINER:
                mHolder.bunkersName.setText("咖啡豆仓");
                break;
            case HOTWATER_CONTAINER:
                mHolder.bunkersName.setText("热水仓");
                break;
            case NO_ONE:
                mHolder.bunkersName.setText("1号仓");
                break;
            case NO_TOW:
                mHolder.bunkersName.setText("2号仓");
                break;
            case NO_THREE:
                mHolder.bunkersName.setText("3号仓");
                break;
            case NO_FOUR:
                mHolder.bunkersName.setText("4号仓");
                break;
            case NO_FIVE:
                mHolder.bunkersName.setText("5号仓");
                break;
            case NO_SIX:
                mHolder.bunkersName.setText("6号仓");
                break;
        }
        // mHolder.bunkersName.setText();
        mHolder.waterInterval.setText(fragment.processList.get(position).getWater_interval() + "");
        mHolder.materialTime.setText(fragment.processList.get(position).getMaterial_time() + "");
        mHolder.waterCapacity.setText(fragment.processList.get(position).getWater_capacity() + "");
        mHolder.rotateSpeed.setText(fragment.processList.get(position).getRotate_speed() + "");
        mHolder.stirSpeed.setText(fragment.processList.get(position).getStir_speed() + "");
        switch (fragment.processList.get(position).getWater_type()) {
            case HOT_WATER:
                mHolder.waterType.setText("热水");
                break;
            case COLD_WATER:
                mHolder.waterType.setText("冷水");
                break;
        }
        return convertView;
    }

    class ViewHolder {
        private TextView bunkersName, waterInterval, waterCapacity, materialTime, rotateSpeed, stirSpeed, waterType;

        public ViewHolder(View view) {
            bunkersName = (TextView) view.findViewById(R.id.bunkersName);
            waterInterval = (TextView) view.findViewById(R.id.waterInterval);
            waterCapacity = (TextView) view.findViewById(R.id.waterCapacity);
            materialTime = (TextView) view.findViewById(R.id.materialTime);
            rotateSpeed = (TextView) view.findViewById(R.id.rotateSpeed);
            stirSpeed = (TextView) view.findViewById(R.id.stirSpeed);
            waterType = (TextView) view.findViewById(R.id.waterType);
        }
    }
}
