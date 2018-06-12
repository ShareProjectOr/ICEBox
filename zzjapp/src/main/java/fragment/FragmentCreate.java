package fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.Activity.AddressActivity;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ViewUtils.ObjectAnimatorUtil;
import ViewUtils.Rotate3dAnimation;
import contentprovider.UserMessage;
import httputil.Constance;
import httputil.ConstanceMethod;
import otherutils.CheakBank;
import otherutils.CheakPhone;
import otherutils.IDCard;

/**
 * Created by WH on 2017/7/17.
 */

public class FragmentCreate extends Fragment implements View.OnClickListener {
    private View cacheView;
    private EditText managerNum, managerName, managerPhone, managerPassword, managerPasswordok, managerEmail,
            managerIdcard, managerCompany, conmpanyNum, address_detail, company_address_detail, companyBank, divideProportion, addressDetail, addressCompanyDetail;
    private Button commit;
    private TextView managerAddress, managerCompanyAddress;//com_address_go, address_go;
    private RelativeLayout cAddress_layout;
    private LinearLayout agent_more_info_layout;
    private int flags;
    private String managerNumValue, managerNameValue, managerPhoneValue, managerPasswordValule,
            passwordOkValue, managerEmailValue, managerAddressValue, managerAddressDetail, companyAddressDetail,
            managerIdCardValue, companyAddressValue, companyValue, companyNumValue, companyBankValue, divideProportionValue;
    private Bundle bundle;
    private String mPressState;
    private String managerAddressForHttp, companyAddressForHttp;

    public static FragmentCreate newInstance(String flags) {
        Bundle bundle = new Bundle();
        bundle.putString("flags", flags);
        FragmentCreate contentFragment = new FragmentCreate();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        flags = Integer.parseInt(bundle.getString("flags"));
        super.onCreate(savedInstanceState);
    }

    private void ShowViewAnim(float star, float to, float end) {
        ObjectAnimator button2fade = ObjectAnimatorUtil.FadeInOutAnimator(cacheView, star, to, end);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(button2fade);
        animSet.setDuration(500);
        animSet.start();
    }

