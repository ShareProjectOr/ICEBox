package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.ItemTradeTotal;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.fragments.machine.MachineDetailFragment;
import com.example.shareiceboxms.views.fragments.machine.MachineFragment;

import java.util.List;

import static com.example.shareiceboxms.models.contants.Constants.TradeTotalTitles;
import static com.example.shareiceboxms.models.contants.Constants.TradeTotalTitlesItem;

/**
 * Created by WH on 2017/12/9.
 */

public class MachineListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_ITEM = 0;
    private static final int VIEW_LOADING = 1;
    Context context;
    List<ItemMachine> itemMachines;
    MachineFragment machineFragment;

    public MachineListAdapter(Context context, List<ItemMachine> itemMachines, MachineFragment machineFragment) {
        this.context = context;
        this.itemMachines = itemMachines;
        this.machineFragment = machineFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_ITEM) {
            viewHolder = new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.machine_list_item, null));
        } else if (viewType == VIEW_LOADING) {
            viewHolder = new LoadingHolder(LayoutInflater.from(context).inflate(R.layout.loading_more, null));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (machineFragment != null) {
                        FragmentFactory.getInstance().getSavedBundle().putString("machineCode", "1212121");
                        machineFragment.addFrameLayout(new MachineDetailFragment());
                    }
                }
            });
        } else if (holder instanceof LoadingHolder) {
            if (holder != null) {
            }
        }
//        viewHolder.totalMoneyNum.setText(itemTradeTotal);
//        viewHolder.hasPayNum.setText(itemTradeTotal);
//        viewHolder.notPayNum.setText(itemTradeTotal);
    }

    @Override
    public int getItemCount() {
        return itemMachines.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView machineAddr, machineName, isOnLine, isException, managerName, machienCode;

        public ItemViewHolder(View itemView) {
            super(itemView);
            machineAddr = (TextView) itemView.findViewById(R.id.machineAddr);
            machineName = (TextView) itemView.findViewById(R.id.machineName);
            isOnLine = (TextView) itemView.findViewById(R.id.isOnLine);
            isException = (TextView) itemView.findViewById(R.id.isException);
            managerName = (TextView) itemView.findViewById(R.id.managerName);
            machienCode = (TextView) itemView.findViewById(R.id.machienCode);

        }
    }

    class LoadingHolder extends RecyclerView.ViewHolder {
        public TextView loading;

        public LoadingHolder(View itemView) {
            super(itemView);
            loading = (TextView) itemView.findViewById(R.id.loading);
        }
    }
}
