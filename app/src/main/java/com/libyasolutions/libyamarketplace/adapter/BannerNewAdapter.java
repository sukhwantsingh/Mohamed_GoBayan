package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.core.Context;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Banner;

import java.util.ArrayList;

public class BannerNewAdapter extends RecyclerView.Adapter<BannerNewAdapter.ViewHolder> {

    private ArrayList<Banner> gallery;
    private Activity activity;
    private GalleryToAddAdapter.OnItemClickListener onItemClickListener;


    public BannerNewAdapter(Activity activity, ArrayList<Banner> gallery) {
        this.gallery = gallery;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BannerNewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_banner, viewGroup, false);
        return new BannerNewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerNewAdapter.ViewHolder holder, int position) {
        Banner imageObj = gallery.get(position);
            if (imageObj != null) {
                if (imageObj.getBitmap() != null) {
                    holder.img.setImageBitmap(imageObj.getBitmap());
                } else if (!imageObj.getImage().isEmpty()) {
                    Glide.with(activity)
                            .load(imageObj.getImage())
                            .error(R.drawable.no_image_available)
                            .dontAnimate()
                            .dontTransform()
                            .into(holder.img);
                }
                // delete banner image
                holder.ivDeleteImage.setOnClickListener(view -> onItemClickListener.onItemClick(view, position));
            }

    }

    @Override
    public int getItemCount() {
        return gallery.size();
    }

    public void setOnItemClickListener(GalleryToAddAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private ImageView ivDeleteImage;

        public ViewHolder(View view) {
            super(view);

            img =  view.findViewById(R.id.img_gallery);
            ivDeleteImage = view.findViewById(R.id.iv_delete_image);
        }
    }

}
