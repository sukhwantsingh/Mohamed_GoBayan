package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.core.Context;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.object.City;

import java.util.ArrayList;
import java.util.List;

public class ShopCityAdapter extends RecyclerView.Adapter<ShopCityAdapter.ViewHolder> {
    private Activity context;
    private List<City> listCities;
    private AdapterListener.onItemClickListener onItemClickListener;

    public ShopCityAdapter(Activity context, List<City> listCities) {
        this.context = context;
        this.listCities = listCities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_city, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = listCities.get(position);
        holder.tvTitle.setText(city.getName());
    }

    @Override
    public int getItemCount() {
        return listCities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(v -> onItemClickListener.onclick(v, getAdapterPosition()));
        }
    }

    public void setOnItemClickListener(AdapterListener.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
