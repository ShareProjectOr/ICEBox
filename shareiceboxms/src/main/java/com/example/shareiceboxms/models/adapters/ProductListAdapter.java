package com.example.shareiceboxms.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.contentprovider.ProductListData;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.product.ProductTypeListFragment;

/**
 * Created by Lyu on 2017/12/12.
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private final static int TYPE_LOAD_ITEM = 0;
    private final static int TYPE_NORMAL_ITEM = 1;
    private ProductTypeListFragment productTypeListFragment;
    private ProductListData contentProvider;

    public ProductListAdapter(Activity mContext, ProductTypeListFragment productTypeListFragment) {
        this.mContext = mContext;
        this.productTypeListFragment = productTypeListFragment;
        contentProvider = new ProductListData(this, mContext);
    }

    public ProductListData getContentProvider() {
        return contentProvider;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_LOAD_ITEM:
                view = LayoutInflater.from(mContext).inflate(R.layout.loading_more, null);
                viewHolder = new HeadViewHolder(view);
                break;
            case TYPE_NORMAL_ITEM:
                view = LayoutInflater.from(mContext).inflate(R.layout.product_list_bodylayout, parent, false);
                viewHolder = new BodyViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_LOAD_ITEM:
                HeadViewHolder head = (HeadViewHolder) holder;
                break;
            case TYPE_NORMAL_ITEM:
                BodyViewHolder body = (BodyViewHolder) holder;
                body.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (productTypeListFragment != null) {
                            Log.e("点击位", position + "");
                            FragmentFactory.getInstance().getSavedBundle().putInt("categoryID", contentProvider.getItem(position).categoryID);
                            productTypeListFragment.addFrameFragment();
                        }
                    }
                });
                body.mProductName.setText(contentProvider.getItem(position).categoryName);
                body.mPrice.setText("￥" + contentProvider.getItem(position).categoryPrice);
                if ( contentProvider.getItem(position).activityPrice.equals("null")){
                    body.mSpecialPrice.setText("暂无特价");
                }else {
                    body.mSpecialPrice.setText("￥" + contentProvider.getItem(position).activityPrice);
                }

                body.mSelled.setText("" + contentProvider.getItem(position).soldOutNum);
                body.mSelling.setText("" + contentProvider.getItem(position).salingNum);
                body.mNotLoading.setText("" + contentProvider.getItem(position).noExhibitNum);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return contentProvider.GetDataSetSize();
    }

    @Override
    public int getItemViewType(int position) {
        return contentProvider.getItem(position) == null ? TYPE_LOAD_ITEM : TYPE_NORMAL_ITEM;
        // return position < contentProvider.GetDataSetSize() ? TYPE_NORMAL_ITEM : TYPE_LOAD_ITEM;
    }


    class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    class BodyViewHolder extends RecyclerView.ViewHolder {
        TextView mProductName, mPrice, mSpecialPrice, mSelled, mSelling, mNotLoading;
        LinearLayout mItemLayout;

        public BodyViewHolder(View itemView) {
            super(itemView);
            mProductName = (TextView) itemView.findViewById(R.id.productName);
            mPrice = (TextView) itemView.findViewById(R.id.price);
            mSpecialPrice = (TextView) itemView.findViewById(R.id.specialPrice);
            mSelled = (TextView) itemView.findViewById(R.id.selled);
            mSelling = (TextView) itemView.findViewById(R.id.selling);
            mNotLoading = (TextView) itemView.findViewById(R.id.notLoading);
            mItemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
        }
    }
}
