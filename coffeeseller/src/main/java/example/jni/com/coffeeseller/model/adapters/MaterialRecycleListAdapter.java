package example.jni.com.coffeeseller.model.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.jni.com.coffeeseller.R;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MaterialRecycleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;

    public MaterialRecycleListAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.material_list_item_layout, null);
        RecyclerView.ViewHolder holder = new ContentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentViewHolder mHolder = (ContentViewHolder) holder;
      //  mHolder.bankersName

    }

    @Override
    public int getItemCount() {
        return 0;
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
