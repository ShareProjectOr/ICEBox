package fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zhazhijiguanlixitong.Activity.ManagerContentActivity;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import ViewUtils.DiaLogUtil;
import contentprovider.UserMessage;
import customview.ThreeMenuDialog;
import httputil.Constance;
import otherutils.IDCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ManageContent extends Fragment implements View.OnClickListener, OnItemClickListener, TextWatcher {
    private View view;
    private TextView mAgentID;
    private TextView mAgentName;
    private TextView mManagerBankAccount;
    private EditText mManagerName;
    private EditText mManagerTelePhone;
    private EditText mManagerEmail;
    private EditText mManagerAddress;
    private LinearLayout mCompanyAddress_layout;
    private EditText mCompanyAddress;
    private LinearLayout mManagerCompany_layout;
    private EditText mManagerCompany;
    private EditText mManagerCard;
    private LinearLayout mCompanyNum_layout;
    private EditText mCompanyNum;
    private EditText divideProportion;
    private Button mSavecommit, change_divideProportion, commit;
    private Intent mIntent;
    private EditText mCustomAddress;
    private String FinalAdress = "";
    private String CompanyFinalAdress = "";
    private LinearLayout divideProportion_layout;
    private EditText Password;
    private AlertView alertView;
    private boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mIntent = getActivity().getIntent();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_managercontent, container, false);
            initview();
            initdata();
            initvisible();
            initlistener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initlistener() {
        mManagerAddress.setOnClickListener(this);
        mCompanyAddress.setOnClickListener(this);
        mSavecommit.setOnClickListener(this);
        change_divideProportion.setOnClickListener(this);
        commit.setOnClickListener(this);
        divideProportion.addTextChangedListener(this);
    }

    private void initvisible() {

        switch (mIntent.getStringExtra("managerType")) {
            case "0":
                divideProportion_layout.setVisibility(View.GONE);
                break;
            case "1":
                divideProportion_layout.setVisibility(View.GONE);
                break;
            case "2":
                mManagerCompany_layout.setVisibility(View.VISIBLE);
                mCompanyAddress_layout.setVisibility(View.VISIBLE);
                mCompanyNum_layout.setVisibility(View.VISIBLE);
                mManagerBankAccount.setVisibility(View.VISIBLE);
                divideProportion_layout.setVisibility(View.VISIBLE);
                if (UserMessage.getManagerType().equals("1")) {
                    change_divideProportion.setVisibility(View.VISIBLE);
                    commit.setVisibility(View.VISIBLE);
                } else {
                    change_divideProportion.setVisibility(View.GONE);
                    commit.setVisibility(View.GONE);
                }
                break;
            case "3":
                mAgentID.setVisibility(View.VISIBLE);
                mAgentName.setVisibility(View.VISIBLE);
                divideProportion_layout.setVisibility(View.GONE);
                break;
        }
    }

    private void initdata() {
        mAgentID.setText(getString(R.string.dailishangbianhao) + mIntent.getStringExtra("agentId"));
        mAgentName.setText(getString(R.string.dailishangxingming) + mIntent.getStringExtra("agentName"));
        FinalAdress = mIntent.getStringExtra("managerAddress");
        CompanyFinalAdress = mIntent.getStringExtra("companyAddress");
        mManagerName.setText(mIntent.getStringExtra("managerName"));
        mManagerTelePhone.setText(mIntent.getStringExtra("managerTelephone"));
        mManagerEmail.setText(mIntent.getStringExtra("managerEmail"));
        Float percent = Float.parseFloat(mIntent.getStringExtra("divideProportion")) * 100;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);//保留两位小数

        divideProportion.setText(nf.format(percent) + "%");
        mManagerBankAccount.setText(getString(R.string.yinghangkahao) + mIntent.getStringExtra("managerBankAccount"));
        splitaddress();
        mManagerCompany.setText(mIntent.getStringExtra("managerCompany"));
        mManagerCard.setText(mIntent.getStringExtra("managerCard"));
        mCompanyNum.setText(mIntent.getStringExtra("companyNum"));
    }

    private void splitaddress() {
        String managerAddress[];
        String companyAddress[];
        if (mIntent.getStringExtra("managerAddress").contains("|")) {
            managerAddress = mIntent.getStringExtra("managerAddress").split("\\|");
            if (managerAddress.length == 2) {
                mManagerAddress.setText(managerAddress[1]);
            } else if (managerAddress.length == 3) {
                mManagerAddress.setText(managerAddress[1]);
                mCustomAddress.setText(managerAddress[2]);
            }
        } else {
            mManagerAddress.setText(mIntent.getStringExtra("managerAddress"));
        }
        if (mIntent.getStringExtra("companyAddress").contains("|")) {
            companyAddress = mIntent.getStringExtra("companyAddress").split("\\|");
            if (companyAddress.length == 2) {
                mCompanyAddress.setText(companyAddress[1]);
            } else if (companyAddress.length == 3) {
                mCompanyAddress.setText(companyAddress[1]);
                mCompanyAddress.setText(companyAddress[2]);
            }

        } else {
            mCompanyAddress.setText(mIntent.getStringExtra("companyAddress"));
        }


    }

    private void initview() {
        mAgentID = (TextView) view.findViewById(R.id.agentID);
        mAgentName = (TextView) view.findViewById(R.id.agentName);
        commit = (Button) view.findViewById(R.id.change_divideProportion_commit);
        mManagerBankAccount = (TextView) view.findViewById(R.id.managerBankAccount);
        mManagerName = (EditText) view.findViewById(R.id.managerName);
        mManagerTelePhone = (EditText) view.findViewById(R.id.managerTelePhone);
        mManagerEmail = (EditText) view.findViewById(R.id.managerEmail);
        mManagerAddress = (EditText) view.findViewById(R.id.managerAddress);
        divideProportion = (EditText) view.findViewById(R.id.divideProportion);
        change_divideProportion = (Button) view.findViewById(R.id.change_divideProportion);
        divideProportion_layout = (LinearLayout) view.findViewById(R.id.divideProportion_layout);
        mCompanyAddress_layout = (LinearLayout) view.findViewById(R.id.companyAddress_layout);
        mCompanyAddress = (EditText) view.findViewById(R.id.companyAddress);
        mManagerCompany_layout = (LinearLayout) view.findViewById(R.id.managerCompany_layout);
        mManagerCompany = (EditText) view.findViewById(R.id.managerCompany);
        mManagerCard = (EditText) view.findViewById(R.id.managerCard);
        mCompanyNum_layout = (LinearLayout) view.findViewById(R.id.companyNum_layout);
        mCompanyNum = (EditText) view.findViewById(R.id.companyNum);
        mSavecommit = (Button) view.findViewById(R.id.savecommit);
        mCustomAddress = (EditText) view.findViewById(R.id.customAddress);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savecommit:
                if (Integer.parseInt(UserMessage.getManagerType()) >= Integer.parseInt(mIntent.getStringExtra("managerType"))) {
                    new AlertView(getString(R.string.tishi), getString(R.string.meiyouquanxianxiugaixinxi), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (!new IDCard().verify(getActivity(),mManagerCard.getText().toString())) {
                    new AlertView(getString(R.string.tishi), getString(R.string.shengfenzhenggeshibuzhengque), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }

                RequestParams params = new RequestParams();
                params.addBodyParameter("managerID", mIntent.getStringExtra("managerId"));
                params.addBodyParameter("tsy", UserMessage.getTsy());
                params.addBodyParameter("params[managerName]", mManagerName.getText().toString());
                params.addBodyParameter("params[managerTelephone]", mManagerTelePhone.getText().toString());
                params.addBodyParameter("params[managerEmail]", mManagerEmail.getText().toString());
                params.addBodyParameter("params[managerAddress]", FinalAdress + mCustomAddress.getText().toString());
                params.addBodyParameter("params[managerCard]", mManagerCard.getText().toString());
                params.addBodyParameter("params[managerCompany]", mManagerCompany.getText().toString());
                params.addBodyParameter("params[companyAddress]", CompanyFinalAdress);
                params.addBodyParameter("params[companyNum]", mCompanyNum.getText().toString());
                dosavecommit(Constance.EDIT_MANAGER_URL, params);
                break;
            case R.id.managerAddress:

                ThreeMenuDialog dialog = new ThreeMenuDialog(getActivity(), false);
                dialog.setonItemClickListener(new ThreeMenuDialog.MenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(Bundle bundle) {
                        String prince = bundle.getString("prince");
                        String city = bundle.getString("city");
                        String area = bundle.getString("area");
                        String code = bundle.getString("code");
                        mManagerAddress.setText(prince + city + area);
                        FinalAdress = code + "|" + prince + "-" + city + "-" + area + "|";
                    }
                });
                dialog.show();
                break;
            case R.id.companyAddress:
                ThreeMenuDialog dialog2 = new ThreeMenuDialog(getActivity(), false);
                dialog2.setonItemClickListener(new ThreeMenuDialog.MenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(Bundle bundle) {
                        String prince = bundle.getString("prince");
                        String city = bundle.getString("city");
                        String area = bundle.getString("area");
                        String code = bundle.getString("code");
                        mCompanyAddress.setText(prince + city + area);
                        CompanyFinalAdress = code + "|" + prince + "-" + city + "-" + area + "|";
                    }
                });
                dialog2.show();
                break;
            case R.id.change_divideProportion:
                ViewGroup passWord_ViewGroup = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.machine_shutdown_input, null, false);
                alertView = new AlertView(getString(R.string.tishi), getString(R.string.shurumimayanzheng), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true);
                Password = (EditText) passWord_ViewGroup.findViewById(R.id.time);
                Password.setHint(getString(R.string.shurumimayanzheng));
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        boolean isOpen = imm.isActive();
                        alertView.setMarginBottom(isOpen && hasFocus ? 120 : 0);
                    }
                });
                alertView.addExtView(passWord_ViewGroup);
                alertView.show();
                break;
            case R.id.change_divideProportion_commit:
                if (!divideProportion.isFocusable()) {
                    return;
                }

                RequestParams post = new RequestParams();
                String percents = divideProportion.getText().toString().replace("%", "");
                if (Float.parseFloat(percents) > 100f) {
                    Toast.makeText(getActivity(), R.string.fenchengbilitishi, Toast.LENGTH_LONG).show();
                    return;
                }
                Float percent = Float.parseFloat(percents) / 100;
                Log.e("TAG", percents + "##" + percent);
                post.addBodyParameter("divideProportion", String.valueOf(percent));
                post.addBodyParameter("logonPassword", UserMessage.getManagerPass());
                post.addBodyParameter("tsy", UserMessage.getTsy());
                post.addBodyParameter("agentID", mIntent.getStringExtra("managerId"));
                dosavecommit(Constance.EDIT_DIVIDER_URL, post);
                break;
        }
    }

    private void dosavecommit(String url, RequestParams params) {
        final Dialog dialog = DiaLogUtil.createLoadingDialog(getActivity(), getString(R.string.shujutijiaozhong));
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(10000);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    String error = object.getString("err");
                    Log.e("result", responseInfo.result);
                    if (error.equals("")) {
                        Toast.makeText(getActivity(), getString(R.string.xiugaichenggong) + "...", Toast.LENGTH_LONG).show();
                        divideProportion.setFocusable(false);
                        ManagerContentActivity parent = (ManagerContentActivity) getActivity();
                        parent.updata();
                    } else {
                        Toast.makeText(getActivity(), R.string.xiugaishibai, Toast.LENGTH_LONG).show();
                        initdata();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), getString(R.string.canshutijiaocuowu) + e, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(), getString(R.string.wangluobugeili) + s + e, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(Object o, int position) {
        alertView.dismiss();
        if (Password.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), R.string.mimaweitianxi, Toast.LENGTH_LONG).show();
        } else {
            if (Password.getText().toString().equals(UserMessage.getManagerPass())) {
                Toast.makeText(getActivity(), R.string.keyixiugaifenchengbili, Toast.LENGTH_LONG).show();
                divideProportion.setFocusable(true);
                divideProportion.setFocusableInTouchMode(true);
            } else {
                Toast.makeText(getActivity(), R.string.mimabuzhengque, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (flag) {
            return;
        }
        flag = true;
        if (!s.toString().contains("%")) {
            divideProportion.setText(s + "%");
        }

        flag = false;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}