package example.jni.com.coffeeseller.model.new_adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.bean.CoffeeFomat;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.bean.Step;
import example.jni.com.coffeeseller.bean.Taste;
import example.jni.com.coffeeseller.contentprovider.MaterialSql;
import example.jni.com.coffeeseller.model.listeners.GridViewItemListener;
import example.jni.com.coffeeseller.utils.MyLog;

/**
 * Created by WH on 2018/5/10.
 */

public class TasteGridAdapter extends BaseAdapter {
    private String TAG = "TasteGridAdapter";
    private List<Taste> tastes;
    private List<ViewHolder> viewHolders;
    private Context context;
    private Step step;


    public TasteGridAdapter(Context context, Step step) {
        this.context = context;
        this.tastes = step.getTastes();
        this.step = step;
        if (tastes == null) {
            this.tastes = new ArrayList<>();
        }
        viewHolders = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return tastes.size();
    }

    @Override
    public Object getItem(int position) {
        return tastes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Taste taste = tastes.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.new_taste_grid_item, null);
            viewHolder = new ViewHolder(convertView, taste);
            viewHolders.add(position, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.gridItem.setText(taste.getRemark());
        viewHolder.amount = taste.getAmount();
        if (viewHolder.isEnable) {
            viewHolder.isDefaultSelected = true;
        } else {
            viewHolder.isDefaultSelected = false;
        }
        MyLog.d(TAG, "getView");
        return convertView;
    }

    /*
    * 选中颜色更新
    * */
    public CoffeeFomat updateTasteSelected(CoffeeFomat coffeeFomat, Step step, int position, int index) {

        for (int i = 0; i < viewHolders.size(); i++) {

            ViewHolder viewHolder = viewHolders.get(i);
            if (position == i) {
                MyLog.d(TAG, "isEnabled =" + viewHolder.gridItem.isEnabled());
                if (viewHolder.gridItem.isEnabled()) {
                    viewHolder.gridItem.setSelected(true);

                    ContainerConfig containerConfig = coffeeFomat.getContainerConfigs().get(index);

                    float materialTime = ((float) step.getTastes().get(position).getAmount()) / 100;

                    int useMaterial = Math.round(materialTime * step.getContainerConfig().getMaterial_time());//

                    MyLog.d(TAG, "step.getMaterial_time()= " + step.getContainerConfig().getMaterial_time()
                            + " ,time= " + materialTime + ",getMaterial_time= " + useMaterial + " ----i= " + step.getTastes().get(position).getRemark());

                    containerConfig.setMaterial_time(useMaterial);

                    coffeeFomat.getContainerConfigs().remove(index);

                    coffeeFomat.getContainerConfigs().add(index, containerConfig);

                } else {
                    viewHolder.gridItem.setSelected(false);
                }
            } else {
                viewHolder.gridItem.setSelected(false);
            }
        }
        return coffeeFomat;
    }

    public int getDefaultSelectedItem() {
        int defaultIndex = 0;
        if (viewHolders != null && viewHolders.size() > 0) {
            for (int i = 0; i < viewHolders.size(); i++) {
                ViewHolder viewHolder = viewHolders.get(i);
                if (viewHolder.isDefaultSelected && viewHolder.amount == 100) {
                    return i;
                } else if (viewHolder.isDefaultSelected) {
                    defaultIndex = i;
                    continue;
                }
            }
        }
        return defaultIndex;
    }

    class ViewHolder {
        public View view;
        public TextView gridItem;
        public int amount;
        public boolean isDefaultSelected = false;
        public boolean isEnable = false;

        public ViewHolder(View view, Taste taste) {
            this.view = view;
            gridItem = (TextView) view.findViewById(R.id.grid_item);
            setEnable(taste);
        }

        /*
 * 根据数据库剩余量计算是否可用
* */
        public void setEnable(Taste taste) {

            MaterialSql table = new MaterialSql(context);
            String sqlRestMaterial = table.getStorkByMaterialID(step.getMaterial().getMaterialID() + "");

            if (TextUtils.isEmpty(sqlRestMaterial)) {
                return;
            }
            int sqlRestMaterialInt = Integer.parseInt(sqlRestMaterial);
            float useMaterial = ((float) taste.getAmount() / 100 * step.getAmount());
            if (sqlRestMaterialInt < useMaterial) {
                gridItem.setEnabled(false);
                isEnable = false;
                MyLog.d(TAG, "setEnable");
                gridItem.setBackground(ContextCompat.getDrawable(context, R.drawable.new_shape_taste_enable));
            } else {
                isEnable = true;
                gridItem.setEnabled(true);
                gridItem.setBackground(ContextCompat.getDrawable(context, R.drawable.selector_taste_bng));
            }
        }

    }
}
