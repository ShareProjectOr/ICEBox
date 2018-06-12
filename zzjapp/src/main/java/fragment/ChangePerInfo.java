package fragment;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import contentprovider.UserMessage;
import customview.ThreeMenuDialog;

/**
 * Created by WH on 2017/7/20.
 */

public class ChangePerInfo {
    private List<String> mUserMsgList;
    private List<String> mUserTipList;
    private LayoutInflater mInflater;
    private Context mContext;
    private BackAddress mBackAddress;
    private boolean isAddress = false;

    public ChangePerInfo(Context context, BackAddress backAddress) {
        mContext = context;
        mBackAddress = backAddress;
        mInflater = LayoutInflater.from(mContext);
        mUserMsgList = setMapValue();
        mUserTipList = setTipValue();
    }

    public List<View> getAllItemView() {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < mUserMsgList.size(); i++) {
            views.add(getView(i));
        }
        return views;
    }

    public View getView(int position) {
        ViewHolder viewHolder;
        View view;
        view = mInflater.inflate(R.layout.item_person_info, null);
        viewHolder = new ViewHolder();
        viewHolder.msg = (EditText) view.findViewById(R.id.msg);
        viewHolder.change = (TextView) view.findViewById(R.id.change_msg);
        viewHolder.msgTip = (TextView) view.findViewById(R.id.tip);
        view.setTag(viewHolder);
        viewHolder.msg.setText(mUserMsgList.get(position));
        viewHolder.msgTip.setText(mUserTipList.get(position));
        viewHolder.position = position;
        if (position == 3) {
            viewHolder.msg.setFocusable(false);
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAddress) {
                        ThreeMenuDialog dialog = new ThreeMenuDialog(mContext, false);
                        dialog.setonItemClickListener(new ThreeMenuDialog.MenuItemClickListener() {

                            @Override
                            public void onMenuItemClick(Bundle bundle) {
                                String prince = bundle.getString("prince");
                                String city = bundle.getString("city");
                                String area = bundle.getString("area");
                                String code = bundle.getString("code");
                                mBackAddress.getAddress(code + "|" + prince + "-" + city + "-" + area);
                                finalViewHolder.msg.setText(prince + "-" + city + "-" + area);
                            }
                        });
                        dialog.show();
                    }
                }
            });
        }

        if (position == 8) {
            viewHolder.msg.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if (position == 9) {
            viewHolder.change.setVisibility(View.GONE);
        }
        if (position == 10) {
            viewHolder.change.setVisibility(View.GONE);
        }
        setNotPasteAndUnFocusMode(viewHolder.msg);
        viewHolder.change.setOnClickListener(new ChangeListener(viewHolder.msg, viewHolder.change, viewHolder.position));
        return view;
    }

    public static class ViewHolder {
        public EditText msg;
        public TextView change;
        private TextView msgTip;
        public int position;
    }

    private List<String> setMapValue() {
        List<String> listValues = new ArrayList<>();
        listValues.add(0, UserMessage.getManagerName());
        listValues.add(1, UserMessage.getManagerTelephone());
        listValues.add(2, UserMessage.getManagerEmail());
        String address1 = UserMessage.getManagerAddress();
        if (TextUtils.isEmpty(address1)) {
            listValues.add(3, "");
            listValues.add(4, "");
        } else {
            String[] addressArry = address1.split("\\|");
            Log.d("debug", "addressArry==" + addressArry);
            if (addressArry.length == 3) {
                listValues.add(3, addressArry[1]);
                listValues.add(4, addressArry[2]);
            } else if (addressArry.length == 2) {
                listValues.add(3, addressArry[1]);
                listValues.add(4, "");
            } else if (addressArry.length == 1) {
                listValues.add(3, addressArry[0]);
                listValues.add(4, "");
            } else {
                listValues.add(3, "");
                listValues.add(4, "");
            }
        }
        listValues.add(5, UserMessage.getManagerCard());
        if (TextUtils.equals(UserMessage.getManagerType(), "2")) {
            listValues.add(6, UserMessage.getManagerCompany());
            if (TextUtils.isEmpty(UserMessage.getCompanyAddress())) {
                listValues.add(7, "");
            } else {
                String[] caddressArry = UserMessage.getCompanyAddress().split("\\|");
                if (caddressArry.length == 3) {
                    String address = caddressArry[1] + " " + caddressArry[2];
                    listValues.add(7, address);
                } else if (caddressArry.length == 2) {
                    String address = caddressArry[1];
                    listValues.add(7, address);
                } else if (caddressArry.length == 1) {
                    listValues.add(7, caddressArry[0]);
                } else {
                    listValues.add(7, "");
                }
            }


            BigDecimal b = new BigDecimal(Float.parseFloat(UserMessage.getDivideProportion()) * 100);
            float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            listValues.add(8, UserMessage.getCompanyNum());
            listValues.add(9, f1 + "%");
            listValues.add(10, UserMessage.getManagerBankAccount());

        }
        return listValues;
    }

    private List<String> setKeyValue() {
        List<String> listValues = new ArrayList<>();
        listValues.add(0, "managerName");
        listValues.add(1, "managerTelephone");
        listValues.add(2, "managerEmail");
        listValues.add(3, "managerAddress");
        listValues.add(4, "managerAddressDetail");
        listValues.add(5, "managerCard");
        if (TextUtils.equals(UserMessage.getManagerType(), "2")) {
            listValues.add(6, "managerCompany");
            listValues.add(7, "companyAddress");
            listValues.add(8, "companyNum");
            listValues.add(9, "divideProportion");
            listValues.add(10, "managerBankAccount");

        }
        return listValues;
    }

    private List<String> setTipValue() {
        List<String> listValues = new ArrayList<>();
        listValues.add(0, mContext.getResources().getString(R.string.managerName));
        listValues.add(1, mContext.getResources().getString(R.string.managerTelephone));
        listValues.add(2, mContext.getResources().getString(R.string.managerEmail));
        listValues.add(3, mContext.getResources().getString(R.string.managerAddress));
        listValues.add(4, mContext.getResources().getString(R.string.managerAddressDetail));
        listValues.add(5, mContext.getResources().getString(R.string.managerCard));
        if (TextUtils.equals(UserMessage.getManagerType(), "2")) {
            listValues.add(6, mContext.getResources().getString(R.string.companyAddress));
            listValues.add(7, mContext.getResources().getString(R.string.managerCompany));
            listValues.add(8, mContext.getResources().getString(R.string.companyNum));
            listValues.add(9, mContext.getResources().getString(R.string.divideProportion_text));
            listValues.add(10, mContext.getResources().getString(R.string.companyBank));

        }
        return listValues;
    }

    public List<String> backKeys() {
        return setKeyValue();
    }

    //EditTextv不可以失去焦点，不可被粘贴
    private void setNotPasteAndUnFocusMode(EditText view) {
        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
        view.setLongClickable(false);
    }

    //EditText得到焦点，可被粘贴
    private void setPasteAndFocusMode(EditText view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setLongClickable(true);
    }

    private class ChangeListener implements View.OnClickListener {
        private EditText msgEdit;
        private TextView changeMsg;
        private boolean isFrist = true;
        private String editContent;
        private int position;

        ChangeListener(View msg, View changeMsg, int position) {
            this.msgEdit = (EditText) msg;
            this.changeMsg = (TextView) changeMsg;
            this.editContent = this.msgEdit.getText().toString();
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (isFrist) {
                if (position != 3) {
                    setPasteAndFocusMode(this.msgEdit);
                }
                isAddress = true;
                this.changeMsg.setText(mContext.getResources().getString(R.string.cancle));
                this.changeMsg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.change_per_info_button_focus_style));
                isFrist = false;
            } else {
                isAddress = false;
                setNotPasteAndUnFocusMode(this.msgEdit);
                this.msgEdit.setText(this.editContent);
                this.changeMsg.setText(mContext.getResources().getString(R.string.change));
                this.changeMsg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.change_per_info_button_unfocus_style));
                isFrist = true;
            }
        }
    }

    public interface BackAddress {
        void getAddress(String address);
    }
}
