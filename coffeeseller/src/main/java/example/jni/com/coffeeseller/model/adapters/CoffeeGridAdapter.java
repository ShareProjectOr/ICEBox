package example.jni.com.coffeeseller.model.adapters;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.utils.CornersTransmation;

/**
 * Created by WH on 2018/3/20.
 */

public class CoffeeGridAdapter extends BaseAdapter {
    private Context context;
    private List<Coffee> coffees;


    public CoffeeGridAdapter(Context context, List<Coffee> coffees) {
        this.context = context;
        this.coffees = coffees;
        if (this.coffees == null) {
            this.coffees = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return coffees.size();
    }

    @Override
    public Object getItem(int position) {
        return coffees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_coffee_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Coffee coffee = coffees.get(position);
        if (coffee != null) {
            viewHolder.coffeeName.setText(coffee.name);
            viewHolder.coffeePrice.setText(coffee.price);

            Glide.with(context)
                    .load("http://ww4.sinaimg.cn/large/610dc034gw1f96kp6faayj20u00jywg9.jpg")
                    .transform(new CornersTransmation(context, 10))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(viewHolder.coffeeImage);
            if (coffee.isOver) {
                viewHolder.overLayout.setVisibility(View.VISIBLE);
                viewHolder.sellOver.setVisibility(View.VISIBLE);
                convertView.setEnabled(false);
            } else {
                viewHolder.overLayout.setVisibility(View.GONE);
                viewHolder.sellOver.setVisibility(View.GONE);
                convertView.setEnabled(true);
            }
        }
        return convertView;
    }

    class ViewHolder {
        public LinearLayout overLayout;
        public ImageView coffeeImage;
        public TextView coffeeName;
        public TextView coffeePrice;
        public ImageView sellOver;

        public ViewHolder(View view) {
            overLayout = (LinearLayout) view.findViewById(R.id.overLayout);
            coffeeImage = (ImageView) view.findViewById(R.id.coffeeImage);
            coffeeName = (TextView) view.findViewById(R.id.coffeeName);
            coffeePrice = (TextView) view.findViewById(R.id.coffeePrice);
            sellOver = (ImageView) view.findViewById(R.id.sellOver);
        }
    }
}
