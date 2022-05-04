package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;

import java.util.ArrayList;

public class GMTAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<String> listGMT;
    private LayoutInflater inflater;

    public GMTAdapter(Activity context, ArrayList<String> listGMT) {
        this.context = context;
        this.listGMT = listGMT;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listGMT.size();
    }

    @Override
    public Object getItem(int position) {
        return listGMT.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_city, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(listGMT.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
    }
}
