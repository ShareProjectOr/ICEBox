package example.jni.com.coffeeseller.model.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.utils.ImageUtil;

/**
 * Created by WH on 2018/3/20.
 */

public class TradeListAdapter extends BaseAdapter {
    private Context context;
    private List<DealRecorder> DealRecorders;

    public TradeListAdapter(Context context, List<DealRecorder> coffees) {
        this.context = context;
        this.DealRecorders = coffees;
        if (this.DealRecorders == null) {
            this.DealRecorders = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return DealRecorders.size();
    }

    @Override
    public Object getItem(int position) {
        return DealRecorders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DealRecorder dealRecorder = DealRecorders.get(position);
        if (dealRecorder != null) {
            viewHolder.mRqCup.setText(dealRecorder.getRqcup());
            viewHolder.mRqTempFormat.setText(dealRecorder.getRqTempFormat());
            viewHolder.mOrder.setText(dealRecorder.getOrder());
            viewHolder.mPrice.setText(dealRecorder.getPrice());
            viewHolder.mTaste.setText(dealRecorder.getTasteRadio());
            viewHolder.mPayTime.setText(dealRecorder.getPayTime());
            viewHolder.mFormulaID.setText(dealRecorder.getFormulaID());
            viewHolder.mPayed.setText(dealRecorder.isPayed() + "");
            viewHolder.mMakeSuccess.setText(dealRecorder.isMakeSuccess() + "");
            viewHolder.mIsReportSuccess.setText(dealRecorder.isReportSuccess() + "");
            viewHolder.mReportMsg.setText(dealRecorder.getReportMsg());
        }
        return viewHolder.view;
    }

    class ViewHolder {
        public View view;
        public TextView mRqCup;
        public TextView mRqTempFormat;
        public TextView mOrder;
        public TextView mPrice;
        public TextView mTaste;
        public TextView mPayTime;
        public TextView mFormulaID;
        public TextView mPayed;
        public TextView mMakeSuccess;
        public TextView mIsReportSuccess;
        public TextView mReportMsg;

        public ViewHolder() {
            bindViews();
        }

        private void bindViews() {
            view = LayoutInflater.from(context).inflate(R.layout.trade_list_item, null);
            mRqCup = (TextView) view.findViewById(R.id.rqCup);
            mRqTempFormat = (TextView) view.findViewById(R.id.rqTempFormat);
            mOrder = (TextView) view.findViewById(R.id.order);
            mPrice = (TextView) view.findViewById(R.id.price);
            mTaste = (TextView) view.findViewById(R.id.taste);
            mPayTime = (TextView) view.findViewById(R.id.payTime);
            mFormulaID = (TextView) view.findViewById(R.id.formulaID);
            mPayed = (TextView) view.findViewById(R.id.payed);
            mMakeSuccess = (TextView) view.findViewById(R.id.makeSuccess);
            mIsReportSuccess = (TextView) view.findViewById(R.id.isReportSuccess);
            mReportMsg = (TextView) view.findViewById(R.id.reportMsg);
        }

    }
}
