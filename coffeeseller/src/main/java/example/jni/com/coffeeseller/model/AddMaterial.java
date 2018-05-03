package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.model.listeners.IAddMaterial;
import example.jni.com.coffeeseller.model.listeners.OnAddMaterialCallBackListener;
import example.jni.com.coffeeseller.utils.SecondToDate;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by Administrator on 2018/4/24.
 */

public class AddMaterial implements IAddMaterial {
    private String TAG = "AddMaterial";
    private boolean isCanCheck = false;

    @Override
    public void addMaterial(final Context context, final String bunkersID, final MaterialSql sql, final OnAddMaterialCallBackListener onAddMaterialCallBackListener) {
        TextView mAdd_bunker_name, mBefore_add_stock;
        final EditText mAdd_account;
        final EditText mLast_add_stock;
        Button mCancel, mSure;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_material_dialog_layout, null);
        builder.setView(view);
        mAdd_bunker_name = (TextView) view.findViewById(R.id.add_bunker_name);
        mBefore_add_stock = (TextView) view.findViewById(R.id.before_add_stock);
        mAdd_account = (EditText) view.findViewById(R.id.add_account);
        mLast_add_stock = (EditText) view.findViewById(R.id.last_add_stock);
       /* mAdd_account.setHint();
        mLast_add_stock.setHint();*/
        mCancel = (Button) view.findViewById(R.id.cancel);
        mSure = (Button) view.findViewById(R.id.sure);
        mAdd_bunker_name.setText(sql.getBunkersNameByID(bunkersID));

        String beforeAddStorktext = sql.getStorkByBunkersID(bunkersID);
        final long beforeAddStork = Long.parseLong(beforeAddStorktext);
        final double dobeforeAddStork = new BigDecimal(((float) beforeAddStork / 1000)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        mBefore_add_stock.setText(dobeforeAddStork + "");


        mLast_add_stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!mLast_add_stock.getText().toString().isEmpty()) {
                        double lastAddAccount = Double.parseDouble(mLast_add_stock.getText().toString());
                        final double lastAddStork = new BigDecimal(((float) lastAddAccount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        if (lastAddStork >= dobeforeAddStork) {
                            mAdd_account.setText((lastAddStork - dobeforeAddStork) + "");
                            Log.e(TAG, "now lastAddStork is " + mLast_add_stock.toString() + " and beforeAddStock is " + dobeforeAddStork + " and addAcount is " + (lastAddStork - dobeforeAddStork));
                        }
                    }

                }
            }
        });

        mAdd_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!mAdd_account.getText().toString().isEmpty()) {
                        double addAccount = Double.parseDouble(mAdd_account.getText().toString());
                        mLast_add_stock.setText(addAccount + dobeforeAddStork + "");
                    }

                }
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
                if (!mAdd_account.getText().toString().isEmpty()) {
                    double addAccount = Double.parseDouble(mAdd_account.getText().toString());
                    mLast_add_stock.setText(addAccount + dobeforeAddStork + "");
                }

                if (sql.updateContact(bunkersID, "", "", "", "", "", (long) (Double.parseDouble(mLast_add_stock.getText().toString()) * 1000) + "", "", "", SecondToDate.getDateToString(System.currentTimeMillis()))) {
                    onAddMaterialCallBackListener.addEnd(sql);
                    Toast.makeText(context, "补料成功", Toast.LENGTH_LONG).show();
                    isCanCheck = false;
                } else {
                    Toast.makeText(context, "补料失败", Toast.LENGTH_LONG).show();
                }
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void addSpecialMaterial(final Context context, final String bunkersID, final MaterialSql sql, final OnAddMaterialCallBackListener onAddMaterialCallBackListener) {
        TextView mAdd_bunker_name, mBefore_add_stock;
        final EditText mAdd_account, mLast_add_stock;
        Button mCancel, mSure;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_material_dialog_layout, null);
        builder.setView(view);
        mAdd_bunker_name = (TextView) view.findViewById(R.id.add_bunker_name);
        mBefore_add_stock = (TextView) view.findViewById(R.id.before_add_stock);
        mAdd_account = (EditText) view.findViewById(R.id.add_account);
        mAdd_account.setHint("单位(个)");
        mLast_add_stock = (EditText) view.findViewById(R.id.last_add_stock);
        mLast_add_stock.setHint("单位(个)");
        mCancel = (Button) view.findViewById(R.id.cancel);
        mSure = (Button) view.findViewById(R.id.sure);
        mAdd_bunker_name.setText(sql.getBunkersNameByID(bunkersID));

        String beforeAddStorktext = sql.getStorkByBunkersID(bunkersID);
        mBefore_add_stock.setText(beforeAddStorktext + "个");
        final long beforeAddCount = Long.parseLong(beforeAddStorktext);
        switch (bunkersID) {
            case "7":
                mAdd_bunker_name.setText("纸杯仓");
                break;
            case "8":
                mAdd_bunker_name.setText("温水仓");
                break;

        }

        mLast_add_stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!mLast_add_stock.getText().toString().isEmpty()) {
                        long lastAddAccount = Long.parseLong(mLast_add_stock.getText().toString());
                        if (lastAddAccount >= beforeAddCount) {
                            mAdd_account.setText((lastAddAccount - beforeAddCount) + "");
                            Log.e(TAG, "now lastAddStork is " + mLast_add_stock.toString() + " and beforeAddStock is " + beforeAddCount + " and addAcount is " + (lastAddAccount - beforeAddCount));
                        }
                    }

                }
            }
        });

        mAdd_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!mAdd_account.getText().toString().isEmpty()) {
                        long addAccount = Long.parseLong(mAdd_account.getText().toString());
                        mLast_add_stock.setText(addAccount + beforeAddCount + "");
                    }

                }
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
                if (!mAdd_account.getText().toString().isEmpty()) {
                    long addAccount = Long.parseLong(mAdd_account.getText().toString());
                    mLast_add_stock.setText(addAccount + beforeAddCount + "");
                }

                if (sql.updateContact(bunkersID, "", "", "", "", "", Long.parseLong(mLast_add_stock.getText().toString()) + "", "", "", SecondToDate.getDateToString(System.currentTimeMillis()))) {
                    onAddMaterialCallBackListener.addEnd(sql);
                    Toast.makeText(context, "补料成功", Toast.LENGTH_LONG).show();
                    isCanCheck = false;
                } else {
                    Toast.makeText(context, "补料失败", Toast.LENGTH_LONG).show();
                }
                alertDialog.dismiss();
            }
        });
    }

}