 /*   @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            ShowViewAnim(0f, 0.5f, 1f);
            return new Rotate3dAnimation(-180f, 0f, 500);
        } else {
            ShowViewAnim(1f, 0.5f, 0f);
            return new Rotate3dAnimation(-180f, 0f, 500);
        }
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null) {
            cacheView = inflater.inflate(R.layout.fragment_create, null);
            initView();
        }
        ViewGroup parent = (ViewGroup) cacheView.getParent();
        if (parent != null) {
            parent.removeView(cacheView);
        }
        return cacheView;
    }


    public void setAddressValue(String address, String id) {
        if (mPressState.equals("address")) {
            String comAddress = id + "|" + address + "|" + address_detail.getText().toString();
            managerAddressForHttp = id + "|" + address;
            managerAddress.setText(address);
//            managerAddressValue = comAddress;
            Log.d("debug", "--------" + managerAddressValue);
        } else if (mPressState.equals("companyAddress")) {
            String comAddress = id + "|" + address + "|" + company_address_detail.getText().toString();
            companyAddressForHttp = id + "|" + address;
            managerCompanyAddress.setText(address);
//            companyAddressValue = comAddress;
            Log.d("debug", "--------" + companyAddressValue);
        }
    }

    private void initView() {
        managerNum = (EditText) cacheView.findViewById(R.id.usernum);
        managerName = (EditText) cacheView.findViewById(R.id.name);
        managerPhone = (EditText) cacheView.findViewById(R.id.phone);
        managerPassword = (EditText) cacheView.findViewById(R.id.password);
        managerPasswordok = (EditText) cacheView.findViewById(R.id.passwordok);
        managerEmail = (EditText) cacheView.findViewById(R.id.email);
        managerAddress = (TextView) cacheView.findViewById(R.id.address);
        managerIdcard = (EditText) cacheView.findViewById(R.id.idcard);
        managerCompanyAddress = (TextView) cacheView.findViewById(R.id.companyAddress);
        managerCompany = (EditText) cacheView.findViewById(R.id.managerCompany);
        conmpanyNum = (EditText) cacheView.findViewById(R.id.conmpanyNum);
        commit = (Button) cacheView.findViewById(R.id.commit);
        agent_more_info_layout = (LinearLayout) cacheView.findViewById(R.id.agent_more_info_layout);
        company_address_detail = (EditText) cacheView.findViewById(R.id.company_address_detail);
        address_detail = (EditText) cacheView.findViewById(R.id.address_detail);
        managerAddress.setOnClickListener(this);
        cAddress_layout = (RelativeLayout) cacheView.findViewById(R.id.cAddress_layout);
        managerCompanyAddress.setOnClickListener(this);
        companyBank = (EditText) cacheView.findViewById(R.id.companyBank);
        divideProportion = (EditText) cacheView.findViewById(R.id.divideProportion);
        addressDetail = (EditText) cacheView.findViewById(R.id.address_detail);
        addressCompanyDetail = (EditText) cacheView.findViewById(R.id.company_address_detail);
        commit.setOnClickListener(this);
        hideEditText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                getValue();
                if (judgeValueIsVlide()) {
                    switch (flags) {
                        case 1:
                            doPost(Constance.CREATE_SYS_MANAGER_URL, getValuesOFCreateSysManager());
                            break;
                        case 2:
                            doPost(Constance.CREATE_AGENT_URL, getValuesOFCreateAgent());
                            break;
                        case 3:
                            doPost(Constance.CREATE_MACHINE_MANAGER, getValuesOFCreateMachineManager());
                            break;
                        default:
                            break;
                    }
                }
                break;
            case R.id.address:

                mPressState = "address";


                ConstanceMethod.startIntent(getActivity(), AddressActivity.class, null);
                break;
            case R.id.companyAddress:
                mPressState = "companyAddress";

                ConstanceMethod.startIntent(getActivity(), AddressActivity.class, null);
                break;
        }
    }


    private void getValue() {
        managerNumValue = managerNum.getText().toString();
        managerNameValue = managerName.getText().toString();
        managerPhoneValue = managerPhone.getText().toString();
        managerPasswordValule = managerPassword.getText().toString();
        passwordOkValue = managerPasswordok.getText().toString();
        managerEmailValue = managerEmail.getText().toString();
        managerIdCardValue = managerIdcard.getText().toString();
        companyValue = managerCompany.getText().toString();
        companyNumValue = conmpanyNum.getText().toString();
        companyBankValue = companyBank.getText().toString();
        divideProportionValue = divideProportion.getText().toString();
        companyAddressDetail = addressCompanyDetail.getText().toString();
        managerAddressDetail = addressDetail.getText().toString();
        managerAddressValue = managerAddressForHttp + "|" + managerAddressDetail;
        companyAddressValue = companyAddressForHttp + "|" + companyAddressDetail;
        Log.d("debug", managerAddressValue + "====com====" + companyAddressValue);
    }

    private boolean judgeValueIsVlide() {
        if (TextUtils.isEmpty(managerNumValue)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.num_is_null), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(managerNameValue)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.name_is_null), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(managerPhoneValue)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.phone_is_null), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!CheakPhone.isLegalPhone(managerPhoneValue)) {
                Toast.makeText(getActivity(), R.string.phone_not_legal, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (TextUtils.isEmpty(managerPasswordValule)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.pass_is_null), Toast.LENGTH_SHORT).show();
            return false;
        } else if (managerPasswordValule.length() < 6) {
            Toast.makeText(getActivity(), getResources().getString(R.string.pass_is_too_short), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtils.isEmpty(managerEmailValue)) {
            if (!managerEmailValue.contains(getString(R.string.art))) {
                Toast.makeText(getActivity(), R.string.email_not_legal, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (TextUtils.isEmpty(managerAddress.getText().toString())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.manager_address_is_null), Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (!TextUtils.isEmpty(managerAddressDetail)) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.manager_address_is_null), Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (TextUtils.isEmpty(managerIdCardValue)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.idcard_is_null), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            IDCard idCard = new IDCard();
            if (!idCard.verify(getActivity(),managerIdCardValue)) {
                Toast.makeText(getActivity(), R.string.idcard_not_legal, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (flags == 2) {
            if (TextUtils.isEmpty(companyValue)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.company_name_is_null), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(companyAddressValue)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.company_address_is_null), Toast.LENGTH_SHORT).show();
                return false;
            }
            Log.d("debug", "companyNumValue:" + companyNumValue);

            if (TextUtils.isEmpty(companyNumValue)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.company_code_is_null), Toast.LENGTH_SHORT).show();
                return false;
            }

            if (TextUtils.isEmpty(companyBankValue)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.company_bank_is_null), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (!CheakBank.isLegalBank(companyBankValue)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.company_bank_not_legal), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if (TextUtils.isEmpty(divideProportionValue)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.divideProportion_is_null), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                float divideProportionValueInt = Float.parseFloat(divideProportionValue);
                if (divideProportionValueInt > 100 || divideProportionValueInt < 0) {
                    Toast.makeText(getActivity(), R.string.fenchengbilibunengdaoyu100, Toast.LENGTH_SHORT).show();
                    return false;
                }
                divideProportionValue = String.valueOf(divideProportionValueInt / 100);
            }
        }
        if (TextUtils.isEmpty(passwordOkValue) || !TextUtils.equals(managerPasswordValule, passwordOkValue)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.pass_not_matche), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private RequestParams getPublicValues() {
        RequestParams body = new RequestParams();
        body.addBodyParameter("userID", UserMessage.getManagerId());
        body.addBodyParameter("managerNum", managerNumValue);
        body.addBodyParameter("managerName", managerNameValue);
        body.addBodyParameter("logonPassword", managerPasswordValule);
        body.addBodyParameter("managerTelephone", managerPhoneValue);
        body.addBodyParameter("managerEmail", managerEmailValue);
        body.addBodyParameter("managerAddress", managerAddressValue);
        body.addBodyParameter("managerCard", managerIdCardValue);
        return body;
    }

    //创建系统管理员
    private RequestParams getValuesOFCreateSysManager() {
        RequestParams body = getPublicValues();
        body.addBodyParameter("managerType", "1");
        return body;
    }

    //创建代理商
    private RequestParams getValuesOFCreateAgent() {
        RequestParams body = getPublicValues();
        body.addBodyParameter("managerBankAccount", companyBankValue);
        body.addBodyParameter("managerCompany", companyValue);
        body.addBodyParameter("companyAddress", companyAddressValue);
        body.addBodyParameter("companyNum", companyNumValue);
        body.addBodyParameter("divideProportion", divideProportionValue);
        body.addBodyParameter("managerType", "2");
        return body;
    }

    //创建机器管理员
    private RequestParams getValuesOFCreateMachineManager() {
        RequestParams body = getPublicValues();
        body.addBodyParameter("managerAgent", UserMessage.getManagerNum());
        body.addBodyParameter("agentID", UserMessage.getManagerId());
        body.addBodyParameter("managerType", "3");
        return body;
    }

    private void hideEditText() {
        switch (flags) {
            case 1:
                agent_more_info_layout.setVisibility(View.GONE);
                break;
            case 2:
                break;
            case 3:
                agent_more_info_layout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void doPost(String url, RequestParams params) {

        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {


            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(getActivity(), R.string.chuanjianshibai_wangluoshibai, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> info) {
                Log.d("debug", "info= " + info.result);
                try {
                    JSONObject jsonObject = new JSONObject(info.result);
                    String err = jsonObject.getString("err");
                    if (TextUtils.isEmpty(err)) {//err.equals("")
                        Toast.makeText(getActivity(), R.string.chuanjianchenggong, Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
