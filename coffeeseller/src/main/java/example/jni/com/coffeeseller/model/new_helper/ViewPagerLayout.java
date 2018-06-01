package example.jni.com.coffeeseller.model.new_helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.ReportBunker;
import example.jni.com.coffeeseller.model.ChooseAndMking;
import example.jni.com.coffeeseller.model.new_listenner.CoffeeItemSelectedListener;
import example.jni.com.coffeeseller.utils.CornersTransmation;
import example.jni.com.coffeeseller.utils.DensityUtil;
import example.jni.com.coffeeseller.utils.ImageUtil;
import example.jni.com.coffeeseller.utils.MyLog;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by WH on 2018/5/10.
 */

public class ViewPagerLayout {
    private String TAG = "ViewPagerLayout";
    private int OnePageCount = 4;
    private Context context;
    private CoffeeItemSelectedListener coffeeItemSelectedListener;
    public List<ViewHolder> viewHolders;
    private int pageCount;


    public ViewPagerLayout(Context context, CoffeeItemSelectedListener coffeeItemSelectedListener) {
        this.context = context;
        this.coffeeItemSelectedListener = coffeeItemSelectedListener;
        viewHolders = new ArrayList<>();
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<LinearLayout> getLinearLayouts(List<Coffee> coffees) {
        List<LinearLayout> layouts = new ArrayList<>();
        if (coffees == null || coffees.size() <= 0) {
            return layouts;
        }

        int countPage = coffees.size() / OnePageCount;
        int count = coffees.size() % OnePageCount;
        int totalPageCount = count == 0 ? countPage : countPage + 1;

        pageCount = totalPageCount;

   /*     if (totalPageCount == 2) {
            for (int i = 0; i < 2; i++) {
                addLayout(coffees, layouts, totalPageCount);
            }
        } else {
            addLayout(coffees, layouts, totalPageCount);
        }*/

        addLayout(coffees, layouts, totalPageCount);
        return layouts;
    }

    private void addLayout(List<Coffee> coffees, List<LinearLayout> layouts, int totalPageCount) {
        for (int i = 0; i < totalPageCount; i++) {
            List<Coffee> layoutCoffees = new ArrayList<>();
            if (i + 1 != totalPageCount) {
                layoutCoffees.addAll(coffees.subList(i * OnePageCount, (i + 1) * OnePageCount));
            } else {
                layoutCoffees.addAll(coffees.subList(i * OnePageCount, coffees.size()));
            }

            layouts.add(getLinearLayout(context, layoutCoffees, i));


        }
    }

    private LinearLayout getLinearLayout(Context context, List<Coffee> coffees, final int page) {
        final LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(linearLayoutParams);


        for (int i = 0; i < coffees.size(); i++) {
            final Coffee coffee = coffees.get(i);

            MyLog.d(TAG, "coffees.size =" + coffees.size());
            final int position = i;

            final View view = LayoutInflater.from(context).inflate(R.layout.new_viewpager_item, linearLayout, false);

            final ViewHolder viewHolder = new ViewHolder(view, context);

            viewHolder.initData(coffee);

            view.setTag(viewHolder);

            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewParams.setMargins(36, 0, 0, 0);
            view.setLayoutParams(textViewParams);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (coffeeItemSelectedListener != null) {

                        coffeeItemSelectedListener.getView(coffee, page, position, viewHolder);
                    }
                }
            });
            viewHolders.add(viewHolder);
            linearLayout.addView(view);
        }
        return linearLayout;
    }

    public void updateSameViewHOlderIF2(boolean isSelected) {
        if (pageCount != 2) {
            return;
        }
        for (int i = 0; i < viewHolders.size() / 2; i++) {
            int count = viewHolders.size() / 2;
            for (int j = viewHolders.size() / 2; j < viewHolders.size(); j++) {
                int curPosition = j % count;
                if (i == curPosition) {

                    if (isSelected){
                        if (viewHolders.get(i).isSelected && !viewHolders.get(j).isSelected) {
                            viewHolders.get(j).isSelected = true;
                            viewHolders.get(j).update(viewHolders.get(j), true);
                        } else if (viewHolders.get(j).isSelected && !viewHolders.get(i).isSelected) {
                            viewHolders.get(i).isSelected = true;
                            viewHolders.get(i).update(viewHolders.get(i), true);
                        }
                    }else{
                        if (viewHolders.get(i).isSelected && !viewHolders.get(j).isSelected) {
                            viewHolders.get(j).isSelected = false;
                            viewHolders.get(j).update(viewHolders.get(i), false);
                        } else if (viewHolders.get(j).isSelected && !viewHolders.get(i).isSelected) {
                            viewHolders.get(i).isSelected = true;
                            viewHolders.get(i).update(viewHolders.get(j), false);
                        }
                    }

                    if (viewHolders.get(i).isSelected && !viewHolders.get(j).isSelected) {
                        viewHolders.get(j).isSelected = true;
                        viewHolders.get(j).update(viewHolders.get(j), true);
                    } else if (viewHolders.get(j).isSelected && !viewHolders.get(i).isSelected) {
                        viewHolders.get(i).isSelected = true;
                        viewHolders.get(i).update(viewHolders.get(i), true);
                    } else {
                        viewHolders.get(i).update(viewHolders.get(i), false);
                        viewHolders.get(j).update(viewHolders.get(j), false);
                    }
                }
            }
        }
    }

    public void updateViewHolder(ViewHolder selectedViewHolder) {


        if (selectedViewHolder.isOver) {
            return;
        }

        if (selectedViewHolder.isSelected) {
            MyLog.d(TAG, "selectedViewHolder.isSelected");

            initViewHolder();
            return;
        }

        for (int i = 0; i < viewHolders.size(); i++) {
            ViewHolder viewHolder = viewHolders.get(i);

            if (selectedViewHolder == viewHolder) {
                selectedViewHolder.update(viewHolder, true);
            } else {
                viewHolder.update(viewHolder, false);
            }
        }
    }

    public void initViewHolder() {
        for (ViewHolder viewHolderEach : viewHolders) {
            viewHolderEach.init();
        }
    }

    public class ViewHolder {
        public View view;
        public ImageView mCoffeeImg;
        public TextView mCoffeeName;
        public TextView mActiveImg;
        public TextView mPrice;
        public TextView mActivePrice;
        public LinearLayout mOverLayout;
        public LinearLayout mOverImgLayout;

        public LinearLayout mViewPagerLayout;

        public LinearLayout mPriceLayout, mActivityPriceLayout;
        public TextView mYuanText, mCupText;


        private Context mContext;
        public boolean isOver;
        public boolean isSelected = false;
        public ChooseAndMking chooseAndMking;


        public ViewHolder(View view, Context context) {
            mContext = context;
            this.view = view;
            bindViews(view);
        }

        private void bindViews(View view) {

            mCoffeeImg = (ImageView) view.findViewById(R.id.coffeeImg);
            mCoffeeName = (TextView) view.findViewById(R.id.coffeeName);
            mActiveImg = (TextView) view.findViewById(R.id.activeImg);
            mPrice = (TextView) view.findViewById(R.id.price);
            mActivePrice = (TextView) view.findViewById(R.id.activePrice);
            mOverLayout = (LinearLayout) view.findViewById(R.id.overLayout);
            mOverImgLayout = (LinearLayout) view.findViewById(R.id.overImgLayout);
            mViewPagerLayout = (LinearLayout) view.findViewById(R.id.viewPagerContainnerLayout);

            mActivityPriceLayout = (LinearLayout) view.findViewById(R.id.activityPriceLayout);
            mPriceLayout = (LinearLayout) view.findViewById(R.id.priceLayout);
            mYuanText = (TextView) view.findViewById(R.id.yuanText);
            mCupText = (TextView) view.findViewById(R.id.cupText);

        }

        public void initData(Coffee coffee) {
            if (coffee != null) {
                mCoffeeName.setText(coffee.name);
             /*   Glide.with(mContext)
                        .load(coffee.cacheUrl)
                        .transform(new CornersTransmation(mContext, 10))
                        .placeholder(R.mipmap.no_coffee)
                        .error(R.mipmap.no_coffee).into(mCoffeeImg);*/

                Bitmap defaultBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.no_coffee);
//                Drawable drawable = new BitmapDrawable(ImageUtil.drawTopRoundRect(defaultBitmap, 10));

                /*RoundedCornersTransformation roundedCornersTransformation = new RoundedCornersTransformation(mContext,
                        DensityUtil.dp2px(context, 10), 0, RoundedCornersTransformation.CornerType.TOP);
                roundedCornersTransformation.*/

                Glide.with(mContext).load(coffee.cacheUrl)
                        .placeholder(R.mipmap.no_coffee)
                        .error(R.mipmap.no_coffee)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, DensityUtil.dp2px(context, 10), 0, RoundedCornersTransformation.CornerType.TOP))
                        .into(mCoffeeImg);


                if (!TextUtils.isEmpty(coffee.activitiesPrice) && !"null".equals(coffee.activitiesPrice)) {
                    float activityPrice = Float.parseFloat(coffee.activitiesPrice);
                    if (activityPrice > 0) {
                        mActiveImg.setVisibility(View.VISIBLE);
                        mActivePrice.setVisibility(View.VISIBLE);
                        mActivePrice.setText(coffee.activitiesPrice);
                        mActivityPriceLayout.setVisibility(View.VISIBLE);

                        mPrice.setText(coffee.price);
                        mPriceLayout.setBackgroundResource(R.drawable.shape_dialog);
                        mPrice.setTextColor(ContextCompat.getColor(context, R.color.border_color));
                        mYuanText.setTextColor(ContextCompat.getColor(context, R.color.border_color));
                        mCupText.setTextColor(ContextCompat.getColor(context, R.color.border_color));
                    } else {
                        mPrice.setText(coffee.price);
                        mPriceLayout.setBackgroundResource(0);
                        mPrice.setTextColor(ContextCompat.getColor(context, R.color.red));
                        mYuanText.setTextColor(ContextCompat.getColor(context, R.color.red));
                        mCupText.setTextColor(ContextCompat.getColor(context, R.color.red));
                    }
                } else {
                    mPrice.setText(coffee.price);
                    mPriceLayout.setBackgroundResource(0);
                    mPrice.setTextColor(ContextCompat.getColor(context, R.color.red));
                    mYuanText.setTextColor(ContextCompat.getColor(context, R.color.red));
                    mCupText.setTextColor(ContextCompat.getColor(context, R.color.red));
                }


                if (coffee.isOver) {
                    mOverLayout.setVisibility(View.VISIBLE);
                    mOverImgLayout.setVisibility(View.VISIBLE);
                    view.setEnabled(false);
                    isOver = true;
                } else {
                    mOverLayout.setVisibility(View.GONE);
                    mOverImgLayout.setVisibility(View.GONE);
                    view.setEnabled(true);
                    isOver = false;
                }
            }
        }

        public void update(ViewHolder viewHolder, boolean isSelected) {


            if (isSelected) {
                mOverLayout.setVisibility(View.GONE);
                viewHolder.isSelected = true;
                viewHolder.mViewPagerLayout.setBackgroundResource(R.drawable.new_layout_shape_selected);//shadow_11137
            } else {
                mOverLayout.setVisibility(View.VISIBLE);
                viewHolder.isSelected = false;
                viewHolder.mViewPagerLayout.setBackgroundResource(R.drawable.new_layout_shape);//shadow_105312
            }
        }

        public void init() {
            isSelected = false;
            mViewPagerLayout.setBackgroundResource(R.drawable.new_layout_shape);
            if (isOver) {
                mOverLayout.setVisibility(View.VISIBLE);
                mOverImgLayout.setVisibility(View.VISIBLE);
                view.setEnabled(false);
                isOver = true;
            } else {
                mOverLayout.setVisibility(View.GONE);
                mOverImgLayout.setVisibility(View.GONE);
                view.setEnabled(true);
                isOver = false;
            }
        }
    }
}
