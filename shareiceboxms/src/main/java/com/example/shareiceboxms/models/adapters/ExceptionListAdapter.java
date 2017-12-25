package com.example.shareiceboxms.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.contants.Constants;
import com.example.shareiceboxms.models.contentprovider.ExceptionListData;
import com.example.shareiceboxms.models.contentprovider.ProductListData;
import com.example.shareiceboxms.models.helpers.ExceptionTypeUtils;
import com.example.shareiceboxms.views.fragments.product.ProductTypeListFragment;

/**
 * Created by Administrator on 2017/12/11.
 */

public class ExceptionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private final static int TYPE_LOAD_ITEM = 0;
    private final static int TYPE_NORMAL_ITEM = 1;
    private ExceptionListData contentProvider;
    public int isdetails = 0;

    public ExceptionListAdapter(Activity mContext) {
        this.mContext = mContext;
        contentProvider = new ExceptionListData(this, mContext);
    }

    public ExceptionListData getContentProvider() {
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
                view = LayoutInflater.from(mContext).inflate(R.layout.exception_list_itemlayout, null);
                viewHolder = new MyHolder(view);
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
                MyHolder body = (MyHolder) holder;

                body.excetionType.setText(ExceptionTypeUtils.getExceptionTypeByCode(contentProvider.getItem(position).exceptionCode));
                body.isDetailsTagIcon.setImageResource(Constants.ExceptionIsDetailsICON[isdetails]);
                body.machineName.setText(contentProvider.getItem(position).machineName);
                body.machineAddr.setText(contentProvider.getItem(position).machineAddress);
                body.exceptionLv.setText(Constants.EXCEPTION_LV_TITLE[contentProvider.getItem(position).exceptionLevel]);
                body.exceptionCreateTime.setText(contentProvider.getItem(position).happenTime);
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
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView excetionType, isDetailsTag, machineName, machineAddr, exceptionLv, exceptionCreateTime;
        ImageView isDetailsTagIcon;

        public MyHolder(View itemView) {
            super(itemView);
            excetionType = (TextView) itemView.findViewById(R.id.excetionType);
            isDetailsTag = (TextView) itemView.findViewById(R.id.isDetailsTag);
            machineName = (TextView) itemView.findViewById(R.id.machineName);
            machineAddr = (TextView) itemView.findViewById(R.id.machineAddr);
            exceptionLv = (TextView) itemView.findViewById(R.id.exceptionLv);
            exceptionCreateTime = (TextView) itemView.findViewById(R.id.exceptionCreateTime);
            isDetailsTagIcon = (ImageView) itemView.findViewById(R.id.isDetailsTagIcon);
        }
    }

}
