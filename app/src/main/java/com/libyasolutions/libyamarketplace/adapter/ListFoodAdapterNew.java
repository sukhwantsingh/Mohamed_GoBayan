package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.ProductDetailsNewActivity;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.listener.OnLoadMoreListener;
import com.libyasolutions.libyamarketplace.object.Menu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFoodAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String SEARCH_SCREEN = "searchActivity";

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private List<Menu> menuArrayList;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;

    private OnLoadMoreListener onLoadMoreListener;

    public ListFoodAdapterNew(RecyclerView recyclerView, Context context, List<Menu> menuArrayList) {
        this.context = context;
        this.menuArrayList = menuArrayList;

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                    if (layoutManager != null) {
                        totalItemCount = layoutManager.getItemCount();
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    }

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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_food_search, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Glide.with(context).load(menuArrayList.get(position).getImage()).into(viewHolder.imgShop);
            viewHolder.tvFoodName.setText(menuArrayList.get(position).getName());
            viewHolder.tvPriceSale.setText(String.valueOf(menuArrayList.get(position).getPrice()) + " " + context.getResources().getString(R.string.currency));
            viewHolder.tvDescription.setText(menuArrayList.get(position).getDescription());
            viewHolder.rtbRating.setRating((menuArrayList.get(position).getRateValue() / 2));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString(GlobalValue.KEY_FOOD_ID, menuArrayList.get(position).getId() + "");
                    b.putString(GlobalValue.KEY_NAVIGATE_TYPE, "FAST");
                    GlobalValue.KEY_LOCAL_NAME = menuArrayList.get(position).getLocalName();
                    b.putString(GlobalValue.KEY_FROM_SCREEN, SEARCH_SCREEN);
                    Intent intent = new Intent(context, ProductDetailsNewActivity.class);
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return menuArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFoodName, tvDescription, tvPrice, tvPriceSale, tvPromotion;
        private ImageView imgShop;
        private RatingBar rtbRating;

        public ViewHolder(View itemView) {
            super(itemView);
            rtbRating = itemView.findViewById(R.id.rtbRating);
            imgShop = itemView.findViewById(R.id.imgShop);
            tvFoodName = itemView.findViewById(R.id.lblShopName);
            tvDescription = itemView.findViewById(R.id.lblCategoryName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPriceSale = itemView.findViewById(R.id.tvPriceSale);
            tvPromotion = itemView.findViewById(R.id.tvPromotions);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
