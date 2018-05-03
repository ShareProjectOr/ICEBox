package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.model.listeners.IAddCup;
import example.jni.com.coffeeseller.model.listeners.OnAddCupCallBackListener;

/**
 * Created by Administrator on 2018/4/29.
 */

public class AddCup implements IAddCup {
    @Override
    public void addCup(final Context context, final OnAddCupCallBackListener onAddCupCallBackListener) {
        final EditText addcount = new EditText(context);
        addcount.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(context).setView(addcount).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!addcount.getText().toString().isEmpty()) {
                    int beforeNum = SharedPreferencesManager.getInstance(context).getCupNum();
                    Log.e("TAG", "current cup num is " + Integer.parseInt(addcount.getText().toString()) + beforeNum);
                    SharedPreferencesManager.getInstance(context).setCupNum(Integer.parseInt(addcount.getText().toString()) + beforeNum);
                    onAddCupCallBackListener.AddSuccess();
                } else {
                    Toast.makeText(context, "加入量不能为空", Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("点错了", null).setTitle("请输入补充个数").create().show();
    }
}
