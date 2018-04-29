package example.jni.com.coffeeseller.model.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.bunkerData;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.presenter.AddMaterialPresenter;
import example.jni.com.coffeeseller.presenter.BindMaterialPresenter;
import example.jni.com.coffeeseller.views.viewinterface.IAddMaterialView;
import example.jni.com.coffeeseller.views.viewinterface.IBindMaterialView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MaterialRecycleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IAddMaterialView, IBindMaterialView {
    private Activity mContext;
    private MaterialSql sql;
    private String bunkersID;
    private AddMaterialPresenter presenter;
    private BindMaterialPresenter bindMaterialPresenter;
    private List<bunkerData> list = new ArrayList<>();

    public MaterialRecycleListAdapter(Activity mContext) {

        sql = new MaterialSql(mContext);
        sql.setAdapter(this);
        this.mContext = mContext;
        presenter = new AddMaterialPresenter(this);
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
        mHolder.CountAndLess.setText(list.get(position).getMaterialStock() + "mg");
        if (!list.get(position).getMaterialName().equals("未启用")) {
            mHolder.bankersName.setTextColor(ContextCompat.getColor(mContext, R.color.black));//已经启用了的料仓才能重新绑定原料
            mHolder.Opration.setText("补料");
            mHolder.Opration.setOnClickListener(new View.OnClickListener() {// 启用了的料仓才能补料
                @Override
                public void onClick(View v) {
                    bunkersID = list.get(position).getBunkerID();
                    presenter.AddMaterial();
                }
            });
            mHolder.bankersName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bindMaterialPresenter.BindMaterial(mHolder.bankersName, list.get(position).getBunkerID());
                }
            });
        } else {
            mHolder.bankersName.setTextColor(ContextCompat.getColor(mContext, R.color.login_black_gray));//未开启的料仓
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
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


    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView bankersName, Material, CountAndLess, AddTime, Opration;

        public ContentViewHolder(View itemView) {
            super(itemView);
            bankersName = (TextView) itemView.findViewById(R.id.bankersName);
            Material = (TextView) itemView.findViewById(R.id.material);
            CountAndLess = (TextView) itemView.findViewById(R.id.countAndless);
            AddTime = (TextView) itemView.findViewById(R.id.addTime);
            Opration = (TextView) itemView.findViewById(R.id.operation);
        }
    }
}
