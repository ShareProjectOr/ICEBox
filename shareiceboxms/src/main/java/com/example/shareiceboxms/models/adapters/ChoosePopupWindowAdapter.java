package com.example.shareiceboxms.models.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemPerson;

import java.util.List;

/**
 * Created by WH on 2018/1/17.
 */

public class ChoosePopupWindowAdapter extends BaseAdapter {
    private List<ItemPerson> persons;
    private Context context;

    public ChoosePopupWindowAdapter(List<ItemPerson> persons, Context context) {
        this.persons = persons;
        this.context = context;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.choose_popup_window_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (persons.get(position) == null) {
            holder.childName.setText("我的");
        } else {
            holder.childName.setText(persons.get(position).name);
        }
        return convertView;
    }

    class ViewHolder {
        TextView childName;

        public ViewHolder(View itemView) {
            childName = (TextView) itemView.findViewById(R.id.childName);
        }
    }
}
