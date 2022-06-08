package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.AddNewPostActivity;
import com.libyasolutions.libyamarketplace.activity.AddNewShopActivityV2;
import com.libyasolutions.libyamarketplace.activity.ShopDetailsNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.object.Shop;

import java.util.ArrayList;

public class PostManagementAdapter extends RecyclerView.Adapter<PostManagementAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Shop> listShops;
    private LayoutInflater inflater;

    public PostManagementAdapter(Activity context, ArrayList<Shop> listShops) {
        this.context = context;
        this.listShops = listShops;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.row_item_list_post_management, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Shop shop = listShops.get(position);
        holder.tvPostDate.setText(Html.fromHtml("<b>Post date: </b>" + shop.getShopName())); // need to change
        holder.tvDescription.setText(shop.getDescription());

        if (shop.getStatus() == 1) {
            holder.tvStatus.setText(context.getResources().getString(R.string.active));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.active_color));
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.background_border_green));
        } else {
            holder.tvStatus.setText(context.getResources().getString(R.string.inactive));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_red_border));
        }

      // watch shop
        holder.tvDeletePost.setOnClickListener(view -> {
//            Bundle bundle = new Bundle();
//            bundle.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
//            Intent intent = new Intent(context, ShopDetailsNew.class);
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
        });

        // edit shop
        holder.tvEditPost.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, AddNewPostActivity.class);
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
        private TextView tvPostDate, tvDescription, tvStatus;
        private TextView tvDeletePost;
        private TextView tvEditPost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostDate = itemView.findViewById(R.id.tv_post_date);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDeletePost = itemView.findViewById(R.id.tv_delete_post);
            tvEditPost = itemView.findViewById(R.id.tv_edit_post);
        }
    }

}
