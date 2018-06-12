package fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ViewUtils.DiaLogUtil;
import contentprovider.UserMessage;
import httputil.Constance;
import otherutis.Tip;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TradeItem extends Fragment implements View.OnClickListener {
    private View view;
    private Button mRefund;
    private TextView mRemark;
    private TextView mCupNum;
    private TextView mOrangeNum;
    private TextView mTradeCode;
    private TextView mMachineCode;
    private TextView mTradeMoney;
    private TextView mOrderStatus;
    private ImageView mPaymentMode;
    private TextView mIsRefund;
    private TextView mRefundMoney;
    private TextView mStartTime;
    private TextView mEndTime;
    private TextView yuan;
    private LinearLayout refundNumlayout;
    private TextView refundNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment__trade_item, container, false);
            bindViews();
            initvisible();
            initdata();
            initListener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initListener() {
        mRefund.setOnClickListener(this);
    }

    private void initvisible() {
       /* switch (UserMessage.getManagerType()) {
            case "3":
                mRefund.setVisibility(View.GONE);
                break;
            default:
                mRefund.setVisibility(View.VISIBLE);
                break;
        }
*/
        if (!mIsRefund.getText().toString().equals(getString(R.string.weituikuan))) {
            mRefund.setVisibility(View.GONE);
        } else {
            mRefund.setVisibility(View.VISIBLE);
        }

    }


    private void initdata() {
        final Dialog dialog = DiaLogUtil.createLoadingDialog(getActivity(), null);
        dialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userID", UserMessage.getManagerId());
        params.addBodyParameter("tradeCode", getActivity().getIntent().getStringExtra("tradeCode"));
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(10000);
        http.send(HttpRequest.HttpMethod.POST, Constance.GET_TRADE_ITEM_CONTENT_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    if (     !object.getString("err").equals("")) {
                        new AlertView(getString(R.string.tishi), object.getString("err"), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    } else {
                        JSONObject d = object.getJSONObject("d");
                        mTradeCode.setText(d.getString("tradeCode"));
                        mMachineCode.setText(d.getString("machineCode"));
                        mCupNum.setText(d.getString("cupNum"));
                        mOrangeNum.setText(d.getString("orangeNum"));
                        mStartTime.setText(d.getString("payTime"));
                        mEndTime.setText(d.getString("endTime"));
                        mTradeMoney.setText(d.getString("tradeMoney"));
                        switch (d.getString("endTime")) {
                            case "":
                                mOrderStatus.setText(R.string.jiaoyijinxingzhong);
                                mOrderStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.orange));
                                break;
                           default:
                                mOrderStatus.setText(R.string.jiaoyiyiwancheng);
                                mOrderStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.sucessgreen));
                                break;
                         /*   case "2":
                                mOrderStatus.setText("交易失败");
                                mOrderStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                                break;*/
                        }
                        switch (d.getString("paymentMode")) {
                            case "0":
                                mPaymentMode.setImageResource(R.mipmap.cash);
                                break;
                            case "1":
                                mPaymentMode.setImageResource(R.mipmap.wechat);
                                break;
                            case "2":
                                mPaymentMode.setImageResource(R.mipmap.alipay);
                                break;
                        }
                    /*    switch (d.getString("isRefund")) {
                            case "0":
                                mIsRefund.setText("未退款");
                                break;
                            case "1":
                                mIsRefund.setText("已退款");
                                break;
                            case "2":
                                mIsRefund.setText("交易成功,无退款");
                                break;
                        }*/

                        switch (d.getString("refundMoney")) {
                            case "0":
                                mRefundMoney.setText(R.string.wutuikuan);
                                yuan.setVisibility(View.GONE);
                                refundNumlayout.setVisibility(View.GONE);
                                break;
                            case "":
                                mRefundMoney.setText(R.string.wushuju);
                                yuan.setVisibility(View.GONE);
                                refundNumlayout.setVisibility(View.GONE);
                                break;
                            default:
                                mRefundMoney.setText(d.getString("refundMoney").replace("-", ""));
                                refundNumlayout.setVisibility(View.VISIBLE);
                                refundNum.setText(d.getString("refundNum"));
                                break;
                        }
                       /* if (d.getString("remark").isEmpty()) {
                            mRemark.setVisibility(View.GONE);
                        } else {
                            mRemark.setVisibility(View.VISIBLE);
                            mRemark.setText(d.getString("remark"));
                        }*/

                    }

                } catch (JSONException e) {
                    Log.e("错误",""+e);
                    new AlertView(getString(R.string.tishi), getString(R.string.fuwuqikaixiaocai_chongshi), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                new AlertView(getString(R.string.tishi), getString(R.string.wangluobugeili2), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
            }
        });
    }

    private void bindViews() {
        yuan = (TextView) view.findViewById(R.id.yuan);
        mRefund = (Button) view.findViewById(R.id.Refund);
        mRemark = (TextView) view.findViewById(R.id.remark);
        refundNum = (TextView) view.findViewById(R.id.refundNum);
        refundNumlayout = (LinearLayout) view.findViewById(R.id.RefundCount);
        mCupNum = (TextView) view.findViewById(R.id.cupNum);
        mOrangeNum = (TextView) view.findViewById(R.id.orangeNum);
        mTradeCode = (TextView) view.findViewById(R.id.tradeCode);
        mMachineCode = (TextView) view.findViewById(R.id.machineCode);
        mTradeMoney = (TextView) view.findViewById(R.id.tradeMoney);
        mOrderStatus = (TextView) view.findViewById(R.id.orderStatus);
        mPaymentMode = (ImageView) view.findViewById(R.id.paymentMode);
        mIsRefund = (TextView) view.findViewById(R.id.isRefund);
        mRefundMoney = (TextView) view.findViewById(R.id.refundMoney);
        mStartTime = (TextView) view.findViewById(R.id.startTime);
        mEndTime = (TextView) view.findViewById(R.id.endTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Refund:
             /*   if (UserMessage.getManagerType().equals("3")) {
                    mRefund.setVisibility(View.GONE);
                    new AlertView("提示", "您无权执行人工退款", null, new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }*/
                if (!mIsRefund.getText().toString().equals(getString(R.string.weituikuan))) {
                    mRefund.setVisibility(View.GONE);
                    new AlertView(getString(R.string.tishi), getString(R.string.bumanzhutuikuantiaojian), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                mRefund.setVisibility(View.VISIBLE);
                final EditText password = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setView(password).setTitle(getString(R.string.shurumimayanzheng)).setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (password.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), R.string.mimabixuyanzheng, Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!password.getText().toString().equals(UserMessage.getManagerPass())) {
                            Toast.makeText(getActivity(), R.string.mimabixuyanzheng, Toast.LENGTH_LONG).show();
                            return;
                        }
                        Refund();
                    }
                }).setNegativeButton(R.string.quxiao, null).create().show();
                Refund();
                break;
        }
    }


    public void Refund() {

        final Dialog dialog = DiaLogUtil.createLoadingDialog(getActivity(), getString(R.string.wangluoqingqiuzhong));
        RequestParams params = new RequestParams();
        params.addBodyParameter("tsy", UserMessage.getTsy());
        params.addBodyParameter("tradeCode", getActivity().getIntent().getStringExtra("tradeCode"));
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(10000);
        http.send(HttpRequest.HttpMethod.POST, Constance.TRADE_REFUND_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    if (object.getBoolean("result")) {
                        new AlertView(getString(R.string.tishi), getString(R.string.chenggong), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    } else {
                        new AlertView(getString(R.string.tishi), getString(R.string.shibai), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    }
                } catch (JSONException e) {
                    new AlertView(getString(R.string.tishi), getString(R.string.cuowu) + e, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                new AlertView(getString(R.string.tishi), getString(R.string.wangluobugeili) + e + s, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
            }
        });
    }

}
