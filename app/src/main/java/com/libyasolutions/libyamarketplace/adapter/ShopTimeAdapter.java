package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.interfaces.OnShopTimeListener;
import com.libyasolutions.libyamarketplace.object.OpenHour;

import java.util.ArrayList;

public class ShopTimeAdapter extends RecyclerView.Adapter<ShopTimeAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<OpenHour> listTimes;
    private LayoutInflater inflater;
    private OnShopTimeListener onShopTimeListener;

    public ShopTimeAdapter(Activity context, ArrayList<OpenHour> listTimes) {
        this.context = context;
        this.listTimes = listTimes;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_list_hour, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        OpenHour shopTimeObj = listTimes.get(position);
        holder.tvDayOfWeek.setText(shopTimeObj.getDateName());
        holder.tvOpenAM.setText(shopTimeObj.getOpenAM());
        holder.tvCloseAM.setText(shopTimeObj.getCloseAM());
        holder.tvOpenPM.setText(shopTimeObj.getOpenPM());
        holder.tvClosePM.setText(shopTimeObj.getClosePM());

        holder.tvOpenAM.setPaintFlags(holder.tvOpenAM.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvOpenPM.setPaintFlags(holder.tvOpenPM.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvCloseAM.setPaintFlags(holder.tvCloseAM.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvClosePM.setPaintFlags(holder.tvClosePM.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        holder.tvOpenAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShopTimeListener.onOpenAMClick(v, position);
            }
        });
        holder.tvCloseAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShopTimeListener.onCloseAMClick(v, position);
            }
        });

        holder.tvOpenPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShopTimeListener.onOpenPMClick(v, position);
            }
        });
        holder.tvClosePM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShopTimeListener.onClosePMClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTimes.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDayOfWeek;
        private TextView tvOpenAM, tvOpenPM;
        private TextView tvCloseAM, tvClosePM;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayOfWeek = itemView.findViewById(R.id.tv_day_of_week);
            tvOpenAM = itemView.findViewById(R.id.tv_open_am);
            tvOpenPM = itemView.findViewById(R.id.tv_open_pm);
            tvCloseAM = itemView.findViewById(R.id.tv_close_am);
            tvClosePM = itemView.findViewById(R.id.tv_close_pm);
        }
    }

    public void setOnShopTimeListener(OnShopTimeListener onShopTimeListener) {
        this.onShopTimeListener = onShopTimeListener;
    }
}
