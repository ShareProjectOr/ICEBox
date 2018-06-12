package fragment;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ViewUtils.DiaLogUtil;
import contentprovider.UserMessage;
import httputil.Constance;
import httputil.HttpRequest;
import otherutils.Json2Map;


public class Fragment_DetailsDivider extends Fragment {
    private View view;
    private TextView mDivideMoney;
    private TextView mType;
    private TextView mTradeTotal;
    private TextView mOperatorName;
    private TextView mOperatorId;
    private LinearLayout mCreatTime_layout;
    private TextView mCreatTime;
    private LinearLayout mReviewTime_layout;
    private TextView mReviewTime;
    private LinearLayout mStartTime_layout;
    private TextView mStartTime;
    private LinearLayout mEndTime_layout;
    private TextView mEndTime;
    private LinearLayout mCompleteTine_layout;
    private TextView mCompleteTine;
    private Map<String, String> map = new HashMap<>();
    private TextView ReviewTimeLine, CompleteTineLine;
    private TextView mDividerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_details_divider, null, false);
            bindViews();
            getdata();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void bindViews() {
        mDivideMoney = (TextView) view.findViewById(R.id.divideMoney);
        mType = (TextView) view.findViewById(R.id.type);
        mTradeTotal = (TextView) view.findViewById(R.id.tradeTotal);
        mDividerId = (TextView) view.findViewById(R.id.dividerId);
        ReviewTimeLine = (TextView) view.findViewById(R.id.reviewTime_line);
        CompleteTineLine = (TextView) view.findViewById(R.id.completeTine_line);
        mOperatorName = (TextView) view.findViewById(R.id.operatorName);
        mOperatorId = (TextView) view.findViewById(R.id.operatorId);
        mCreatTime_layout = (LinearLayout) view.findViewById(R.id.creatTime_layout);
        mCreatTime = (TextView) view.findViewById(R.id.creatTime);
        mReviewTime_layout = (LinearLayout) view.findViewById(R.id.reviewTime_layout);
        mReviewTime = (TextView) view.findViewById(R.id.reviewTime);
        mStartTime_layout = (LinearLayout) view.findViewById(R.id.startTime_layout);
        mStartTime = (TextView) view.findViewById(R.id.startTime);
        mEndTime_layout = (LinearLayout) view.findViewById(R.id.endTime_layout);
        mEndTime = (TextView) view.findViewById(R.id.endTime);
        mCompleteTine_layout = (LinearLayout) view.findViewById(R.id.completeTine_layout);
        mCompleteTine = (TextView) view.findViewById(R.id.completeTine);
    }

    private void getdata() {
        final Map<String, String> requeastparams = new HashMap<>();
        requeastparams.put("userID", UserMessage.getManagerId());
        requeastparams.put("divideID", getActivity().getIntent().getStringExtra("divideID"));
        final Dialog dialog = DiaLogUtil.createLoadingDialog(getActivity(),getString(R.string.shujujiazaizhong));
        dialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            String error;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    JSONObject object = new JSONObject(HttpRequest.postString(
                            Constance.GET_DIVIDER_URL, requeastparams));

                    JSONObject d = object.getJSONObject("d");
                    Log.i("d", d.toString());
                    error = object.getString("err");
                    if (!error.equals("")) {
                        return false;
                    }

                    map = Json2Map.toHashMap(d);

                } catch (IOException e1) {
                    error = getString(R.string.wangluocuowu);
                    return false;

                } catch (JSONException e1) {
                    error = getString(R.string.json_jiexicuowu);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dialog.dismiss();
                if (!aBoolean) {
                    new AlertView(getString(R.string.tishi), error, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                } else {
                    BigDecimal bd = new BigDecimal(map.get("divideMoney"));
                    mDivideMoney.setText(String.valueOf(bd));
                    switch (map.get("type")) {
                        case "0":
                            mType.setText(getString(R.string.weishenhe));
                            mType.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                            break;
                        case "1":
                            mType.setText(getString(R.string.yishenhe));
                            mType.setTextColor(ContextCompat.getColor(getActivity(), R.color.orange));
                            break;
                        case "2":
                            mType.setText(R.string.yidaozhang);
                            mType.setTextColor(ContextCompat.getColor(getActivity(), R.color.sucessgreen));
                            break;
                    }
                    mTradeTotal.setText(map.get("tradeTotal"));
                    //   name.setText(map.get("agentName") + "的工单");
                    mOperatorName.setText(map.get("operatorName"));
                    mOperatorId.setText(getString(R.string.bianhao) + map.get("operatorID"));
                    if (map.get("creatTime").isEmpty()) {
                        mCreatTime_layout.setVisibility(View.GONE);
                    } else {
                        mCreatTime.setText(map.get("creatTime"));
                    }

                    if (map.get("reviewTime").isEmpty()) {
                        mReviewTime_layout.setVisibility(View.GONE);
                        ReviewTimeLine.setVisibility(View.GONE);
                    } else {
                        mReviewTime.setText(map.get("reviewTime"));
                    }

                    if (map.get("startTime").isEmpty()) {
                        mStartTime_layout.setVisibility(View.GONE);
                    } else {
                        mStartTime.setText(map.get("startTime"));
                    }

                    if (map.get("endTime").isEmpty()) {
                        mEndTime_layout.setVisibility(View.GONE);
                    } else {
                        mEndTime.setText(map.get("endTime"));
                    }

                    if (map.get("completeTime").isEmpty()) {
                        CompleteTineLine.setVisibility(View.GONE);
                        mCompleteTine_layout.setVisibility(View.GONE);
                    } else {
                        mCompleteTine.setText(map.get("completeTime"));
                    }
                    mDividerId.setText(map.get("divideID"));

                }

            }
        }.execute();
    }



}
