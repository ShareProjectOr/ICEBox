package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.contentprovider.SingleMaterialLsit;
import example.jni.com.coffeeseller.model.listeners.ISaveCoffee;
import example.jni.com.coffeeseller.model.listeners.OnSaveCoffeeCallBackListener;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SaveCoffee implements ISaveCoffee, View.OnClickListener {
    private EditText mName;
    private EditText mPrice;
    private Button mSave;
    private Button mCancelsave;
    private List<ContainerConfig> list;
    private AlertDialog dialog;
    private Context mContext;
    private OnSaveCoffeeCallBackListener onSaveCoffeeCallBackListener;

    @Override
    public void saveCoffee(Context context, List<ContainerConfig> list, OnSaveCoffeeCallBackListener onSaveCoffeeCallBackListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.savecoffee_dialog_layout, null);
        builder.setView(view);
        dialog = builder.create();
        mContext = context;
        this.onSaveCoffeeCallBackListener = onSaveCoffeeCallBackListener;
        mName = (EditText) view.findViewById(R.id.name);
        mPrice = (EditText) view.findViewById(R.id.price);
        mSave = (Button) view.findViewById(R.id.save);
        mCancelsave = (Button) view.findViewById(R.id.cancelsave);
        dialog.show();
        this.list = list;
        mSave.setOnClickListener(this);
        mCancelsave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                Coffee coffee = new Coffee();
                coffee.setName(mName.getText().toString());
                coffee.setPrice(mPrice.getText().toString());
                coffee.setProcessList(list);
                if (SingleMaterialLsit.getInstance().AddCoffeeList(coffee)) {
                    onSaveCoffeeCallBackListener.saveSuccess();
                    dialog.dismiss();
                } else {
                    Toast.makeText(mContext, "保存失败", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

                break;
            case R.id.cancelsave:
                dialog.dismiss();
                break;
        }
    }
}
