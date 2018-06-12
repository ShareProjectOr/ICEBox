package fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zhazhijiguanlixitong.Activity.LoginActivity;
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
import ViewUtils.ObjectAnimatorUtil;
import ViewUtils.Rotate3dAnimation;
import contentprovider.UserMessage;
import httputil.Constance;
import httputil.ConstanceMethod;
import httputil.Sql;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_RepairPassword extends Fragment implements View.OnClickListener {
    private EditText mLastpassword;
    private EditText mNewpassword;
    private EditText mComfirepassword;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment__repair_password, null, false);
            bindViews();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void bindViews() {
        mLastpassword = (EditText) view.findViewById(R.id.lastpassword);
        mNewpassword = (EditText) view.findViewById(R.id.newpassword);
        mComfirepassword = (EditText) view.findViewById(R.id.comfirepassword);
        Button mCommit = (Button) view.findViewById(R.id.commit);
        mCommit.setOnClickListener(this);
    }

 /*   private void ShowViewAnim(float star, float to, float end) {
        ObjectAnimator button2fade = ObjectAnimatorUtil.FadeInOutAnimator(view, star, to, end);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(button2fade);
        animSet.setDuration(500);
        animSet.start();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            ShowViewAnim(0f, 0.5f, 1f);
            return new Rotate3dAnimation(-180f, 0f, 500);
        } else {
            ShowViewAnim(1f, 0.5f, 0f);
            return new Rotate3dAnimation(-180f, 0f, 500);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                if (TextUtils.isEmpty(mLastpassword.getText().toString()) || TextUtils.isEmpty(mNewpassword.getText().toString())
                        || TextUtils.isEmpty(mComfirepassword.getText().toString())) {
                    new AlertView(getString(R.string.tishi), getString(R.string.qingjiangneirongtianxiewanzheng), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (!TextUtils.equals(mLastpassword.getText().toString(), UserMessage.getManagerPass())) {
                    new AlertView(getString(R.string.tishi), getString(R.string.shuruyuanmimayouwu), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (!TextUtils.equals(mNewpassword.getText().toString(), mComfirepassword.getText().toString())) {
                    new AlertView(getString(R.string.tishi), getString(R.string.mimabuyizhi), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                docommit();
                break;
        }
    }

    //
//    public void DoSql() {
//        Sql sql = new Sql(getActivity());
//        boolean isupdated = false;
//        Log.e("获取的managerid", UserMessage.getManagerId());
//        Log.e("保存的list", sql.getAllManagerId().toString());
//        for (String saves : sql.getAllManagerId()) {
//            if (UserMessage.getManagerId().equals(saves)) {
//                Log.d("debug", "managerIdsdfds=" + UserMessage.getManagerId());
//                sql.updateContact(UserMessage.getManagerId(), UserMessage.getManagerNum(), UserMessage.getManagerPass());
//                isupdated = true;
//                break;
//            }
//        }
//        if (!isupdated) {
//            sql.insertContact(UserMessage.getManagerId(), UserMessage.getManagerNum(), UserMessage.getManagerPass());
//        }
//
//    }
    public void DoSql() {
        Sql sql = new Sql(getActivity());
        sql.deleteAllContact(sql.getAllCotacts().size());
    }

    private void docommit() {
        final Dialog dialog = DiaLogUtil.createLoadingDialog(getActivity(), getString(R.string.shujutijiaozhong));
        dialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userID", UserMessage.getManagerId());
        params.addBodyParameter("managerID", UserMessage.getManagerId());
        params.addBodyParameter("logonPassword", mNewpassword.getText().toString());
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(10000);
        http.send(HttpRequest.HttpMethod.POST, Constance.REPAIR_PASSEORD_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                try {
                    JSONObject js = new JSONObject(responseInfo.result);
                    if (js.getString("err").equals("")) {
                        new AlertView(getString(R.string.tishi), getString(R.string.xiugaichenggong_1) + getString(R.string.xinmimawei) + mNewpassword.getText().toString() + getString(R.string.qingninaoji), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                switch (position) {
                                    case 0:
                                        //     getActivity().onBackPressed();
                                        DoSql();
                                        ConstanceMethod.isFirstLogin(getContext(), true);
                                        Log.d("debug", "isFirst=" + ConstanceMethod.getSharedPreferences(getActivity(),"ShowWelcome").getBoolean("isFirst", true));
                                        ConstanceMethod.clearActivityFromStack(getActivity(), LoginActivity.class);
                                        getActivity().finish();
                                        break;
                                }
                            }
                        }).setCancelable(true).show();
                    } else {
                        new AlertView(getString(R.string.tishi), js.getString("err"), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    }
                } catch (JSONException e) {
                    new AlertView(getString(R.string.tishi), getString(R.string.cuowu_1) + e, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                new AlertView(getString(R.string.tishi), getString(R.string.wangluobugeili2) + e + s, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
            }
        });
    }
}
