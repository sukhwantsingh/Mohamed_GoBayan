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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.AddNewPostActivity;
import com.libyasolutions.libyamarketplace.activity.AddNewShopActivityV2;
import com.libyasolutions.libyamarketplace.activity.ShopDetailsNew;
import com.libyasolutions.libyamarketplace.activity.addpost.ModelPostAdded;
import com.libyasolutions.libyamarketplace.activity.addpost.ModelPosts;
import com.libyasolutions.libyamarketplace.activity.addpost.OnPostCallback;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.object.Shop;

import java.io.File;
import java.util.ArrayList;

public class PostManagementAdapter extends RecyclerView.Adapter<PostManagementAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<ModelPosts.Data.Post> listShops;
    private LayoutInflater inflater;

    private String shopId;
    private OnPostCallback mCallback;

    public PostManagementAdapter(Activity context, String shopId, ArrayList<ModelPosts.Data.Post> listShops, OnPostCallback mCallback) {
        this.shopId = shopId;
        this.context = context;
        this.listShops = listShops;
        this.mCallback = mCallback;
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
        ModelPosts.Data.Post postObj = listShops.get(position);
        holder.tvPostDate.setText(Html.fromHtml("<b>Post date: </b>" + postObj.postDate()));
        holder.tvDescription.setText(postObj.getDescription());

        if (null != postObj.getStatus() && postObj.getStatus().equalsIgnoreCase("1")) {
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
            mCallback.onPostDelete("" + postObj.getId());
        });

        // edit shop
        holder.tvEditPost.setOnClickListener(view1 -> {
            AddNewPostActivity.post = postObj;
            Intent intent = new Intent(context, AddNewPostActivity.class);
            intent.putExtra(Constant.POST_ID, ""+postObj.getId());
            intent.putExtra(Constant.SHOP_ID, ""+shopId);
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
