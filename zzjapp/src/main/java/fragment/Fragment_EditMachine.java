package fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.Activity.MachineContentActivity;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import contentprovider.UserMessage;
import customview.DateTimeDialog;
import customview.ThreeMenuDialog;
import handlerUtil.HandlerEvent;
import httputil.Constance;


public class Fragment_EditMachine extends Fragment implements View.OnClickListener, DateTimeDialog.MyOnDateSetListener {
    private View view;
    private EditText machine_producetime, machine_factoryTime, machine_name, machine_address, customaddress, weChatPrice, cashPrice, alipayPrice, showPrice;
    private DateTimeDialog dateTimeDialog1;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private int current = 1;
    private Button commit;
    private String FinalAdress = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment__machine_edit, null, false);
            initview();
            initListener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initListener() {
        machine_factoryTime.setOnClickListener(this);
        machine_producetime.setOnClickListener(this);
        machine_address.setOnClickListener(this);
        commit.setOnClickListener(this);
    }

    private void initview() {
        machine_producetime = (EditText) view.findViewById(R.id.machine_produce_time);
        machine_factoryTime = (EditText) view.findViewById(R.id.machine_factoryTime);
        dateTimeDialog1 = new DateTimeDialog(getActivity(), null, this);
        machine_name = (EditText) view.findViewById(R.id.machine_name);
        machine_address = (EditText) view.findViewById(R.id.machine_address);
        customaddress = (EditText) view.findViewById(R.id.customaddress);
        weChatPrice = (EditText) view.findViewById(R.id.weChatPrice);
        cashPrice = (EditText) view.findViewById(R.id.cashPrice);
        alipayPrice = (EditText) view.findViewById(R.id.alipayPrice);
        showPrice = (EditText) view.findViewById(R.id.showPrice);
        commit = (Button) view.findViewById(R.id.commit);
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.head_in);
        } else {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.head_out);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.machine_produce_time:
                dateTimeDialog1.hideOrShow();
                current = 1;
                break;
            case R.id.machine_factoryTime:
                dateTimeDialog1.hideOrShow();
                current = 2;
                break;
            case R.id.machine_address:
                ThreeMenuDialog dialog = new ThreeMenuDialog(getActivity(), false);
                dialog.setonItemClickListener(new ThreeMenuDialog.MenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(Bundle bundle) {
                        String prince = bundle.getString("prince");
                        String city = bundle.getString("city");
                        String area = bundle.getString("area");
                        String code = bundle.getString("code");
                        machine_address.setText(prince + city + area);
                        FinalAdress = code + "|" + prince + "-" + city + "-" + area + "|";
                    }
                });
                dialog.show();
                break;
            case R.id.commit:
                if (TextUtils.equals("", machine_name.getText().toString()) || TextUtils.equals("", machine_producetime.getText().toString()) ||
                        TextUtils.equals("", machine_factoryTime.getText().toString()) || TextUtils.equals("", machine_address.getText().toString())
                        || TextUtils.isEmpty(weChatPrice.getText().toString()) || TextUtils.isEmpty(cashPrice.getText().toString()) || TextUtils.isEmpty(alipayPrice.getText().toString())
                        || TextUtils.isEmpty(showPrice.getText().toString())) {
                    Toast.makeText(getActivity(), getString(R.string.buchongxinxi), Toast.LENGTH_LONG).show();
                    return;
                }
                RequestParams params = new RequestParams();
                params.addBodyParameter("machineCode", getActivity().getIntent().getBundleExtra("allcontent").getString("machineCode"));
                params.addBodyParameter("params[machineName]", machine_name.getText().toString());
                params.addBodyParameter("params[productTime]", machine_producetime.getText().toString());
                params.addBodyParameter("params[factoryTime]", machine_producetime.getText().toString());
                params.addBodyParameter("params[machineAddress]", FinalAdress + customaddress.getText().toString());
                params.addBodyParameter("tsy", UserMessage.getTsy());
                params.addBodyParameter("params[alipayPrice]", alipayPrice.getText().toString());
                params.addBodyParameter("params[weChatPrice]", weChatPrice.getText().toString());
                params.addBodyParameter("params[cashPrice]", cashPrice.getText().toString());
                params.addBodyParameter("params[showPrice]", showPrice.getText().toString());
                dosavecommit(Constance.EDIT_MACHINE_CONTENT_URL, params);
                break;
        }
    }

    private void dosavecommit(String url, RequestParams params) {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, getString(R.string.shujutijiaozhong), false, false);
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(10000);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Log.e("修改结果", responseInfo.result);
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    String error = object.getString("err");
                    if (error.equals("")) {
                        new AlertView(getString(R.string.tishi), getString(R.string.xiugaichenggong), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).show();
                        MachineContentActivity parent = (MachineContentActivity) getActivity();
                     //   parent.updata();
                    }
                } catch (JSONException e) {
                    new AlertView(getString(R.string.tishi), getString(R.string.canshutijiaocuowu) + e, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                new AlertView(getString(R.string.tishi), getString(R.string.wangluobugeili) + s + e, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).show();
            }
        });
    }

    @Override
    public void onDateSet(Date date) {

        switch (current) {
            case 1:
                machine_producetime.setText(mFormatter.format(date) + ":00");
                break;
            case 2:
                machine_factoryTime.setText(mFormatter.format(date) + ":00");
                break;

        }
    }
}
