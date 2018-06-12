package ViewHolder;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/28.
 */

public class DetailsDividerListViewHolder {
    public DetailsDividerListViewHolder() {
    }
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();//j节省内存，提高性能，使用SparseArray
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView=view.findViewById(id);
            viewHolder.put(id,childView);
        }
        return (T) childView;
    }
}
