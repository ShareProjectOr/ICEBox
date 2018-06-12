package fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alex.alexswitch.ISwitch;
import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.Activity.CaptureActivity;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ViewUtils.DiaLogUtil;
import adapter.ChooseMachineManagerAdapter;
import contentprovider.UserListContentProvider;
import contentprovider.UserMessage;
import customview.DateTimeDialog;
import httputil.Constance;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AddMachine extends Fragment implements View.OnClickListener, DateTimeDialog.MyOnDateSetListener, AdapterView.OnItemClickListener {
    private View view;
    private EditText mMachineCode;
    private LinearLayout mScanf;
    private EditText mMachineName;
    private EditText mProductTime;
    private EditText mFactoryTime;
    private EditText mDriver;
    private EditText mSellSoftware;
    private EditText mMediaSortware;
    private EditText mManagerID;
    private EditText mAgentID;
    private com.alex.alexswitch.ISwitch mPaperMachine;
    private com.alex.alexswitch.ISwitch mCoinMachine;
    private com.alex.alexswitch.ISwitch mAdvertisingScreen;
    private com.alex.alexswitch.ISwitch mDocoinMachine;
    private com.alex.alexswitch.ISwitch mMISPos;
    private com.alex.alexswitch.ISwitch mCardReader;
    private Button mCommit;
    private String[] state = {"1", "0"};
    private int paperMachinestate = 0;
    private int coinMachinestate = 0;
    private int advertisingScreenstate = 0;
    private int cardReaderstate = 0;
    private int docoinMachinestate = 0;
    private int MISPosstate = 0;
    private int current = 1;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListPopupWindow agentwindow, managerwindow;
    private ChooseMachineManagerAdapter mAdapter1, mAdapter2;
    private UserListContentProvider mProvider1, mProvider2;
    private DateTimeDialog dateTimeDialog1;
    private String managerId = "";
    private String agentId = "";
    private final int SCANNIN_GREQUEST_CODE = 1;
    private static final int CAMERA_OK = 517;
    private static final int OPEN_CAMERA_RESULT_CODE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment__add_machine, null, false);
            bindViews();
            initdata();
            initlistener();

        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initdata() {
        agentwindow.setAdapter(mAdapter1);
        agentwindow.setAnimationStyle(R.style.popwin_anim_style);
        managerwindow.setAdapter(mAdapter2);
        managerwindow.setAnimationStyle(R.style.popwin_anim_style);
    }

    @Override
    public void onDateSet(Date date) {

        switch (current) {
            case 1:
                mProductTime.setText(mFormatter.format(date) + ":00");
                break;
            case 2:
                mFactoryTime.setText(mFormatter.format(date) + ":00");
                break;

        }
    }

    private void bindViews() {
        mMachineCode = (EditText) view.findViewById(R.id.machineCode);
        mScanf = (LinearLayout) view.findViewById(R.id.scanf);
        mMachineName = (EditText) view.findViewById(R.id.machineName);
        mProductTime = (EditText) view.findViewById(R.id.productTime);
        mFactoryTime = (EditText) view.findViewById(R.id.factoryTime);
        mDriver = (EditText) view.findViewById(R.id.driver);
        mSellSoftware = (EditText) view.findViewById(R.id.sellSoftware);
        mMediaSortware = (EditText) view.findViewById(R.id.mediaSortware);
        mManagerID = (EditText) view.findViewById(R.id.managerID);
        mAgentID = (EditText) view.findViewById(R.id.agentID);
        mPaperMachine = (com.alex.alexswitch.ISwitch) view.findViewById(R.id.paperMachine);
        mCoinMachine = (com.alex.alexswitch.ISwitch) view.findViewById(R.id.coinMachine);
        mAdvertisingScreen = (com.alex.alexswitch.ISwitch) view.findViewById(R.id.advertisingScreen);
        mDocoinMachine = (com.alex.alexswitch.ISwitch) view.findViewById(R.id.docoinMachine);
        mMISPos = (com.alex.alexswitch.ISwitch) view.findViewById(R.id.MISPos);
        mCardReader = (com.alex.alexswitch.ISwitch) view.findViewById(R.id.cardReader);
        mCommit = (Button) view.findViewById(R.id.commit);
        agentwindow = new ListPopupWindow(getActivity());
        managerwindow = new ListPopupWindow(getActivity());
        agentwindow.setAnchorView(mAgentID);
        agentwindow.setModal(true);
        managerwindow.setAnchorView(mManagerID);
        managerwindow.setModal(true);
        mAdapter1 = new ChooseMachineManagerAdapter(getActivity(), mProvider1);
        mProvider1 = mAdapter1.getmProvider();
        mAdapter2 = new ChooseMachineManagerAdapter(getActivity(), mProvider2);
        mProvider2 = mAdapter2.getmProvider();
        dateTimeDialog1 = new DateTimeDialog(getActivity(), null, this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("QR_CODE");
                    // TODO 获取结果，做逻辑操作
                    mMachineCode.setText(result);
                    //    tvResult.setText(result);
                } else {
                    new AlertView(getString(R.string.tishi), getString(R.string.wufahuoqushaomiaojieguo), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).show();
                }
                break;

        }
    }

    private void dosavecommit(String url, RequestParams params) {
        final Dialog dialog = DiaLogUtil.createLoadingDialog(getActivity(), getString(R.string.shujushangchuanzhong));
        dialog.show();
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(10000);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("修改结果", responseInfo.result);
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    String error = object.getString("err");
                    if (error.equals("")) {
                        new AlertView(getString(R.string.tishi), getString(R.string.tianjiachenggong), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    } else {
                        new AlertView(getString(R.string.cuowu), error, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    }
                } catch (JSONException e) {
                    new AlertView(getString(R.string.cuowu), getString(R.string.canshutijiaocuowu) + e, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                new AlertView(getString(R.string.tishi), getString(R.string.wangluobugeili)+ s + e, null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                if (TextUtils.equals("", mMachineCode.getText().toString()) || TextUtils.equals("", mMachineName.getText().toString()) ||
                        TextUtils.equals("", mProductTime.getText().toString()) || TextUtils.equals("", mFactoryTime.getText().toString())
                        || TextUtils.equals("", mDriver.getText().toString()) || TextUtils.equals("", mSellSoftware.getText().toString()) || TextUtils.equals("", mMediaSortware.getText().toString())) {
                    new AlertView(getString(R.string.tishi), getString(R.string.buchongxinxi), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                RequestParams params = new RequestParams();
                params.addBodyParameter("machineCode", mMachineCode.getText().toString());
                params.addBodyParameter("tsy", UserMessage.getTsy());
                Log.e("tsy", UserMessage.getTsy());
                params.addBodyParameter("machineName", mMachineName.getText().toString());
                params.addBodyParameter("productTime", mProductTime.getText().toString());
                params.addBodyParameter("factoryTime", mFactoryTime.getText().toString());
                params.addBodyParameter("managerID", managerId);
                params.addBodyParameter("agentID", agentId);
                params.addBodyParameter("mediaSortware", mMediaSortware.getText().toString());
                params.addBodyParameter("sellSoftware", mSellSoftware.getText().toString());
                params.addBodyParameter("driver", mDriver.getText().toString());
                params.addBodyParameter("MISPos", state[MISPosstate]);
                params.addBodyParameter("docoinMachine", state[docoinMachinestate]);
                params.addBodyParameter("cardReader", state[cardReaderstate]);
                params.addBodyParameter("advertisingScreen", state[advertisingScreenstate]);
                params.addBodyParameter("coinMachine", state[coinMachinestate]);
                params.addBodyParameter("paperMachine", state[paperMachinestate]);
                dosavecommit(Constance.ADD_MACHINE_URL, params);
                break;
            case R.id.productTime:
                dateTimeDialog1.hideOrShow();
                current = 1;
                break;
            case R.id.factoryTime:
                dateTimeDialog1.hideOrShow();
                current = 2;
                break;
            case R.id.agentID:
                if (agentwindow.isShowing()) {
                    agentwindow.dismiss();
                    return;
                }
                if (mProvider1.GetDataSetSize() != 0) {
                    agentwindow.show();
                    return;
                }
                mProvider1.getData(Constance.GET_USER_LIST_URL, getpostmap("2"),false);
                agentwindow.show();
                break;
            case R.id.managerID:
                if (managerwindow.isShowing()) {
                    managerwindow.dismiss();
                    return;
                }
                if (mProvider2.GetDataSetSize() != 0) {
                    managerwindow.show();
                    return;
                }

                mProvider2.getData(Constance.GET_USER_LIST_URL, getpostmap("3"),false);
                managerwindow.show();
                break;
            case R.id.scanf:
                if (Build.VERSION.SDK_INT > 22) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //先判断有没有权限 ，没有就在这里进行权限的申请
                        requestPermissions(
                                new String[]{android.Manifest.permission.CAMERA}, CAMERA_OK);//fragment申请权限是fragment本身申请权限,不需要ActivityCompat.requestPermissions.不然无法执行授权回调

                    } else {
                        //说明已经获取到摄像头权限了 想干嘛干嘛
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), CaptureActivity.class);
                        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                    }
                } else {
//这个说明系统版本在6.0之下，不需要动态获取权限。
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), CaptureActivity.class);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

                }

        }
       /* Intent intent = new Intent();
        intent.setClass(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);*/

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), CaptureActivity.class);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    Toast.makeText(getActivity(), getString(R.string.jujueshoujishiyongquanxian), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    private Map<String, String> getpostmap(String type) {
        Map<String, String> body2 = new HashMap<>();
        body2.put("P", "1");
        body2.put("N", "1000");
        body2.put("W[activatedType]", "1");
        body2.put("W[managerType]", type);
        body2.put("tsy", UserMessage.getTsy());
        return body2;
    }

    private void initlistener() {
        mPaperMachine.setOnISwitchOnClickListener(new MyIsSwitchOnClickListener(mPaperMachine));
        mCoinMachine.setOnISwitchOnClickListener(new MyIsSwitchOnClickListener(mPaperMachine));
        mAdvertisingScreen.setOnISwitchOnClickListener(new MyIsSwitchOnClickListener(mAdvertisingScreen));
        mDocoinMachine.setOnISwitchOnClickListener(new MyIsSwitchOnClickListener(mDocoinMachine));
        mMISPos.setOnISwitchOnClickListener(new MyIsSwitchOnClickListener(mMISPos));
        mCardReader.setOnISwitchOnClickListener(new MyIsSwitchOnClickListener(mCardReader));
        mFactoryTime.setOnClickListener(this);
        mProductTime.setOnClickListener(this);
        mAgentID.setOnClickListener(this);
        mManagerID.setOnClickListener(this);
        agentwindow.setOnItemClickListener(this);
        managerwindow.setOnItemClickListener(this);
        mCommit.setOnClickListener(this);
        mScanf.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter() == mAdapter1) {
            agentId = mProvider1.getItem(position).getManagerId();
            agentwindow.dismiss();
            mAgentID.setText(mProvider1.getItem(position).getManagerName());
        } else {
            managerId = mProvider2.getItem(position).getManagerId();
            mManagerID.setText(mProvider2.getItem(position).getManagerName());
            managerwindow.dismiss();
        }
    }

    private class MyIsSwitchOnClickListener implements ISwitch.ISwitchOnClickListeners {
        private View view;

        private MyIsSwitchOnClickListener(View view) {
            this.view = view;
        }

        @Override
        public void open() {
            switch (view.getId()) {
                case R.id.paperMachine:
                    paperMachinestate = 0;
                    break;
                case R.id.coinMachine:
                    coinMachinestate = 0;
                    break;
                case R.id.advertisingScreen:
                    advertisingScreenstate = 0;
                    break;
                case R.id.docoinMachine:
                    docoinMachinestate = 0;
                    break;
                case R.id.MISPos:
                    MISPosstate = 0;
                    break;
                case R.id.cardReader:
                    cardReaderstate = 0;
                    break;

            }
        }

        @Override
        public void close() {
            switch (view.getId()) {
                case R.id.paperMachine:
                    paperMachinestate = 1;
                    break;
                case R.id.coinMachine:
                    coinMachinestate = 1;
                    break;
                case R.id.advertisingScreen:
                    advertisingScreenstate = 1;
                    break;
                case R.id.docoinMachine:
                    docoinMachinestate = 1;
                    break;
                case R.id.MISPos:
                    MISPosstate = 1;
                    break;
                case R.id.cardReader:
                    cardReaderstate = 1;
                    break;
            }
        }
    }
}
