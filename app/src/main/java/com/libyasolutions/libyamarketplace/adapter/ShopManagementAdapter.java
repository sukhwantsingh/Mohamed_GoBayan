package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.AddNewShopActivityV2;
import com.libyasolutions.libyamarketplace.activity.ShopDetailsNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.object.Shop;

import java.util.ArrayList;

public class ShopManagementAdapter extends RecyclerView.Adapter<ShopManagementAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Shop> listShops;
    private LayoutInflater inflater;

    public ShopManagementAdapter(Activity context, ArrayList<Shop> listShops) {
        this.context = context;
        this.listShops = listShops;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_list_shop_management, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Shop shop = listShops.get(position);
        holder.tvShopName.setText(shop.getShopName());
        holder.tvDescription.setText(shop.getDescription());

        if (shop.getStatus() == 1) {
            holder.tvStatus.setText(context.getResources().getString(R.string.active));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_red_border));
        } else {
            holder.tvStatus.setText(context.getResources().getString(R.string.inactive));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.gray_light));
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_gray_border));
        }

        // load image
        Glide.with(context)
                .load(shop.getImage())
                .error(R.drawable.no_image_available)
                .into(holder.ivShop);

        // watch shop
        holder.tvWatchShop.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
            Intent intent = new Intent(context, ShopDetailsNew.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        // edit shop
        holder.tvEditShop.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, AddNewShopActivityV2.class);
            intent.putExtra(Constant.SHOP_OBJ, shop);
            intent.putParcelableArrayListExtra(Constant.OPEN_HOUR, shop.getArrOpenHour());
            GlobalValue.currentShop = shop;
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
        });
    }

    @Override
    public int getItemCount() {
        return listShops.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvShopName, tvDescription, tvStatus;
        private ImageView ivShop;
        private TextView tvWatchShop;
        private TextView tvEditShop;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ivShop = itemView.findViewById(R.id.iv_shop);
            tvWatchShop = itemView.findViewById(R.id.tv_watch_shop);
            tvEditShop = itemView.findViewById(R.id.tv_edit_shop);
        }
    }

}
