package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.listeners.IAddMaterial;
import example.jni.com.coffeeseller.model.listeners.OnAddMaterialCallBackListener;

/**
 * Created by Administrator on 2018/4/24.
 */

public class AddMaterial implements IAddMaterial {

    @Override
    public void addMaterial(final Context context, final String bunkersID, final MaterialSql sql, final OnAddMaterialCallBackListener onAddMaterialCallBackListener) {
        TextView mAdd_bunker_name, mBefore_add_stock;
        final EditText mAdd_account, mLast_add_stock;
        Button mCancel, mSure;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_material_dialog_layout, null);
        builder.setView(view);
        mAdd_bunker_name = (TextView) view.findViewById(R.id.add_bunker_name);
        mBefore_add_stock = (TextView) view.findViewById(R.id.before_add_stock);
        mAdd_account = (EditText) view.findViewById(R.id.add_account);
        mLast_add_stock = (EditText) view.findViewById(R.id.last_add_stock);
        mCancel = (Button) view.findViewById(R.id.cancel);
        mSure = (Button) view.findViewById(R.id.sure);
        mAdd_bunker_name.setText(sql.getBunkersNameByID(bunkersID));
        mBefore_add_stock.setText(sql.getStorkByBunkersID(bunkersID));
        final int beforeAddStork = Integer.parseInt(mBefore_add_stock.getText().toString());

        mAdd_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                int addAccount;
                if (s.length() == 0) {
                    addAccount = 0;
                } else {
                    addAccount = Integer.parseInt(s.toString());
                }

                mLast_add_stock.setText(addAccount + beforeAddStork + "");
            }
        });

        mLast_add_stock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int lastAddStork;
                if (s.length() == 0) {
                    lastAddStork = 0;
                } else {
                    lastAddStork = Integer.parseInt(s.toString());
                }
                mAdd_account.setText(lastAddStork - beforeAddStork + "");
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdd_account.getText().toString().isEmpty() && mLast_add_stock.getText().toString().isEmpty()) {
                    Toast.makeText(context, "请输入补料量或补料量后余量", Toast.LENGTH_LONG).show();
                    return;
                }
                if (sql.updateContact(bunkersID, "", "", "", "", mLast_add_stock.getText().toString(), "", "", "")) {
                    onAddMaterialCallBackListener.addEnd(sql);
                    Toast.makeText(context, "补料成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "补料失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
