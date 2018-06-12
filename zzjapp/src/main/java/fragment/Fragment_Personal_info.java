package fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.Activity.LoginActivity;
import com.example.zhazhijiguanlixitong.Activity.PersonalInfoActivity;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.ChangePerInfo;
import contentprovider.UserMessage;
import httputil.Constance;
import httputil.ConstanceMethod;
import httputil.Sql;
import httputil.XUtils;
import otherutils.CheakPhone;
import otherutils.IDCard;

public class Fragment_Personal_info extends Fragment implements View.OnClickListener, ChangePerInfo.BackAddress {
    private View view;
    private LinearLayout mParent;
    private Context mContext;
    private ChangePerInfo mChangePerInfo;
    private Handler mHandler;
    private List<View> mItemViews;
    private Map<String, String> newValues;
    private String address, comAddress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment__personal_info, null, false);
            initview();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initview() {
        mContext = getActivity();
        newValues = new HashMap<>();
        mParent = (LinearLayout) view.findViewById(R.id.parent);
        Button mCommit = (Button) view.findViewById(R.id.commit);
        Button mDropOut_bnt = (Button) view.findViewById(R.id.drop_out_login);
        Button mRepairPassword = (Button) view.findViewById(R.id.repairpassword);
        mCommit.setOnClickListener(this);
        mDropOut_bnt.setOnClickListener(this);
        mRepairPassword.setOnClickListener(this);
        mChangePerInfo = new ChangePerInfo(mContext, this);
        mItemViews = mChangePerInfo.getAllItemView();
        setHandler();
        setItemView();
    }

    //提交成功后，更新本地数据
    public void setHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj != null) {
                    try {
                        UserMessage.setUserMessage((JSONObject) msg.obj);
                        onRefresh();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        };

    }

    //刷新界面
    private void onRefresh() {
        if (mChangePerInfo != null) {
            mChangePerInfo = null;
        }
        mChangePerInfo = new ChangePerInfo(mContext, this);
        if (mItemViews != null) {
            mItemViews = null;
        }
        mItemViews = mChangePerInfo.getAllItemView();
        if (mParent != null) {
            mParent.removeAllViews();
            setItemView();
        }
    }

    private void setItemView() {
        if (mItemViews != null) {
            for (View itemView : mItemViews) {
                if (itemView != null) {
                    mParent.addView(itemView);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                getInputValue();
                if (!judgeValueIsVlide(newValues)) {
                    break;
                }
                XUtils mXutils = XUtils.newInstance();
                mXutils.setContext(getActivity());
                mXutils.setHandler(mHandler);
                mXutils.doPost(Constance.EDIT_MANAGER_URL, setChangeParams());
                break;
            case R.id.drop_out_login:
                //退出登录监听
                Dosql();
                ConstanceMethod.isFirstLogin(mContext, true);
                ConstanceMethod.clearActivityFromStack(getActivity(), LoginActivity.class);
                getActivity().finish();
                break;
            case R.id.repairpassword:
                PersonalInfoActivity parent = (PersonalInfoActivity) getActivity();
                parent.ReplaceToFragment_Repair_Password();

                break;

        }
    }

    private boolean judgeValueIsVlide(Map<String, String> newVaules) {

      //  Log.d("debug", "my result=" + newVaules.toString());


        if (TextUtils.isEmpty(newVaules.get("managerName"))) {
            Toast.makeText(getActivity(), getResources().getString(R.string.name_is_null), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(newVaules.get("managerTelephone"))) {
            Toast.makeText(getActivity(), getResources().getString(R.string.phone_is_null), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!CheakPhone.isLegalPhone(newVaules.get("managerTelephone"))) {
                Toast.makeText(getActivity(), "电话号码无效！", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (TextUtils.isEmpty(newVaules.get("managerAddress"))) {
            Toast.makeText(getActivity(), getResources().getString(R.string.manager_address_is_null), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(newVaules.get("managerCard"))) {
            Toast.makeText(getActivity(), getResources().getString(R.string.idcard_is_null), Toast.LENGTH_SHORT).show();
            return false;
        }
//        else {
//            IDCard idCard = new IDCard();
//            if (!idCard.verify(newVaules.get("managerCard"))) {
//                Toast.makeText(getActivity(), "身份证号码无效！", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }
        if (!TextUtils.isEmpty(newVaules.get("managerEmail"))) {
            if (!newVaules.get("managerEmail").contains("@")) {
                Toast.makeText(getActivity(), getResources().getString(R.string.email_not_valid), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (TextUtils.equals(UserMessage.getManagerType(), "2")) {
            if (TextUtils.isEmpty(newVaules.get("managerCompany"))) {
                Toast.makeText(getActivity(), getResources().getString(R.string.company_name_is_null), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(newVaules.get("companyAddress"))) {
                Toast.makeText(getActivity(), getResources().getString(R.string.company_address_is_null), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(newVaules.get("companyNum"))) {
                Toast.makeText(getActivity(), getResources().getString(R.string.company_code_is_null), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void Dosql() {
        Sql sql = new Sql(getActivity());
        sql.deleteAllContact(sql.getAllCotacts().size());
    }

    //取得更新的值
    private void getInputValue() {
        List<String> keys = mChangePerInfo.backKeys();
        if (mItemViews != null) {
            int position = 0;
            for (View itemView : mItemViews) {
                if (itemView != null) {
                    EditText text = (EditText) itemView.findViewById(R.id.msg);
                    if (TextUtils.equals(keys.get(position), "managerAddress")) {
                        if (TextUtils.isEmpty(address)) {
                            if (!TextUtils.isEmpty(UserMessage.getManagerAddress())) {
                                String[] addressArry = UserMessage.getManagerAddress().split("\\|");
                                if (addressArry.length == 2 || addressArry.length == 3){
                                    newValues.put(keys.get(position), addressArry[0] + "|" + addressArry[1]);
                                }else{
                                    newValues.put(keys.get(position), UserMessage.getManagerAddress());
                                }
                            } else {
                                newValues.put(keys.get(position), UserMessage.getManagerAddress());
                            }

                        } else {
                            newValues.put(keys.get(position), address);
                        }
                    } else if (TextUtils.equals(keys.get(position), "managerAddressDetail")) {
                        String address = newValues.get("managerAddress") + "|" + text.getText().toString();
                        newValues.put("managerAddress", address);
                    } else {
                        newValues.put(keys.get(position), text.getText().toString());
//                        if (TextUtils.equals(keys.get(position), "companyAddress")) {
//                            if (TextUtils.isEmpty(comAddress)) {
//                                String[] addressArry = UserMessage.getCompanyAddress().split("\\|");
//                                newValues.put(keys.get(position), addressArry[0] + "|" + addressArry[1]);
//                            } else {
//                                newValues.put(keys.get(position), comAddress);
//                            }
//                        } else if (TextUtils.equals(keys.get(position), "companyAddressDetail")) {
//                            String address = newValues.get("companyAddress") + "|" + text.getText().toString();
//                            newValues.put("companyAddress", address);
//                        } else {
//                            newValues.put(keys.get(position), text.getText().toString());
//                        }
                    }
                }
                position++;
            }
        }
    }

    private RequestParams setChangeParams() {
        RequestParams body = new RequestParams();
        body.addBodyParameter("managerID", UserMessage.getManagerId());
        body.addBodyParameter("userID", UserMessage.getManagerId());
        if (newValues.containsKey("managerName")) {
            body.addBodyParameter("params[managerName]", newValues.get("managerName"));
        }
        if (newValues.containsKey("managerTelephone")) {
            body.addBodyParameter("params[managerTelephone]", newValues.get("managerTelephone"));
        }
        if (newValues.containsKey("managerEmail")) {
            body.addBodyParameter("params[managerEmail]", newValues.get("managerEmail"));
        }
        if (newValues.containsKey("managerAddress")) {
            body.addBodyParameter("params[managerAddress]", newValues.get("managerAddress"));
        }
        if (newValues.containsKey("managerCard")) {
            body.addBodyParameter("params[managerCard]", newValues.get("managerCard"));
        }
        if (TextUtils.equals(UserMessage.getManagerType(), "2")) {
            if (newValues.containsKey("managerCompany")) {
                body.addBodyParameter("params[managerCompany]", newValues.get("managerCompany"));
            }
            if (newValues.containsKey("companyAddress")) {
                body.addBodyParameter("params[companyAddress]", newValues.get("companyAddress"));
            }
            if (newValues.containsKey("companyNum")) {
                body.addBodyParameter("params[companyNum]", newValues.get("companyNum"));
            }
        }
        return body;
    }

    @Override
    public void getAddress(int tag, String address) {
        if (tag == 0) {
            this.address = address;
            Log.d("debug", "address==========" + address);
        }
//        else if (tag == 1) {
//            this.comAddress = address;
//        }

    }
}
