package example.jni.com.coffeeseller.model.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.utils.CornersTransmation;
import example.jni.com.coffeeseller.utils.ImageUtil;
import example.jni.com.coffeeseller.utils.MyLog;

/**
 * Created by WH on 2018/3/20.
 */

public class CoffeeGridAdapter extends BaseAdapter {
    private Context context;
    private List<Coffee> coffees;

    private int[] images = {
            R.mipmap.cafe_latte, R.mipmap.americano, R.mipmap.cafe_mocha, R.mipmap.cappuccino,
            R.mipmap.fragrant_milk,
            R.mipmap.chocolate_milk, R.mipmap.rich_chocolate,
            R.mipmap.espresso
    };


    public CoffeeGridAdapter(Context context, List<Coffee> coffees) {

        MyLog.d("CoffeeGridAdapter","CoffeeGridAdapter");
        this.context = context;
        this.coffees = coffees;
        if (this.coffees == null) {
            this.coffees = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return coffees.size() > 0 ? coffees.size() : 0;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_coffee_layout, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Coffee coffee = coffees.get(position);
        if (coffee != null) {
            viewHolder.coffeeName.setText(coffee.name);
            if (!TextUtils.isEmpty(coffee.activitiesPrice) && !"null".equals(coffee.activitiesPrice)) {
                float activityPrice = Float.parseFloat(coffee.activitiesPrice);
                if (activityPrice > 0) {
                    viewHolder.activitePriceLayout.setVisibility(View.VISIBLE);
                    viewHolder.coffeePrice.setText(coffee.activitiesPrice);
                    viewHolder.oldPriceLayout.setBackgroundResource(R.drawable.shape_dialog);
                }
            }
            viewHolder.oldCoffeePrice.setText(coffee.price);

            Glide.with(context)
                    .load(coffee.cacheUrl)
                    .transform(new CornersTransmation(context, 10))
                    .placeholder(R.mipmap.no_coffee)
                    .error(R.mipmap.no_coffee).into(viewHolder.coffeeImage);
/*
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), images[position]);
            bitmap = ImageUtil.getCornerBitmap(bitmap, 10);
            if (bitmap == null) {
                viewHolder.coffeeImage.setImageResource(R.mipmap.no_coffee);
            } else {
                viewHolder.coffeeImage.setImageBitmap(bitmap);
            }*/
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
        public LinearLayout coffeeLayout;
        public ImageView coffeeImage;
        public TextView coffeeName;
        public TextView activePriceTxt;
        public TextView coffeePrice;
        public LinearLayout oldPriceLayout;
        public LinearLayout activitePriceLayout;
        public TextView oldPriceTxt;
        public TextView oldCoffeePrice;
        public LinearLayout overLayout;
        public LinearLayout sellOver;

        public ViewHolder(View view) {
            coffeeLayout = (LinearLayout) view.findViewById(R.id.coffeeLayout);
            coffeeImage = (ImageView) view.findViewById(R.id.coffeeImage);
            coffeeName = (TextView) view.findViewById(R.id.coffeeName);
            activePriceTxt = (TextView) view.findViewById(R.id.activePriceTxt);
            coffeePrice = (TextView) view.findViewById(R.id.coffeePrice);
            oldPriceLayout = (LinearLayout) view.findViewById(R.id.oldPriceLayout);
            activitePriceLayout = (LinearLayout) view.findViewById(R.id.activitePriceLayout);
            oldPriceTxt = (TextView) view.findViewById(R.id.oldPriceTxt);
            oldCoffeePrice = (TextView) view.findViewById(R.id.oldCoffeePrice);
            overLayout = (LinearLayout) view.findViewById(R.id.overLayout);
            sellOver = (LinearLayout) view.findViewById(R.id.sellOver);

        }
    }
}
