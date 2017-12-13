package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.product.ProductTypeListFragment;

/**
 * Created by Lyu on 2017/12/12.
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private final static int TYPE_LOAD_ITEM = 0;
    private final static int TYPE_NORMAL_ITEM = 1;
    private int Total = 20;
    private ProductTypeListFragment productTypeListFragment;

    public ProductListAdapter(Context mContext, ProductTypeListFragment productTypeListFragment) {
        this.mContext = mContext;
        this.productTypeListFragment = productTypeListFragment;
    }

    /*   public ProductListAdapter(Context mContext) {
            this.mContext = mContext;
        }
    */
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_LOAD_ITEM:
                HeadViewHolder head = (HeadViewHolder) holder;
                break;
            case TYPE_NORMAL_ITEM:
                BodyViewHolder body = (BodyViewHolder) holder;
                body.mItemLayout.setOnClickListener(this);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {

        return position < 9 ? TYPE_NORMAL_ITEM : TYPE_LOAD_ITEM;
    }

    @Override
    public void onClick(View v) {
        if (productTypeListFragment != null) {
            Log.e("xxx","item点击");
            FragmentFactory.getInstance().getSavedBundle().putString("productId", "1212121");
            productTypeListFragment.addFrameFragment();
        }
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
