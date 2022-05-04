package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.ImageObj;

import java.util.ArrayList;

public class GalleryToAddAdapter extends RecyclerView.Adapter<GalleryToAddAdapter.ViewHolder> {

    private ArrayList<Banner> gallery;
    private Activity act;
    private AQuery mAq;
    private OnItemClickListener onItemClickListener;

    public GalleryToAddAdapter(Activity act, ArrayList<Banner> gallery) {
        this.gallery = gallery;
        this.act = act;
        mAq = new AQuery(act);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            final Banner imageObj = gallery.get(position);
            if (imageObj != null) {
                if (imageObj.getBitmap() != null) {
                    holder.img.setImageBitmap(imageObj.getBitmap());
                } else if (!imageObj.getImage().isEmpty()) {
                    Glide.with(act).load(imageObj.getImage()).into(holder.img);
                } else {
                    holder.img.setImageResource(R.drawable.no_image_available);
                }

                // Click on image
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        try {
            return gallery.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;

        public ViewHolder(View view) {
            super(view);

            img =  view.findViewById(R.id.img_gallery);
        }
    }
}
