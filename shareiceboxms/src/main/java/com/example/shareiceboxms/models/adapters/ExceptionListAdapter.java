package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareiceboxms.R;

/**
 * Created by Administrator on 2017/12/11.
 */

public class ExceptionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private final static int TYPE_HEAD_ITEM = 0;//第一项
    private final static int TYPE_NORMAL_ITEM = 1;

    public ExceptionListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.exception_list_itemlayout, null);
        RecyclerView.ViewHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder body = (MyHolder) holder;

    }

    @Override
    public int getItemCount() {
        return 10;
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
