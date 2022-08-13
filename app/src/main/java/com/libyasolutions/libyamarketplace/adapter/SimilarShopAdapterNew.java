package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.ShopDetailsNew;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.listener.OnLoadMoreListener;
import com.libyasolutions.libyamarketplace.responses.ShopDetailResponse;

import java.util.List;

public class SimilarShopAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private List<ShopDetailResponse.SimilarProduct> shops;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    private OnLoadMoreListener onLoadMoreListener;

    public SimilarShopAdapterNew(RecyclerView recyclerView, List<ShopDetailResponse.SimilarProduct> shops, Context context) {
        this.shops = shops;
        this.context = context;

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return shops.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.row_list_shop_similar, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Glide.with(context).load(shops.get(position).shopImage).into(viewHolder.imgShop);
            viewHolder.tvShopName.setText(shops.get(position).shopName);
            viewHolder.tvDescription.setText(shops.get(position).shopDescription);
//          holder.ratingBar.setRating(Float.valueOf(shops.get(position).getRateNumber()));
            viewHolder.ratingBar.setRating(Float.parseFloat(Math
                    .floor(Float.parseFloat(shops.get(position).rate) / 2) + ""));

            viewHolder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt(GlobalValue.KEY_SHOP_ID, Integer.parseInt(shops.get(position).shopId));
                Intent intent = new Intent(context, ShopDetailsNew.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity)context).finish();
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgShop;
        private TextView tvShopName, tvDescription;
        private RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            imgShop = itemView.findViewById(R.id.imgShop);
            tvShopName = itemView.findViewById(R.id.lblBuyerInformation);
            tvDescription = itemView.findViewById(R.id.lblCategoryName);
            ratingBar = itemView.findViewById(R.id.rtbRating);

        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

       private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }


}
