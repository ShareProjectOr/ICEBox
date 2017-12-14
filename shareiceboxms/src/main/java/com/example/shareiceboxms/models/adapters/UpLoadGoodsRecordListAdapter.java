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
import com.example.shareiceboxms.views.fragments.product.UpLoadGoodsDetailsFragment;
import com.example.shareiceboxms.views.fragments.product.UpLoadGoodsRecordFragment;

/**
 * Created by Administrator on 2017/12/13.
 */

public class UpLoadGoodsRecordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private final static int TYPE_LOAD_ITEM = 0;
    private final static int TYPE_NORMAL_ITEM = 1;
    private int Total = 20;
    private UpLoadGoodsRecordFragment upLoadGoodsRecordFragment;
    private int position;

    public UpLoadGoodsRecordListAdapter(Context mContext, UpLoadGoodsRecordFragment upLoadGoodsRecordFragment) {
        this.mContext = mContext;
        this.upLoadGoodsRecordFragment = upLoadGoodsRecordFragment;
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
                view = LayoutInflater.from(mContext).inflate(R.layout.uploadgoodslist_item_layout, parent, false);
                viewHolder = new BodyViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_LOAD_ITEM:
                HeadViewHolder load = (HeadViewHolder) holder;
                break;
            case TYPE_NORMAL_ITEM:
                BodyViewHolder body = (BodyViewHolder) holder;
                this.position = position;
                body.itemLayout.setOnClickListener(this);
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
        if (upLoadGoodsRecordFragment != null) {
            Log.e("xxx", "item点击");
            FragmentFactory.getInstance().getSavedBundle().putString("upLoadGoodsId", "1212121");
            upLoadGoodsRecordFragment.addFrameFragment();
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    class BodyViewHolder extends RecyclerView.ViewHolder {
        private TextView mOperationTime;
        private TextView mOperationDate;
        private TextView mUpLoadNum;
        private TextView mDownLoadNum;
        private TextView mMachineName;
        private TextView mMachineAddr;
        private TextView mUpLoadCode;
        private LinearLayout itemLayout;

        public BodyViewHolder(View itemView) {
            super(itemView);
            mOperationTime = (TextView) itemView.findViewById(R.id.operationTime);
            mOperationDate = (TextView) itemView.findViewById(R.id.operationDate);
            mUpLoadNum = (TextView) itemView.findViewById(R.id.upLoadNum);
            mDownLoadNum = (TextView) itemView.findViewById(R.id.downLoadNum);
            mMachineName = (TextView) itemView.findViewById(R.id.machineName);
            mMachineAddr = (TextView) itemView.findViewById(R.id.machineAddr);
            mUpLoadCode = (TextView) itemView.findViewById(R.id.upLoadCode);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
        }
    }
}
