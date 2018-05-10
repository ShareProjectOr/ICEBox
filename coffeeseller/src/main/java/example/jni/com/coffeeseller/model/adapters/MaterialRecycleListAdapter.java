package example.jni.com.coffeeseller.model.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.bunkerData;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.presenter.AddMaterialPresenter;
import example.jni.com.coffeeseller.presenter.BindMaterialPresenter;
import example.jni.com.coffeeseller.presenter.DropMaterialPresenter;
import example.jni.com.coffeeseller.views.viewinterface.IAddMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.IBindMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.IDebugDropMaterialView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MaterialRecycleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IAddMaterialView, IBindMaterialView, IDebugDropMaterialView {
    private Activity mContext;
    private MaterialSql sql;
    private String bunkersID;
    private AddMaterialPresenter presenter;
    private BindMaterialPresenter bindMaterialPresenter;
    private List<bunkerData> list = new ArrayList<>();
    private DropMaterialPresenter mDropMaterialPresenter;

    public MaterialRecycleListAdapter(Activity mContext) {

        sql = new MaterialSql(mContext);
        sql.setAdapter(this);
        this.mContext = mContext;
        presenter = new AddMaterialPresenter(this);
        mDropMaterialPresenter = new DropMaterialPresenter(this);
        bindMaterialPresenter = new BindMaterialPresenter(this, this);
        if (list.size() != 0) {
            list.clear();
        }

        list = sql.getRecycleBunkersList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.material_list_item_layout, parent, false);
        RecyclerView.ViewHolder holder = new ContentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ContentViewHolder mHolder = (ContentViewHolder) holder;
        mHolder.bankersName.setText(list.get(position).getBunkersName());
        mHolder.Material.setText(list.get(position).getMaterialName());
        mHolder.AddTime.setText(list.get(position).getLastLoadingTime());
        if (list.get(position).getMaterialDropSpeed().equals("null") || list.get(position).getMaterialDropSpeed().equals("0") || list.get(position).getMaterialDropSpeed().equals("")) {
            mHolder.output.setText("未设置");
        } else {
            mHolder.output.setText(list.get(position).getMaterialDropSpeed());
        }

        long stockmg;
        if (list.get(position).getMaterialStock().equals("")) {
            stockmg = 0;
        } else {
            stockmg = Long.parseLong(list.get(position).getMaterialStock());
        }


        double realstock = new BigDecimal(((float) stockmg / 1000)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        switch (list.get(position).getMaterialType()) {
            case "3":
                mHolder.CountAndLess.setText(realstock*1000 + "个");
                break;
            case "2":
                mHolder.CountAndLess.setText(realstock + "L");
                break;
            default:
                mHolder.CountAndLess.setText(realstock + "g");
                break;
        }

        if (!list.get(position).getMaterialName().equals("未启用") || list.get(position).getMaterialName().equals("null")) {
            mHolder.bankersName.setTextColor(ContextCompat.getColor(mContext, R.color.black));//已经启用了的料仓才能重新绑定原料

            mHolder.Opration.setText("补料");
            mHolder.Opration.setOnClickListener(new View.OnClickListener() {// 启用了的料仓才能补料
                @Override
                public void onClick(View v) {
                    Log.e("补料","bunkerType is "+list.get(position).getBunkerType());
                    if (list.get(position).getBunkerType().equals("3")) {  //如果为包装仓 则为补杯界面
                        bunkersID = list.get(position).getBunkerID();
                        presenter.addSepcialMaterial();
                    } else {                    //否则为常规补料
                        bunkersID = list.get(position).getBunkerID();
                        presenter.AddMaterial();
                    }

                }
            });
            mHolder.bindMaterial.setOnClickListener(new View.OnClickListener() { //点击 绑定原料
                @Override
                public void onClick(View v) {
                    bindMaterialPresenter.BindMaterial(mHolder.bindMaterial, list.get(position).getBunkerID(), list.get(position).getBunkerType());
                }
            });
            mHolder.textDrop.setOnClickListener(new View.OnClickListener() { // 点击进行落料
                @Override
                public void onClick(View v) {
                    mDropMaterialPresenter.startDrop(mContext, Integer.parseInt(list.get(position).getContainerID()));
                }
            });
        } else {
            mHolder.bankersName.setTextColor(ContextCompat.getColor(mContext, R.color.login_black_gray));//未开启的料仓
            mHolder.bindMaterial.setTextColor(ContextCompat.getColor(mContext, R.color.login_black_gray));//未开启料仓时,绑定的原料为灰色且不能被点击
        }


    }


    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }

    }

    @Override
    public MaterialSql getSql() {
        return sql;
    }

    @Override
    public Context getcontext() {
        return mContext;
    }

    @Override
    public String getBunkersID() {
        return bunkersID;
    }


    @Override
    public void notifySetDataChange(MaterialSql sql) {
        if (list.size() != 0) {
            list.clear();
        }

        list = sql.getRecycleBunkersList();

        notifyDataSetChanged();
    }

    @Override
    public void ShowResult(String Result) {
        Toast.makeText(mContext, Result, Toast.LENGTH_LONG).show();
    }

    ProgressDialog dialog;

    @Override
    public void ShowLoading() {
        dialog = ProgressDialog.show(mContext, null, "正在获取原料列表", false, false);
        dialog.show();
    }

    @Override
    public void HideLoading() {
        dialog.dismiss();
    }

    @Override
    public TextView getview() {
        return null;
    }

    @Override
    public void ShowDropResult(String result, String ContainerID) {

    }

    @Override
    public void ShowEditDialog(final String ContainerID) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.edit_drop_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        final EditText materialDropSpeed = (EditText) view.findViewById(R.id.materialDropSpeed);
        Button sure, cancel;
        sure = (Button) view.findViewById(R.id.sure);
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
                if (materialDropSpeed.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "请输入出料量", Toast.LENGTH_LONG).show();
                    return;
                }
                String bunkersID = sql.getBunkerIDByContainerID(ContainerID);
                sql.updateContact(bunkersID, "", "", "", "", "", "", materialDropSpeed.getText().toString(), "", "", "");//将校准值传入数据库
            }
        });
        alertDialog.show();
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView bankersName, Material, CountAndLess, AddTime, Opration, bindMaterial, textDrop, output;

        public ContentViewHolder(View itemView) {
            super(itemView);
            bankersName = (TextView) itemView.findViewById(R.id.bankersName);
            Material = (TextView) itemView.findViewById(R.id.material);
            CountAndLess = (TextView) itemView.findViewById(R.id.countAndless);
            AddTime = (TextView) itemView.findViewById(R.id.addTime);
            Opration = (TextView) itemView.findViewById(R.id.operation);
            bindMaterial = (TextView) itemView.findViewById(R.id.bindMarterial);
            textDrop = (TextView) itemView.findViewById(R.id.textDrop);
            output = (TextView) itemView.findViewById(R.id.output);
        }
    }
}
