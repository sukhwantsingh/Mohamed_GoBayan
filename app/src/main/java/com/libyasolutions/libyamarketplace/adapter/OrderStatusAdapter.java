package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.StatusObj;

import java.util.ArrayList;

public class OrderStatusAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<StatusObj> listStatus;
    private LayoutInflater inflater;
    private int currentPosition = -1;

    public OrderStatusAdapter(Activity context, ArrayList<StatusObj> listStatus) {
        this.context = context;
        this.listStatus = listStatus;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listStatus.size();
    }

    @Override
    public Object getItem(int position) {
        return listStatus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_status, parent, false);
            holder = new ViewHolder();
            holder.tvStatus = convertView.findViewById(R.id.tv_status);
            holder.ivStatus = convertView.findViewById(R.id.iv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StatusObj statusObj = listStatus.get(position);
        holder.tvStatus.setText(statusObj.getName());
        holder.ivStatus.setVisibility(currentPosition == position ? View.VISIBLE : View.INVISIBLE);
        return convertView;
    }

    class ViewHolder {
        private TextView tvStatus;
        private ImageView ivStatus;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
