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
import com.example.shareiceboxms.models.contentprovider.UpLoadRecordListData;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.helpers.SecondToDate;
import com.example.shareiceboxms.views.fragments.product.UpLoadGoodsDetailsFragment;
import com.example.shareiceboxms.views.fragments.product.UpLoadGoodsRecordFragment;

import java.text.ParseException;

/**
 * Created by Administrator on 2017/12/13.
 */

public class UpLoadGoodsRecordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private final static int TYPE_LOAD_ITEM = 0;
    private final static int TYPE_NORMAL_ITEM = 1;
    private UpLoadGoodsRecordFragment upLoadGoodsRecordFragment;
    private UpLoadRecordListData contentProvider;

    public UpLoadGoodsRecordListAdapter(Activity mContext, UpLoadGoodsRecordFragment upLoadGoodsRecordFragment) {
        this.mContext = mContext;
        this.upLoadGoodsRecordFragment = upLoadGoodsRecordFragment;
        contentProvider = new UpLoadRecordListData(this, mContext);
    }

    public UpLoadRecordListData getContentProvider() {
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
                view = LayoutInflater.from(mContext).inflate(R.layout.uploadgoodslist_item_layout, parent, false);
                viewHolder = new BodyViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_LOAD_ITEM:
                HeadViewHolder load = (HeadViewHolder) holder;
                break;
            case TYPE_NORMAL_ITEM:
                BodyViewHolder body = (BodyViewHolder) holder;
                try {
                    long time = SecondToDate.dateToStamp(contentProvider.getItem(position).closingTime) - SecondToDate.dateToStamp(contentProvider.getItem(position).openingTime);
                    String[] mOperationTime = SecondToDate.formatLongToTimeStr(time / 1000);
                    if (mOperationTime[1].equals("0")) {
                        body.mOperationTime.setText("操作耗时:" + mOperationTime[2] + "分" + mOperationTime[3] + "秒");
                    } else {
                        body.mOperationTime.setText("操作耗时:" + mOperationTime[1] + "时" + mOperationTime[2] + "分" + mOperationTime[3] + "秒");
                    }
                } catch (ParseException e) {
                    body.mOperationTime.setText("操作耗时:数据异常,解析失败");
                }

                if (contentProvider.getItem(position).openingTime.equals("null")) {
                    body.mOperationDate.setText("数据异常,解析失败");
                } else {
                    body.mOperationDate.setText(contentProvider.getItem(position).openingTime);
                }

                if (contentProvider.getItem(position).UpLoadNum.equals("null")) {
                    body.mUpLoadNum.setText("0");
                } else {
                    body.mUpLoadNum.setText(contentProvider.getItem(position).UpLoadNum);
                }
                if (contentProvider.getItem(position).DownLoadNum.equals("null")) {
                    body.mDownLoadNum.setText("0");
                } else {
                    body.mDownLoadNum.setText(contentProvider.getItem(position).DownLoadNum);
                }

                body.mMachineName.setText(contentProvider.getItem(position).machineName);
                body.mMachineAddr.setText("(" + contentProvider.getItem(position).machineAddress + ")");
                body.mUpLoadCode.setText("记录编号" + contentProvider.getItem(position).recordID);
                body.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (upLoadGoodsRecordFragment != null) {
                            FragmentFactory.getInstance().getSavedBundle().putInt("recordID", contentProvider.getItem(position).recordID);
                            upLoadGoodsRecordFragment.addFrameFragment();
                        }
                    }
                });
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
