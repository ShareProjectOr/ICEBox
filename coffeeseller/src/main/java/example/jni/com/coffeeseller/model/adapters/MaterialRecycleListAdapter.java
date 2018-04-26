package example.jni.com.coffeeseller.model.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.bunkerData;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.presenter.AddMaterialPresenter;
import example.jni.com.coffeeseller.views.viewinterface.IAddMaterialView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MaterialRecycleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IAddMaterialView {
    private Activity mContext;
    private MaterialSql sql;
    private String bunkersID;
    private AddMaterialPresenter presenter;

    public MaterialRecycleListAdapter(Activity mContext) {
        sql = new MaterialSql(mContext);
        this.mContext = mContext;
        presenter = new AddMaterialPresenter(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.material_list_item_layout, null);
        RecyclerView.ViewHolder holder = new ContentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ContentViewHolder mHolder = (ContentViewHolder) holder;
        final List<bunkerData> list = sql.getBunkersList();
        mHolder.bankersName.setText(list.get(position).getContainerID() + "-" + list.get(position).getMaterialName() + "仓");
        mHolder.Material.setText(list.get(position).getMaterialName());
        mHolder.AddTime.setText(list.get(position).getLastLoadingTime());
        mHolder.Opration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bunkersID = list.get(position).getBunkerID();
                presenter.AddMaterial();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sql.getBunkersList().size();
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
        this.sql = sql;
        notifyDataSetChanged();
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
