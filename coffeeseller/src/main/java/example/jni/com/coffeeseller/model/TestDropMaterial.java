package example.jni.com.coffeeseller.model;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.ContainerType;
import cof.ac.inter.DebugAction;
import cof.ac.inter.Result;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.listeners.ITestDropMaterial;
import example.jni.com.coffeeseller.model.listeners.TestDropMaterialCallBackListener;
import example.jni.com.coffeeseller.utils.Waiter;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TestDropMaterial implements ITestDropMaterial {
    String TAG = "TestDropMaterial";

    @Override
    public void StartDrop(final Context mContext, final int ContainerID, final TestDropMaterialCallBackListener testDropMaterialCallBackListener) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.edit_drop_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        final MaterialSql sql = new MaterialSql(mContext);
        builder.setTitle("确定开始落料测试吗?落料时间将持续5秒.");
        final AlertDialog alertDialog = builder.create();
        final EditText materialDropSpeed = (EditText) view.findViewById(R.id.materialDropSpeed);
        Button sure, cancel, save;
        TextView bunkerName;
        bunkerName = (TextView) view.findViewById(R.id.bunkerName);
        Log.e(TAG, "ContainerID is " + ContainerID);

        bunkerName.setText("落料仓:" + sql.getMaterialNameByContainerID(ContainerID + ""));
        sure = (Button) view.findViewById(R.id.sure);
        save = (Button) view.findViewById(R.id.save);
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoffMsger coffMsger = CoffMsger.getInstance();
                if (ContainerID == 0) {
                    Result result = coffMsger.Debug(DebugAction.CRUSH_BEAN, 0, 50);
                    if (result.getCode() == Result.SUCCESS) {
                        testDropMaterialCallBackListener.TestSuccess();
                    } else {
                        testDropMaterialCallBackListener.TestFailed(result.getCode());
                    }
                } else if (ContainerID == 7) {
                    Result result = coffMsger.Debug(DebugAction.OUT_HOTWATER, 0, 50);
                    if (result.getCode() == Result.SUCCESS) {
                        testDropMaterialCallBackListener.TestSuccess();
                    } else {
                        testDropMaterialCallBackListener.TestFailed(result.getCode());
                    }
                } else {
                    Result result = coffMsger.Debug(DebugAction.OUT_INGREDIENT, ContainerID, 50);
                    if (result.getCode() == Result.SUCCESS) {
                        testDropMaterialCallBackListener.TestSuccess();
                    } else {
                        testDropMaterialCallBackListener.TestFailed(result.getCode());
                    }
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialDropSpeed.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "请输入出料量", Toast.LENGTH_LONG).show();
                    return;
                }


                String bunkersID = sql.getBunkerIDByContainerID(ContainerID + "");
                sql.updateContact(bunkersID, "", "", "", "", "", "", materialDropSpeed.getText().toString(), "", "", "");//将校准值传入数据库
                alertDialog.dismiss();
                testDropMaterialCallBackListener.TestEnd();
            }
        });
        alertDialog.show();


    }
}
